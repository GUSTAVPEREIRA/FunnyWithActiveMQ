package study.com.miagenda.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import study.com.miagenda.dto.FechaCreateRequest;
import study.com.miagenda.dto.FechaResponse;
import study.com.miagenda.exceptions.*;
import study.com.miagenda.mapper.FechaMapper;
import study.com.miagenda.model.Fecha;
import study.com.miagenda.queue.MessageSender;
import study.com.miagenda.repository.FechaRepository;

import java.util.UUID;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class FechaServiceImpl implements FechaService {
    private FechaRepository fechaRepository;
    private FechaMapper fechaMapper;
    private MessageSender messageSender;

    private final String FECHA_QUEUE = "agendamentos";

    @Transactional
    public FechaResponse createFecha(FechaCreateRequest fechaCreateRequest) throws JsonProcessingException, FechaInvalidaException {

        var fecha = fechaMapper.fechaCreateRequestToFecha(fechaCreateRequest);

        validaSiFechaEsValida(fecha);

        return getFechaResponseYEnviaParaLaFila(fechaRepository.save(fecha));
    }

    @Transactional
    public FechaResponse updateFecha(FechaCreateRequest fechaCreateRequest, UUID id) throws FechaInvalidaException, JsonProcessingException {
        var fecha = fechaMapper.fechaCreateRequestToFecha(fechaCreateRequest);
        fecha.setId(id);

        validaSiFechaEsValida(fecha);

        return getFechaResponseYEnviaParaLaFila(fechaRepository.save(fecha));
    }

    @Transactional
    public void deleteFecha(UUID id) {

        if (fechaRepository.existsFechaById(id)) {
            fechaRepository.deleteById(id);
            messageSender.sender(FECHA_QUEUE, String.format("Fecha con id fue cancelada %s con éxito", id));
            return;
        }

        throw new FechaLaFechaNoFueEncontradaException(String.format("Fecha no encontrada %s", id));
    }

    public Page<FechaResponse> listTodasLasFechas(Pageable pageable) {
        var fechas = fechaRepository.findAll(pageable);

        return fechas.map(fecha -> fechaMapper.fechaToFechaResponse(fecha));
    }

    private FechaResponse getFechaResponseYEnviaParaLaFila(Fecha fecha) throws JsonProcessingException {
        var fechaResponse = fechaMapper.fechaToFechaResponse(fecha);
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        messageSender.sender(FECHA_QUEUE, objectMapper.writeValueAsString(fechaResponse));
        return fechaResponse;
    }


    private void validaSiFechaEsValida(Fecha fecha) throws FechaInvalidaException {
        if (!fecha.FechaEsValida()) {
            throw new FechaInvalidaException();
        }

        var existedFecha = fechaRepository.findFechaByFechaInicioAndFechaFin(fecha.getFechaInicio(), fecha.getFechaFin())
                .stream()
                .findFirst().orElse(null);


        if (existedFecha != null && !existedFecha.getId().equals(fecha.getId())) {
            throw new FechaInvalidaException("No se puede crear la fecha, porque ya existe una en ese horario.");
        }
    }
}

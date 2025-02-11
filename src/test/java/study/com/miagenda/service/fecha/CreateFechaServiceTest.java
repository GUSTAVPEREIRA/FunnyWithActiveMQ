package study.com.miagenda.service.fecha;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import study.com.miagenda.dto.FechaCreateRequest;
import study.com.miagenda.dto.FechaResponse;
import study.com.miagenda.exceptions.FechaInvalidaException;
import study.com.miagenda.mapper.FechaMapper;
import study.com.miagenda.model.Fecha;
import study.com.miagenda.queue.MessageSender;
import study.com.miagenda.repository.FechaRepository;
import study.com.miagenda.service.FechaServiceImpl;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class CreateFechaServiceTest {

    @Mock
    FechaRepository fechaRepository;

    @Mock
    FechaMapper fechaMapper;

    @Mock
    MessageSender messageSender;

    @InjectMocks
    FechaServiceImpl fechaService;

    @Test
    void testFechaConDatosInvalidos_ThrowsFechaInvalidaException() {
        OffsetDateTime now = OffsetDateTime.now();

        var fechaCreateRequest = new FechaCreateRequest("Test", now, now);
        var fecha = new Fecha(UUID.randomUUID(), "Test", now, now);

        when(fechaMapper.fechaCreateRequestToFecha(fechaCreateRequest))
                .thenReturn(fecha);

        var exception = assertThrows(FechaInvalidaException.class, () -> fechaService.createFecha(fechaCreateRequest));

        assertEquals("Fecha no es válida, las reglas son, \"fecha fin\" tiene que ser mayor que la fecha de inicio, además de inicio no puede ser antes del fin y no pueden ser iguales.", exception.getMessage());

        verify(fechaMapper, times(1)).fechaCreateRequestToFecha(fechaCreateRequest);
    }


    @Test
    void testFechaConConflito_ThrowsFechaInvalidaException() {
        OffsetDateTime fechaInicio = OffsetDateTime.now();
        OffsetDateTime fechaFin = OffsetDateTime.now().plusDays(5);

        var fechaCreateRequest = new FechaCreateRequest("Test", fechaInicio, fechaFin);
        var fecha = new Fecha(UUID.randomUUID(), "Test", fechaInicio, fechaFin);
        var nuevaFecha = new Fecha(UUID.randomUUID(), "Test", fechaInicio, fechaFin);

        var fechas = new ArrayList<Fecha>();
        fechas.add(fecha);

        when(fechaMapper.fechaCreateRequestToFecha(fechaCreateRequest))
                .thenReturn(nuevaFecha);

        when(fechaRepository.findFechaByFechaInicioAndFechaFin(fechaInicio, fechaFin)).thenReturn(fechas);

        var exception = assertThrows(FechaInvalidaException.class, () -> fechaService.createFecha(fechaCreateRequest));

        assertEquals("No se puede crear la fecha, porque ya existe una en ese horario.", exception.getMessage());

        verify(fechaMapper, times(1)).fechaCreateRequestToFecha(fechaCreateRequest);
        verify(fechaMapper, times(1)).fechaCreateRequestToFecha(fechaCreateRequest);
    }


    @Test
    void testCriaLaFecha_EnviaLaFechaParaLaFilaYRetornaFecha() throws JsonProcessingException, FechaInvalidaException {
        OffsetDateTime fechaInicio = OffsetDateTime.now();
        OffsetDateTime fechaFin = OffsetDateTime.now().plusDays(5);
        var description = "Fecha creada para la fiesta de cumpleaños de mi hijo";

        var fechaCreateRequest = new FechaCreateRequest(description, fechaInicio, fechaFin);
        var fecha = new Fecha(UUID.randomUUID(), description, fechaInicio, fechaFin);
        var expectedReturn = new FechaResponse(fecha.getId());

        expectedReturn.setFechaInicio(fechaInicio);
        expectedReturn.setFechaFin(fechaFin);
        expectedReturn.setDescripcion(description);

        var fechas = new ArrayList<Fecha>();
        fechas.add(fecha);

        when(fechaMapper.fechaCreateRequestToFecha(any())).thenReturn(fecha);
        when(fechaRepository.findFechaByFechaInicioAndFechaFin(any(), any())).thenReturn(fechas);
        when(fechaRepository.save(any())).thenReturn(fecha);
        when(fechaMapper.fechaToFechaResponse(any())).thenReturn(expectedReturn);

        var result = fechaService.createFecha(fechaCreateRequest);

        assertThat(result.getFechaFin()).isEqualTo(fecha.getFechaFin());
        assertThat(result.getFechaInicio()).isEqualTo(fecha.getFechaInicio());
        assertThat(result.getId()).isEqualTo(fecha.getId());
        assertThat(result.getDescripcion()).isEqualTo(fecha.getDescripcion());

        verify(fechaMapper, times(1)).fechaCreateRequestToFecha(fechaCreateRequest);
        verify(fechaMapper, times(1)).fechaToFechaResponse(fecha);
        verify(fechaRepository, times(1)).findFechaByFechaInicioAndFechaFin(fechaInicio, fechaFin);
        verify(fechaRepository, times(1)).save(fecha);

        var objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        verify(messageSender, times(1)).sender("agendamentos.agendador.notificar.success", objectMapper.writeValueAsString(result));
    }


    @Test
    void testCriaLaFechaCuandoEsNueva_EnviaLaFechaParaLaFilaYRetornaFecha() throws JsonProcessingException, FechaInvalidaException {
        OffsetDateTime fechaInicio = OffsetDateTime.now();
        OffsetDateTime fechaFin = OffsetDateTime.now().plusDays(5);
        var description = "Fecha creada para la fiesta de cumpleaños de mi hijo";

        var fechaCreateRequest = new FechaCreateRequest(description, fechaInicio, fechaFin);
        var fecha = new Fecha(UUID.randomUUID(), description, fechaInicio, fechaFin);
        var expectedReturn = new FechaResponse(fecha.getId());

        expectedReturn.setFechaInicio(fechaInicio);
        expectedReturn.setFechaFin(fechaFin);
        expectedReturn.setDescripcion(description);

        var fechas = new ArrayList<Fecha>();

        when(fechaMapper.fechaCreateRequestToFecha(any())).thenReturn(fecha);
        when(fechaRepository.findFechaByFechaInicioAndFechaFin(any(), any())).thenReturn(fechas);
        when(fechaRepository.save(any())).thenReturn(fecha);
        when(fechaMapper.fechaToFechaResponse(any())).thenReturn(expectedReturn);

        var result = fechaService.createFecha(fechaCreateRequest);

        assertThat(result.getFechaFin()).isEqualTo(fecha.getFechaFin());
        assertThat(result.getFechaInicio()).isEqualTo(fecha.getFechaInicio());
        assertThat(result.getId()).isEqualTo(fecha.getId());
        assertThat(result.getDescripcion()).isEqualTo(fecha.getDescripcion());

        verify(fechaMapper, times(1)).fechaCreateRequestToFecha(fechaCreateRequest);
        verify(fechaMapper, times(1)).fechaToFechaResponse(fecha);
        verify(fechaRepository, times(1)).findFechaByFechaInicioAndFechaFin(fechaInicio, fechaFin);
        verify(fechaRepository, times(1)).save(fecha);

        var objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        verify(messageSender, times(1)).sender("agendamentos.agendador.notificar.success", objectMapper.writeValueAsString(result));
    }
}

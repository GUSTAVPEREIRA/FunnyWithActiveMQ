package study.com.miagenda.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.com.miagenda.dto.FechaCreateRequest;
import study.com.miagenda.dto.FechaResponse;
import study.com.miagenda.dto.WrapperResponse;
import study.com.miagenda.exceptions.FechaInvalidaException;
import study.com.miagenda.queue.MessageSender;
import study.com.miagenda.service.FechaService;

import java.util.UUID;

@RestController
@RequestMapping(value = "fecha")
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class FechaController {

    private FechaService fechaService;
    private MessageSender messageSender;
    private final String ERROR_FECHA_QUEUE = "agendamentos.agendador.notificar.erro";

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<WrapperResponse<FechaResponse>> createFecha(@Valid FechaCreateRequest fechaCreateRequest) {

        try {
            var response = fechaService.createFecha(fechaCreateRequest);
            var wrapperResponse = new WrapperResponse<>(response);

            return new ResponseEntity<>(wrapperResponse, HttpStatus.CREATED);
        } catch (FechaInvalidaException | JsonProcessingException e) {
            var wrapperResponse = new WrapperResponse<FechaResponse>(e.getMessage());

            messageSender.sender(ERROR_FECHA_QUEUE, e.getMessage());
            return new ResponseEntity<>(wrapperResponse, HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping(value = "/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<WrapperResponse<FechaResponse>> updateFecha(@Valid FechaCreateRequest fechaCreateRequest, @PathVariable UUID id) {
        try {
            var response = fechaService.updateFecha(fechaCreateRequest, id);
            var wrapperResponse = new WrapperResponse<>(response);

            return new ResponseEntity<>(wrapperResponse, HttpStatus.OK);
        } catch (FechaInvalidaException | JsonProcessingException e) {
            var wrapperResponse = new WrapperResponse<FechaResponse>(e.getMessage());

            messageSender.sender(ERROR_FECHA_QUEUE, e.getMessage());
            return new ResponseEntity<>(wrapperResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> deleteFecha(@PathVariable UUID id) {

        try {
            fechaService.deleteFecha(id);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {

            messageSender.sender(ERROR_FECHA_QUEUE, e.getMessage());

            throw new RuntimeException(e);
        }

    }

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<WrapperResponse<Page<FechaResponse>>> listFechas(@PageableDefault(page = 0, value = 10) Pageable pageable) {
        var fechas = fechaService.listTodasLasFechas(pageable);

        var response = new WrapperResponse<>(fechas);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}

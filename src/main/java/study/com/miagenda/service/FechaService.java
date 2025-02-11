package study.com.miagenda.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import study.com.miagenda.dto.FechaCreateRequest;
import study.com.miagenda.dto.FechaResponse;
import study.com.miagenda.exceptions.FechaInvalidaException;

import java.util.UUID;

@Service
public interface FechaService {

    FechaResponse createFecha(FechaCreateRequest fechaCreateRequest) throws FechaInvalidaException, JsonProcessingException;
    FechaResponse updateFecha(FechaCreateRequest fechaCreateRequest, UUID id) throws FechaInvalidaException, JsonProcessingException;
    void deleteFecha(UUID id);
    Page<FechaResponse> listTodasLasFechas(Pageable pageable);
}

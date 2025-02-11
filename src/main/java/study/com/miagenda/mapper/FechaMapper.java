package study.com.miagenda.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import study.com.miagenda.dto.FechaCreateRequest;
import study.com.miagenda.dto.FechaResponse;
import study.com.miagenda.model.Fecha;

@Mapper(componentModel = "spring")
public interface FechaMapper {

    @Mapping(target = "id", ignore = true)
    Fecha fechaCreateRequestToFecha(FechaCreateRequest fechaCreateRequest);


    FechaResponse fechaToFechaResponse(Fecha fecha);
}

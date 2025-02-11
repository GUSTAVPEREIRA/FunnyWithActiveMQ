package study.com.miagenda.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FechaCreateRequest {
    private String descripcion;
    private OffsetDateTime fechaInicio;
    private OffsetDateTime fechaFin;
}

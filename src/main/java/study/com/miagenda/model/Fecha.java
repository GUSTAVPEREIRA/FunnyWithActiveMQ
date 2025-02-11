package study.com.miagenda.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity(name = "Fecha")
@NoArgsConstructor
@AllArgsConstructor
public class Fecha {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "fecha_inicio")
    private OffsetDateTime fechaInicio;

    @Column(name = "fecha_fin")
    private OffsetDateTime fechaFin;

    private Boolean fechaInicioEsMayorQueFechaFin() {
        return fechaInicio.isBefore(fechaFin);
    }

    private Boolean fechaFinEsMenorQueFechaInicio() {
        return fechaFin.isAfter(fechaInicio);
    }

    private boolean fechaFinDaIgualFechaInicio() {
        return !fechaFin.isEqual(fechaInicio);
    }


    public boolean FechaEsValida() {
        return fechaFinDaIgualFechaInicio() && fechaFinEsMenorQueFechaInicio() && fechaInicioEsMayorQueFechaFin();
    }
}

package study.com.miagenda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.com.miagenda.model.Fecha;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface FechaRepository extends JpaRepository<Fecha, UUID> {

    Boolean existsFechaById(UUID id);

    List<Fecha> findFechaByFechaInicioAndFechaFin(OffsetDateTime fechaInicio, OffsetDateTime fechaFin);
}

create schema la_agenda

create table Fecha
(
    id           UUID PRIMARY KEY,
    fecha_inicio TIMESTAMP NOT NULL,
    fecha_fin    TIMESTAMP NOT NULL,
    descripcion  VARCHAR(250),
    UNIQUE (fecha_inicio, fecha_fin)
)

ALTER TABLE Fecha
    add constraint unica_fecha_horario UNIQUE (fecha_inicio, fecha_fin)

ALTER TABLE Fecha
    ADD CONSTRAINT check_fecha_inicio_fin CHECK (fecha_inicio <> fecha_fin);
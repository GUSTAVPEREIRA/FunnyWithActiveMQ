package study.com.miagenda.exceptions;

public class FechaLaFechaNoFueEncontradaException extends RuntimeException {
    public FechaLaFechaNoFueEncontradaException(String id) {
        super(String.format("Fecha no encontrada %s", id));
    }
}

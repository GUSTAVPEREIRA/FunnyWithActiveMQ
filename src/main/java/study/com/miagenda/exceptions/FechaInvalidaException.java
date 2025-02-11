package study.com.miagenda.exceptions;

import org.apache.coyote.BadRequestException;

public class FechaInvalidaException extends BadRequestException {
    
    public FechaInvalidaException(String message) {
        super(message);
    }

    public FechaInvalidaException() {
        super("Fecha no es válida, las reglas son, \"fecha fin\" tiene que ser mayor que la fecha de inicio, además de inicio no puede ser antes del fin y no pueden ser iguales.");
    }
}

package study.com.miagenda.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import study.com.miagenda.dto.WrapperResponse;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAccessDeniedException(Exception ex) {

        var wrapperResponse = new WrapperResponse<>(String.format("%s:%s", "Ocurrió un error en el servidor", ex.getMessage()));

        return new ResponseEntity<>(
                wrapperResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
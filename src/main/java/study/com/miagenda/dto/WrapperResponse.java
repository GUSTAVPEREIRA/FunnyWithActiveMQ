package study.com.miagenda.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WrapperResponse<T> {

    private T data;
    private String message;

    public WrapperResponse(T data, String message) {
        this.data = data;
        this.message = message;
    }

    public WrapperResponse(T data) {
        this.data = data;
    }

    public WrapperResponse(String message) {
        this.message = message;
    }

}

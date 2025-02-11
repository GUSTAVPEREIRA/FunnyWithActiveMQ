package study.com.miagenda.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.com.miagenda.dto.WrapperResponse;

@RestController
@RequestMapping("alive")
public class ItsAliveController {

    @GetMapping(produces = "application/json")
    public ResponseEntity<WrapperResponse<Object>> ItsAlive() {
        String ALIVE_MESSAGE = "It's alive";
        var wrapper = new WrapperResponse<>(ALIVE_MESSAGE);

        wrapper.setData(ALIVE_MESSAGE);

        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }
}

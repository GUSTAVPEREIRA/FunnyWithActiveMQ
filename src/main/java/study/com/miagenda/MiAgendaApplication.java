package study.com.miagenda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class MiAgendaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiAgendaApplication.class, args);
    }
}

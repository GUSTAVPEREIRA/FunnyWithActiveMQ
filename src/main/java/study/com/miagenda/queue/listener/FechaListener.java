package study.com.miagenda.queue.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class FechaListener implements BaseListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @JmsListener(destination = "agendamentos.agendador.notificar.success")
    public void receiveMessage(String message) {
        logger.info(message);
    }

    @JmsListener(destination = "agendamentos.agendador.notificar.erro")
    public void receiveErrorMessage(String message) {
        logger.error("Ocurrió un error con la API.: {}", message);
    }
}

package study.com.miagenda.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageSender {
    private final JmsTemplate jmsTemplate;

    @Autowired
    public MessageSender(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sender(String queueName, String message) {
        jmsTemplate.convertAndSend(queueName, message);
    }
}

package kafka;

import com.example.service.DefaultEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
    private DefaultEmailService emailService;

    @Autowired
    public KafkaConsumer(DefaultEmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(
            topics = "${kafka.topics.test-topic}",
            groupId = "notification-group"
    )
    public void consumeMessage(MessageObject message) {
        if (message != null) {
            logger.info("Received message: {}", message);
            emailService.sendSimpleEmail(message);
        } else {
            logger.error("Received null message");
        }
    }
}
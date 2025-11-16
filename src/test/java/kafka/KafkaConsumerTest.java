package kafka;

import service.DefaultEmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaConsumerTest {

    @Mock
    private DefaultEmailService emailService;

    @InjectMocks
    private KafkaConsumer kafkaConsumer;

    @Test
    void shouldCallEmailServiceWhenMessageIsNotNull() {
        MessageObject message = new MessageObject();
        message.setUserEmail("test@example.com");
        message.setNameMethod("создан.");

        kafkaConsumer.consumeMessage(message);

        verify(emailService, times(1)).sendSimpleEmail(message);
    }

    @Test
    void shouldNotCallEmailServiceWhenMessageIsNull() {
        kafkaConsumer.consumeMessage(null);

        verifyNoInteractions(emailService);
    }
}
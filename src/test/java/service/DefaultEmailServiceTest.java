package service;

import constant.ConstantMassageForEmail;
import kafka.MessageObject;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
class DefaultEmailServiceTest {

    @Autowired
    private DefaultEmailService emailService;

    @MockBean
    private JavaMailSender emailSender;

    @Test
    void shouldSendSimpleEmailSuccessfully() {
        MessageObject messageObject = new MessageObject();
        messageObject.setUserEmail("test@example.com");
        messageObject.setNameMethod("создан.");

        emailService.sendSimpleEmail(messageObject);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(emailSender, times(1)).send(captor.capture());

        SimpleMailMessage sentMessage = captor.getValue();

        assertThat(sentMessage.getTo()).containsExactly("test@example.com");
        assertThat(sentMessage.getText()).isEqualTo(ConstantMassageForEmail.MESSAGE + "создан.");
    }
}
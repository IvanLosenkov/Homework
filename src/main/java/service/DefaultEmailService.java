package service;
import com.example.constant.ConstantMassageForEmail;
import com.example.kafka.MessageObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class DefaultEmailService {

    private final Logger logger = LoggerFactory.getLogger(DefaultEmailService.class);

    private JavaMailSender emailSender;
    private SimpleMailMessage simpleMailMessage;

    @Autowired
    public DefaultEmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
        this.simpleMailMessage = new SimpleMailMessage();
    }

    public void sendSimpleEmail(MessageObject messageObject) {

        String userEmail = messageObject.getUserEmail();
        String nameMethod = messageObject.getNameMethod();

        simpleMailMessage.setTo(userEmail);
        simpleMailMessage.setText(ConstantMassageForEmail.MESSAGE + nameMethod);

        emailSender.send(simpleMailMessage);

        logger.debug("Message send: {}", simpleMailMessage);
    }
}
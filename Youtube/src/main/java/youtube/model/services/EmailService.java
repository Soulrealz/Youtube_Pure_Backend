package youtube.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String email;

    private static final String BODY = "Hello, thank you for registering! In order to complete your registration " +
                                        "please click on the following link: localhost:9999/verify/";
    private static final String TOPIC = "Registration @YoutubeClone";

    public void sendEmail(String to) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        String token = generateToken(to);

        simpleMailMessage.setFrom(this.email);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(TOPIC);
        simpleMailMessage.setText(BODY + token);
        javaMailSender.send(simpleMailMessage);
    }

    private String generateToken(String mailToHash) {
        String hash = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA");
            messageDigest.update(mailToHash.getBytes());
            byte[] resultByteArray = messageDigest.digest();

            StringBuilder sb = new StringBuilder();
            for (byte b : resultByteArray) {
                sb.append(String.format("%02x", b));
            }
            hash = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Hashing Algorithm not found");
        }

        return hash;
    }
}

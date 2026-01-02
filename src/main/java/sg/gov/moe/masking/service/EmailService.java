package sg.gov.moe.masking.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

	@Value("${email.from}")
	private String fromEmail;

	private JavaMailSender javaMailSender;

	@Autowired
	public EmailService(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

    /**
     * Send Email
     * @param recipient
     * @param subject
     * @param content
     */
	public void sendEmail(String recipient, String subject, String content) {
		try {
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			helper.setFrom(fromEmail);
			helper.setTo(recipient);
			helper.setSubject(subject);
			helper.setText(content, true);
			javaMailSender.send(mimeMessage);

			logger.info("Email sent successfully!");
		} catch (Exception e) {
			logger.error("Failed to send email {} ", e.getMessage());
		}
	}
}
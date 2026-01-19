package sg.gov.moe.masking.service;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import sg.gov.moe.masking.model.SecretManagerResponseBody;

@Service
public class EmailService {

	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

	@Value("${email.from}")
	private String fromEmail;

	@Value("${smtp.host}")
	private String smtpHost;

	@Value("${smtp.port}")
	private int smtpPort;

	@Autowired
	private SecretWrapperService secretWrapperService;

    /**
     * Send Email
     * @param recipient
     * @param subject
     * @param content
     */
	public void sendEmail(String recipient, String subject, String content) {
		try {
			logger.info("Preparing to send email to {}", recipient);
			SecretManagerResponseBody secret = secretWrapperService.getSecretValue("aws_smtp_credentials");
			
			HtmlEmail email = new HtmlEmail();
			email.setHostName(smtpHost);
			email.setSmtpPort(smtpPort);
			System.out.println("accessKeyId: " + secret.getAccessKeyId() + ", secretAccessKey: " + secret.getSecretAccessKey());
			if (secret != null) {
				email.setAuthentication(secret.getAccessKeyId(), secret.getSecretAccessKey());
			}
			email.setSSLOnConnect(true);
			
			email.setFrom(fromEmail);
			if(recipient.contains("|")){
				String [] toMail = recipient.split("\\|");
				for (String to : toMail) {
					email.addTo(to);
				}
			} else {
				email.addTo(recipient);
			}
			email.setSubject(subject);
			email.setHtmlMsg(content);
			email.send();
			logger.info("Email sent successfully to: {}", recipient);
		} catch (Exception e) {
			logger.error("Failed to send email to {}: {}", recipient, e.getMessage(), e);
		}
	}
}
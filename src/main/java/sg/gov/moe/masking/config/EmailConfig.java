package sg.gov.moe.masking.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import sg.gov.moe.masking.model.SecretManagerResponseBody;
import sg.gov.moe.masking.service.SecretWrapperService;
import sg.gov.moe.masking.util.common.ServiceUtil;

@Configuration
@PropertySource("classpath:application.properties")
public class EmailConfig {

	@Value("${smtp.host}")
	private String smtpHost;

	@Value("${smtp.port}")
	private int smtpPort;

	@Value("${smtp.username}")
	private String smtpUserName;

	@Value("${smtp.password}")
	private String smtpPassword;

	private String appEnvironment;

	private static final String SUPPORT_DATA_SMTP = "aws_smtp_credentials";

	private SecretWrapperService secretWrapperService;

	private SecretManagerResponseBody secretResponse;

	public EmailConfig(SecretWrapperService secretWrapperService, SecretManagerResponseBody secretResponse) {
		this.secretWrapperService = secretWrapperService;
		this.secretResponse = secretResponse;
	}

    /**
     * Java Mail Sender Bean
     * @return JavaMailSender
     */
	@Bean
	JavaMailSender javaMailSender() {
		String envSecretName = "aws_smtp_credentials";
		secretResponse = secretWrapperService.getSecretValue(envSecretName);
        System.out.println("SMTP Secret Retrieved: " + secretResponse);
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		javaMailSender.setHost(smtpHost);
		javaMailSender.setPort(smtpPort);
		javaMailSender.setUsername(secretResponse.getAccessKeyId());
		javaMailSender.setPassword(secretResponse.getSecretAccessKey());

		System.out.println(secretResponse.getAccessKeyId());
		System.out.println(secretResponse.getSecretAccessKey());
		if (secretResponse != null) {
			javaMailSender.setUsername(secretResponse.getAccessKeyId());
			javaMailSender.setPassword(secretResponse.getSecretAccessKey());
		}
		Properties props = javaMailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", "true");
		props.put("mail.smtp.starttls.required", "true");
		return javaMailSender;
	}
}
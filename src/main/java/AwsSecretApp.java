
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import sg.gov.moe.masking.service.SecretWrapperService;
import sg.gov.moe.masking.service.EmailService;

@Configuration
@ComponentScan(basePackages = "sg.gov.moe.masking")
public class AwsSecretApp {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AwsSecretApp.class);

        SecretWrapperService secretService = context.getBean(SecretWrapperService.class);
        EmailService emailService = context.getBean(EmailService.class);

        try {
            var secret = secretService.getSecretValue("hashSalt");
            if (secret != null) {
                // Send email with the secret instead of printing
                String recipient = "genie7480@gmail.com";
                String subject = "Retrieved Secret: hashSalt";
                String content = "The secret value is: " + secret.toString() + "\nHash Salt: " + secret.getHashSalt();
                emailService.sendEmail(recipient, subject, content);
                
                System.out.println("Email sent with the secret.");
            } else {
                System.out.println("No secret retrieved.");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            context.close();
        }
    }
}
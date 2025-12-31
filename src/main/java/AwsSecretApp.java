
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import sg.gov.moe.masking.service.SecretWrapperService;

@Configuration
@ComponentScan(basePackages = "sg.gov.moe.masking")
public class AwsSecretApp {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AwsSecretApp.class);

        SecretWrapperService secretService = context.getBean(SecretWrapperService.class);

        try {
            var secret = secretService.getSecretValue("hash_salt2");
            if (secret != null) {
                // For simple string secrets, the value is stored in the password field
                System.out.println("the secret: " + secret);
                System.out.println("the secret is a: " + secret.getHashSalt());
            } else {
                System.out.println("the secret is a: null");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            context.close();
        }
    }
}
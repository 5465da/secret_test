
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import sg.gov.moe.masking.service.SecretWrapperService;
import sg.gov.moe.masking.service.EmailService;
import sg.gov.moe.masking.service.S3WrapperService;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Configuration
@ComponentScan(basePackages = "sg.gov.moe.masking")
@PropertySource("classpath:application.properties")
public class AwsSecretApp {

    public static void main(String[] args) {
        System.out.println("Starting AWS Secret App...");
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AwsSecretApp.class);

        SecretWrapperService secretService = context.getBean(SecretWrapperService.class);
        EmailService emailService = context.getBean(EmailService.class);
        S3WrapperService s3Service = context.getBean(S3WrapperService.class);

        try {
            var secret = secretService.getSecretValue("hashSalt");
            if (secret != null) {
                // Send email with the secret instead of printing
                String recipient = "genie7480@gmail.com";
                String subject = "Retrieved Secret: hashSalt";
                String content = "The secret value is: " + secret.toString() + "\nHash Salt: " + secret.getHashSalt();
                emailService.sendEmail(recipient, subject, content);

                System.out.println("Email sent with the secret.");
                 System.out.println("creating zip and uploading to S3...");
                // Create sample .txt file with "hello world"
                File sampleFile = new File("hello-world.txt");
                try (FileWriter writer = new FileWriter(sampleFile)) {
                    writer.write("hello world");
                } catch (IOException e) {
                    System.err.println("Error creating sample file: " + e.getMessage());
                }

                 System.out.println("start zip");
                // Create zip file
                File zipFile = new File("hello-world.zip");
                try (FileOutputStream fos = new FileOutputStream(zipFile);
                     ZipOutputStream zos = new ZipOutputStream(fos);
                     FileInputStream fis = new FileInputStream(sampleFile)) {

                    ZipEntry zipEntry = new ZipEntry(sampleFile.getName());
                    zos.putNextEntry(zipEntry);

                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) >= 0) {
                        zos.write(buffer, 0, length);
                    }
                    zos.closeEntry();

                } catch (IOException e) {
                    System.err.println("Error creating zip file: " + e.getMessage());
                }

                System.out.println("uploading to S3");
                // Upload zip file to S3
                System.out.println("creating zip and uploading to S3...");
                System.out.println("start zip");
                try {
                    var result = s3Service.uploadFileToSD("hello-world.zip", zipFile);
                    System.out.println("Zip file uploaded to S3: " + result.getETag());
                } catch (Exception e) {
                    System.err.println("Error uploading zip to S3: " + e.getMessage());
                    e.printStackTrace();
                } finally {
                    // Clean up
                    sampleFile.delete();
                    zipFile.delete();
                }
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
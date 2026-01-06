package sg.gov.moe.masking.config;

import com.amazonaws.ClientConfiguration;
import sg.gov.moe.masking.model.SecretManagerResponseBody;
import sg.gov.moe.masking.service.SecretWrapperService;
import sg.gov.moe.masking.util.common.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class S3Config {

	private static final Logger LOG = LoggerFactory.getLogger(S3Config.class);
	private static final String SUPPORT_DATA_S3 = "aws_supportdata_s3key";

	private SecretWrapperService  secretWrapperService;
	private SecretManagerResponseBody secretResponse;

	@Value("${region}")
	private String region;

	@Value("${utilityPortal.region}")
	private String utilityPortalRegion;

	@Value("${app.environment}")
	private String appEnvironment;

	public S3Config(SecretWrapperService secretWrapperService, SecretManagerResponseBody secretResponse){
		this.secretWrapperService = secretWrapperService;
		this.secretResponse = secretResponse;
	}

	@Bean
	AmazonS3 s3() {
		String envSecretName = ServiceUtil.getEnvBasedSecretName(SUPPORT_DATA_S3, appEnvironment);
		secretResponse = secretWrapperService.getSecretValue(envSecretName);
		AWSCredentials awscredentials = new AWSCredentials() {
			@Override
			public String getAWSAccessKeyId() {
				return secretResponse != null ?	ServiceUtil.trimAndReplaceNewLine(secretResponse.getAccessKeyId()) : "";
			}

			@Override
			public String getAWSSecretKey() {
				return secretResponse != null ? ServiceUtil.trimAndReplaceNewLine(secretResponse.getSecretAccessKey()) : "";
			}
		};
		// Create a ClientConfiguration object to set timeouts
		ClientConfiguration clientConfig = new ClientConfiguration()
				.withConnectionTimeout(10000) // Connection timeout in milliseconds
				.withSocketTimeout(300000); // Socket timeout in milliseconds
		
		LOG.info("S3 Region: {}", region);
		LOG.info("S3 Credentials - AccessKey: {}", awscredentials.getAWSAccessKeyId() != null ? "present" : "null");
		
		return AmazonS3ClientBuilder.standard().withRegion(region != null && !region.isEmpty() ? region : "ap-southeast-2")
				.withClientConfiguration(clientConfig)
				.withCredentials(new AWSStaticCredentialsProvider(awscredentials)).build();

	}

	@Bean
	AmazonS3 s3UtilityPortal() {
		String envSecretName = ServiceUtil.getEnvBasedSecretName(SUPPORT_DATA_S3, appEnvironment);
		secretResponse = secretWrapperService.getSecretValue(envSecretName);

		AWSCredentials awscredentials = new AWSCredentials() {
			@Override
			public String getAWSAccessKeyId() {
				return 	secretResponse!=null ? ServiceUtil.trimAndReplaceNewLine(secretResponse.getAccessKeyId()):"";
			}

			@Override
			public String getAWSSecretKey() {
				return secretResponse!=null ? ServiceUtil.trimAndReplaceNewLine(secretResponse.getSecretAccessKey()):"";
			}
		};
		LOG.info("Initialized the S3Utility Bean");
		LOG.info("S3 Utility Portal Region: {}", utilityPortalRegion);
		return AmazonS3ClientBuilder.standard().withRegion(utilityPortalRegion != null && !utilityPortalRegion.isEmpty() ? utilityPortalRegion : "ap-southeast-2")
				.withCredentials(new AWSStaticCredentialsProvider(awscredentials)).build();
	}
}
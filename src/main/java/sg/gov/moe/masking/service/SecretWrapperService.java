package sg.gov.moe.masking.service;

import java.security.InvalidParameterException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.amazonaws.services.secretsmanager.model.InvalidRequestException;
import com.amazonaws.services.secretsmanager.model.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import sg.gov.moe.masking.model.SecretManagerResponseBody;

@Service
public class SecretWrapperService implements SecretWrapperInterface{

	private static final Logger LOG = LoggerFactory.getLogger(SecretWrapperService.class);

    private final AWSSecretsManager secretManagerClient;

    public SecretWrapperService(AWSSecretsManager secretManagerClient) {
    	this.secretManagerClient = secretManagerClient;
	}

    /**
     * Get the secret value
     * @param secretName
     * @return SecretManagerResponseBody
     */
    @Override
	public SecretManagerResponseBody getSecretValue(String secretName) {
		GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest().withSecretId(secretName);
        GetSecretValueResult getSecretValueResponse = null;
        SecretManagerResponseBody secretResponse = null;

		try {
			getSecretValueResponse = secretManagerClient.getSecretValue(getSecretValueRequest);

			if (getSecretValueResponse == null) {
				LOG.info("getSecretValueResponse is null \n Secret Name : {} \t Secret Value : {}\n", secretName,
						"null");
				return secretResponse;
			}

			// Decrypted secret using the associated KMS CMK
			String secretResponseJson = getSecretValueResponse.getSecretString();
			if (secretResponseJson != null) {
				secretResponse = new ObjectMapper().readValue(secretResponseJson, SecretManagerResponseBody.class);
			}

		} catch (ResourceNotFoundException e) {
			LOG.error("The requested secret : {} was not found , error : {} ", secretName,
					ExceptionUtils.getStackTrace(e));
		} catch (InvalidRequestException e) {
			LOG.error("The request was invalid due to : {} ", ExceptionUtils.getStackTrace(e));
		} catch (InvalidParameterException e) {
			LOG.error("The request had invalid params : {}", ExceptionUtils.getStackTrace(e));
		} catch (JsonProcessingException e) {
			LOG.error("Error inside AwsSecretManagerService getSecretValue() method: {}", e.getOriginalMessage());
		} catch (Exception e) {
			LOG.error("The request had error : {} ", e.getMessage());
		}

		return secretResponse;
	}
}
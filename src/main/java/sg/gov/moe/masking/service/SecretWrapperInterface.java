package sg.gov.moe.masking.service;

import sg.gov.moe.masking.model.SecretManagerResponseBody;

public interface SecretWrapperInterface {

	SecretManagerResponseBody getSecretValue(String secretName);

}
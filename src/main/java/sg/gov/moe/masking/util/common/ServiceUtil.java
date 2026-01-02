package sg.gov.moe.masking.util.common;

public class ServiceUtil {

    public static String getEnvBasedSecretName(String baseName, String environment) {
        if (environment == null || environment.isEmpty()) {
            return baseName;
        }
        return environment + "_" + baseName;
    }
}
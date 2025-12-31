package sg.gov.moe.masking.model;

import org.springframework.stereotype.Component;

@Component
public class SecretManagerResponseBody {

	private String username;
	private String password;
	private String engine;
	private String host;
	private String port;
	private String dbClusterIdentifier;
	private String accessKeyId;
	private String secretAccessKey;
    private String hashSalt;

    public String getHashSalt() {
        return hashSalt;
    }

    public void setHashSalt(String hashSalt) {
        this.hashSalt = hashSalt;
    }
	/**
	 * Get the password
	 *
	 * @return String
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Set the password
	 *
	 * @param password
	 * @return void
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Get the username
	 *
	 * @return String
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Set the username
	 *
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Get the engine
	 *
	 * @return String
	 */
	public String getEngine() {
		return engine;
	}

	/**
	 * Set the engine
	 *
	 * @param engine
	 */
	public void setEngine(String engine) {
		this.engine = engine;
	}

	/**
	 * Get the host
	 *
	 * @return String
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Set the host
	 *
	 * @param host
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * Get the port
	 *
	 * @return String
	 */
	public String getPort() {
		return port;
	}

	/**
	 * Set the port
	 *
	 * @param port
	 */
	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * Get the db cluster identifier
	 *
	 * @return String
	 */
	public String getDbClusterIdentifier() {
		return dbClusterIdentifier;
	}

	/**
	 * Set the db cluster identifier
	 *
	 * @param dbClusterIdentifier
	 */
	public void setDbClusterIdentifier(String dbClusterIdentifier) {
		this.dbClusterIdentifier = dbClusterIdentifier;
	}

	/**
	 * Get the access key id
	 *
	 * @return String
	 */
	public String getAccessKeyId() {
		return accessKeyId;
	}

	/**
	 * Set the access key id
	 *
	 * @param accessKeyId
	 */
	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	/**
	 * Get the secret access key
	 *
	 * @return String
	 */
	public String getSecretAccessKey() {
		return secretAccessKey;
	}

	/**
	 * Set the secret access key
	 *
	 * @param secretAccessKey
	 */
	public void setSecretAccessKey(String secretAccessKey) {
		this.secretAccessKey = secretAccessKey;
	}

    //add toString method for printing
    @Override
    public String toString() {
        return "SecretManagerResponseBody{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", engine='" + engine + '\'' +
                ", host='" + host + '\'' +
                ", port='" + port + '\'' +
                ", dbClusterIdentifier='" + dbClusterIdentifier + '\'' +
                ", accessKeyId='" + accessKeyId + '\'' +
                ", secretAccessKey='" + secretAccessKey + '\'' +
                ", hashSalt='" + hashSalt + '\'' +
                '}';
    }

}
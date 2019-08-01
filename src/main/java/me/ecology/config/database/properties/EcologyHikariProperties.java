package me.ecology.config.database.properties;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@ConfigurationProperties(prefix = "spring.datasource.ecology")
public class EcologyHikariProperties extends DataSourceProperties implements HikariProperties {
	private int connectionTimeout;
	private int validationTimeout;
	private int idleTimeout;
	private int maxLifetime;
	private int maximumPoolSize;
	private int minimumIdle;
	private String connectionTestQuery;
	private boolean autoCommit;
	private boolean readOnly = false;
}

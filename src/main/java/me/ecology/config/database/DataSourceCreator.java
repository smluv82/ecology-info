package me.ecology.config.database;

import java.util.function.Function;
import java.util.function.Supplier;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.stereotype.Component;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;
import me.ecology.config.database.properties.HikariProperties;

@Slf4j
@Component
public class DataSourceCreator {
	private final Supplier<HikariConfig> dataSourceConfigSupplier = HikariConfig::new;
	private final Function<HikariConfig, HikariDataSource> dataSourceFunction = HikariDataSource::new;

	public DataSource createDataSource(final DataSourceProperties properties, final HikariProperties connectionProperties) {
		final HikariConfig dataSourceConfig = dataSourceConfigSupplier.get();

		try {
			dataSourceConfig.setJdbcUrl(properties.getUrl());
			dataSourceConfig.setUsername(properties.getUsername());
			dataSourceConfig.setPassword(properties.getPassword());
			dataSourceConfig.setDriverClassName(properties.getDriverClassName());
			dataSourceConfig.setConnectionTimeout(connectionProperties.getConnectionTimeout());
			dataSourceConfig.setValidationTimeout(connectionProperties.getValidationTimeout());
			dataSourceConfig.setIdleTimeout(connectionProperties.getIdleTimeout());
			dataSourceConfig.setMaxLifetime(connectionProperties.getMaxLifetime());
			dataSourceConfig.setMaximumPoolSize(connectionProperties.getMaximumPoolSize());
			dataSourceConfig.setMinimumIdle(connectionProperties.getMinimumIdle());
			dataSourceConfig.setConnectionTestQuery(connectionProperties.getConnectionTestQuery());
			dataSourceConfig.setAutoCommit(connectionProperties.isAutoCommit());
			dataSourceConfig.setReadOnly(connectionProperties.isReadOnly());

			dataSourceConfig.addDataSourceProperty("cachePrepStmts", "true");
			dataSourceConfig.addDataSourceProperty("prepStmtCacheSize", "250");
			dataSourceConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

			return dataSourceFunction.apply(dataSourceConfig);
		}catch(Exception e) {
			log.error("createDataSource Exception : ", e);
			return null;
		}
	}
}

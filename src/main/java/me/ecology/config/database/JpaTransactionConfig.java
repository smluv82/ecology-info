package me.ecology.config.database;

//@RequiredArgsConstructor
//@EnableConfigurationProperties({EcologyHikariProperties.class})
//@EnableTransactionManagement
//@EnableJpaRepositories(basePackages = {"me.ecology.app.*.repository"}, entityManagerFactoryRef = "ecologyEntityManagerFactory", transactionManagerRef = "ecologyJpaTxManager")
//@EntityScan(basePackages = {"me.ecology.vo.*"})
//@Configuration
public class JpaTransactionConfig {
//	private final DataSourceCreator dataSourceCreator;

//	@Bean(name = "ecologyDataSource", destroyMethod = "close")
//	public DataSource ecologyDataSource(final EcologyHikariProperties ecologyHikariProperties) {
//		return dataSourceCreator.createDataSource(ecologyHikariProperties, ecologyHikariProperties);
//	}
//
//	@Primary
//	@Bean(name="ecologyJpaTxManager")
//	public PlatformTransactionManager ecologyJpaTxManager(@Qualifier("ecologyEntityManagerFactory") EntityManagerFactory cologyEntityManagerFactory) {
//		JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
//		jpaTransactionManager.setEntityManagerFactory(cologyEntityManagerFactory);
//		return jpaTransactionManager;
//	}
//
//	@Bean(name="ecologyEntityManagerFactory")
//	public LocalContainerEntityManagerFactoryBean ecologyEntityManagerFactory(@Qualifier("ecologyDataSource") DataSource ecologyDataSource, @Qualifier("jpaVendorAdapter") HibernateJpaVendorAdapter jpaVendorAdapter) {
//		final LocalContainerEntityManagerFactoryBean lef = new LocalContainerEntityManagerFactoryBean();
//		lef.setDataSource(ecologyDataSource);
//		lef.setPackagesToScan("me.ecology.vo.*");
//		lef.setJpaVendorAdapter(jpaVendorAdapter);
//		lef.setPersistenceUnitName("ecologyJpa");
//
////		final Properties jpaProperties = new Properties();
////		jpaProperties.setProperty("hibernate.show_sql", "true");
////		jpaProperties.setProperty("hibernate.show_sql", "true");
////		lef.setJpaProperties(jpaProperties);
//		return lef;
//	}
//
//	@Bean(name="jpaVendorAdapter")
//	public HibernateJpaVendorAdapter jpaVendorAdapter() {
//		final HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
//		jpaVendorAdapter.setShowSql(true);
//		jpaVendorAdapter.setGenerateDdl(false);
//		jpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQL5InnoDBDialect");
//		return jpaVendorAdapter;
//	}
}

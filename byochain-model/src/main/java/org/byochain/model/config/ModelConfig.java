package org.byochain.model.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * Spring Boot configuration class to define dataSource for "production" profile.
 * @author Giuseppe Vincenzi
 *
 */
@Configuration
@ComponentScan(basePackages = { "org.byochain.model" })
@EnableJpaRepositories("org.byochain.model.repository")
@Profile("production")
public class ModelConfig {
	@Configuration
    @Profile("default")
    @PropertySource("classpath:application-model.properties")
    static class Defaults
    {}
	
	@Configuration
    @Profile("test")
    @PropertySource("classpath:application-model-test.properties")
    static class Test
    {}

    @Configuration
    @Profile("production")
    @PropertySource("classpath:application-model-production.properties")
    static class Production
    {}
    
	@Value("${db.jdbcurl}")
	private String url;
	
	@Value("${db.username}")
	private String username;
	
	@Value("${db.password}")
	private String password;
	
	@Value("${db.driver}")
	private String driver;
	
	/**
	 * DataSource loaded by properties in application.properties file 
	 * @return DriverManagerDataSource
	 */
	@Bean
	public DriverManagerDataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource(url, username, password);
		dataSource.setDriverClassName(driver);
		return dataSource;
	}

}

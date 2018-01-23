package org.byochain.services.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
public class ServiceConfig {
	@Configuration
    @Profile("default")
    @PropertySource("classpath:application-service.properties")
    static class Defaults
    {}
	
	@Configuration
    @Profile("test")
    @PropertySource("classpath:application-service-test.properties")
    static class Test
    {}

    @Configuration
    @Profile("production")
    @PropertySource("classpath:application-service-production.properties")
    static class Production
    {}
}

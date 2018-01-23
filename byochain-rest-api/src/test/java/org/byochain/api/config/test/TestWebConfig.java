package org.byochain.api.config.test;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"org.byochain.services","org.byochain.api","org.byochain.model"})
@PropertySource(value = {"classpath:application-service-test.properties", "classpath:application-model-test.properties"})
@Profile("test")
public class TestWebConfig extends WebMvcConfigurerAdapter {

}
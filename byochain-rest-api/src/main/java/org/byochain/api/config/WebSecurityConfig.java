package org.byochain.api.config;

import org.byochain.commons.encoder.BYOChainPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.google.common.collect.ImmutableList;

/**
 * Spring Boot configuration class to define Authentication beans for "production" profile.
 * @author Giuseppe Vincenzi
 *
 */
@Configuration
@EnableWebSecurity
@Profile("production")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	DriverManagerDataSource dataSource;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
				.passwordEncoder(passwordEncoder())
				.usersByUsernameQuery("select username,password,enabled from user where username=?")
				.authoritiesByUsernameQuery(
						"select user.username as username,role.role as role from user_roles "
						+ "inner join role on role.role_id=user_roles.role_id "
						+ "inner join user on user.user_id=user_roles.user_id "
						+ "where username=?");
		auth.eraseCredentials(false);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.cors().and()
		.authorizeRequests()
		.antMatchers("/api/v1/blocks/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
		.antMatchers("/api/v1/admin/**").access("hasRole('ROLE_ADMIN')")
		.antMatchers("/api/v1/certifications/admin/**").access("hasRole('ROLE_ADMIN')")
		.antMatchers("/api/v1/certifications/token/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
		.antMatchers("/api/v1/certifications/check/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
		.antMatchers("/web/public/**").permitAll()
		.and().httpBasic().realmName(BYOChainBasicAuthenticationEntryPoint.REALM).authenticationEntryPoint(new BYOChainBasicAuthenticationEntryPoint())
		.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	@Bean
	public PasswordEncoder passwordEncoder(){
		PasswordEncoder encoder = new BYOChainPasswordEncoder();
		return encoder;
	}
	
	@Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(ImmutableList.of("*"));
        configuration.setAllowedMethods(ImmutableList.of("HEAD",
                "GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(ImmutableList.of("Authorization", "Cache-Control", "Content-Type"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

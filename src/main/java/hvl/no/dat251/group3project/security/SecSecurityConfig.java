package hvl.no.dat251.group3project.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecSecurityConfig extends WebSecurityConfigurerAdapter {

	public SecSecurityConfig() {
		super();
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
		.antMatchers("/oauth_login", "/h2-console/*", "/styles.css", "/fonts/*", "/images/*", "/map")
		.permitAll() // permit all to access some sites
		.anyRequest().authenticated() // Needs authentication to access other sites
		// Login method, login screen, successful login screen
		.and().oauth2Login().loginPage("/oauth_login").defaultSuccessUrl("/userAdd", true).and().headers()
		.frameOptions().disable(); // Necessary for h2-console for debugging
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
package com.example.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.jwt.AuthEntryPointJwt;
import com.example.jwt.AuthTokenFilter;
import com.example.service.impl.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan({ "com.example.jwt" })
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	@Override
	public void configure(final AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable()
			.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.authorizeRequests()
			.antMatchers("/api/auth/**").anonymous()
			.antMatchers("/api/user/info/**").permitAll()
			.antMatchers("/api/user/follow").hasRole("USER")
			.antMatchers("/api/user/unfollow").hasRole("USER")
			.antMatchers("/api/user/favorites").hasRole("USER")
			.antMatchers("/api/user/new").hasRole("USER")
			.antMatchers("/api/user/posts/**").permitAll()
			.antMatchers("/api/user/subscriptions/**").permitAll()
			.antMatchers("/api/user/subscribers/**").permitAll()
			.antMatchers("/api/user/profile_picture").hasRole("USER")
			.antMatchers("/api/user/reset_profile_picture").hasRole("USER")
			.antMatchers("/api/user/profile_picture/**").permitAll()
			.antMatchers("/api/publication/delete/**").hasRole("USER")
			.antMatchers("/api/publication/like").hasRole("USER")
			.antMatchers("/api/publication/dislike").hasRole("USER")
			.antMatchers("/api/publication/create").hasRole("USER")
			.antMatchers("/api/publication/view/**").permitAll()
			.antMatchers("/api/publication/**").permitAll()
			.antMatchers("/api/comment/add").hasRole("USER")
			.antMatchers("/api/comment/delete/**").hasRole("USER")
			.antMatchers("/api/comment/all/**").permitAll()
			.anyRequest().authenticated();

		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}
}

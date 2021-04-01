package com.example.cloudinaction.config;

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@Profile("!test")
@Configuration
@EnableWebSecurity
@ComponentScan(basePackageClasses = KeycloakSecurityComponents.class)
public class KeycloakOauthSecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

    private static final String WRITE = "write";
    private static final String READ = "read";

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {

        KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
        auth.authenticationProvider(keycloakAuthenticationProvider);
    }

    @Bean
    public KeycloakSpringBootConfigResolver KeycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }


    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/product*").hasRole(READ)
                .antMatchers(HttpMethod.GET,"/products/*").hasRole(READ)
                .antMatchers(HttpMethod.GET,"/category*").hasRole(READ)
                .antMatchers(HttpMethod.GET,"/categories*").hasRole(READ)
                .antMatchers(HttpMethod.POST, "/product/**").hasRole(WRITE)
                .antMatchers(HttpMethod.PUT, "/product*").hasRole(WRITE)
                .antMatchers(HttpMethod.DELETE, "/product*").hasRole(WRITE)
                .antMatchers(HttpMethod.POST,"/category*").hasRole(WRITE)
                .antMatchers(HttpMethod.PUT,"/category*").hasRole(WRITE)
                .antMatchers(HttpMethod.DELETE,"/category*").hasRole(WRITE)
                .antMatchers(HttpMethod.GET,"/categories*").hasRole(WRITE)
                .anyRequest()
                .permitAll();
    }
}

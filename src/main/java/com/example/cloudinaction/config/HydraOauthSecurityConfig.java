package com.example.cloudinaction.config;

import java.net.InetAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.server.resource.introspection.NimbusOpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.web.client.RestTemplate;

@Profile("!test")
public class HydraOauthSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String HTTP_PROTOCOL = "http://";

    @Value("${spring.security.oauth2.resourceserver.opaque.introspection-uri}")
    String introspectionEndpoint;

    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests(authorize -> authorize
                        .antMatchers("/swagger-ui/**").permitAll()
                        .antMatchers("/swagger-resources/**").permitAll()
                        .antMatchers("/v2/api-docs/**").permitAll()
                        .antMatchers("/actuator/**").permitAll()
                        .antMatchers("/category/**").authenticated()
                        .antMatchers("/product/**").authenticated()
                        .antMatchers("/products/**").authenticated()
                        .antMatchers("/categories/**").authenticated()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer()
                .opaqueToken()
                .introspector(customIntrospector());
    }

    @Bean
    public OpaqueTokenIntrospector customIntrospector() {
        String hostName = InetAddress.getLoopbackAddress().getHostName();
        String introspectionUrl = HTTP_PROTOCOL + hostName + ":" + introspectionEndpoint;
        return new NimbusOpaqueTokenIntrospector(introspectionUrl, restTemplate());
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

package com.example.cloudinaction.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Profile("test")
@Configuration
@Order(200)
public class TestSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    public  void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/**");
    }
}

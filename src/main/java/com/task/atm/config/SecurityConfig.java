package com.task.atm.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // todo implement security
    @Override
    protected void configure(HttpSecurity security) throws Exception
    {
        security.httpBasic()
                .disable()
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/**")
                .permitAll();
    }
}
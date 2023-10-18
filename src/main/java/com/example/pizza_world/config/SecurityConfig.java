package com.example.pizza_world.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers( "/bucket", "/feedback", "/personalPage").hasAnyRole("USER", "ADMIN")
                .antMatchers("/registration", "/home").permitAll()
                .anyRequest().authenticated()
                .and().formLogin()
                .loginPage("/login").permitAll()
                .loginProcessingUrl("/login")
                .usernameParameter("login")
                .passwordParameter("password")
                .defaultSuccessUrl("/home")
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/home").permitAll();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/static/**", "/css/**", "/error");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }
}

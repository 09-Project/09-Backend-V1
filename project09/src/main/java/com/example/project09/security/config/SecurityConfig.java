package com.example.project09.security.config;

import com.example.project09.error.ExceptionHandlerFilter;
import com.example.project09.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider tokenProvider;
    private final ExceptionHandlerFilter exceptionHandlerFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .formLogin().disable()

                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/member/auth/signup").permitAll()
                .antMatchers("/member/auth/login").permitAll()
                .antMatchers("/member/auth/reissue").permitAll()

                .antMatchers("/post/").permitAll()
                .antMatchers("/post/search").permitAll()
                .antMatchers("/post/{post-id}").permitAll()

                .antMatchers("/member/profile/{user-id}").permitAll()
                .anyRequest().authenticated()

                .and()
                .apply(new FilterConfig(tokenProvider, exceptionHandlerFilter));
    }

}

package com.usyd.capstone.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http

                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/public/**").permitAll() // 公开访问的API
                .antMatchers("/user/**").permitAll()
                .antMatchers("/paypal/**").permitAll()
                // 放行所有的websocket请求
                .antMatchers("/imserver/**").permitAll()
                .antMatchers("/notification/**").permitAll()
//                   .antMatchers("/user/registration").permitAll()
                .antMatchers("/member/**").hasRole("USER") // 需要USER角色才能访问的URL
                .antMatchers("/admin/**").hasRole("ADMIN") // 需要ADMIN角色才能访问的URL
                .antMatchers("/superadmin/**").hasRole("SUPERADMIN") // 需要SUPERADMIN角色才能访问的URL

                .anyRequest().authenticated() // 其他URL需要登录才能访问
                .and()
                // cors跨域
                .cors()
                .and()
                .formLogin()
                .loginPage("/login") // 登录页面的URL
                .permitAll()
                .and()
                .logout()
                .permitAll();

        http.addFilterBefore(new JwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
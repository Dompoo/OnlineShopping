package com.dompoo.onlineshopping.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize ->
                    authorize
                            .requestMatchers(new AntPathRequestMatcher("/auth/login")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
                            .anyRequest().authenticated()
                )
                .csrf((csrf) -> csrf.ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")).disable())
                .headers((headers) -> headers.addHeaderWriter(new XFrameOptionsHeaderWriter(SAMEORIGIN)))
                .formLogin((formLogin) -> formLogin
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/auth/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/"))
                .rememberMe(rm -> rm
                        .rememberMeParameter("remember")
                        .alwaysRemember(false)
                        .tokenValiditySeconds(2592000))
                .userDetailsService(customUserDetailsService());
//                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public UserDetailsService customUserDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        UserDetails user = User.withUsername("dompoo")
                .password("1234")
                .roles("ADMIN")
                .build();

        manager.createUser(user);

        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}

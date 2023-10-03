package com.dompoo.onlineshopping.config;

import com.dompoo.onlineshopping.domain.User;
import com.dompoo.onlineshopping.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(new AntPathRequestMatcher("/auth/login")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/auth/signup")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/user")).hasRole("USER")
                        .requestMatchers(new AntPathRequestMatcher("/admin")).hasRole("ADMIN")
                        .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
                        .anyRequest().authenticated())
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
                        .tokenValiditySeconds(2592000));

        return http.build();
    }

    @Bean
    public UserDetailsService customUserDetailsService(UserRepository userRepository) {
        return username -> {
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException(username + "을 찾을 수 업습니다."));
            return new UserPrincipal(user);
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

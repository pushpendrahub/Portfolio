package com.portfolio.pushpendra.config;

import com.portfolio.pushpendra.admin.service.LoginService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // ✅ Allow static resources to be ignored by security
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                "/assets/**",
                "/css/**",
                "/js/**",
                "/img/**",
                "/vendor/**",
                "/fonts/**",
                "/favicon.ico"
        );
    }

    @Bean
    public FilterRegistrationBean<OncePerRequestFilter> logFilter() {
        FilterRegistrationBean<OncePerRequestFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                System.out.println("Incoming request: " + request.getRequestURI());
                filterChain.doFilter(request, response);
            }
        });
        registration.setOrder(0);
        return registration;
    }


    // ✅ Main filter chain configuration
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
//                .csrf(csrf -> csrf
//                        .ignoringRequestMatchers("/admin/**","/index/book-slot") // Ignore CSRF for admin APIs if needed
//                )
                // ❌ Disable CSRF completely
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/api/quote","/", "/index", "/sendMail", "/admin/login", "/access-denied","/index/book-slot","/chat/message").permitAll()

                        // Add fragment/template endpoints here if used in index.html
                        .requestMatchers("/assets/**",
                                "/css/**",
                                "/js/**",
                                "/img/**",
                                "/vendor/**",
                                "/fonts/**",
                                "/favicon.ico"
                                ).permitAll()

                        // Admin-only
                        .requestMatchers("/admin/dashboard").hasAuthority("ADMIN")

                        // All others require auth
                        .anyRequest().authenticated()
                )

                .logout(logout -> logout
                        .logoutUrl("/admin/logout")
                        .logoutSuccessUrl("/admin/login?logout")
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            if (!response.isCommitted()) {
                                response.sendRedirect("/admin/login");
                            }
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            if (!response.isCommitted()) {
                                request.getRequestDispatcher("/access-denied").forward(request, response);
                            }
                        })
                );

        return http.build();
    }

    // ✅ Custom user details service (your login logic)
    @Bean
    public UserDetailsService userDetailsService() {
        return new LoginService();
    }

    // ✅ BCrypt password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ✅ Authentication manager setup
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
                                                       PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }

}

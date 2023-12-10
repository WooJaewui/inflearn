package io.security.basicsecurity;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Lazy @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public UserDetailsManager users() {
        final UserDetails user = User.builder()
                .username("user")
                .password("{noop}1111")
                .roles("USER")
                .build();

        final UserDetails sys = User.builder()
                .username("sys")
                .password("{noop}1111")
                .roles("SYS")
                .build();

        final UserDetails admin = User.builder()
                .username("admin")
                .password("{noop}1111")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, sys, admin);
    }

    @Bean
    @Order(1)
    public SecurityFilterChain filterChain1(HttpSecurity http) throws Exception {

        // 인가 API
        http.authorizeHttpRequests(
                requestMatcherRegistry -> requestMatcherRegistry
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/user").hasRole("USER")
                        .requestMatchers("/admin/pay").hasRole("ADMIN")
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN", "SYS")
                        .requestMatchers("/test").anonymous()
                        .anyRequest().authenticated()
        );


        // 인증 API
        http.formLogin(
                formLoginConfigurer -> formLoginConfigurer
                        //.loginPage("/loginPage")
                        .defaultSuccessUrl("/")
                        .failureUrl("/login")
                        .usernameParameter("userId")
                        .passwordParameter("passwd")
                        .loginProcessingUrl("/login_proc")
                        .successHandler((request, response, authentication) -> {
                            System.out.println("authentication = " + authentication.getName());
                            final HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
                            final SavedRequest savedRequest = requestCache.getRequest(request, response);
                            if (savedRequest == null || savedRequest.getRedirectUrl() == null) {
                                response.sendRedirect("/");
                                return;
                            }
                            response.sendRedirect(savedRequest.getRedirectUrl());
                        })
                        .failureHandler((request, response, exception) -> {
                            System.out.println("exception = " + exception.getMessage());
                            response.sendRedirect("/login");
                        })
                        .permitAll()
        );

        // 로그아웃
        http.logout(
                logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .addLogoutHandler((request, response, authentication) -> {
                            final HttpSession session = request.getSession();
                            session.invalidate();
                        })
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.sendRedirect("/login");
                        })
                        .deleteCookies("remember-me")
        );

        // remember-me
        http.rememberMe(
                remember-> remember
                        .rememberMeParameter("remember")            // 기본 파라미터명은 remember-me
                        .tokenValiditySeconds(3600)                 // remember-me 만료시간(기본값 14일)
                        .alwaysRemember(true)                       // 항상 remember-me 적용
                        .userDetailsService(userDetailsService)     // userDetailsService 클래스를 넣어줘야 함
        );

        // 세션 관리
        http.sessionManagement(
                session -> session
                        .sessionFixation().changeSessionId()
                        //.sessionCreationPolicy(SessionCreationPolicy.NEVER)
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                        .expiredUrl("/")
        );

        // 인증, 인가 ExceptionHandling
        http.exceptionHandling(
                exceptionHandling -> exceptionHandling
                        /*.authenticationEntryPoint((request, response, authException) -> {
                            response.sendRedirect("/login");
                        })*/
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.sendRedirect("/denied");
                        })
        );


        // SecurityContextHolder 전략 변경
        // SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);

        return http.build();
    }

    @Bean
    public SecurityFilterChain filterChain2(HttpSecurity http) throws Exception {
        // 인가 API
        http.authorizeHttpRequests(
                requestMatcherRegistry -> requestMatcherRegistry
                        .requestMatchers("/test").anonymous()
                        .anyRequest().authenticated()
        );

        return http.build();
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

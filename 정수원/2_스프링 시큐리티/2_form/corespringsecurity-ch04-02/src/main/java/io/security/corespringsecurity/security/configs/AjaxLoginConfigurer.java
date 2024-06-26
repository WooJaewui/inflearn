package io.security.corespringsecurity.security.configs;

import io.security.corespringsecurity.security.filter.AjaxLoginProcessingFilter;
import io.security.corespringsecurity.security.handler.AjaxAuthenticationFailureHandler;
import io.security.corespringsecurity.security.handler.AjaxAuthenticationSuccessHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class AjaxLoginConfigurer<H extends HttpSecurityBuilder<H>>
        extends AbstractAuthenticationFilterConfigurer<H, AjaxLoginConfigurer<H>, AjaxLoginProcessingFilter> {

    private AuthenticationSuccessHandler successHandler;
    private AuthenticationFailureHandler failureHandler;
    private AuthenticationManager authenticationManager;

    public AjaxLoginConfigurer() {
        super(new AjaxLoginProcessingFilter(), null);
    }

    @Override
    public void init(final H http) throws Exception {
        super.init(http);
    }

    @Override
    public void configure(final H http) throws Exception {
        if (authenticationManager == null) {
            authenticationManager = http.getSharedObject(AuthenticationManager.class);
        }
        getAuthenticationFilter().setAuthenticationManager(authenticationManager);
        getAuthenticationFilter().setAuthenticationSuccessHandler(successHandler);
        getAuthenticationFilter().setAuthenticationFailureHandler(failureHandler);

        SessionAuthenticationStrategy strategy = http.getSharedObject(SessionAuthenticationStrategy.class);

        if (strategy != null) {
            getAuthenticationFilter().setSessionAuthenticationStrategy(strategy);
        }

        RememberMeServices rememberMeServices = http.getSharedObject(RememberMeServices.class);

        if (rememberMeServices != null) {
            getAuthenticationFilter().setRememberMeServices(rememberMeServices);
        }

        http.setSharedObject(AjaxLoginProcessingFilter.class, getAuthenticationFilter());
        http.addFilterBefore(getAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    public AjaxLoginConfigurer<H> successHandlerAjax(AuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
        return this;
    }

    public AjaxLoginConfigurer<H> failureHandlerAjax(AuthenticationFailureHandler failureHandler) {
        this.failureHandler = failureHandler;
        return this;
    }

    public AjaxLoginConfigurer<H> setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        return this;
    }

    @Override
    protected RequestMatcher createLoginProcessingUrlMatcher(final String s) {
        return new AntPathRequestMatcher(super.getLoginPage(), "POST");
    }
}

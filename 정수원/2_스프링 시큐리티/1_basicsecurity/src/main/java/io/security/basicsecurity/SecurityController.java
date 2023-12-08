package io.security.basicsecurity;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Supplier;

@RestController
public class SecurityController {
    @GetMapping("/")
    public String index(HttpSession session) {
        /*final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final SecurityContext context =
                (SecurityContext) session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);

        final Authentication sessionAuthentication = context.getAuthentication();
        System.out.println(authentication.equals(sessionAuthentication));*/

        return "home";
    }

    @GetMapping("/thread")
    public String thread() {
        new Thread(
                () -> {
                    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    System.out.println(authentication);
                }
        ).start();

        return "thread";
    }

    @GetMapping("/loginPage")
    public String loginPage() {
        return "loginPage";
    }

    @GetMapping("/user")
    public String user() {
        return "user";
    }

    @GetMapping("/admin/pay")
    public String adminPay() {
        return "adminPay";
    }

    @GetMapping("/admin/**")
    public String adminAll() {
        return "adminAll";
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/denied")
    public String denied() {
        return "denied";
    }
}

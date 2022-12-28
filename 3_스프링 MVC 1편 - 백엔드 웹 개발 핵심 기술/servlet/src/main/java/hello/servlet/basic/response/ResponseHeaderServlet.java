package hello.servlet.basic.response;

import org.apache.catalina.connector.Response;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintStream;

@WebServlet(name = "responseHeaderServlet", urlPatterns = "/response-header")
public class ResponseHeaderServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // [status-line]
        response.setStatus(Response.SC_BAD_REQUEST);

        // [response-headers]
        responseHeaders(response);

        // [헤더 편의 메서드]
        // response.setContentLength(10);


        // [쿠키 관련]
        responseCookie(response);


        // [redirect]
        /*response.setStatus(Response.SC_FOUND);
        response.setHeader("Location", "/basic/hello-form.html");*/

        /*response.sendRedirect("/basic/hello-form.html");*/

        response.getWriter().println("성공");
        response.getWriter().write("성공2");

    }

    private void responseCookie(HttpServletResponse response) {
        response.setHeader("Set-Cookie", "myCookie=good; Max-age=600");

        Cookie cookie = new Cookie("myCookie", "good");
        cookie.setMaxAge(600);
        response.addCookie(cookie);
    }

    private void responseHeaders(HttpServletResponse response) {
        response.setHeader("Content-Type", "text/plain; charset=utf-8");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("my-header", "hello");
    }
}

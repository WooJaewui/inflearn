package hello.servlet.basic.request;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@WebServlet(name= "requestHeaderServlet" , urlPatterns = "/request-header")
public class RequestHeaderServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String method = request.getMethod();
        String uri = request.getRequestURI();
        StringBuffer url = request.getRequestURL();

        Enumeration<String> headerNames = request.getHeaderNames();

        System.out.println("------------header start--------------------");

        while(headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            System.out.println("headerName = " + headerName);
        }

        System.out.println("------------header start2--------------------");

        request.getHeaderNames().asIterator()
                        .forEachRemaining(header -> System.out.println("header = " + header));


        System.out.println("------------header end--------------------");

        System.out.println("method = " + method);

        request.getLocales().asIterator()
                .forEachRemaining(locale -> System.out.println("locale = " + locale));


        String contentType = request.getContentType();
        String length = String.valueOf(request.getContentLength());
        String characterEncoding =  request.getCharacterEncoding();

        content(contentType, length, characterEncoding);


    }

    private void content(String contentType, String length, String characterEncoding) {
        System.out.println("characterEncoding = " + characterEncoding);
        System.out.println("length = " + length);
        System.out.println("contentType = " + contentType);
    }


}

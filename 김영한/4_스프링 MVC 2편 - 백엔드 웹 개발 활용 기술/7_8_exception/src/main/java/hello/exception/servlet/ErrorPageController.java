package hello.exception.servlet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
public class ErrorPageController {

    static final String FORWARD_QUERY_STRING = "javax.servlet.forward.query_string";
    static final String INCLUDE_REQUEST_URI = "javax.servlet.include.request_uri";
    static final String INCLUDE_CONTEXT_PATH = "javax.servlet.include.context_path";
    public static final String ERROR_EXCEPTION = "javax.servlet.error.exception";
    public static final String ERROR_STATUS_CODE = "javax.servlet.error.status_code";

    @RequestMapping("/error-page/404")
    public String errorPage404(HttpServletRequest request, HttpServletResponse response) {
        log.info("errorPage 404");
        printErrorInfo(request);
        return "error-page/404";
    }

    @RequestMapping("/error-page/500")
    public String errorPage500(HttpServletRequest request, HttpServletResponse response) {
        log.info("errorPage 500");
        printErrorInfo(request);
        return "error-page/500";
    }


    @RequestMapping(value = "/error-page/500", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> errorPage500Api(HttpServletRequest request, HttpServletResponse response) {

        log.info("API errorPage 500");

        Map<String, Object> result = new HashMap<>();
        Exception ex =(Exception) request.getAttribute(ERROR_EXCEPTION);

        result.put("status", request.getAttribute(ERROR_STATUS_CODE));
        result.put("message", ex.getMessage());

        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        return new ResponseEntity<>(result, HttpStatus.valueOf(statusCode));

    }


    private void printErrorInfo(HttpServletRequest request) {
        log.info("FORWARD_QUERY_STRING", request.getAttribute(FORWARD_QUERY_STRING));
        log.info("INCLUDE_REQUEST_URI", request.getAttribute(INCLUDE_REQUEST_URI));
        log.info("INCLUDE_CONTEXT_PATH", request.getAttribute(INCLUDE_CONTEXT_PATH));
        log.info("dispacherType={}", request.getDispatcherType());
    }

}

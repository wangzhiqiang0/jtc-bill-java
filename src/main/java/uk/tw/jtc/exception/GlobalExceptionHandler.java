package uk.tw.jtc.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import uk.tw.jtc.response.JtcResponse;

import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public JtcResponse globalException(HttpServletResponse response, Exception ex) {

        JtcResponse jtcResponse = JtcResponse.serverError();
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        jtcResponse.setCode(response.getStatus());
        jtcResponse.setMsg(ex.getMessage());
        ex.printStackTrace();
        log.error("Get Exception ,the message is {}",ex.getMessage());
        return jtcResponse;
    }

    @ResponseBody
    @ExceptionHandler(ServletRequestBindingException.class)
    public JtcResponse servletRequestBindingException(HttpServletResponse response, ServletRequestBindingException ex) {

        JtcResponse jtcResponse = JtcResponse.serverError();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        log.error("Get Exception ,the message is {}",ex.getMessage());
        jtcResponse.setCode(response.getStatus());
        jtcResponse.setMsg(ex.getMessage());
        return jtcResponse;
    }

    @ResponseBody
    @ExceptionHandler(JwtException.class)
    public JtcResponse jwtException(HttpServletResponse response, JwtException ex) {

        JtcResponse jtcResponse = JtcResponse.error(ex.getErrorCode());
        response.setStatus(ex.getErrorCode());
        log.error("Get Exception ,the message is {}",ex.getMessage());
        jtcResponse.setCode(response.getStatus());
        jtcResponse.setMsg(ex.getMessage());
        return jtcResponse;
    }

}

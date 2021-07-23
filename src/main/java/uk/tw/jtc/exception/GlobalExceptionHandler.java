package uk.tw.jtc.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import uk.tw.jtc.response.JwtResponse;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public JwtResponse globalException(HttpServletResponse response, Exception ex) {

        JwtResponse jwtResponse = JwtResponse.serverError();
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        jwtResponse.setCode(response.getStatus());
        jwtResponse.setMsg(ex.getMessage());
        return jwtResponse;
    }

}

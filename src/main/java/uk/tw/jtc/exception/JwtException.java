package uk.tw.jtc.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JwtException extends RuntimeException{
    private static final long serialVersionUID = 1L;


    protected String errorCode;

    protected String errorMsg;




}

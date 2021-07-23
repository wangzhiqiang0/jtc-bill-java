package uk.tw.jtc.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JwtResponse<T> {

    private int code;
    private String msg;
    private T data;

    public static <T> JwtResponse<T> ok(T data) {
        return new JwtResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), data);
    }

    public static <T> JwtResponse<T> badRequest() {
        return new JwtResponse<>(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),null);
    }

    public static <T> JwtResponse<T> notFound() {
        return new JwtResponse<>(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(),null);
    }


    public static <T> JwtResponse<T> serverError() {
        return new JwtResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),null);
    }
}

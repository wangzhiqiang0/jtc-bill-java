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
public class JtcResponse<T> {

    private int code;
    private String msg;
    private T data;

    public static <T> JtcResponse<T> ok(T data) {
        return new JtcResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), data);
    }

    public static <T> JtcResponse<T> badRequest() {
        return new JtcResponse<>(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),null);
    }

    public static <T> JtcResponse<T> notFound() {
        return new JtcResponse<>(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(),null);
    }


    public static <T> JtcResponse<T> serverError() {
        return new JtcResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),null);
    }

    public static <T> JtcResponse<T> error(int code) {
        return new JtcResponse<>(code, null,null);
    }
}

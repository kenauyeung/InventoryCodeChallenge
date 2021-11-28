package inventoryCodeChallenge.handler;

import inventoryCodeChallenge.model.RequestResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        BindingResult result = e.getBindingResult();
        List<String> fieldErrors = result.getFieldErrors().stream()
                .map(f -> new String(f.getObjectName() + "::" + f.getField() + "::" + f.getCode()))
                .collect(Collectors.toList());

        return new ResponseEntity(new RequestResponse(RequestResponse.State.ERROR, fieldErrors.toString(), null), headers, HttpStatus.OK);
    }
}

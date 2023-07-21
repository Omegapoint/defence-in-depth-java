package defence.in.depth.config;

import defence.in.depth.domain.exceptions.ProductMarketMismatchException;
import defence.in.depth.domain.exceptions.ProductNotFoundException;
import defence.in.depth.domain.exceptions.ReadProductNotAllowedException;
import defence.in.depth.domain.exceptions.WriteProductNotAllowedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ProductNotFoundException.class, ProductMarketMismatchException.class})
    protected ResponseEntity<Object> handleNotFound(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, "Product not found",
            new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {ReadProductNotAllowedException.class, WriteProductNotAllowedException.class})
    protected ResponseEntity<Object> handleForbidden(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, "Not authorized",
            new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }


}

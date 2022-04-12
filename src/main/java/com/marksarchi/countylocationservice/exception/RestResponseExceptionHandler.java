package com.marksarchi.countylocationservice.exception;





import com.marksarchi.countylocationservice.dto.ResponseWrapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

    private Object AppConstants = "Internal server error";


    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<Object> handle(Exception exception) {

        if (exception instanceof ConstraintViolationException) {
            ConstraintViolationException ex = (ConstraintViolationException) exception;
            List<String> errors = new ArrayList<>();
            for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
                errors.add(
                        violation.getRootBeanClass().getName()
                                + " "
                                + violation.getPropertyPath()
                                + ": "
                                + violation.getMessage());
            }
            ResponseWrapper response =
                    ResponseWrapper.builder()
                            .code(HttpStatus.BAD_REQUEST.value())
                            .message("Constraint validation errors")
                            .data(errors)
                            .build();
            //    return ResponseEntity.status(response.getCode()).body(response);
            return new ResponseEntity<>(response, new HttpHeaders(), response.getCode());
        }
        exception.printStackTrace();
        ResponseWrapper response =
                ResponseWrapper.builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(AppConstants.toString())
                        .data(exception.getMessage())
                        .build();
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(
            DataIntegrityViolationException ex, WebRequest request) {

        ResponseWrapper response =
                ResponseWrapper.builder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message("Constraint validation errors")
                        .data(ex.getMostSpecificCause().getMessage())
                        .build();
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(" method is not supported for this request. Supported methods are ");
        ex.getSupportedHttpMethods().forEach(m -> builder.append(m + " "));

        ResponseWrapper response = ResponseWrapper.builder()
                .code(HttpStatus.METHOD_NOT_ALLOWED.value())
                .message(builder.toString())
                .build();
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        List<String> details = new ArrayList<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }

        ResponseWrapper response =
                ResponseWrapper.builder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message("Bad request")
                        .data(details)
                        .build();
        return ResponseEntity.status(response.getCode()).body(response);
    }

}

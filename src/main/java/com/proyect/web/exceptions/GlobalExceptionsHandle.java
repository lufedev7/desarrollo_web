package com.proyect.web.exceptions;

import com.proyect.web.dtos.otherFuntionalities.ErrorDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;



@ControllerAdvice
public class GlobalExceptionsHandle extends ResponseEntityExceptionHandler{
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails>  checkResourceNotFoundExeption(ResourceNotFoundException exception, WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails,HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(WebException.class)
    public ResponseEntity<ErrorDetails>  checkBlogAppFoundExeption(WebException exception, WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails>  checkGlobalExeption(Exception exception, WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails,HttpStatus.INTERNAL_SERVER_ERROR);

    }
    @Override
    @Nullable
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@SuppressWarnings("null") MethodArgumentNotValidException ex,
            @SuppressWarnings("null") HttpHeaders headers, @SuppressWarnings("null") HttpStatusCode status, @SuppressWarnings("null") WebRequest request) {
                Map<String, String> errors = new HashMap <>();
                ex.getBindingResult().getAllErrors().forEach((error) -> {
                    String nameField = ((FieldError)error).getField();
                    String message = error.getDefaultMessage();
                    errors.put(nameField,message);
                });
                return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
    }
}

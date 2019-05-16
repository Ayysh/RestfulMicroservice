package com.example.restfulservice.exception.handling;

import com.example.restfulservice.exception.NoUsersFoundException;
import com.example.restfulservice.exception.UserNotFoundException;
import com.example.restfulservice.exception.UserUnderAgeException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
@RestController
public class CustomizedExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllException(Exception ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse
                (new Date(), ex.getMessage(), request.getDescription(false)); // clientinfo = false
        return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({UserNotFoundException.class,NoUsersFoundException.class})
    public final ResponseEntity<Object> handleNotFoundException(Exception ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse
                (new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({UserUnderAgeException.class})
    public final ResponseEntity<Object> handleUserUngeAgeException(Exception ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse
                (new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity(exceptionResponse, HttpStatus.NOT_ACCEPTABLE);
    }

   @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
   {
     //  ExceptionResponse exceptionResponse = new ExceptionResponse
       //     (new Date(),ex.getMessage(),ex.getBindingResult().toString());

        ExceptionResponse exceptionResponse = new ExceptionResponse
                (new Date(), "Validation failed",ex.getBindingResult().toString());
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

}

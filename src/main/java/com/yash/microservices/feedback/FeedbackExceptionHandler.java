package com.yash.microservices.feedback;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class FeedbackExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {

        BindingResult bindingResult = ex.getBindingResult();
        FeedbackFieldError feedbackFieldError = new FeedbackFieldError(new Date(),
                "Validation Error",
                bindingResult.getFieldError().getDefaultMessage());
        logger.error("Validation Error" , ex);
        return new ResponseEntity<>(feedbackFieldError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FeedbackNotFoundException.class)
    public final ResponseEntity<Object> handleFeedbackNotFoundException(FeedbackNotFoundException ex, WebRequest request) {
        FeedbackFieldError feedbackFieldError = new FeedbackFieldError(new Date(),
                "Record Not Found",
                 ex.getMessage());
        logger.error("Record Not Found" , ex);
        return new ResponseEntity(feedbackFieldError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        FeedbackFieldError feedbackFieldError = new FeedbackFieldError(new Date(),
                "Server Error",
                ex.getMessage());
        logger.error("Server Error" , ex);
        return new ResponseEntity(feedbackFieldError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

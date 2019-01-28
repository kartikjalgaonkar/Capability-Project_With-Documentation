package com.yash.microservices.feedback;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class FeedbackExceptionHandlerTest {

    @InjectMocks
    private FeedbackExceptionHandler feedbackExceptionHandler;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private WebRequest webRequest;

    @Mock
    private MethodParameter parameter;

    @Test
    public void handleMethodArgumentNotValid() {
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(parameter, bindingResult);
        FieldError fieldError = new FieldError("feedback", "rating", 7,
                false, null, null,
                "Rating should be between 1-5");
        Mockito.when(bindingResult.getFieldError()).thenReturn(fieldError);

        ResponseEntity<Object> responseEntity = feedbackExceptionHandler.handleMethodArgumentNotValid(ex,
                HttpHeaders.EMPTY, HttpStatus.BAD_REQUEST, webRequest);
        FeedbackFieldError feedbackFieldError = (FeedbackFieldError) responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Validation Error", feedbackFieldError.getError());
        assertEquals(fieldError.getDefaultMessage(), feedbackFieldError.getDescription());
    }

    @Test
    public void testFeedbackNotFoundException() {
        FeedbackNotFoundException feedbackNotFoundException = new FeedbackNotFoundException("feedback with id 2 not found");

        ResponseEntity<Object> responseEntity = feedbackExceptionHandler.handleFeedbackNotFoundException(feedbackNotFoundException, webRequest);
        FeedbackFieldError feedbackFieldError = (FeedbackFieldError) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Record Not Found", feedbackFieldError.getError());
        assertEquals("feedback with id 2 not found", feedbackFieldError.getDescription());
    }

    @Test
    public void testHandleAllExceptions() {
        Exception exception = new Exception("DB Down");

        ResponseEntity<Object> responseEntity = feedbackExceptionHandler.handleAllExceptions(exception, webRequest);
        FeedbackFieldError feedbackFieldError = (FeedbackFieldError) responseEntity.getBody();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Server Error", feedbackFieldError.getError());
        assertEquals("DB Down", feedbackFieldError.getDescription());
    }
}

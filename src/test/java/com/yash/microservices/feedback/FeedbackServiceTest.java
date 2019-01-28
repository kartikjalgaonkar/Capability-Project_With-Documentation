package com.yash.microservices.feedback;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Resources;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FeedbackServiceTest {
    FeedbackService feedbackService;

    @Mock
    FeedbackRepository repository;

    @Before
    public void setUp() {
        ModelMapper modelMapper = new ModelMapper();
        feedbackService = new FeedbackService(repository, modelMapper, new FeedbackResourceAssembler(modelMapper));
    }

    @Test
    public void should_return_feedback_based_on_input_id() throws Exception {
        Feedback feedback = new Feedback(1L, "SM75719", "My Application", 5, "This is a test.", new Date());
        when(repository.findById(feedback.getId())).thenReturn(Optional.of(feedback));

        FeedbackResource feedbackResource = feedbackService.getFeedbackById(feedback.getId());

        verifyData(feedback, feedbackResource);
    }

    @Test(expected = FeedbackNotFoundException.class)
    public void should_return_not_found_error_if_feedback_does_not_exists() {
        Long id = 5L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        feedbackService.getFeedbackById(id);
    }

    @Test
    public void should_return_all_feedback_order_by_id_desc() throws Exception {
        List<Feedback> feedback = new ArrayList<>();
        Feedback feedback_1 = new Feedback(1L, "SM75719", "My Application 1", 5, "This is a test 1.", new Date());
        Feedback feedback_2 = new Feedback(2L, "SM75729", "My Application 2", 4, "This is a test 2.", new Date());
        feedback.add(feedback_2);
        feedback.add(feedback_1);
        when(repository.findAllByOrderByIdDesc()).thenReturn(feedback);

        Resources<FeedbackResource> resources = feedbackService.getAllFeedback();

        int size = 0;
        Iterator<FeedbackResource> feedbackResourceIterator = resources.iterator();
        while (feedbackResourceIterator.hasNext()) {
            FeedbackResource feedbackResource = feedbackResourceIterator.next();
            verifyData(feedback.get(size), feedbackResource);
            size++;
        }

        assertEquals(2, size);
    }

    @Test
    public void should_save_feedback() throws Exception {
        Feedback feedback = new Feedback(1L, "SM75719", "My Application", 5, "This is a test.", new Date());
        when(repository.save(any(Feedback.class))).thenReturn(feedback);

        FeedbackResource feedbackResource = feedbackService.createFeedback(new FeedbackDTO());

        verifyData(feedback, feedbackResource);
    }

    private void verifyData(Feedback feedback, FeedbackResource feedbackResource) {
        assertEquals("self", feedbackResource.getId().getRel());
        assertEquals("/feedback/" + feedback.getId(), feedbackResource.getId().getHref());
        assertEquals(feedback.getUsername(), feedbackResource.getUsername());
        assertEquals(feedback.getSource(), feedbackResource.getSource());
        assertEquals(feedback.getRating(), feedbackResource.getRating());
        assertEquals(feedback.getCreatedTime(), feedbackResource.getCreatedTime());
    }
}

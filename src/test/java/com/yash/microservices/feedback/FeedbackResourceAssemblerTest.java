package com.yash.microservices.feedback;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.modelmapper.ModelMapper;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(value = Parameterized.class)
public class FeedbackResourceAssemblerTest {

    @Parameterized.Parameter
    public Feedback feedback;

    @Parameterized.Parameters(name = "feedback")
    public static Object[] data() {
        return new Object[] {
                new Feedback(1L, "SM75719", "My Application", 5, "This is a test.", new Date()),
                new Feedback(2L, "SM75719", "My Application", 4, null, new Date())
        };
    }

    @Test
    public void convertsEntityToResource() {
        FeedbackResourceAssembler assembler = new FeedbackResourceAssembler(new ModelMapper());
        FeedbackResource resource = assembler.toResource(feedback);

        assertEquals(feedback.getUsername(), resource.getUsername());
        assertEquals(feedback.getSource(), resource.getSource());
        assertTrue(feedback.getRating() == resource.getRating());
        assertEquals(feedback.getComment(), resource.getComment());
        assertEquals(feedback.getCreatedTime(), resource.getCreatedTime());
        assertEquals("self", resource.getId().getRel());
        assertEquals("/feedback/" + feedback.getId(), resource.getId().getHref());
    }
}

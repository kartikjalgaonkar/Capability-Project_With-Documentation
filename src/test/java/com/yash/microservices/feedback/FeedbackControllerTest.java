package com.yash.microservices.feedback;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.Resources;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(FeedbackController.class)
public class FeedbackControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FeedbackService feedbackService;

    private final ObjectMapper mapper = new ObjectMapper();
    private final ModelMapper modelMapper = new ModelMapper();
    private final FeedbackResourceAssembler assembler = new FeedbackResourceAssembler(modelMapper);
    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS+0000");

    @Before
    public void setUp() {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    @Test
    public void should_save_feedback() throws Exception {
        Feedback feedback = new Feedback(1L, "SM75719", "My Application", 5, "This is a test.", new Date());

        given(feedbackService.createFeedback(BDDMockito.any(FeedbackDTO.class))).willReturn(assembler.toResource(feedback));

        final ResultActions result = mockMvc.perform(post("/feedback")
                .contentType("application/json")
                .content(mapper.writeValueAsString(modelMapper.map(feedback, FeedbackDTO.class)))
        )
                .andExpect(status().isOk());

        verifyJson(result, feedback, "", "$.");
    }


    @Test
    public void should_return_feedback_based_on_input_id() throws Exception {
        Feedback feedback = new Feedback(1L, "SM75719", "My Application", 5, "This is a test.", new Date());

        given(feedbackService.getFeedbackById(feedback.getId())).willReturn(assembler.toResource(feedback));

        final ResultActions result = mockMvc.perform(get("/feedback/"+feedback.getId())
                .contentType("application/json")
        )
                .andExpect(status().isOk());

        verifyJson(result, feedback, "", "$.");
    }

    @Test
    public void should_return_all_feedback() throws Exception {
        List<Feedback> feedback = new ArrayList<>();
        Feedback feedback_1 = new Feedback(1L, "SM75719", "My Application 1", 5, "This is a test 1.", new Date());
        Feedback feedback_2 = new Feedback(2L, "SM75729", "My Application 2", 4, "This is a test 2.", new Date());

        feedback.add(feedback_2);
        feedback.add(feedback_1);


        given(feedbackService.getAllFeedback()).willReturn(new Resources<>(assembler.toResources(feedback)));

        final ResultActions result = mockMvc.perform(get("/feedback")
                .contentType("application/json")
        )
                .andExpect(status().isOk());

        verifyJson(result, feedback_2, "_embedded.feedbackResourceList[0].", "");
        verifyJson(result, feedback_1, "_embedded.feedbackResourceList[1].", "");
    }

    @Test
    public void should_return_not_found_error_if_feedback_does_not_exists() throws Exception {
        Feedback feedback = new Feedback(1L, "SM75719", "My Application", 5, "This is a test.", new Date());

        given(feedbackService.getFeedbackById(2L)).willThrow(new FeedbackNotFoundException("feedback with id 2 not found"));

        final ResultActions result = mockMvc.perform(get("/feedback/2")
                .contentType("application/json")
        )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error").value("Record Not Found"))
                .andExpect(jsonPath("description").value("feedback with id 2 not found"));
    }

    @Test
    public void should_return_validation_error_if_username_is_empty() throws Exception {
        final ResultActions result = mockMvc.perform(post("/feedback")
                .contentType("application/json")
                .content("{\"username\":\"\",\"source\":\"My Application\",\"rating\":5,\"comment\":\"This is a test.\"}")
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("error").value("Validation Error"))
                .andExpect(jsonPath("description").value("Username must not be blank"));
    }

    @Test
    public void should_return_validation_error_if_username_is_not_valid() throws Exception {
        final ResultActions result = mockMvc.perform(post("/feedback")
                .contentType("application/json")
                .content("{\"username\":\"SM123AB\",\"source\":\"My Application\",\"rating\":5,\"comment\":\"This is a test.\"}")
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("error").value("Validation Error"))
                .andExpect(jsonPath("description").value("Username must be 7 characters long." +
                        " Username should start with letters and followed by numbers." +
                        " Example: Valid {SM12345, AB54321}, Invalid {SM1234A, 12345AM}"));
    }

    @Test
    public void should_return_validation_error_if_source_is_empty() throws Exception {
        final ResultActions result = mockMvc.perform(post("/feedback")
                .contentType("application/json")
                .content("{\"username\":\"AB12345\",\"source\":\"\",\"rating\":5,\"comment\":\"This is a test.\"}")
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("error").value("Validation Error"))
                .andExpect(jsonPath("description").value("Source must not be blank"));
    }

    @Test
    public void should_return_validation_error_if_rating_is_not_present() throws Exception {
        final ResultActions result = mockMvc.perform(post("/feedback")
                .contentType("application/json")
                .content("{\"username\":\"AB12345\",\"source\":\"My Application\",\"comment\":\"This is a test.\"}")
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("error").value("Validation Error"))
                .andExpect(jsonPath("description").value("Rating is mandatory and should be between 1-5"));
    }

    @Test
    public void should_return_validation_error_if_rating_is_greater_than_5() throws Exception {
        final ResultActions result = mockMvc.perform(post("/feedback")
                .contentType("application/json")
                .content("{\"username\":\"AB12345\",\"source\":\"My Application\",\"rating\":6,\"comment\":\"This is a test.\"}")
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("error").value("Validation Error"))
                .andExpect(jsonPath("description").value("Rating should be between 1-5"));
    }

    @Test
    public void should_return_validation_error_if_rating_is_less_than_1() throws Exception {
        final ResultActions result = mockMvc.perform(post("/feedback")
                .contentType("application/json")
                .content("{\"username\":\"AB12345\",\"source\":\"My Application\",\"rating\":0,\"comment\":\"This is a test.\"}")
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("error").value("Validation Error"))
                .andExpect(jsonPath("description").value("Rating should be between 1-5"));
    }

    private void verifyJson(final ResultActions action, final Feedback feedback, String path, String linkPath) throws Exception {
        action
                .andExpect(jsonPath(path + "username", is(feedback.getUsername())))
                .andExpect(jsonPath(path + "createdTime", is(format.format(feedback.getCreatedTime()))))
                .andExpect(jsonPath(path + "source", is(feedback.getSource())))
                .andExpect(jsonPath(path + "rating", is(feedback.getRating().intValue())))
                .andExpect(jsonPath(path + "comment", is(feedback.getComment())))
                .andExpect(jsonPath(path + linkPath + "_links.self.href", is("/feedback/"+feedback.getId())));
    }
}


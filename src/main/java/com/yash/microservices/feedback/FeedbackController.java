package com.yash.microservices.feedback;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedback")
@Api(value = "Feedback REST API")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(final FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }


    @GetMapping(path = "/{id}", produces = "application/json")
    @ApiOperation("Returns a specific feedback by their identifier.")
    public ResponseEntity<FeedbackResource> getFeedbackById(@ApiParam(value = "Id of the feedback to be obtained. Cannot be empty.", required = true, example = "1")
                                              @PathVariable long id) {

        return ResponseEntity.ok(feedbackService.getFeedbackById(id));
    }

    @GetMapping(value = "", produces = "application/json")
    @ApiOperation("Returns list of all Feedbacks in the system.")
    public Resources<FeedbackResource> getAllFeedback() {
       return feedbackService.getAllFeedback();
    }


    @PostMapping(produces = "application/json")
    @ApiOperation("Creates a new feedback.")
    public ResponseEntity<FeedbackResource> createFeedback(@ApiParam("Feedback information for a new feedback to be created.")
                                             @Validated(ValidationSequence.class) @RequestBody FeedbackDTO feedbackDTO) {

        return ResponseEntity.ok(feedbackService.createFeedback(feedbackDTO));
    }
}

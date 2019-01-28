package com.yash.microservices.feedback;

import org.modelmapper.ModelMapper;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class FeedbackResourceAssembler extends ResourceAssemblerSupport<Feedback, FeedbackResource> {

    private final ModelMapper modelMapper;

    public FeedbackResourceAssembler(final ModelMapper modelMapper) {
        super(FeedbackController.class, FeedbackResource.class);
        this.modelMapper = modelMapper;
    }

    @Override
    public FeedbackResource toResource(Feedback feedback) {
        FeedbackResource resource = super.createResourceWithId(feedback.getId(), feedback);
        modelMapper.map(feedback, resource);
        return resource;
    }
}

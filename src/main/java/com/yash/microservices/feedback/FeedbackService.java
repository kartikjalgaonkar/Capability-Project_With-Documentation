package com.yash.microservices.feedback;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {
	
	private static final Logger logger = LoggerFactory.getLogger(FeedbackService.class);
    private final FeedbackRepository repository;
    private final ModelMapper modelMapper;
    private final FeedbackResourceAssembler assembler;

    public FeedbackService(final FeedbackRepository repository,
                           final ModelMapper modelMapper,
                           final FeedbackResourceAssembler assembler) {
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.assembler = assembler; 
    }

    public FeedbackResource getFeedbackById(Long id) {
        logger.info("action=getFeedbackById status=start id={}", id);
        Feedback feedbackById = repository.findById(id).orElseThrow(
                () -> {
                    logger.info("action=getFeedbackById found=false status=finished id={}", id);
                    return new FeedbackNotFoundException("feedback with id " + id + " not found");
                }
        );
        FeedbackResource feedbackResource = assembler.toResource(feedbackById);
        logger.info("action=getFeedbackById found=true status=finished id={}", id);
        return feedbackResource;
    }


    public Resources<FeedbackResource> getAllFeedback() {
    	  logger.info("action=getAllFeedback status=start");
          List<FeedbackResource> resources = assembler.toResources(repository.findAllByOrderByIdDesc());
          logger.info("action=getAllFeedback status=finished found={}", resources.size());
          return new Resources<> (resources);
    }

    public FeedbackResource createFeedback(FeedbackDTO feedbackDTO) {
    	 logger.info("action=createFeedback status=start feedback={}", feedbackDTO);
         Feedback feedback = modelMapper.map(feedbackDTO, Feedback.class);
         Feedback savedFeedback = repository.save(feedback);
         FeedbackResource feedbackResource = assembler.toResource(savedFeedback);
         logger.info("action=createFeedback status=finished id={}", savedFeedback.getId());
         return feedbackResource;
    }
}

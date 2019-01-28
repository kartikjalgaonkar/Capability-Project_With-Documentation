package com.yash.microservices.feedback;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class FeedbackFieldError {

	private Date timestamp;
	private String error;
	private String description;

}

package com.yash.microservices.feedback;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
public class FeedbackDTO {
    @NotBlank(message = "Username must not be blank", groups = UsernamePattern.class)
    @Pattern(regexp = "^[ A-Za-z ][ A-Za-z ][ A-Za-z0-9][0-9]{1,4}$", message = "Rajesh, Username must be 7 characters long." +
            " Username should start with letters and followed by numbers. Example: " +
            "Valid {SM12345, AB54321}, " +
            "Invalid {SM1234A, 12345AM}" , groups = Username.class)
    @ApiModelProperty(notes = "ID of the person providing the feedback", required = true, position = 0)
    private String username;

    @NotBlank(message = "Source must not be blank", groups = Source.class)
    @ApiModelProperty(notes = "Source application", required = true, position = 1)
    private String source;

    @Min(value = 1, message = "Rating should be between 1-5", groups = Rating.class)
    @Max(value = 5, message = "Rating should be between 1-5", groups = Rating.class)
    @Digits(integer = 1, fraction = 0)
    @NotNull(message = "Rating is mandatory and should be between 1-5", groups = Rating.class)
    @ApiModelProperty(notes = "Rate Your Experience. 1 - awful, 2 - poor, 3 - fair, 4 - good, 5 - excellent", required = true, position = 2)
    private Integer rating;

    @ApiModelProperty(notes = "Share your feedback, comment and/or suggestions", position = 3)
    private String comment;
}

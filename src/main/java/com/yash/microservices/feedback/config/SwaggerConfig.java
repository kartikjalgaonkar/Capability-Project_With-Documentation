package com.yash.microservices.feedback.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket feedbackApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("feedback-api")
                .select().apis(RequestHandlerSelectors.basePackage("com.yash.microservices.feedback"))
                .paths(regex("/feedback.*"))
                .build()
                .apiInfo(metaData());
    }

    private ApiInfo metaData() {
        return new ApiInfo("Feedback API",
                "Rest Service to post and get feedback.",
                "1.0",
                null,
                new Contact("Yash Technologies", "http://www.yash.com", "feedback@yash.com"),
                "", "", new ArrayList<VendorExtension>());
    }
}

FROM java:alpine
VOLUME /tmp
ADD target/feedback-service.jar feedback-service.jar
EXPOSE 8085
ENTRYPOINT ["java","-jar","feedback-service.jar"]

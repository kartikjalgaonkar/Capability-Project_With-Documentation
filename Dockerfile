FROM java:alpine
VOLUME /tmp
ADD target/feedback-service.jar feedback-service.jar
EXPOSE 8085
ENV JAVA_OPTS=""
ENTRYPOINT ["java","-jar","java $JAVA_OPTS -Dspring.profiles.active=dev feedback-service.jar"]

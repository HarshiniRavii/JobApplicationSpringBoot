FROM openjdk:17
ADD target/job-application-docker.jar job-application-docker.jar
ENTRYPOINT ["java","-jar","/job-application-docker.jar"]
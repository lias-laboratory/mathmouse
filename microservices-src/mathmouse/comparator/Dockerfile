FROM openjdk:8-jdk
MAINTAINER fr.ensma.lias.ponchateau
EXPOSE 15672 5672
RUN mkdir resources
ADD target/*.jar src/main/resources/* /resources/
ENTRYPOINT ["java", "-jar", "/resources/comparator-0.0.1-SNAPSHOT.jar"]
CMD ["rabbitmq"]

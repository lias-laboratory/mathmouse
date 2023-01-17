FROM openjdk:8-jdk
MAINTAINER fr.ensma.lias.ponchateau
EXPOSE 15672 5672
RUN mkdir resources
ADD target/*.jar src/main/resources/* /resources/
ENV XSD_FILE_PATH="/resources/differential-equation-2.0.xsd"
ENTRYPOINT ["java", "-jar", "/resources/generator-0.0.1-SNAPSHOT.jar"]
CMD ["rabbitmq"]

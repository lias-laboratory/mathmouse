FROM openjdk:8-jdk
MAINTAINER fr.ensma.lias.ponchateau
EXPOSE 15672 5672
RUN mkdir resources
ENV RETRIEVAL_QUERIES_FILE_PATH="/resources/retrieval.sql"
ENV XSD_FILE_PATH="/resources/differential-equation-2.0.xsd"
ENV RESOURCES="/resources/"
ADD target/*.jar src/main/resources/* /resources/
ENTRYPOINT ["java", "-jar", "/resources/dbcore-0.0.1-SNAPSHOT.jar"]
CMD ["rabbitmq", "mathmouse_db", "5432", "db_mmw", "db_admin", "password"]
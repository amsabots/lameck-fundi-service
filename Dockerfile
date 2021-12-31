FROM openjdk
WORKDIR ./app

COPY target/fundi_service-0.0.1-SNAPSHOT.jar ./app/application.jar

RUN cd app/ && ls -a

EXPOSE 27800

ENTRYPOINT ["java", "-jar", "app/application.jar"]
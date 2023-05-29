FROM azul/zulu-openjdk:17
VOLUME /tmp
COPY target/*.jar app.jar

ARG CONNECT_STRING
ARG DATABASE_USER
ARG DATABASE_PASS

ENV DATABASE_USER=$DATABASE_USER
ENV CONNECT_STRING=$CONNECT_STRING
ENV DATABASE_PASS=$DATABASE_PASS

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]
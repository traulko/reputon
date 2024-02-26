FROM maven:3.9.4 AS maven

WORKDIR /usr/src/app
COPY . /usr/src/app
RUN mvn package

FROM openjdk:17-oracle

ARG JAR_FILE=reputon-test-0.0.1-SNAPSHOT.jar

WORKDIR /opt/app

COPY --from=maven /usr/src/app/target/${JAR_FILE} /opt/app/

ENTRYPOINT ["java","-jar","reputon-test-0.0.1-SNAPSHOT.jar"]
FROM maven:3.5.4-jdk-8-alpine as maven
COPY ./pom.xml ./pom.xml
COPY ./src ./src
RUN mvn dependency:go-offline -B
RUN mvn package

FROM jboss/wildfly
EXPOSE 8080
COPY --from=maven target/usermanagement-*.war /opt/jboss/wildfly/standalone/deployments/usermanagement.war
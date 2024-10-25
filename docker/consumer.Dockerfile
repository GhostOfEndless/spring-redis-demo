FROM gradle:jdk21-alpine AS builder
ENV APP_HOME=/opt/app
WORKDIR $APP_HOME
COPY build.gradle settings.gradle ./
COPY consumer/build.gradle ./consumer/build.gradle
COPY consumer/src ./consumer/src
RUN gradle clean bootJar

FROM eclipse-temurin:21.0.4_7-jre-alpine AS final
ENV APP_HOME=/opt/app
WORKDIR $APP_HOME
COPY --from=builder $APP_HOME/consumer/build/libs/*.jar .
EXPOSE 8081
ENTRYPOINT exec java -jar *.jar
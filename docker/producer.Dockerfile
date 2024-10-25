FROM gradle:jdk21-alpine AS builder
ENV APP_HOME=/opt/app
WORKDIR $APP_HOME
COPY build.gradle settings.gradle ./
COPY producer/build.gradle ./producer/build.gradle
COPY producer/src ./producer/src
RUN gradle clean bootJar

FROM eclipse-temurin:21.0.4_7-jre-alpine AS final
ENV APP_HOME=/opt/app
WORKDIR $APP_HOME
COPY --from=builder $APP_HOME/producer/build/libs/*.jar .
EXPOSE 8080
ENTRYPOINT exec java -jar *.jar
FROM adoptopenjdk:11-jre-hotspot as builder
ARG PROJECT_STARTER_BINARY
ARG PROJECT_STARTER_BASE_DIR
WORKDIR ${PROJECT_STARTER_BASE_DIR}
COPY ${PROJECT_STARTER_BINARY} project-starter.jar
RUN java -Djarmode=layertools -jar project-starter.jar extract

FROM adoptopenjdk:11-jre-hotspot
#LABEL maintainer="First Last <email-address>"
#LABEL site="https://project-website.com"
ARG USER
ARG PROJECT_STARTER_BASE_DIR
WORKDIR ${PROJECT_STARTER_BASE_DIR}
COPY --from=builder ${PROJECT_STARTER_BASE_DIR}/dependencies/ ./
COPY --from=builder ${PROJECT_STARTER_BASE_DIR}/snapshot-dependencies/ ./
COPY --from=builder ${PROJECT_STARTER_BASE_DIR}/application/ ./
COPY --from=builder ${PROJECT_STARTER_BASE_DIR}/spring-boot-loader/ ./
RUN useradd -ms /bin/bash ${USER}
RUN chown -R ${USER}:${USER} ${PROJECT_STARTER_BASE_DIR}
USER ${USER}
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
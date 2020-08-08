FROM adoptopenjdk:11-jre-hotspot as builder
WORKDIR /opt/project-starter
ARG PROJECT_STARTER_VERSION
ARG PROJECT_STARTER_BINARY
COPY ${PROJECT_STARTER_BINARY} project-starter.jar
RUN java -Djarmode=layertools -jar project-starter.jar extract

FROM adoptopenjdk:11-jre-hotspot
WORKDIR /opt/project-starter
COPY --from=builder /opt/project-starter/dependencies/ ./
COPY --from=builder /opt/project-starter/snapshot-dependencies/ ./
COPY --from=builder /opt/project-starter/application/ ./
COPY --from=builder /opt/project-starter/spring-boot-loader/ ./

ARG USER
RUN useradd -ms /bin/bash ${USER}
RUN chown -R ${USER}:${USER} /opt/project-starter
USER ${USER}

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
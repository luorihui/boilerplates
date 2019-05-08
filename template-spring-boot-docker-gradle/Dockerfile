FROM openjdk:8-jdk-alpine
VOLUME /tmp
## Set the default main class, should be overwritten by the build script
ARG MAINCLASS="com.sunray.templatespringbootdockergradle.TemplateSpringBootDockerGradleApplication"
ARG DEPENDENCY=build/dependency
ENV MAINCLASS_VAR=${MAINCLASS}
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app

## Using the shell form so that variable substitution will work
ENTRYPOINT ["sh", "-c", "java -cp app:app/lib/* ${MAINCLASS_VAR}"]

#ENTRYPOINT ["java","-cp","app:app/lib/*","com.sunray.templatespringbootdockergradle.TemplateSpringBootDockerGradleApplication"]
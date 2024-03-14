FROM eclipse-temurin:21-jre-alpine

WORKDIR /opt/shortbin/
RUN mkdir -p /opt/shortbin/storage /opt/shortbin/config
COPY target/shortbin-6.jar /opt/shortbin/shortbin.jar
COPY entrypoint.sh /opt/shortbin/
COPY src/main/resources/application-docker.yml /opt/shortbin/application.yml
COPY shortbin.db /opt/shortbin/shortbin.db
RUN adduser shortbin --disabled-password --gecos "shortbin Service Account" && \
    chmod 777 /opt/shortbin/entrypoint.sh && \
    chown -R 1000:1000 /opt/shortbin
#RUN su -c "/usr/share/maven/bin/mvn -Dmaven.test.skip=true install" shortbin
#RUN su -c "/usr/share/maven/bin/mvn dependency:resolve-plugins" shortbin
EXPOSE 8080
# Ensure this is after the directory is created
VOLUME /opt/shortbin/storage /opt/shortbin/config
ENTRYPOINT ["/bin/bash", "/opt/shortbin/entrypoint.sh"]
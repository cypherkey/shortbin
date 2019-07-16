FROM maven:3-jdk-8

EXPOSE 8080
VOLUME /opt/shortbin/storage /opt/shortbin/config
WORKDIR /opt/shortbin/
RUN mkdir -p /opt/shortbin/storage /opt/shortbin/config
COPY target/shortbin-1.1.jar /opt/shortbin/
COPY entrypoint.sh /opt/shortbin/
COPY src/main/resources/application-docker.yml /opt/shortbin/application.yml
COPY shortbin.db /opt/shortbin/shortbin.db
RUN adduser shortbin --disabled-password --gecos "shortbin Service Account"
RUN chmod 777 /opt/shortbin/entrypoint.sh
RUN chown -R 1000:1000 /opt/shortbin
#RUN su -c "/usr/share/maven/bin/mvn -Dmaven.test.skip=true install" shortbin
#RUN su -c "/usr/share/maven/bin/mvn dependency:resolve-plugins" shortbin
ENTRYPOINT ["/bin/bash", "/opt/shortbin/entrypoint.sh"]
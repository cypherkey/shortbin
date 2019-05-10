#!/bin/bash
#/usr/share/maven/bin/mvn spring-boot:run

if [ ! -e /opt/shortbin/config/application.yml ]; then
	echo "Copying default application.yml"
	cp /opt/shortbin/application.yml /opt/shortbin/config/application.yml
fi

if [ ! -e /opt/shortbin/config/shortbin.db ]; then
	echo "Copying default database shortbin.db"
	cp /opt/shortbin/shortbin.db /opt/shortbin/config/shortbin.db
fi

java -jar /opt/shortbin/shortbin-1.0.jar --spring.config.location=/opt/shortbin/config/application.yml
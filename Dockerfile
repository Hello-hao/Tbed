FROM openjdk:8

# Add the service itself
ARG JAR_FILE
#ADD target/${JAR_FILE} /usr/share/docker-springboot-demo.jar
ADD web.tar /web.tar

#COPY web/webapps/hellohao/config.json /HellohaoData/web/config/config.json
COPY application.properties /application.properties
#COPY hellohao /hellohao
COPY Tbed.jar /Tbed.jar
#CMD ["/bin/bash","/web/start.sh"]
EXPOSE 10088
EXPOSE 10089
ENTRYPOINT["/start.sh"]
#ENTRYPOINT ["java","-jar", "./Tbed.jar"]
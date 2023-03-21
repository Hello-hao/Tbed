FROM openjdk:8

MAINTAINER 923453645@qq.com

ADD hellohaotbed/ /hellohaotbed

COPY appstart.sh /appstart.sh

COPY application.properties /application.properties

COPY Tbed.jar /Tbed.jar

EXPOSE 10088

EXPOSE 10089

CMD ["/bin/bash","/appstart.sh"]

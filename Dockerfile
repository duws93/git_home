#FROM ubuntu:lts
#MAINTAINER duws wenshu.du.ext@nokia-sbell.com
#ENV https_proxy http://135.251.33.31:8080
#ENV http_prpxy http://135.251.33.31:8080
#RUN echo "deb http://mirrors.aliyun.com/ubuntu/ bionic main restricted universe multiverse" > /etc/apt/sources.list
#RUN echo "deb http://mirrors.aliyun.com/ubuntu/ bionic-security main restricted universe multiverse" >> /etc/apt/sources.list
#RUN echo "deb http://mirrors.aliyun.com/ubuntu/ bionic-updates main restricted universe multiverse" >> /etc/apt/sources.list
#RUN echo "deb http://mirrors.aliyun.com/ubuntu/ bionic-proposed main restricted universe multiverse" >> /etc/apt/sources.list
#RUN echo "deb http://mirrors.aliyun.com/ubuntu/ bionic-backports main restricted universe multiverse" >> /etc/apt/sources.list
#RUN echo "deb-src http://mirrors.aliyun.com/ubuntu/ bionic main restricted universe multiverse" >> /etc/apt/sources.list
#RUN echo "deb-src http://mirrors.aliyun.com/ubuntu/ bionic-security main restricted universe multiverse" >> /etc/apt/sources.list
#RUN echo "deb-src http://mirrors.aliyun.com/ubuntu/ bionic-updates main restricted universe multiverse" >> /etc/apt/sources.list
#RUN echo "deb-src http://mirrors.aliyun.com/ubuntu/ bionic-proposed main restricted universe multiverse" >> /etc/apt/sources.list
#RUN echo "deb-src http://mirrors.aliyun.com/ubuntu/ bionic-backports main restricted universe multiverse" >> /etc/apt/sources.list
#RUN apt-get update --fix-missing
#RUN apt-get -y upgrade 
#RUN apt-get -y install apache2 --fix-missing
#ENV APACHE_#RUN_USER www-data
#ENV APACHE_#RUN_GROUP www-data
#ENV APACHE_LOG_DIR /var/log/apache2
#ENV APACHE_LOCK_DIR /var/lock/apache2
#ENV APACHE_PID_FILE /var/#RUN/apache2.pid
#EXPOSE 80
#ADD website /var/www/website
#ADD apache-config.conf /etc/apache2/sites-enabled/000-default.conf
#CMD ["/usr/sbin/apache2ctl","-D", "FOREGROUND"]
FROM busybox

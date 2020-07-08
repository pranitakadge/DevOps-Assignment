FROM alpine:3.9
LABEL author="Pranita Kadge"

ARG env_project_name
 
ENV project_name=$env_project_name 

COPY ${project_name}.zip /root/${project_name}.zip

RUN apk --update --no-cache add bash which curl build-base python3 python3-dev apache2 apache2-dev  wget ca-certificates make gcc musl-dev;\
ln -s pip3 /usr/bin/pip; pip install -U pip setuptools wheel

RUN wget -O /tmp/mod_wsgi.tar.gz https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/modwsgi/mod_wsgi-3.4.tar.gz && \
    tar -C /tmp -xvf /tmp/mod_wsgi.tar.gz && \
    rm /tmp/mod_wsgi.tar.gz 

WORKDIR /tmp/mod_wsgi-3.4
RUN ln -s /usr/lib/libpython3.6m.so /usr/lib/libpython3.6.so && \
    ./configure --with-python=/usr/bin/python3.6 --with-apxs=/usr/bin/apxs && \
    make && make install clean; rm -rf /tmp/mod_wsgi-3.4; \
mkdir -p /etc/apache2/sites-available/; \
mkdir -p /etc/apache2/mods-available/

COPY httpd-foreground /usr/local/bin/
RUN chown apache:apache /etc/apache2/; \
chown -R apache:apache /var/www/; chmod +x /usr/local/bin/httpd-foreground

RUN cd && \
   echo ${project_name} && \
   ls && \
   unzip ${project_name}.zip && \
   ls && \
   pip3 install -r requirements.txt && \
   cp -r ${project_name} /var/www/ && \
   ls -la /var/www 

RUN echo -e "import os\n\
import sys\n\
path='/var/www/${project_name}'\n\
if path not in sys.path:\n\
    sys.path.append(path)\n\
os.environ['DJANGO_SETTINGS_MODULE'] = '${project_name}.settings'\n\
from django.core.wsgi import get_wsgi_application\n\
application = get_wsgi_application()" >> /var/www/${project_name}/django.wsgi;\
sed -i -r 's@#(LoadModule rewrite_module modules/mod_rewrite.so)@\1@i' /etc/apache2/httpd.conf;\
sed -i -r 's@Errorlog .*@Errorlog /var/log/apache2/error.log@i' /etc/apache2/httpd.conf;\
sed -i -r 's@#Servername .*@ServerName localhost@i' /etc/apache2/httpd.conf;\
sed -i -r 's@Listen 80.*@Listen 80@i' /etc/apache2/httpd.conf;\
sed -i "s@DocumentRoot \"/var/www/localhost/htdocs\".*@DocumentRoot \"/var/www/${project_name}\"@i" /etc/apache2/httpd.conf;\
sed -i "s@Timeout 300@Timeout 3600@" /etc/apache2/httpd.conf;\
sed -i "s@KeepAliveTimeout 5@KeepAliveTimeout 65@" /etc/apache2/httpd.conf;\
sed -i "s@Group apache@Group root@" /etc/apache2/httpd.conf
 
RUN echo -e "Transferlog /dev/stdout\n\
LoadModule wsgi_module modules/mod_wsgi.so\n\
WSGIPythonPath /usr/lib/python3.6\n\
WSGIScriptAlias / /var/www/${project_name}/django.wsgi\n\
WSGIApplicationGroup %{GLOBAL}\n\
WSGIPassAuthorization On\n\
LimitRequestFieldSize 1048576\n\
<Directory /var/www/${project_name}>\n\
    Options ExecCGI Indexes FollowSymLinks\n\
    AllowOverride All\n\
    Require all granted\n\
    <Files django.wsgi>\n\
       Require all granted\n\
    </Files>\n\
</Directory>" >> /etc/apache2/httpd.conf
 
 
EXPOSE 80
CMD ["httpd-foreground"]


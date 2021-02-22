# SDU2021元宵节活动
这里是sdu学生在线在2021元宵节上线的“线上元宵节猜灯谜”系列活动的后端项目。

## how to build locally

1. 从`database/sduonline_lantern_festival_2021.sql`中导入表结构至MySQL数据库。
2. 在`src/main/resources`路径下找到`application-develop.yml`，配置好正确的MySQL和Redis的URL、用户、密码。
3. maven选择`develop` profile，然后build即可。

## how to release

1. 获得项目jar文件：配置好`src/main/resources/application-release.yml`的MySQL、Redis的URL、用户、密码，然后运行maven package，得到jar文件。

2. 如果不用docker配置，请分别配置好MySQL、Redis，然后运行起来jar就好了。以下内容是关于用docker配置项目的。

3. 在docker中创建3个容器，分别运行MySQL、Redis、jar文件，然后用docker network把他们连接起来：

   + shell运行`docker network create project_network`

   + 构建MySQL容器：shell运行  
     ``` shell
     docker container run -d -it --name mysql_container --network project_network --network-alias mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=rootpass -v /some_path/mysql/my.conf:/etc/mysql/my.conf -v /some_path/mysql/mysql-files:/var/lib/mysql mysql
     ```
     
     更改其中的参数，改变数据库URL、root密码等，并创建合适的文件夹放置数据库文件、数据库配置文件my.conf，然后用-v挂载。

     my.conf文件参考：

     ``` my.conf
     [mysqld]
     secure_file_priv=/var/lib/mysql
     
     [mysql]
     default-time_zone = '+8:00'
     ```

     最后用MySQL连接工具连接之，导入数据库`database/sduonline_lantern_festival_2021.sql`。

   + 构建Redis容器：shell运行

      ``` shell
      docker container run -p 6379:6379 -itd --name redis_container --network project_network --network-alias redis redis:latest
      ```

   + 构建项目容器：先把项目的.jar文件放到某个目录下，改名为`sdu_lantern_festival_2021.jar`，然后同级目录新建一个文件，名为`Dockerfile`：

     ``` dockerfile
      FROM openjdk:11
      COPY sdu_lantern_festival_2021.jar /sdu_lantern_festival_2021.jar
     ```

     然后用shell运行：

     ``` shell
     docker rm -f project_container
     docker rmi -f project_image:latest
     docker build . -t project_image:latest
     docker container run -p 8080:8080 -itd --name project_container --network project_network --network-alias tomcat project_image:0.5 /bin/bash -c "java -jar /sdu_lantern_festival_2021.jar"
     ```

     注意docker build要用`.`指出`Dockerfile`所在的路径（为当前目录，如果不是请把`.`改成对应路径，或者cd到该目录下）。

     如果执行无误的话，最后一句已经在容器内执行`java -jar`来执行项目的.jar文件了，所以此时后端项目已经开始运行。


## 接口文档

见`doc/InterfaceDocument.md`。
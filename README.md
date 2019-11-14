in application.yml change spring.datasource.url from jdbc:mysql://localhost:3304/lotto to jdbc:mysql://lotto:3304/lotto<br>
mvn clean install -DskipTests<br>
docker build -t docker-lotto .<br>
docker network create lotto-network<br>
docker pull mysql:8.0.17<br>
docker run --name lottodb --network lotto-network -e MYSQL_USER=freedom -e MYSQL_PASSWORD=123 -e MYSQL_ROOT_PASSWORD=123 -e MYSQL_DATABASE=lotto -e TZ=Asia/Bangkok -p 3306:3306 -d --restart=always mysql:8.0<br><br>
docker run --name lotto --network lotto-network -p 8080:8080 -d --restart=always docker-lotto<br>
docker pull phpmyadmin/phpmyadmin:4.8<br>
docker run --name lottodb-admin -d --network lotto-network -p 8081:80 phpmyadmin/phpmyadmin:4.8<br><br>
docker ps ดู containerid ของ container name = lottodb<br>
docker exec -it #containerid# bash<br>
mysql -uroot -p123<br>
ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY '123';<br>
set jasypt<br>
add in pom<br>
```xml
<dependency>
    <groupId>org.jasypt</groupId>
    <artifactId>jasypt</artifactId>
    <version>1.9.2</version>
</dependency>
```
cmd C:\Users\%user%\.m2\repository\org\jasypt\jasypt\1.9.2<br>
java -cp jasypt-1.9.2.jar org.jasypt.intf.cli.JasyptPBEStringEncryptionCLI input="123" password=secretkey algorithm=PBEWithMD5AndDES<br>
remove org.jasypt in pom<br>

<br>
push to repository<br>
docker tag docker-lotto nut077/lotto<br>
docker push nut077/lotto<br>
docker run --name lotto --network lotto-network -p 8080:8080 -d --restart=always nut077/lotto<br><br>
run docker-compose<br>
docker-compose run --rm start_dependencies

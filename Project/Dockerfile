FROM amazoncorretto:25-alpine3.22-jdk
ADD target/*.jar /home/app.jar
WORKDIR /home/
ENV DB_HOST=localhost
ENV DB_PORT=3307
ENV DB_USER=root
ENV DB_PASSWORD=root
ENV DATABASE=inventory_db
CMD java -jar app.jar
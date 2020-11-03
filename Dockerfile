FROM amazoncorretto:11.0.4
VOLUME /tmp
COPY build/libs/reservation-service-0.0.1-SNAPSHOT.jar /tmp/reservation-service.jar
ENTRYPOINT ["java","-jar","/tmp/reservation-service.jar"]
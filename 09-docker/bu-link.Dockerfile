FROM openjdk:17-alpine
ADD 02-bu-services/75-link-management/03-link-service/target/*.jar service.jar
ADD 02-bu-services/75-link-management/03-link-service/target/uploads /uploads
RUN ls -al /uploads/*
ADD 02-bu-services/75-link-management/03-link-service/target/camel /camel
RUN ls -al /camel/*
ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.9.0/wait /wait
RUN chmod +x /wait

CMD /wait && java -jar /service.jar

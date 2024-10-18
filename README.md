# web jobApplication microservices

## Architecture

Our sample microservices-based system consists of the following modules:

- **gateway-service** - a module that Spring Cloud Netflix Zuul for running Spring Boot jobApplication that acts as a
  proxy/gateway in our architecture.
- **config-service** - a module that uses Spring Cloud Config Server for running configuration server in the `native`
  mode. The configuration files are placed on the classpath.
- **discovery-service** - a module that depending on the example it uses Spring Cloud Netflix Eureka or Spring Cloud
  Netlix Alibaba Nacos as an embedded discovery server.
- **messaging-management-service** - a module containing the first of our sample microservices that allows to perform
  CRUD operation on in-memory repository of employees
- **identity-management-service** - a module containing the second of our sample microservices that allows to perform
  CRUD operation on in-memory repository of departments. It communicates with messaging-management-service.
- **key-management-service** - a module containing the third of our sample microservices that allows to perform CRUD
  operation on in-memory repository of organizations. It communicates with both messaging-management-service and
  key-management-service.

### Servers and links

- **Servername** | **Port** | **Link** -
- config.dev.prm.isygoit.eu | 8088 | https://config.dev.prm.isygoit.eu -
- eureka.dev.prm.isygoit.eu | 8061 | https://eureka.dev.prm.isygoit.eu -
- gateway.dev.prm.isygoit.eu | 8060 | https://gateway.dev.prm.isygoit.eu -
- sms.dev.prm.isygoit.eu | 55400 | https://sms.dev.prm.isygoit.eu -
- ims.dev.prm.isygoit.eu | 55402 | https://ims.dev.prm.isygoit.eu -
- kms.dev.prm.isygoit.eu | 55403 | https://kms.dev.prm.isygoit.eu -
- mms.dev.prm.isygoit.eu | 55404 | https://mms.dev.prm.isygoit.eu -
- dms.dev.prm.isygoit.eu | 55405 | https://dms.dev.prm.isygoit.eu -
- cms.dev.prm.isygoit.eu | 55407 | https://cms.dev.prm.isygoit.eu -
- hrm.dev.prm.isygoit.eu | 55408 | https://hrm.dev.prm.isygoit.eu -
- rpm.dev.prm.isygoit.eu | 55409 | https://rpm.dev.prm.isygoit.eu -
- lms.dev.prm.isygoit.eu | 55410 | https://lms.dev.prm.isygoit.eu -
- pms.dev.prm.isygoit.eu | 55411 | https://pms.dev.prm.isygoit.eu -
- integrationApi.dev.prm.isygoit.eu | 55412 | https://integrationApi.dev.prm.isygoit.eu -
- lnk.dev.prm.isygoit.eu | 55413 | https://lnk.dev.prm.isygoit.eu -
- fe-gateway.dev.prm.isygoit.eu | 4000 | https://fe-gateway.dev.prm.isygoit.eu -
- fe-sysadmin.dev.prm.isygoit.eu | 4001 | https://fe-sysadmin.dev.prm.isygoit.eu -
- fe-recruitment.dev.prm.isygoit.eu | 4002 | https://fe-recruitment.dev.prm.isygoit.eu -
- fe-calendar.dev.prm.isygoit.eu | 4003 | https://fe-calendar.dev.prm.isygoit.eu -
- fe-candidate.dev.prm.isygoit.eu | 4004 | https://fe-candidate.dev.prm.isygoit.eu -
- fe-integrationApi.dev.prm.isygoit.eu | 4005 | https://fe-integrationApi.dev.prm.isygoit.eu -
- fe-hrm.dev.prm.isygoit.eu | 4006 | https://fe-hrm.dev.prm.isygoit.eu -
- fe-document.dev.prm.isygoit.eu | 4007 | https://fe-document.dev.prm.isygoit.eu -
- dev.smartcode.isygoit.eu | 4008 | https://dev.smartcode.isygoit.eu -
- qa.smartcode.isygoit.eu | 4009 | https://qa.smartcode.isygoit.eu -
- fe-visio.dev.prm.isygoit.eu | 4010 | https://fe-visio.dev.prm.isygoit.eu -
- fe-lnk.dev.prm.isygoit.eu | 4011 | https://fe-lnk.dev.prm.isygoit.eu -
- fe-minio.dev.prm.isygoit.eu | 9001 | https://fe-minio.dev.prm.isygoit.eu -

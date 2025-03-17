# Bootcamp Java (Spring, Javascript, NodeJS, Angular)

## Instalación

### Back end

- [JDK](https://www.oracle.com/java/technologies/downloads/)
- [Eclipse Installer](https://www.eclipse.org/downloads/download.php?file=/oomph/epp/2024-12/R/eclipse-inst-jre-win64.exe)
  - [Eclipse IDE for Enterprise Java and Web Developers](https://www.eclipse.org/downloads/download.php?file=/technology/epp/downloads/release/2024-12/R/eclipse-jee-2024-12-R-win32-x86_64.zip)
- [Spring Tools 4 for Eclipse](https://spring.io/tools/)
- [Project Lombok](https://projectlombok.org/download)
- [Git](https://git-scm.com/)
- [Maven](https://maven.apache.org/download.cgi)
- [Postman](https://www.postman.com/downloads/)
- Docker (opcional pero recomendable)
  - [WSL 2 feature on Windows](https://learn.microsoft.com/es-es/windows/wsl/install)
  - [Docker Desktop](https://www.docker.com/get-started/)
  - Alternativas a Docker Desktop
    - [Podman](https://podman.io/docs/installation)
    - [Rancher Desktop](https://rancherdesktop.io/)

- Clientes de bases de datos (opcionales)
  - [DBeaver Community](https://dbeaver.io/download/)
  - [HeidiSQL](https://www.heidisql.com/download.php)
  - [MongoDB Compass](https://www.mongodb.com/try/download/compass)

### Front end

- [Visual Studio Code](https://code.visualstudio.com/download)
- [Angular DevTools](https://chrome.google.com/webstore/detail/angular-devtools/ienfalfjdbdpebioblfackkekamfmbnh)
- Node.js (Alternativas)
  - [Node.js](https://nodejs.org/es)
  - [NVM for Windows](https://github.com/coreybutler/nvm-windows/releases)

            nvm install lts

#### Extensiones Visual Studio Code

- [Spanish Language Pack for Visual Studio Code](https://marketplace.visualstudio.com/items?itemName=MS-CEINTL.vscode-language-pack-es)
- [Angular Essentials](https://marketplace.visualstudio.com/items?itemName=johnpapa.angular-essentials)
- [Auto Close Tag](https://marketplace.visualstudio.com/items?itemName=formulahendry.auto-close-tag)
- [Auto Rename Tag](https://marketplace.visualstudio.com/items?itemName=formulahendry.auto-rename-tag)
- [Code Spell Checker](https://marketplace.visualstudio.com/items?itemName=streetsidesoftware.code-spell-checker) + [Spanish - Code Spell Checker](https://marketplace.visualstudio.com/items?itemName=streetsidesoftware.code-spell-checker-spanish)
- [IntelliSense for CSS class names](https://marketplace.visualstudio.com/items?itemName=Zignd.html-css-class-completion)
- [Path Intellisense](https://marketplace.visualstudio.com/items?itemName=christian-kohler.path-intellisense)
- [Error Lens](https://marketplace.visualstudio.com/items?itemName=usernamehw.errorlens)
- [REST Client](https://marketplace.visualstudio.com/items?itemName=humao.rest-client)
- [Postman](https://marketplace.visualstudio.com/items?itemName=Postman.postman-for-vscode)

#### Utilidades y ORM

    npm install -g nodemon npm-check-updates 
    npm install sequelize mysql2  
    npm install sequelize-auto --save-dev  
    npx sequelize-auto -o "./models" -e mysql -h localhost -p 3306 -d sakila -u root -x root

## Documentación

- Spring
  - <https://docs.spring.io/spring-boot/docs/current/reference/html/>
  - <https://docs.spring.io/spring-data/commons/docs/current/reference/html/>
  - <https://docs.spring.io/spring-data/jpa/docs/current/reference/html/>
  - <https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/>
  - <https://docs.spring.io/spring-data/redis/docs/current/reference/html/>
  - <https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#spring-web>
  - <https://docs.spring.io/spring-data/rest/docs/current/reference/html/>
  - <https://docs.spring.io/spring-cloud-commons/docs/current/reference/html/#spring-cloud-loadbalancer>
  - <https://docs.spring.io/spring-cloud-config/docs/current/reference/html/>
  - <https://docs.spring.io/spring-security/reference/index.html>
- [PATRONES de DISEÑO](https://refactoring.guru/es/design-patterns)
- [Markdown (es)](https://markdown.es/sintaxis-markdown/)
- [Markdown](https://www.markdownguide.org/basic-syntax/)

## Laboratorios

- [Working a Getting Started guide with STS](https://spring.io/guides/gs/sts)
- [Building an Application with Spring Boot](https://spring.io/guides/gs/spring-boot)
- [Testing the Web Layer](https://spring.io/guides/gs/testing-web)
- [Consumer Driven Contracts](https://spring.io/guides/gs/contract-rest)

### Fundamentos Back end

#### Ejercicios de refuerzo

- <https://www.javatpoint.com/>
- Iniciación:
    1. <http://puntocomnoesunlenguaje.blogspot.com/p/ejercicios.html>
    2. <https://tutobasico.com/basicos-java/>
    3. <https://tutobasico.com/basicos2-java/>
    4. <https://www.discoduroderoer.es/ejercicios-propuestos-y-resueltos-basicos-java/>
    5. <https://www.discoduroderoer.es/ejercicios-propuestos-y-resueltos-metodos-y-funciones-de-java/>
- Intermedio:
    1. <https://tutobasico.com/basicos3-java/>
    2. <http://ejerciciosresueltosprogramacion.blogspot.com/>
    3. <https://www.discoduroderoer.es/ejercicios-propuestos-y-resueltos-programacion-orientado-a-objetos-java/>

### Fundamentos Front end

- <https://www.w3schools.com/html>
- <https://www.w3schools.com/css>
- [El Tutorial de JavaScript Moderno](https://es.javascript.info/)
- <https://developer.mozilla.org/es/docs>
- <https://github.com/Asabeneh/30-Days-Of-JavaScript>
- <https://github.com/Asabeneh/30-Days-Of-React>

#### Ejercicios de refuerzo

  1. <https://www.discoduroderoer.es/category/ejercicio/javascript-ejercicio/>
  2. <https://www.arkaitzgarro.com/javascript/capitulo-18.html>
  3. <https://uniwebsidad.com/libros/javascript/capitulo-11>

## Ejemplos

- <https://github.com/spring-projects/spring-data-examples>
- <https://github.com/spring-projects/spring-data-rest-webmvc>
- <https://github.com/spring-projects/spring-hateoas-examples>
- <https://github.com/spring-projects/spring-amqp-samples>
- <https://github.com/rabbitmq/rabbitmq-tutorials/tree/main/spring-amqp>
- <https://github.com/spring-projects/spring-kafka/tree/main/samples>
- <https://github.com/spring-projects/spring-petclinic>

## Ejercicios

- [Kata: Gilded Rose](https://github.com/emilybache/GildedRose-Refactoring-Kata/blob/master/GildedRoseRequirements_es.md)

## Base de datos de ejemplos

- [Página principal Sakila](https://dev.mysql.com/doc/sakila/en/)
- [Diagrama de la BD Sakila](http://trifulcas.com/wp-content/uploads/2018/03/sakila-er.png)

## Paquetes Java

- <https://downloads.mysql.com/archives/get/p/3/file/mysql-connector-java-5.1.49.zip>
- <https://sourceforge.net/projects/hibernate/files/hibernate-orm/5.6.5.Final/hibernate-release-5.6.5.Final.zip/download>

## Servidores en Docker

### Testing

#### SonarQube

    docker run -d --name sonarQube --publish 9000:9000 sonarqube:latest

### Bases de datos

#### MySQL

    podman run -d --name mysql-sakila -e MYSQL_ROOT_PASSWORD=root -p 3306:3306 jamarton/mysql-sakila

#### MongoDB

    podman run -d --name mongodb -p 27017:27017 -v .:/externo jamarton/mongodb-contactos

#### Redis

    podman run -d --name redis -p 6379:6379 -p 6380:8001 -v .:/data redis/redis-stack:latest

#### Apache Cassandra

    docker run -d --name cassandra -p 9042:9042 -v .:/externo jamarton/cassandra-videodb
      
    docker exec -it cassandra sh -c /init-db.sh

### Agentes de Mensajería

#### RabbitMQ (AMQP)

    docker run -d --name rabbitmq -p 4369:4369 -p 5671:5671 -p 5672:5672 -p 15671:15671 -p 15672:15672 -p 25672:25672 -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=curso rabbitmq:management-alpine

#### Kafka (docker compose)

Fichero docker-compose.yml:

    services:
    zookeeper:
        image: confluentinc/cp-zookeeper:latest
        container_name: zookeeper
        environment:
        ZOOKEEPER_CLIENT_PORT: 2181
        ZOOKEEPER_TICK_TIME: 2000
        ports:
        - 2181:2181
    
    kafka:
        image: confluentinc/cp-kafka:latest
        container_name: kafka
        depends_on:
        - zookeeper
        ports:
        - 9092:9092
        environment:
        KAFKA_BROKER_ID: 1
        KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
        KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:29092,PLAINTEXT_HOST://localhost:9092
        KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
        KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
        KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
    
    kafka-ui:
        image: provectuslabs/kafka-ui
        container_name: kafka-ui
        depends_on:
        - kafka
        ports:
        - 9091:8080
        environment:
        - KAFKA_CLUSTERS_0_NAME=local
        - KAFKA_CLUSTERS_0_BOOTSTRAPSERVERS=kafka:29092
        - KAFKA_CLUSTERS_0_ZOOKEEPER=localhost:2181

Comando:

    cd docker-compose\kafka && docker compose up -d

#### Apache ActiveMQ o Artemis (JMS)

    docker run -d --name activemq -p 1883:1883 -p 5672:5672 -p 8161:8161 -p 61613:61613 -p 61614:61614 -p 61616:61616 jamarton/activemq

    docker run -d --name artemis -p 1883:1883 -p 5445:5445 -p 5672:5672 -p 8161:8161 -p 9404:9404 -p 61613:61613 -p 61616:61616 jamarton/artemis

### Monitorización, supervisión y trazabilidad

#### Prometheus (Monitorización)

    docker run -d -p 9090:9090 --name prometheus -v ./config-dir/prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus

#### Grafana (Monitorización)

    docker run -d -p 3000:3000 --name grafana grafana/grafana

#### Zipkin (Trazabilidad)

    docker run -d -p 9411:9411 --name zipkin openzipkin/zipkin-slim

#### ELK (supervisión)

    docker run -d -p 9200:9200 -p 9300:9300 --name=elasticsearch -h elasticsearch -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:7.12.0
    docker run -d -p 5601:5601 --name kibana -h kibana --link elasticsearch:elasticsearch docker.elastic.co/kibana/kibana:7.12.0

## Angular

### Angular Command Line Interface

    npm install -g @angular/cli
    ng version

### Servidor REST

    git clone https://github.com/jmagit/MOCKWebServer.git MOCKWebServer
    cd MOCKWebServer
    npm i
    npm start

### Contenedor: Mock REST

    docker run -d -p 4321:4321 --name mock-web-server jamarton/mock-web-server:latest

### Documentación

- [Oficial](https://angular.dev/)
- [Oficial (legacy)](https://angular.io/docs)

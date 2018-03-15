## TP
### Dans la console, créer un topic "KafkaMongoDB"
```
C:\Users\Fitec\kafka_2.11-1.0.1>bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic KafkaMongoDB
Created topic "KafkaMongoDB".
```
### Modifier les topics de application.yaml et application.yml
#### Consumer : application.yaml
```
kafka:
  bootstrap-servers: localhost:9092
  topic: KafkaMongoDB


swagger:
  title: Formation KAFKA
  description: Swagger Documentation api for Formation KAFKA PRODUCER application
  version: @project.version@
  enabled: true

server:
  port: 8077
```

#### Producer : application.yml
```
kafka:
  bootstrap-servers: localhost:9092
  topic: KafkaMongoDB


swagger:
  title: Formation KAFKA
  description: Swagger Documentation api for Formation KAFKA PRODUCER application
  version: @project.version@
  enabled: true

server:
  port: 8077
```

### Visualisation dans Swagger : http://localhost:8077/swagger-ui.html
Envoyer des données via le swagger
![](https://github.com/ctith/Kafka/blob/master/Kafka_screenshot/kafka%2001.PNG?raw=true)
![](https://github.com/ctith/Kafka/blob/master/Kafka_screenshot/kafka%2002.PNG?raw=true)

### Lancer le producer et consumer sur IntelliJ
- run ConsumerApplication
- run FormationkafkaApplication
- visualiser l'envoi et la réception des données envoyés par le swagger
![](https://github.com/ctith/Kafka/blob/master/Kafka_screenshot/kafka%20033.PNG?raw=true)
![](https://github.com/ctith/Kafka/blob/master/Kafka_screenshot/kafka%2004.PNG?raw=true)

### Visualiser la création du topic et les données sur MongoDB
![](https://github.com/ctith/Kafka/blob/master/Kafka_screenshot/kafka%20050.PNG?raw=true)
![](https://github.com/ctith/Kafka/blob/master/Kafka_screenshot/kafka%2005.PNG?raw=true)

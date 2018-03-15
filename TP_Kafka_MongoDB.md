# TP Kafka vers MongoDB
Envoyer les données du Producer -> Kafka -> Consumer -> MongoDB

## Lancer MongoDB 

### Terminal 1 : lancer le server MongoDB
```
C:\Program Files\MongoDB\Server\3.6\bin>mongod
```

### Terminal 2 : lancer le client port 27017
```
C:\Program Files\MongoDB\Server\3.6\bin>mongo
```
## Lancer Kafka
### Terminal 3 : lancer zookeeper
```
C:\Users\Fitec\kafka_2.11-1.0.1>bin\windows\zookeeper-server-start.bat config\zookeeper.properties
```

### Terminal 4 : lancer serveur kafka
```
C:\Users\Fitec\kafka_2.11-1.0.1>bin\\windows\\kafka-server-start.bat config\\server.properties
```

### Terminal 5 : créer un topic "KafkaMongoDB"
```
C:\Users\Fitec\kafka_2.11-1.0.1>bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic KafkaMongoDB
Created topic "KafkaMongoDB".
```

## Lancer les applications sur IntelliJ
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
Envoyer des données via le swagger en modifiant le champs Parameters puis cliquer sur "Try it out"
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

## Tests de performance

### Flooding
Création d'une boucle pour envoyer 100 000 requêtes d'insertion de données à MongoDB
![](https://github.com/ctith/Kafka/blob/master/Kafka_screenshot/kafka%2012.PNG?raw=true)

WARN 
![](https://github.com/ctith/Kafka/blob/master/Kafka_screenshot/kafka%2014.PNG?raw=true)

Visualisation des données sur MongoDB
![](https://github.com/ctith/Kafka/blob/master/Kafka_screenshot/kafka%2013.PNG?raw=true)

### Fault-tolerance
#### Modifier le fichier application.yml (producer) en précisant le port des brokers
```
kafka:
  bootstrap-servers: localhost:9092, localhost:9093
  topic: KafkaMongoDB


swagger:
  title: Formation KAFKA
  description: Swagger Documentation api for Formation KAFKA PRODUCER application
  version: @project.version@
  enabled: true

server:
  port: 8077
```

![](https://github.com/ctith/Kafka/blob/master/Kafka_screenshot/kafka%2015.PNG?raw=true)

#### Modifier le fichier application.yaml (consumer) en précisant le port des brokers
```
server:
  port: 8000

kafka:
  host: localhost:9092, localhost:9093
  #port: 9092
  topic:
    car: KafkaMongoDB
    default-group-id: default-group-id

spring:
  data:
    mongodb:
      host: localhost
      port: 27017
      database: car-test
```

![](https://github.com/ctith/Kafka/blob/master/Kafka_screenshot/kafka%2016.PNG?raw=true)

#### Modifier le fichier ContainersConfiguration.java 
> C:\Users\Fitec\IdeaProjects\formationspringkafka\comformationspringkafkaconsumer\src\main\java\com\formation\kafka\consumer\configuration\technical\ContainersConfiguration.java 

```
	private ConsumerFactory<String, CarMessage> carConsumerFactory() {
		Map<String, Object> props = new HashMap<>();

		// si application.yaml avec port
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getHost() + ":" + kafkaProperties.getPort());
		// si application.yaml sans port
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getHost());

		props.put(ConsumerConfig.GROUP_ID_CONFIG, topicProperties.getDefaultGroupId());
		return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(CarMessage.class));
	}
```

![](https://github.com/ctith/Kafka/blob/master/Kafka_screenshot/kafka%2017.PNG?raw=true)

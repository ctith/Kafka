# TP Kafka Spring
Envoyer les données du Producer -> Kafka -> Consumer -> MongoDB

## Lancer MongoDB 

### Terminal 1 : lancer le server MongoDB
```shell
C:\Program Files\MongoDB\Server\3.6\bin>mongod
```

### Terminal 2 : lancer le client port 27017
```shell
C:\Program Files\MongoDB\Server\3.6\bin>mongo
```
## Lancer Kafka
### Terminal 3 : lancer zookeeper
```shell
C:\Users\Fitec\kafka_2.11-1.0.1>bin\windows\zookeeper-server-start.bat config\zookeeper.properties
```

### Terminal 4 : lancer serveur kafka
```shell
C:\Users\Fitec\kafka_2.11-1.0.1>bin\\windows\\kafka-server-start.bat config\\server.properties
```

### Terminal 5 : créer un topic "KafkaMongoDB"
```shell
C:\Users\Fitec\kafka_2.11-1.0.1>bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic KafkaMongoDB
Created topic "KafkaMongoDB".
```

## Lancer les applications sur IntelliJ
### Modifier les topics de application.yaml et application.yml
#### Consumer : application.yaml
```yml
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
```yml
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

### Rajouter une donnée via Swagger : http://localhost:8077/swagger-ui.html
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

--------------------------------
## Tests de performance
-------------------------------

### Flooding
Création d'une boucle pour envoyer 100 000 requêtes d'insertion de données à MongoDB
![](https://github.com/ctith/Kafka/blob/master/Kafka_screenshot/kafka%2012.PNG?raw=true)

WARN : cette erreur signifie qu'on a utilisé le server kafka pour un autre processus
![](https://github.com/ctith/Kafka/blob/master/Kafka_screenshot/kafka%2014.PNG?raw=true)

Visualisation des données sur MongoDB
![](https://github.com/ctith/Kafka/blob/master/Kafka_screenshot/kafka%2013.PNG?raw=true)

-----------------------------

### Fault-tolerance

#### Configurer 2 brokers 

Configurer fichier server-2.properties
[voir TP initiation](https://github.com/ctith/Kafka/blob/master/TP_machine_locale.md)

Lancer le server dans un terminal
```shell
ctith@L50T-048:/mnt/c/Users/Fitec/kafka_2.11-1.0.1$ vi config//server-1.properties
```

#### Modifier les fichiers java pour préciser la présence de plusieurs brokers

##### Application.yml (producer)
```yml
kafka:
  bootstrap-servers: localhost:9092, localhost:9093
  topic: killBroker


swagger:
  title: Formation KAFKA
  description: Swagger Documentation api for Formation KAFKA PRODUCER application
  version: @project.version@
  enabled: true

server:
  port: 8077
```

![](https://github.com/ctith/Kafka/blob/master/Kafka_screenshot/kafka%2015.PNG?raw=true)

##### Application.yaml (consumer) 
```yml
server:
  port: 8000

kafka:
  host: localhost:9092, localhost:9093
  #port: 9092
  topic:
    car: killBroker
    default-group-id: default-group-id

spring:
  data:
    mongodb:
      host: localhost
      port: 27017
      database: kill-test
```

![](https://github.com/ctith/Kafka/blob/master/Kafka_screenshot/kafka%2016.PNG?raw=true)

##### Programme ContainersConfiguration.java 
> C:\Users\Fitec\IdeaProjects\formationspringkafka\comformationspringkafkaconsumer\src\main\java\com\formation\kafka\consumer\configuration\technical\ContainersConfiguration.java 

```java
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

#### Créer un topic "killBroker" avec 2 brokers de réplication 2

Création du topic
```shell
C:\Users\Fitec\kafka_2.11-1.0.1>bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 2 --partitions 2 --topic killBroker
Created topic "killBroker".
```

Liste de topic
```shell
ctith@L50T-048:/mnt/c/Users/Fitec/kafka_2.11-1.0.1$ bin/kafka-topics.sh --list --zookeeper localhost:2181
KafkaMongoDB
__consumer_offsets
car-test
killBroker
my-replicated-topic
test
test-consumer
topic-in
topic-out
```

Ici, le topic a aucun server leader. Cela signifie qu'un des serveurs n'a pas été lancé.
```shell
ctith@L50T-048:/mnt/c/Users/Fitec/kafka_2.11-1.0.1$ bin/kafka-topics.sh --describe --zookeeper localhost:2181 --topic killBroker
Topic:killBroker        PartitionCount:2        ReplicationFactor:2     Configs:
        Topic: killBroker       Partition: 0    Leader: none    Replicas: 1,0   Isr:
        Topic: killBroker       Partition: 1    Leader: none    Replicas: 0,1   Isr:
```

Après démarrage du 2ème serveur 
```shell
ctith@L50T-048:/mnt/c/Users/Fitec/kafka_2.11-1.0.1$ bin/kafka-topics.sh --describe --zookeeper localhost:2181 --topic killBroker
Topic:killBroker        PartitionCount:2        ReplicationFactor:2     Configs:
        Topic: killBroker       Partition: 0    Leader: 1       Replicas: 1,0   Isr: 1,0
        Topic: killBroker       Partition: 1    Leader: 1       Replicas: 0,1   Isr: 1,0
```

#### Comment faire marcher les scripts

0. Supprimer les dossiers logs dans C:/tmp/
1. Run les deux programmes java "Producer" puis "Consumer"
2. Rajouter une donnée via Swagger
3. Regarder la bonne insertion des données sur l'interface MongoDB
4. Tuer un serveur (ctrl+c)
5. Re-regarder l'interface de MongoDB => les données continuent d'être insérées même si un des broker est shutdown

# TP Kafka avec programme Java
[Voir dossier JavaProducerConsumer](https://github.com/ctith/Kafka/tree/master/JavaProducerConsumer)

## Ecoute basique des messages écrits en console : ProducerMain et ConsumerBasic

### Producer : envoie "Hello mom" + index (id de la partition)
![](https://github.com/ctith/Kafka/blob/master/Kafka_screenshot/kafka%2006.PNG?raw=true)

### Consumer : reçoit "Hello mom" + index (id de la partition)
#### Sur IntelliJ
![](https://github.com/ctith/Kafka/blob/master/Kafka_screenshot/kafka%2007.PNG)

#### Dans la console
![](https://github.com/ctith/Kafka/blob/master/Kafka_screenshot/kafka%2008.PNG)

--------------------
## Ecoute de documents JSON

### Classe "contact"
![](https://github.com/ctith/Kafka/blob/master/Kafka_screenshot/kafka%2009.PNG)

### Création d'un objet "contact" dans ProducerJson
![](https://github.com/ctith/Kafka/blob/master/Kafka_screenshot/kafka%2010.PNG)

### Ecoute de l'objet "contact" dans KafkaConsumerMainJson
![](https://github.com/ctith/Kafka/blob/master/Kafka_screenshot/kafka%2011.PNG)

---------------------
## Kafka Stream 

à tester car incompatibilité SLF4J et IntelliJ
> Class path contains multiple SLF4J bindings avec IntelliJ Enterprise)

# TP KAFKA
## Sous windows 
### Installation de Kafka sous windows
> [https://kafka.apache.org/downloads](https://kafka.apache.org/downloads)
télécharger la version correspondant à la version qu’on a de scala, puis décompresser en local

### Commandes windows kafka
> attention aux backslash (différent sous linux)

faire ces commandes dans le dossier kafka : 
> C:\Users\Fitec\kafka_2.11-1.0.1>
```
bin\\windows\\zookeeper-server-start.bat config\\zookeeper.properties

bin\\windows\\kafka-server-start.bat config\\server.properties

bin\\windows\\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test

bin\\windows\\kafka-topics.bat --list --zookeeper localhost:2181

bin\\windows\\kafka-console-producer.bat --broker-list localhost:9092 --topic test

bin\\windows\\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic test --from-beginning
```
----------
## Sous linux
### Installer kafka sur la machine
```
ctith@L50T-048:/mnt/c/Users/Fitec$ wget http://apache.mindstudios.com/kafka/1.0.1/kafka_2.11-1.0.1.tgz

ctith@L50T-048:/mnt/c/Users/Fitec$ tar -xf kafka_2.11-1.0.1.tgz
```
### Terminal 1 : Démarrer zookeeper
```
ctith@L50T-048:/mnt/c/Users/Fitec/kafka_2.11-1.0.1$ ./bin/zookeeper-server-start.sh ./config/zookeeper.properties
```
#### Fichier de configuration de zookeeper
```
ctith@L50T-048:/mnt/c/Users/Fitec/kafka_2.11-1.0.1$ cat config/zookeeper.properties
	dataDir=/tmp/zookeeper
	clientPort=2181
	maxClientCnxns=0
```
#### Vérification du bon lancement de zookeeper, serveur avec lequel on peut se connecter via le port 2181
```
ctith@L50T-048:/mnt/c/Users/Fitec/kafka_2.11-1.0.1$ telnet localhost 2181

Trying 127.0.0.1...
Connected to localhost.
Escape character is '^\]'.
^CConnection closed by foreign host.
```
----------
### Terminal 2 : Démarrer un broker (serveur) kafka

```
ctith@L50T-048:/mnt/c/Users/Fitec/kafka_2.11-1.0.1$ ./bin/kafka-server-start.sh ./config/server.properties
```

#### Fichier de configuration de kafka
```
./bin/kafka-server-start.sh ./config/server.properties

# Identifiant de notre broker
broker.id=0

# Nom d'hôte et port sur lequel écoute le broker Kafka
listeners=PLAINTEXT://:9092

# Décommentez cette ligne pour permettre la suppression de topic, ce qui sera utile par la suite
delete.topic.enable=true

# Vérifiez que vous disposez de suffisamment d'espace disque sur la partition qui contient ce répertoire
log.dirs=/tmp/kafka-logs
num.partitions=1

# Paramètres concernant la rétention des données
log.retention.hours=168
log.segment.bytes=1073741824
log.retention.check.interval.ms=300000

\# Connexion à Zookeeper
zookeeper.connect=localhost:2181
zookeeper.connection.timeout.ms=6000
```
----------
### Terminal 3 : Producer va écrire des messages sur le port 2181

#### Création d’un topic “test”
```
ctith@L50T-048:/mnt/c/Users/Fitec/kafka_2.11-1.0.1$ bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test

Created topic "test".
```

#### Voir la liste des topics
```
ctith@L50T-048:/mnt/c/Users/Fitec/kafka_2.11-1.0.1$ bin/kafka-topics.sh --list --zookeeper localhost:2181

test
```

#### Producer envoie des messages au topic “test” à écouter sur le port 9092
```
ctith@L50T-048:/mnt/c/Users/Fitec/kafka_2.11-1.0.1$ bin/kafka-console-producer.sh --broker-list localhost:9092 --topic test

>ceci est un message
>helloooo
>coucou
>taratata
>hehehehe
```
----------

### Terminal 4 : Consumer va écouter les messages sur le port 9092

```
ctith@L50T-048:/mnt/c/Users/Fitec/kafka_2.11-1.0.1$ bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test --from-beginning

ceci est un message
helloooo
coucou
taratata
hehehehe
```
----------
### Terminal 5 : étendre kafka de 1 à 3 broker

#### Copier fichiers config broker 0 vers broker 1 et 2
```
ctith@L50T-048:/mnt/c/Users/Fitec/kafka_2.11-1.0.1$ cp config/server.properties config/server-1.properties

ctith@L50T-048:/mnt/c/Users/Fitec/kafka_2.11-1.0.1$ cp config/server.properties config/server-2.properties
```
#### Configurer fichiers config de broker 1 et broker 2
```
ctith@L50T-048:/mnt/c/Users/Fitec/kafka_2.11-1.0.1$ vi config//server-1.properties
	broker.id=1
	listeners=PLAINTEXT://:9093
	log.dir=/tmp/kafka-logs-1

ctith@L50T-048:/mnt/c/Users/Fitec/kafka_2.11-1.0.1$ vi config//server-2.properties
	broker.id=2
	listeners=PLAINTEXT://:9094
	log.dir=/tmp/kafka-logs-2
```

#### Démarrer le broker 1 et 2
```
ctith@L50T-048:/mnt/c/Users/Fitec/kafka_2.11-1.0.1$ bin/kafka-server-start.sh config/server-1.properties &
[1] 1827

ctith@L50T-048:/mnt/c/Users/Fitec/kafka_2.11-1.0.1$ bin/kafka-server-start.sh config/server-2.properties &
[2] 1909
```
#### Création d’un nouveau topic “my-replicated-topic” avec un facteur de réplication de 3
```
ctith@L50T-048:/mnt/c/Users/Fitec/kafka_2.11-1.0.1$ bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 3 --partitions 1 --topic my-replicated-topic
```

#### Visualiser les partitions par topic
```
ctith@L50T-048:/mnt/c/Users/Fitec/kafka_2.11-1.0.1$ bin/kafka-topics.sh --describe --zookeeper localhost:2181 --topic test

Topic:test PartitionCount:1 ReplicationFactor:1 Configs:
Topic: test Partition: 0 Leader: 0 Replicas: 0 Isr: 0

ctith@L50T-048:/mnt/c/Users/Fitec/kafka_2.11-1.0.1$ bin/kafka-topics.sh --describe --zookeeper localhost:2181 --topic my-replicated-topic

Topic:my-replicated-topic PartitionCount:1 ReplicationFactor:3 Configs:
Topic: my-replicated-topic Partition: 0 Leader: 1 Replicas: 0,1,2 Isr: 1,2,0
```
#### Producer : Ecrire des messages sur le topic “my-replicated-topic”

```
ctith@L50T-048:/mnt/c/Users/Fitec/kafka_2.11-1.0.1$ bin/kafka-console-producer.sh --broker-list localhost:9092 --topic my-replicated-topic
>hellooooooo

```

#### Consumer : Recevoir les messages du topic “my-replicated-topic”
```
ctith@L50T-048:/mnt/c/Users/Fitec/kafka_2.11-1.0.1$ bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --from-beginning --topic my-replicated-topic

hellooooooo
```
----------
### Election d’un nouveau broker leader

#### Tuer le broker 1 qui est leader
```
ctith@L50T-048:/mnt/c/Users/Fitec/kafka_2.11-1.0.1$ bin/kafka-topics.sh --describe --zookeeper localhost:2181 --topic my-replicated-topic
  
Topic:my-replicated-topic PartitionCount:1 ReplicationFactor:3 Configs:
Topic: my-replicated-topic Partition: 0 Leader: 1 Replicas: 0,1,2 Isr: 1,2,0
```
#### Récupérer l’adresse de la machine broker 1
```
ctith@L50T-048:/mnt/c/Users/Fitec/kafka_2.11-1.0.1$ ps aux | grep server-1.properties

ctith 1827 1.4 2.0 4669364 349644 tty5 Sl 11:54 0:52
```
#### Tuer le broker 1
```
ctith@L50T-048:/mnt/c/Users/Fitec/kafka_2.11-1.0.1$ kill -9 1827
```

#### Afficher le nouveau broker leader
```
ctith@L50T-048:/mnt/c/Users/Fitec/kafka_2.11-1.0.1$ bin/kafka-topics.sh --describe --zookeeper localhost:2181 --topic my-replicated-topic

Topic:my-replicated-topic PartitionCount:1 ReplicationFactor:3 Configs:
Topic: my-replicated-topic Partition: 0 Leader: 0 Replicas: 0,1,2 Isr: 2,0
```

#### Producer : Ecrire des messages sur le topic “my-replicated-topic”
```
ctith@L50T-048:/mnt/c/Users/Fitec/kafka_2.11-1.0.1$ bin/kafka-console-producer.sh --broker-list localhost:9092 --topic my-replicated-topic
>hellooooooo
```
#### Consumer : Recevoir les messages du topic “my-replicated-topic”
```
ctith@L50T-048:/mnt/c/Users/Fitec/kafka_2.11-1.0.1$ bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --from-beginning --topic my-replicated-topic

hellooooooo
```

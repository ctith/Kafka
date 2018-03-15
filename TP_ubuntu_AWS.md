# TP KAFKA
## Suite du TP sur ubuntu AWS 
Sur AWS, nous avons un cluster composé de 3 noeuds : 
- Karine : 34.245.31.28
- Caroline : 34.240.128.32
- Pierre-Dominique : 34.249.95.103

Chaque noeud va devenir un broker kafka et on utilise le zookeeper de Pierre-Dominique.

### Installer java
```
ubuntu@ip-172-31-31-224:/usr/hdp/2.6.3.0-235/kafka$ sudo apt-get install openjdk-8-jdk
```
#### Ecouter les blagues de Karine 
```
ubuntu@ip-172-31-31-224:/usr/hdp/2.6.3.0-235/kafka$ bin/kafka-console-consumer.sh --bootstrap-server 34.249.95.103:9092 --from-beginning --topic karine
```

### Paramétrer son noeud en broker 2 (Pierre-Dom = broker 0, Karine = broker 1)
```
ubuntu@ip-172-31-31-224:/usr/hdp/2.6.3.0-235/kafka$ cp config/server.properties config/server-2.properties
	broker.id=2
	listeners=PLAINTEXT://:9094
	log.dir=/tmp/kafka-logs-2
	zookeeper.connect=34.249.95.103:2181
```

#### Démarrer le broker 2
```
ubuntu@ip-172-31-31-224:/usr/hdp/2.6.3.0-235/kafka$ bin/kafka-server-start.sh config/server-2.properties &
```

#### Créer le topic "cars-topic" : nombre de répartition 5 et facteur de réplication 3  
```
ubuntu@ip-172-31-31-224:/usr/hdp/2.6.3.0-235/kafka$ bin/kafka-topics.sh --describe --zookeeper 34.249.95.103:2181 --topic cars-topic
Topic:cars-topic        PartitionCount:5        ReplicationFactor:3     Configs:
        Topic: cars-topic       Partition: 0    Leader: 2       Replicas: 2,0,1 Isr: 2,0,1
        Topic: cars-topic       Partition: 1    Leader: 3       Replicas: 3,1,2 Isr: 3,1,2
        Topic: cars-topic       Partition: 2    Leader: 0       Replicas: 0,2,3 Isr: 0,2,3
        Topic: cars-topic       Partition: 3    Leader: 1       Replicas: 1,3,0 Isr: 1,3,0
        Topic: cars-topic       Partition: 4    Leader: 2       Replicas: 2,1,3 Isr: 2,1,3
```
#### Lister les topics existants sur Kafka
```
ubuntu@ip-172-31-31-224:/usr/hdp/2.6.3.0-235/kafka$ bin/kafka-topics.sh --list --zookeeper 34.249.95.103:2181
	__consumer_offsets
	caroline
	cars-topic
	karine
	la-yoloattitude
	my-replicated-topic
	test
	yolo
```

_____________
### Lire le topic cars-topic à partir d’un groupe de consumer

#### Créer un fichier consumer-propertie pour chaque nouveau consumer
```
ubuntu@ip-172-31-31-224:/usr/hdp/2.6.3.0-235/kafka/config$ cp consumer.properties consumer-2.properties
```

#### Modifier le fichier config
```
ubuntu@ip-172-31-31-224:/usr/hdp/2.6.3.0-235/kafka/config$ sudo vi consumer-2.properties

zookeeper.connect=34.249.95.103:2181
group.id=yolo
```
#### Ecouter les messages de Pierre-Dominique
```
ubuntu@ip-172-31-31-224:/usr/hdp/2.6.3.0-235/kafka$ sudo bin/kafka-console-consumer.sh --bootstrap-server 34.249.95.103:9092 --topic cars-topic --from-beginning --consumer.config config/consumer-2.properties

\[2018-03-13 15:09:36,542\] WARN The configuration 'zookeeper.connect' was supplied but isn't a known config. (org.apache.kafka.clients.consumer.ConsumerConfig)
\[2018-03-13 15:09:36,542\] WARN The configuration 'zookeeper.connection.timeout.ms' was supplied but isn't a known config. (org.apache.kafka.clients.consumer.ConsumerConfig)
2
7
13
18
23
28
33
```
### LE KILL DES BROCKERS
#### AVANT
```
ubuntu@ip-172-31-31-224:/usr/hdp/2.6.3.0-235/kafka$ bin/kafka-topics.sh --describe --zookeeper 34.249.95.103:2181 --topic cars-topic
Topic:cars-topic        PartitionCount:5        ReplicationFactor:3     Configs:
        Topic: cars-topic       Partition: 0    Leader: 2       Replicas: 2,0,1 Isr: 2,0,1
        Topic: cars-topic       Partition: 1    Leader: 3       Replicas: 3,1,2 Isr: 3,1,2
        Topic: cars-topic       Partition: 2    Leader: 0       Replicas: 0,2,3 Isr: 0,2,3
        Topic: cars-topic       Partition: 3    Leader: 1       Replicas: 1,3,0 Isr: 1,3,0
        Topic: cars-topic       Partition: 4    Leader: 2       Replicas: 2,1,3 Isr: 2,1,3
```
#### APRES
> On passe de 4 brockers à 1 brocker : certaines partitions n'auront plus de brocker leader donc kafka n'enverra pas ces partitions au producer pour une réécriture. Les données de ces partitions existent toujours mais elles sont dans un brocker qui est fermé. Pour y accéder, il faudra juste restarter le brocker.
```
ubuntu@ip-172-31-31-224:/usr/hdp/2.6.3.0-235/kafka$ bin/kafka-topics.sh --describe --zookeeper 34.249.95.103:2181 --topic cars-topic
Topic:cars-topic        PartitionCount:5        ReplicationFactor:3     Configs:
        Topic: cars-topic       Partition: 0    Leader: 0       Replicas: 2,0,1 Isr: 0,1,2
        Topic: cars-topic       Partition: 1    Leader: 3       Replicas: 3,1,2 Isr: 3,1,2
        Topic: cars-topic       Partition: 2    Leader: 0       Replicas: 0,2,3 Isr: 0,3,2
        Topic: cars-topic       Partition: 3    Leader: 1       Replicas: 1,3,0 Isr: 1,3,0
        Topic: cars-topic       Partition: 4    Leader: 1       Replicas: 2,1,3 Isr: 1,3,2
	
ubuntu@ip-172-31-31-224:/usr/hdp/2.6.3.0-235/kafka$ bin/kafka-topics.sh --describe --zookeeper 34.249.95.103:2181 --topic cars-topic
Topic:cars-topic        PartitionCount:5        ReplicationFactor:3     Configs:
        Topic: cars-topic       Partition: 0    Leader: 0       Replicas: 2,0,1 Isr: 0
        Topic: cars-topic       Partition: 1    Leader: -1      Replicas: 3,1,2 Isr:
        Topic: cars-topic       Partition: 2    Leader: 0       Replicas: 0,2,3 Isr: 0
        Topic: cars-topic       Partition: 3    Leader: 0       Replicas: 1,3,0 Isr: 0
        Topic: cars-topic       Partition: 4    Leader: -1      Replicas: 2,1,3 Isr:
```

### LE RETOUR DES BROCKERS 
#### BROCKER 2 BACK
```
ubuntu@ip-172-31-31-224:/usr/hdp/2.6.3.0-235/kafka$bin/kafka-server-start.sh config/server-2.properties &

ubuntu@ip-172-31-31-224:/usr/hdp/2.6.3.0-235/kafka$ bin/kafka-topics.sh --describe --zookeeper 34.249.95.103:2181 --topic cars-topic
Topic:cars-topic        PartitionCount:5        ReplicationFactor:3     Configs:
        Topic: cars-topic       Partition: 0    Leader: 0       Replicas: 2,0,1 Isr: 0,2
        Topic: cars-topic       Partition: 1    Leader: 2       Replicas: 3,1,2 Isr: 2
        Topic: cars-topic       Partition: 2    Leader: 0       Replicas: 0,2,3 Isr: 0,2
        Topic: cars-topic       Partition: 3    Leader: 0       Replicas: 1,3,0 Isr: 0
        Topic: cars-topic       Partition: 4    Leader: 2       Replicas: 2,1,3 Isr: 2
```
#### ALL BROCKERS BACK
> On reprend la même configuration de leader qu'au départ mais cela est sûrement du à la rapidité de réponse des brockers aux signaux de kafka. Il ne s'agit pas d'une réattribution consciente, l'attribution des leaders se fait toujours "aléatoirement".
```
ubuntu@ip-172-31-31-224:/usr/hdp/2.6.3.0-235/kafka$ bin/kafka-topics.sh --describe --zookeeper 34.249.95.103:2181 --topic cars-topic
Topic:cars-topic        PartitionCount:5        ReplicationFactor:3     Configs:
        Topic: cars-topic       Partition: 0    Leader: 2       Replicas: 2,0,1 Isr: 0,2,1
        Topic: cars-topic       Partition: 1    Leader: 3       Replicas: 3,1,2 Isr: 2,3,1
        Topic: cars-topic       Partition: 2    Leader: 0       Replicas: 0,2,3 Isr: 0,2,3
        Topic: cars-topic       Partition: 3    Leader: 1       Replicas: 1,3,0 Isr: 0,3,1
        Topic: cars-topic       Partition: 4    Leader: 2       Replicas: 2,1,3 Isr: 2,3,1
```

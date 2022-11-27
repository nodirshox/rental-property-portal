#!/bin/bash

kubectl apply -f configmap.yaml
kubectl apply -f secret.yaml

# cassandra
kubectl apply -f services/cassandra-service.yaml
kubectl exec --stdin --tty cassandra-deployment-56fbcb86dc-54pjt -- /bin/bash
cqlsh
CREATE KEYSPACE airbnb WITH REPLICATION = {'class' : 'SimpleStrategy','replication_factor' : 1};
use airbnb;
create table user(id int primary key, firstname text, lastname text, email text, password text, role text);
create table orders(id int primary key, userId int, email text, propertyId text, price double, isPaid boolean);
create table notification(id int primary key, email text, message text, error text);



# kafka
kubectl apply -f services/zookeeper.yaml
kubectl apply -f services/kafka.yaml
#kubectl exec -it kafka-deployment-7cc5d4577d-rp8rw /bin/bash
#kafka-topics --create --bootstrap-server localhost:29092 --replication-factor 1 --partitions 1 --topic minikube-topic

kubectl apply -f services/mongo.yaml
kubectl exec -it mongodb-deployment-67649c4d4c-wghdt mongosh
use admin
db.auth("airbnb", "123456")
use property_service

db.createUser({
    user: 'airbnb',
    pwd: '123456',
    roles: [{ role: 'readWrite', db:'airbnb'}]
})

db.property.deleteMany({})
db.property.find()

kubectl apply -f services/notification-service.yaml
kubectl apply -f services/user-service.yaml
kubectl apply -f services/payment.yaml


redis password: mlWI3UQwv1
Gu3Ejq1cTC

To get your password run:
    export REDIS_PASSWORD=$(kubectl get secret --namespace "default" my-redis-cluster -o jsonpath="{.data.redis-password}" | base64 --decode)

You have deployed a Redis&trade; Cluster accessible only from within you Kubernetes Cluster.INFO: The Job to create the cluster will be created.To connect to your Redis&trade; cluster:

1. Run a Redis&trade; pod that you can use as a client:
kubectl run --namespace default my-redis-cluster-client --rm --tty -i --restart='Never' \
 --env REDIS_PASSWORD=$REDIS_PASSWORD \
--image docker.io/bitnami/redis-cluster:6.2.7-debian-10-r0 -- bash

2. Connect using the Redis&trade; CLI:

redis-cli -c -h my-redis-cluster -a $REDIS_PASSWORD


-----

Redis&reg; can be accessed on the following DNS names from within your cluster:

    my-redis-master.default.svc.cluster.local for read/write operations (port 6379)
    my-redis-replicas.default.svc.cluster.local for read-only operations (port 6379)



To get your password run:

    export REDIS_PASSWORD=$(kubectl get secret --namespace default my-redis -o jsonpath="{.data.redis-password}" | base64 -d)

To connect to your Redis&reg; server:

1. Run a Redis&reg; pod that you can use as a client:

   kubectl run --namespace default redis-client --restart='Never'  --env REDIS_PASSWORD=$REDIS_PASSWORD  --image docker.io/bitnami/redis:7.0.5-debian-11-r7 --command -- sleep infinity

   Use the following command to attach to the pod:

   kubectl exec --tty -i redis-client \
   --namespace default -- bash

2. Connect using the Redis&reg; CLI:
   REDISCLI_AUTH="$REDIS_PASSWORD" redis-cli -h my-redis-master
   REDISCLI_AUTH="$REDIS_PASSWORD" redis-cli -h my-redis-replicas

To connect to your database from outside the cluster execute the following commands:

    kubectl port-forward --namespace default svc/my-redis-master 6379:6379 &
    REDISCLI_AUTH="$REDIS_PASSWORD" redis-cli -h 127.0.0.1 -p 6379

ia7SAk6108
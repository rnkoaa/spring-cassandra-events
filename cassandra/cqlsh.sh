#!/bin/sh


docker run --name cassandra_dev -m 2g -d   -d -p "7191:7191"
-p "7000:7000" -p "7001:7001" -p "9160:9160" -p "9042:9042" -e
CASSANDRA_BROADCAST_ADDRESS=192.168.99.100 cassandra:3.7

docker run -it --link cassandra_dev --rm cassandra:3.7 cqlsh cassandra_dev

CREATE KEYSPACE IF NOT EXISTS manuel
WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

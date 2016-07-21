#!/bin/sh

docker run --name cassandra_dev -m 2g -d  -p 127.0.0.1:9042:9042 -p 127.0.0.1:9160:9160 cassandra:3.7
docker run -it --link cassandra_dev --rm cassandra:3.7 cqlsh cassandra_dev

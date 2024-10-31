# Create a custom Dockerfile based on a Neo4j image:
FROM neo4j:5.20.0-community

WORKDIR /var/lib/neo4j
COPY conf/neo4j.conf conf/neo4j.conf
COPY conf/apoc.conf conf/apoc.conf

WORKDIR /var/lib/neo4j
COPY plugins/*.jar plugins/

ENV NEO4J_AUTH=neo4j/localhost

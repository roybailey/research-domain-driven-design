# bud-cloud-container-java

**Docker image of Neo4 graph database with apoc plugins**

Module of [**`..`**](../README.md) *parent*


## Design

* Standard Neo4j docker image with apoc plugin jar pre-installed


## User Guide

* Install as container `roybailey/container-neo4j:<version>`


## Developers Guide

* `mvn clean install`
* run the neo4j docker image : `docker run --rm --platform=linux/arm64 --publish=7474:7474 --publish=7687:7687 roybailey/container-neo4j:0.1.0`
* attach to neo4j docker container : `docker run --rm --platform linux/amd64 -it roybailey/container-neo4j:<version> /bin/bash`



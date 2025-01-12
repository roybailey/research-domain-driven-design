# research-domain-driven-design

**Motivation to setup a multi-module project to experiment with Domain Driven Design approach**


## Design

* Separate domain models from all else, centrally defined
* Separate domain application interfaces
* build tests using https://testcontainers.org to demonstrate the models and ports working 


## Modules

| Module                                                 | Description                           |
|--------------------------------------------------------|---------------------------------------| 
| [**`ddd-domain-model`**](./ddd-domain-model/README.md) | Domain _models_, _interfaces_         |
| [**`ddd-domain-spi`**](./ddd-domain-spi/README.md)     | Domain _'driven'_ spi implementations |
| [**`ddd-domain-api`**](./ddd-domain-api/README.md)     | Domain _'driver'_ api implementations |

### Model Module

_Contains data classes, use-case methods for spi (service-provider-interface) ports such as storage, notifications etc._

* [**`Domain`**](./ddd-domain-model/src/main/java/me/roybailey/domain) - generic/shared classes
* [**`Audit`**](./ddd-domain-model/src/main/java/me/roybailey/domain/audit) - bounded context for recording/publishing audit events
* [**`Entitlement`**](./ddd-domain-model/src/main/java/me/roybailey/domain/entitlement) - bounded context for access control and entitlements

### SPI Module

_Contains application domain business logic combined with real or mock service provider implementations sufficient to 
test all incoming business logic calls with data and verify storage and spi outgoing interactions._

* [**`bounded contexts`**](./ddd-domain-spi/src/main/java/me/roybailey/domain)


### API Module

_Contains incoming api implementations sufficient to test all incoming interactions with REST APIs, message listeners etc._

* [**`bounded contexts`**](./ddd-domain-api/src/main/java/me/roybailey/domain) - generic/shared classes


## User Guide


## Developers Guide

* `mvn clean install`


### Prerequisites

* Docker and https://testcontainers.org
* See [Neo4j DockerFile](container-neo4j/Dockerfile)
* See [Neo4j TestContainer](ddd-domain-spi/src/test/java/me/roybailey/domain/container/Neo4jTestContainer.java)
* See [Postgres TestContainer](ddd-domain-spi/src/test/java/me/roybailey/domain/container/PostgresTestContainer.java)


## Handover Suggestions

_Nuggets of Knowledge and Thinking from last people to work on the project._
_e.g. suggestions for technical debt reduction, simplification or enhancements_

- [ ] Add Kafka test containers and audit publication of events
- [ ] Add Entitlement/Group/Package yaml based import/export support and use for more complex test data use-cases
- [ ] Add detailed Audit query by reference, type, action, to improve test verification of audit data
- [ ] Add Entitlement owner field and find/delete by owner to improve test data isolation
- [ ] Expand Package storage/domain implementations and tests to cover basic CRUD flows
- [ ] Expand Group storage/domain implementations and tests to cover basic CRUD flows
- [ ] Expand Entitlement storage/domain implementations and tests to cover basic CRUD flows
- [ ] Expand Audit storage/domain implementations and tests to cover basic CRUD flows
- [X] Setup Audit & Entitlement bounded context domains with basic creation tests
- [X] Setup module structure to cover domain models/ports, driven implementations, driver implementations
- [X] Setup Neo4j Docker image with apoc pre-bundled
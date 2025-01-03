# research-domain-driven-design

**Motivation to setup a multi-module project to experiment with Domain Driven Design approach**


## Design

* Separate domain models from all else, centrally defined
* Separate domain application interface
* build tests using https://testcontainers.org to demonstrate the models and ports working 


## Modules

| Module                                           | Description                                             |
|--------------------------------------------------|---------------------------------------------------------| 
| [**`ddd-domain`**](./ddd-domain/README.md)       | Domain _models_ and _api interfaces_, use-cases, ports  |
| [**`ddd-framework`**](./ddd-framework/README.md) | Domain _'driven'_ api implementations, use-cases, ports |
| [**`ddd-service`**](./ddd-service/README.md)     | Domain _'driver'_ api implementations, use-cases, ports |


## User Guide


## Developers Guide

* `mvn clean install`


### Prerequisites

* Docker and https://testcontainers.org


## Handover Suggestions

_Nuggets of Knowledge and Thinking from last people to work on the project._
_e.g. suggestions for technical debt reduction, simplification or enhancements_

- [ ] Expand Audit & Entitlement storage/domain implementations and tests to cover basic CRUD flows   
- [X] Setup Audit & Entitlement bounded context domains with basic creation tests
- [X] Setup module structure to cover domain models/ports, driven implementations, driver implementations
- [X] Setup Neo4j Docker image with apoc pre-bundled
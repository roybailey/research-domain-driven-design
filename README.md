# research-domain-driven-design

**Motivation to setup a multi-module project to experiment with Domain Driven Design approach**


## Design

* Separate domain models from all else, centrally defined
* Separate domain application interface
* build tests using https://testcontainers.org to demonstrate the models and ports working 


## Modules

| Module                                                | Description                                           |
|-------------------------------------------------------|-------------------------------------------------------| 
| [**`ddd-domain`**](./bud-domain-blueprint/README.md)  | Domain models and api interfaces, use-cases, ports    |
| [**`ddd-framework`**](./bud-domain-library/README.md) | Domain 'driven' api implementations, use-cases, ports |
| [**`ddd-service`**](./bud-domain-manager/README.md)   | Domain 'driver' api implementations, use-cases, ports |


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
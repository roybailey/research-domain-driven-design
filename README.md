# research-domain-driven-design

**Motivation to setup a multi-module project to experiment with Domain Driven Design approach**


## Design

* Separate domain models from all else, centrally defined
* Separate domain application interface
* build tests using https://testcontainers.org to demonstrate the models and ports working 


## Modules

|| Module                                                        || Description  ||
| -------------------------------------------------------------- | ------------- | 
| [**`ddd-common`**](./bud-domain-framework/README.md)    |  Domain common code, base classes, utilities |
| [**`ddd-domain`**](./bud-domain-blueprint/README.md)    |  Domain models |
| [**`ddd-framework`**](./bud-domain-library/README.md) |  Domain framework of interfaces, use-cases, ports |
| [**`ddd-support`**](./bud-domain-manager/README.md)   |  Domain support application |


## User Guide


## Developers Guide

* `mvn clean install`


### Prerequisites

* Docker and https://testcontainers.org

## Handover Suggestions

_Nuggets of Knowledge and Thinking from last people to work on the project._
_e.g. suggestions for technical debt reduction, simplification or enhancements_


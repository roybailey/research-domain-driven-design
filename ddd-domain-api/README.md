# Domain Service

**Module containing deployable domain api implementations (adapters)**

Module of [**`..`**](../README.md) *parent*
Also see [**`Domain`**](../ddd-domain-model/README.md) *DDD domain module*


## Design

Design intentions of this module...

* Implement domain related api interfaces (adapters) with testable code
* Unit test all _'driver'_ related interfaces (e.g. incoming API controllers, message listeners etc.)


## User Guide

* [**`Domain`**](src/main/java/me/roybailey/domain)
* [**`Audit`**](src/main/java/me/roybailey/domain/audit)
* [**`Entitlement`**](src/main/java/me/roybailey/domain/entitlement)


## Developer Guide

Each bounded context should be in a separate package, with `model` and `api` sub-packages.

* `<base-package>/<bounded-context>/model/*` folder, contains all data model objects
* `<base-package>/<bounded-context>/api/*` folder, contains all api interfaces, ports

Examples in this project...

| Bounded Context     | Description                              |
|---------------------|------------------------------------------|
| **`audit`**         | Audit data capture                       |
| **`entitlement`**   | Access control and authorization capture |


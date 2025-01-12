# Domain Framework

**Module containing domain spi implementations (i.e. service provider adapters)**

Module of [**`..`**](../README.md) *parent*
Also see [**`Domain`**](../ddd-domain-model/README.md) *DDD domain module*


## Design

Design intentions of this module...

* Implement domain related spi interfaces (adapters) with testable code
* Unit test all domain business logic
* Unit test all _'driven'_ related interfaces (e.g. storage, notifications etc.)


## User Guide

* [**`Domain`**](src/main/java/me/roybailey/domain)
* [**`Audit`**](src/main/java/me/roybailey/domain/audit)
* [**`Entitlement`**](src/main/java/me/roybailey/domain/entitlement)


## Developer Guide

Each bounded context should be in a separate package.

* `<base-package>/<bounded-context>/*` folder, contains spi implementations (e.g. storage, notifications)

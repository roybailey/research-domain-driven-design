# Domain Framework

**Module containing domain api implementations (adapters)**

Module of [**`..`**](../README.md) *parent*
Also see [**`Domain`**](../ddd-domain/README.md) *DDD domain module*


## Design

Design intentions of this module...

* Implement domain related api interfaces (adapters) with testable code
* Unit test all domain business logic
* Unit test all _'driven'_ related interfaces (e.g. storage, notifications etc.)


## User Guide

Add the dependency to your `pom.xml`...

```xml
    <dependency>
        <groupId>me.roybailey</groupId>
        <artifactId>ddd-framework</artifactId>
        <version>${ddd-framework.version}</version>
    </dependency>
```

## Developer Guide

Each bounded context should be in a separate package, with `model` and `api` sub-packages.

* `<base-package>/<bounded-context>/model/*` folder, contains all data model objects
* `<base-package>/<bounded-context>/api/*` folder, contains all api interfaces, ports

Examples in this project...

| Bounded Context    | Description                              |
|--------------------|------------------------------------------|
| **`audit`**        | Audit data capture                       |
| **`entitlements`** | Access control and authorization capture |


## Handover Suggestions

_Nuggets of Knowledge and Thinking from last people to work on the project._
_e.g. suggestions for technical debt reduction, simplification or enhancements_

* [ ] Define & test example implementation of audit Store
* [ ] Define & test example implementation of entitlements Store


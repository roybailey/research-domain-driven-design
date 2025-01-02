# Domain

**Module containing domain models and api interfaces (ports)**

Module of [**`..`**](../README.md) *parent*


## Design

Design intentions of this module...

* Defines data models (immutable value objects, mutable identifiable entity objects)
* Defines apis, use-case interfaces, ports
* No other dependencies, ensures the Domain Driven Design is defined in isolation of all other wiring technology


## User Guide

Add the dependency to your `pom.xml`...

```xml
    <dependency>
        <groupId>me.roybailey</groupId>
        <artifactId>ddd-domain</artifactId>
        <version>${ddd-domain.version}</version>
    </dependency>
```

### Domain Driven Design 

#### Value objects

Immutable (usually small) objects that can be compared as equals based on all member fields being equal.
i.e. structurally identical

Example

```java
public record Money(Double amount, String currencyCode) {}
```


#### Entity objects

Identifiable objects that can be compared as equals based on an `id` member only, regardsless of other members values.
i.e. Objects that have an identity member field, such as a GUID or database primary key

Example

```java
public record Money(Double amount, String currencyCode) {}
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

* Care must be taken with dependencies in this module as it is used by the generator in a build process
  and by the manager for database migration and for services to load and support an API

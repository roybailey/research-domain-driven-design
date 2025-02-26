# Domain

**Module containing domain _models_ and _api interfaces_ (ports)**

Module of [**`..`**](../README.md) *parent*


## Design

Design intentions of this module...

* Defines data models (immutable value objects, mutable identifiable entity objects)
* Defines apis, use-case interfaces, ports
* No other dependencies, ensures the Domain Driven Design is defined in isolation of all other wiring technology


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
import lombok.Builder;

@Data
@Builder
public class Record {
  private String id;
  private String name;
  private String description;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DomainEntity that = (DomainEntity) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

}
```


## Developer Guide

### Package conventions

Each bounded context should be in a separate package, with `model` and `api` sub-packages.

* `<base-package>/<bounded-context>/model/*` folder, contains all data model objects
* `<base-package>/<bounded-context>/api/*` folder, contains all api interfaces, ports

### Class/File naming conventions

* `.../api/*Domain` the `Domain` suffix defines a Domain Service or Domain Aggregate, controls and coordinates all business logic
* `.../api/*Store` the `Store` suffix defines a Storage interface/port, methods to save, load, update and delete from a database or other form of storage

### DomainResult 

All domain driven api/port methods return the `DomainResult` which carries a status enum, a message, and either a data result type or an exception.

This design is commonly used to allow methods to return meta information (success, failure, different types of failure, errors) with some data type result.
It makes coding the exception paths and error handling easier throughout the codebase.

### Examples in this project

| Bounded Contexts                                                   | Description                              |
|--------------------------------------------------------------------|------------------------------------------|
| [**`audit`**](src/main/java/me/roybailey/domain/audit)             | Audit data capture                       |
| [**`entitlement`**](src/main/java/me/roybailey/domain/entitlement) | Access control and authorization capture |

_Also see [**`domain`**](src/main/java/me/roybailey/domain) for shared/base classes._


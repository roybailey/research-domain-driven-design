# API Blueprint

**Module containing API Blueprint Templates**

Module of [**`..`**](../README.md) *parent*


## Design

Module to encapsulate all blueprint templates.
These blueprints provide the following...

* Controller API Endpoints
* DAO Services with Filtering
* Database Schema Changes
* Database Table Details
* Data Models for loading from Database and serving as JSON

These are stored in the `resources` folder so that they can be used both by the build system
for code generation and loaded at runtime for common code logic.

For example:  The build system can code generate a `RestController` while the runtime
might use the database column lists to provide generic column logic.

### Blueprint Database Schema Migration

This module contains the Flyway Database Migration schema DDLs and code to apply to a target database.

It uses a custom migration table `blueprint_schema_history` and is enabled via a property `blueprint.flyway.enabled=true`

Only called by the code generator (in DEV to ensure code is generated against the target database state)
and by the manager service to apply the schema migration through all environments (UAT and PROD)

NOTE: the schema files should be idempotent, meaning you can destroy the `blueprint_schema_history` table and
re-run all the schema changes to arrive at the correct schema state without errors.
i.e. always use DROP IF EXISTS, then CREATE

## User Guide

* `src/main/resources/api` folder, add new Blueprint template file
  * e.g. `<api-name>-blueprint.json` file, create and populate accordingly

* `src/main/resources/ddl` folder, add new Database schema view
  * e.g. `V0003__<api-name>.sql` file and populate accordingly (using next free sequence number)

* `application-blueprint.yml`, add `- api/<api-name>-blueprint.json` to `blueprint-templates` list 

```
blueprint-templates:
  - blueprint/sample-blueprint.json
  - blueprint/<name>-blueprint.json
```

## Blueprint Specification

See `Blueprint.kt` source for full data class definition

See [`sample-blueprint.json`](src/main/resources/blueprint/codegen-sample-blueprint.json) for example of blueprint definition

The codegen-sample-blueprint is used to test the code generation process and functionality.
It should be enhanced first and tests updated with expected results before use by other 
production APIs.

At the root of a blueprint template are the following key fields...

key                      | Description
------------------------ | ------------- 
**`id`**                 | globally unique id value, typically `<api-name>-blueprint`
**`namespace`**          | Java Classname style namespace (e.g. `CodegenSample`) used for package, class and variable naming


### Controller Mappings

`controllers` define a RESTful controller class for linking API calls to Service DAO methods.

key                      | Description
------------------------ | ------------- 
**`id`**                 | globally unique id value, typically `<api-name>-controller` 
**`serviceMappingId`**   | globally unique id value of service this controller will use, typically `<api-name>-service`
**`apiPath`**            | controller root api path e.g. `/api-name`
**`endpoints`**          | array of controller methods

#### Controller Endpoint Mappings

`endpoints` define a RESTful controller endpoint method for linking an API call to a specific Service DAO method and data model.

key                         | Description
--------------------------- | ------------- 
**`apiPath`**               | endpoint api path appended to the controller api path
**`apiMethodName`**         | optional name of controller method (defaults to `getAllData`)
**`apiRequestParameters`**  | optional map of static parameters
**`serviceNameMethod`**     | optional service method to call (defaults to `getAllData`)


### Service Mappings

`services` define a DAO class for loading from the database into an object model.

key                         | Description
--------------------------- | ------------- 
**`id`**                    | globally unique id value, typically `<api-name>-service`
**`tableMappingId`**        | globally unique id value of table this service will use, typically `<api-name>-table`
**`modelMappingId`**        | globally unique id value of model this service will use, typically `<api-name>-model`


### TableMappings

`tables` define a database table and is used to generate table/column details directly from the database.

key                         | Description
--------------------------- | ------------- 
**`id`**                    | globally unique id value, typically `<api-name>-table`
**`tableName`**             | name of the database table (or view)
**`filters`**               | array of custom and override filters


#### Table Filter Mappings

`filters` define a custom or override filters

key                         | Description
--------------------------- | ------------- 
**`name`**                  | filter name, typically the column name or name to be given a value used by this filter
**`column`**                | table column the filter value will be used with
**`type`**                  | type of filter (`EQUAL`, `LIKE`)
**`params`**                | map of optional parameters that may be needed to configure the filter strategy implementation


#### Model Mappings

`models` define a POJO data class for loading from the database and serving via Services/Controllers.

key                         | Description
--------------------------- | ------------- 
**`id`**                    | globally unique id value, typically `<api-name>-model`
**`fields`**                | array of fields for the Java data model object 

#### Model Field Mappings

`fields` are typically derived automatically from the database column schema by the code generator. 

key                         | Description
--------------------------- | ------------- 
**`fieldName`**             | name of the field
**`fieldType`**             | type of the field (Java type)
**`jsonName`**              | name of the field when returned as JSON


## Developers Guide

* `mvn clean install` 

### Running the Generator from IDEA

GeneratorApplicationKt

```
--project.basedir=<repo-root-folder>/bud-domain/bud-domain-service
--project.version=0.2.0
```

## Handover Suggestions

_Nuggets of Knowledge and Thinking from last people to work on the project._
_e.g. suggestions for technical debt reduction, simplification or enhancements_

* Care must be taken with dependencies in this module as it is used by the generator in a build process
  and by the manager for database migration and for services to load and support an API

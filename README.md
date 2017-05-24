# data-model-generator
Data model generator based on Scala case classes.

[![Build Status](https://api.travis-ci.org/piotr-kalanski/data-model-generator.png?branch=development)](https://api.travis-ci.org/piotr-kalanski/data-model-generator.png?branch=development)
[![codecov.io](http://codecov.io/github/piotr-kalanski/data-model-generator/coverage.svg?branch=development)](http://codecov.io/github/piotr-kalanski/data-model-generator/coverage.svg?branch=development)
[<img src="https://img.shields.io/maven-central/v/com.github.piotr-kalanski/data-model-generator_2.11.svg?label=latest%20release"/>](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22data-model-generator_2.11%22)
[![License](http://img.shields.io/:license-Apache%202-red.svg)](http://www.apache.org/licenses/LICENSE-2.0.txt)

# Table of contents

- [Goals](#goals)
- [Getting started](#getting-started)
- [Dialects](#dialects)
  * [H2 dialect](#h2-dialect)
  * [Hive dialect](#hive-dialect)
  * [Redshift dialect](#redshift-dialect)
  * [Avro schema dialect](#avro-schema-dialect)
  * [Elasticsearch dialect](#elasticsearch-dialect)
- [Customizations](#customizations)
  * [Custom column name](#custom-column-name)
  * [Custom table name](#custom-table-name)
  * [Documentation comments](#documentation-comments)
  * [Column length](#column-length)

# Goals

- Generate data model (e.g. DDL, avro schema, Elasticsearch mapping) based on Scala case classes

# Getting started

Include dependency:

```scala
"com.github.piotr-kalanski" % "data-model-generator_2.11" % "0.1.0"
```

or

```xml
<dependency>
    <groupId>com.github.piotr-kalanski</groupId>
    <artifactId>data-model-generator_2.11</artifactId>
    <version>0.1.0</version>
</dependency>
```

# Dialects

## H2 dialect

```scala
import com.datawizards.dmg.{DataModelGenerator, dialects}

case class Person(name: String, age: Int, skills: Seq[String])

object H2Example extends App {
  println(DataModelGenerator.generate[Person](dialects.H2))
}
```

```sql
CREATE TABLE Person(
   name VARCHAR,
   age INT,
   skills ARRAY
);
```

## Hive dialect

```scala
import com.datawizards.dmg.{DataModelGenerator, dialects}

case class Person(name: String, age: Int, skills: Seq[String])

object HiveExample extends App {
  println(DataModelGenerator.generate[Person](dialects.Hive))
}
```

```sql
CREATE TABLE Person(
   name STRING,
   age INT,
   skills ARRAY<STRING>
);
```

## Redshift dialect

```scala
import com.datawizards.dmg.{DataModelGenerator, dialects}

case class Person(name: String, age: Int, skills: Seq[String])

object RedshiftExample extends App {
  println(DataModelGenerator.generate[Person](dialects.Redshift))
}
```

```sql
CREATE TABLE Person(
   name VARCHAR,
   age INT,
   skills VARCHAR
);
```

## Avro schema dialect

### Avro schema

```scala

case class Person(name: String, age: Int)

DataModelGenerator.generate[Person](dialects.AvroSchema)
```

```json
{
   "namespace": "com.datawizards.dmg.examples",
   "type": "record",
   "name": "Person",
   "fields": [
      {"name": "name", "type": "string"},
      {"name": "age", "type": "int"},
      {"name": "skills", "type": "array", "items": "string"}
   ]
}
```

### Avro schema for Avro Schema Registry

```scala

case class Person(name: String, age: Int, skills: Seq[String])

DataModelGenerator.generate[Person](dialects.AvroSchemaRegistry)
```

```json
{"schema":
"{
   \"namespace\": \"com.datawizards.dmg.examples\",
   \"type\": \"record\",
   \"name\": \"Person\",
   \"fields\": [
      {\"name\": \"name\", \"type\": \"string\"},
      {\"name\": \"age\", \"type\": \"int\"},
      {\"name\": \"skills\", \"type\": \"array\", \"items\": \"string\"}
   ]
}"
}
```

### Registering Avro schema to Avro schema registry

```scala
import com.datawizards.dmg.service.AvroSchemaRegistryServiceImpl

case class Person(name: String, age: Int)

object RegisterAvroSchema extends App {
  val service = new AvroSchemaRegistryServiceImpl("http://localhost:8081")
  service.registerSchema[Person]("person")

  println("Subjects:")
  println(service.subjects())

  println("Registered schema:")
  println(service.fetchSchema("person"))
}
```

```scala
"Subjects:"
["person"]
"Registered schema:"
{"type":"record","name":"Person","namespace":"com.datawizards.dmg.examples","fields":[{"name":"name","type":"string"},{"name":"age","type":"int"}]}
```

## Elasticsearch dialect

### Elasticsearch mapping

```scala

case class Person(name: String, age: Int, skills: Seq[String])

DataModelGenerator.generate[Person](dialects.Elasticsearch)
```

```json
{
   "mappings": {
      "Person": {
         "name": {"type": "string"},
         "age": {"type": "integer"},
         "skills": {"type": "string"}
      }
   }
}
```

# Customizations

## Custom column name

```scala
import com.datawizards.dmg.annotations._

case class Person(
  @column(name="personName")
  name: String,
  age: Int
)

DataModelGenerator.generate[Person](dialects.H2)
```

```sql
CREATE TABLE Person(
   personName VARCHAR,
   age INT
);
```

### Custom column name specific for dialect

```scala
import com.datawizards.dmg.annotations._

case class Person(
  @column(name="NAME")
  @column(name="personName", dialects.Elasticsearch)
  name: String,
  @column(name="AGE")
  @column(name="personAge", dialects.Elasticsearch)
  age: Int
)

DataModelGenerator.generate[Person](dialects.H2)
DataModelGenerator.generate[Person](dialects.Elasticsearch)
```

```sql
CREATE TABLE PEOPLE(
   NAME VARCHAR,
   AGE INT
);
```

```json
{
   "mappings": {
      "person": {
         "personName": {"type": "string"},
         "personAge": {"type": "integer"}
      }
   }
}
```

## Custom table name

```scala
import com.datawizards.dmg.annotations._

@table("PEOPLE")
case class Person(
  name: String,
  age: Int
)

DataModelGenerator.generate[Person](dialects.H2)
```

```sql
CREATE TABLE PEOPLE(
   name VARCHAR,
   age INT
);
```

### Custom table name specific for dialect

```scala
import com.datawizards.dmg.annotations._

@table("PEOPLE")
@table("person", dialects.Elasticsearch)
case class Person(
  name: String,
  age: Int
)

DataModelGenerator.generate[Person](dialects.H2)
DataModelGenerator.generate[Person](dialects.Elasticsearch)
```

```sql
CREATE TABLE PEOPLE(
   name VARCHAR,
   age INT
);
```

```json
{
   "mappings": {
      "person": {
         "name": {"type": "string"},
         "age": {"type": "integer"}
      }
   }
}
```

## Documentation comments

```scala
@comment("People data")
case class PersonWithComments(
    @comment("Person name") name: String,
    age: Int
)
```

### H2

```scala
DataModelGenerator.generate[PersonWithComments](dialects.H2)
```

```sql
CREATE TABLE PersonWithComments(
   name VARCHAR COMMENT 'Person name',
   age INT
);
COMMENT ON TABLE PersonWithComments IS 'People data';
```

### Hive

```scala
DataModelGenerator.generate[PersonWithComments](dialects.Hive)
```

```sql
CREATE TABLE PersonWithComments(
   name STRING COMMENT 'Person name',
   age INT
)
COMMENT 'People data';
```

### Redshift

```scala
DataModelGenerator.generate[PersonWithComments](dialects.Redshift)
```

```sql
CREATE TABLE PersonWithComments(
   name VARCHAR,
   age INTEGER
);
COMMENT ON TABLE PersonWithComments IS 'People data';
COMMENT ON COLUMN PersonWithComments.name IS 'Person name';
```

### Avro schema

```scala
DataModelGenerator.generate[PersonWithComments](dialects.AvroSchema)
```

```json
{
   "namespace": "com.datawizards.dmg.examples",
   "type": "record",
   "name": "PersonWithComments",
   "doc": "People data",
   "fields": [
      {"name": "name", "type": "string", "doc": "Person name"},
      {"name": "age", "type": "int"}
   ]
}
```

## Column length

```scala
import com.datawizards.dmg.annotations._

case class Person(
  @length(1000) name: String,
  age: Int
)

DataModelGenerator.generate[Person](dialects.H2)
```

```sql
CREATE TABLE PEOPLE(
   name VARCHAR(1000),
   age INT
);
```
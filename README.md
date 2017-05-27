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
  * [Java dialect](#java-dialect)
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
"com.github.piotr-kalanski" % "data-model-generator_2.11" % "0.2.0"
```

or

```xml
<dependency>
    <groupId>com.github.piotr-kalanski</groupId>
    <artifactId>data-model-generator_2.11</artifactId>
    <version>0.2.0</version>
</dependency>
```

# Dialects

## H2 dialect

```scala
import com.datawizards.dmg.{DataModelGenerator, dialects}

case class Person(name: String, age: Int)
case class Book(title: String, year: Int, owner: Person, authors: Seq[Person])

object H2Example extends App {
  println(DataModelGenerator.generate[Book](dialects.H2))
}
```

```sql
CREATE TABLE Book(
   title VARCHAR,
   year INT,
   owner OTHER,
   authors ARRAY
);
```

## Hive dialect

```scala
import com.datawizards.dmg.{DataModelGenerator, dialects}

case class Person(name: String, age: Int)
case class Book(title: String, year: Int, owner: Person, authors: Seq[Person])

object HiveExample extends App {
  println(DataModelGenerator.generate[Book](dialects.Hive))
}
```

```sql
CREATE TABLE Book(
   title STRING,
   year INT,
   owner STRUCT<name : STRING, age : INT>,
   authors ARRAY<STRUCT<name : STRING, age : INT>>
);
```

### Hive external table

```scala

@hiveExternalTable(location="hdfs:///data/people")
case class Person(name: String, age: Int)

DataModelGenerator.generate[Person](dialects.Hive)
```

```sql
CREATE EXTERNAL TABLE Person(
   name STRING,
   age INT
)
LOCATION 'hdfs:///data/people';
```

### Hive ROW FORMAT SERDE

```scala
@hiveRowFormatSerde(format="org.apache.hadoop.hive.serde2.avro.AvroSerDe")
case class Person(name: String, age: Int)

DataModelGenerator.generate[Person](dialects.Hive)
```

```sql
CREATE TABLE Person(
   name STRING,
   age INT
)
ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.avro.AvroSerDe';
```

### Hive STORED AS

```scala
@hiveStoredAs(format="PARQUET")
case class Person(name: String, age: Int)

DataModelGenerator.generate[Person](dialects.Hive)
```

```sql
CREATE TABLE Person(
   name STRING,
   age INT
)
STORED AS PARQUET;
```

### Hive TABLE PROPERTIES

```scala
@hiveTableProperty("key1", "value1")
@hiveTableProperty("key2", "value2")
@hiveTableProperty("key3", "value3")
case class Person(name: String, age: Int)

DataModelGenerator.generate[Person](dialects.Hive)
```

```sql
CREATE TABLE Person(
   name STRING,
   age INT
)
TBLPROPERTIES(
   'key1' = 'value1',
   'key2' = 'value2',
   'key3' = 'value3'
);
```

### Hive avro schema url property

```scala
@hiveTableProperty("avro.schema.url", "hdfs:///metadata/person.avro")
case class Person(name: String, age: Int)

DataModelGenerator.generate[Person](dialects.Hive)
```

If "avro.schema.url" table property is provided then generated data model doesn't have any columns definitions, because they are taken by Hive from avro schema.

```sql
CREATE TABLE Person
TBLPROPERTIES(
   'avro.schema.url' = 'hdfs:///metadata/person.avro'
);
```

### Hive partition columns

```scala
case class ClicksPartitioned(
    time: Timestamp,
    event: String,
    user: String,
    @hivePartitionColumn
    year: Int,
    @hivePartitionColumn
    month: Int,
    @hivePartitionColumn
    day: Int
)

DataModelGenerator.generate[ClicksPartitioned](dialects.Hive)
```

```sql
CREATE TABLE ClicksPartitioned(
   time TIMESTAMP,
   event STRING,
   user STRING
)
PARTITIONED BY(year INT, month INT, day INT);
```

### Hive partition columns - order

```scala
case class ClicksPartitioned(
    time: Timestamp,
    event: String,
    user: String,
    @hivePartitionColumn(order=3)
    day: Int,
    @hivePartitionColumn(order=1)
    year: Int,
    @hivePartitionColumn(order=2)
    month: Int
)

DataModelGenerator.generate[ClicksPartitionedWithOrder](dialects.Hive)
```

```sql
CREATE TABLE ClicksPartitionedWithOrder(
   time TIMESTAMP,
   event STRING,
   user STRING
)
PARTITIONED BY(year INT, month INT, day INT);
```

### Hive Parquet table with many annotations

```scala
@table("CUSTOM_TABLE_NAME")
@comment("Table comment")
@hiveStoredAs(format="PARQUET")
@hiveExternalTable(location="hdfs:///data/table")
@hiveTableProperty("key1", "value1")
@hiveTableProperty("key2", "value2")
@hiveTableProperty("key3", "value3")
case class ParquetTableWithManyAnnotations(
    @column("eventTime")
    @comment("Event time")
    time: Timestamp,
    @comment("Event name")
    event: String,
    @comment("User id")
    user: String,
    @hivePartitionColumn(order=3)
    day: Int,
    @hivePartitionColumn(order=1)
    year: Int,
    @hivePartitionColumn(order=2)
    month: Int
)

DataModelGenerator.generate[ParquetTableWithManyAnnotations](dialects.Hive)
```

```sql
CREATE EXTERNAL TABLE CUSTOM_TABLE_NAME(
   eventTime TIMESTAMP COMMENT 'Event time',
   event STRING COMMENT 'Event name',
   user STRING COMMENT 'User id'
)
COMMENT 'Table comment'
PARTITIONED BY(year INT, month INT, day INT)
STORED AS PARQUET
LOCATION 'hdfs:///data/table'
TBLPROPERTIES(
   'key1' = 'value1',
   'key2' = 'value2',
   'key3' = 'value3'
);
```

### Hive Avro table with many annotations

```scala
@table("CUSTOM_TABLE_NAME")
@comment("Table comment")
@hiveRowFormatSerde(format="org.apache.hadoop.hive.serde2.avro.AvroSerDe")
@hiveStoredAs("INPUTFORMAT 'org.apache.hadoop.hive.ql.io.avro.AvroContainerInputFormat' OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.avro.AvroContainerOutputFormat'")
@hiveExternalTable(location="hdfs:///data/table")
@hiveTableProperty("avro.schema.url", "hdfs:///metadata/table.avro")
@hiveTableProperty("key1", "value1")
@hiveTableProperty("key2", "value2")
@hiveTableProperty("key3", "value3")
case class AvroTableWithManyAnnotations(
    @column("eventTime")
    @comment("Event time")
    time: Timestamp,
    @comment("Event name")
    event: String,
    @comment("User id")
    user: String,
    @hivePartitionColumn(order=3)
    day: Int,
    @hivePartitionColumn(order=1)
    year: Int,
    @hivePartitionColumn(order=2)
    month: Int
)

DataModelGenerator.generate[AvroTableWithManyAnnotations](dialects.Hive)
```

```sql
CREATE EXTERNAL TABLE CUSTOM_TABLE_NAME
COMMENT 'Table comment'
PARTITIONED BY(year INT, month INT, day INT)
ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.avro.AvroSerDe'
STORED AS INPUTFORMAT 'org.apache.hadoop.hive.ql.io.avro.AvroContainerInputFormat' OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.avro.AvroContainerOutputFormat'
LOCATION 'hdfs:///data/table'
TBLPROPERTIES(
   'avro.schema.url' = 'hdfs:///metadata/table.avro',
   'key1' = 'value1',
   'key2' = 'value2',
   'key3' = 'value3'
);
```

## Redshift dialect

```scala
import com.datawizards.dmg.{DataModelGenerator, dialects}

case class Person(name: String, age: Int)
case class Book(title: String, year: Int, owner: Person, authors: Seq[Person])

object RedshiftExample extends App {
  println(DataModelGenerator.generate[Book](dialects.Redshift))
}
```

```sql
CREATE TABLE Book(
   title VARCHAR,
   year INTEGER,
   owner VARCHAR,
   authors VARCHAR
);
```

## Avro schema dialect

### Avro schema

```scala

case class Person(name: String, age: Int)
case class Book(title: String, year: Int, owner: Person, authors: Seq[Person])

DataModelGenerator.generate[Book](dialects.AvroSchema)
```

```json
{
   "namespace": "com.datawizards.dmg.examples",
   "type": "record",
   "name": "Book",
   "fields": [
      {"name": "title", "type": "string"},
      {"name": "year", "type": "int"},
      {"name": "owner", "type": "record", "fields": [{"name": "name", "type": "string"}, {"name": "age", "type": "int"}]},
      {"name": "authors", "type": "array", "items": {"type": "record", "fields": [{"name": "name", "type": "string"}, {"name": "age", "type": "int"}]}}
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

case class Person(name: String, age: Int)
case class Book(title: String, year: Int, owner: Person, authors: Seq[Person])

DataModelGenerator.generate[Book](dialects.Elasticsearch)
```

```json
{
   "mappings": {
      "Book": {
         "properties": {
            "title": {"type": "string"},
            "year": {"type": "integer"},
            "owner": {
               "properties": {
                  "name": {"type": "string"},
                  "age": {"type": "integer"}
               }
            },
            "authors": {
               "properties": {
                  "name": {"type": "string"},
                  "age": {"type": "integer"}
               }
            }
         }
      }
   }
}
```

## Java dialect

```scala
case class Person(name: String, age: Int)

DataModelGenerator.generate[Person](dialects.Java)
```

```java
public class Person {
   private String name;
   private Integer age;

   public Person() {}

   public Person(String name, Integer age) {
      this.name = name;
      this.age = age;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public Integer getAge() {
      return age;
   }

   public void setAge(Integer age) {
      this.age = age;
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
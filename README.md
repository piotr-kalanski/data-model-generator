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
  * [MySQL dialect](#mysql-dialect)
  * [Avro schema dialect](#avro-schema-dialect)
  * [Elasticsearch dialect](#elasticsearch-dialect)
  * [Java dialect](#java-dialect)
- [Installers](#installers)
  * [Register Avro schema to Avro schema registry](#register-avro-schema-to-avro-schema-registry)
  * [Copy Avro schema to HDFS](#copy-avro-schema-to-hdfs)
  * [Create Elasticsearch index](#create-elasticsearch-index)
  * [Create Elasticsearch template](#create-elasticsearch-template)
  * [Create Hive table](#create-hive-table)
- [Extracting class metadata](#extracting-class-metadata)
- [Customizations](#customizations)
  * [Custom column name](#custom-column-name)
  * [Custom table name](#custom-table-name)
  * [Placeholders](#placeholders)
  * [Documentation comments](#documentation-comments)
  * [Column length](#column-length)
  * [Not null](#not-null)
  * [Underscore](#underscore)
  * [Hive customizations](#hive-customizations)
  * [Elasticsearch customizations](#elasticsearch-customizations)

# Goals

- Generate data model (e.g. DDL, avro schema, Elasticsearch mapping) based on Scala case classes

# Getting started

Include dependency:

```scala
"com.github.piotr-kalanski" % "data-model-generator_2.11" % "0.8.1"
```

or

```xml
<dependency>
    <groupId>com.github.piotr-kalanski</groupId>
    <artifactId>data-model-generator_2.11</artifactId>
    <version>0.8.1</version>
</dependency>
```

# Dialects

## H2 dialect

```scala
import com.datawizards.dmg.{DataModelGenerator, dialects}

case class Person(name: String, age: Int)
case class Book(title: String, year: Int, owner: Person, authors: Seq[Person])

object H2Example extends App {
  println(DataModelGenerator.generate[Book](dialects.H2Dialect))
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
import com.datawizards.dmg.generator.HiveGenerator
import com.datawizards.dmg.{DataModelGenerator, dialects}

case class Person(name: String, age: Int)
case class Book(title: String, year: Int, owner: Person, authors: Seq[Person])

object HiveExample extends App {
  println(DataModelGenerator.generate[Book](new HiveGenerator()))
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

## Redshift dialect

```scala
import com.datawizards.dmg.{DataModelGenerator, dialects}

case class Person(name: String, age: Int)
case class Book(title: String, year: Int, owner: Person, authors: Seq[Person])

object RedshiftExample extends App {
  println(DataModelGenerator.generate[Book](dialects.RedshiftDialect))
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

## MySQL dialect

```scala
import com.datawizards.dmg.{DataModelGenerator, dialects}

case class Person(name: String, age: Int)
case class Book(title: String, year: Int, owner: Person, authors: Seq[Person])

object MySQLExample extends App {
  println(DataModelGenerator.generate[Book](dialects.MySQLDialect))
}
```

```sql
CREATE TABLE Book(
   title VARCHAR,
   year INTEGER,
   owner JSON,
   authors JSON
);
```

## Avro schema dialect

### Avro schema

```scala
import com.datawizards.dmg.{DataModelGenerator, dialects}

case class Person(name: String, age: Int)
case class Book(title: String, year: Int, owner: Person, authors: Seq[Person])

DataModelGenerator.generate[Book](dialects.AvroSchemaDialect)
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
import com.datawizards.dmg.{DataModelGenerator, dialects}

case class Person(name: String, age: Int, skills: Seq[String])

DataModelGenerator.generate[Person](dialects.AvroSchemaRegistryDialect)
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

## Elasticsearch dialect

```scala
import com.datawizards.dmg.{DataModelGenerator, dialects}

case class Person(name: String, age: Int)
case class Book(title: String, year: Int, owner: Person, authors: Seq[Person])

DataModelGenerator.generate[Book](dialects.ElasticsearchDialect)
```

```json
{
   "mappings" : {
      "Book" : {
         "properties" : {
            "title" : {"type" : "string"},
            "year" : {"type" : "integer"},
            "owner" : {
               "properties" : {
                  "name" : {"type" : "string"},
                  "age" : {"type" : "integer"}
               }
            },
            "authors" : {
               "properties" : {
                  "name" : {"type" : "string"},
                  "age" : {"type" : "integer"}
               }
            }
         }
      }
   }
}
```

## Java dialect

```scala
import com.datawizards.dmg.{DataModelGenerator, dialects}

case class Person(name: String, age: Int)

DataModelGenerator.generate[Person](dialects.JavaDialect)
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

# Installers

Library enables installing generated data model at target data store e.g. registering generated avro schema at Avro Schema Registry, creating Elasticsearch index or creating Hive table.

## Register Avro schema to Avro schema registry

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

## Copy Avro schema to HDFS

```scala
import com.datawizards.dmg.service.AvroSchemaRegistryServiceImpl

case class Person(name: String, age: Int)

object CopyAvroSchemaToHDFS extends App {
  val service = new AvroSchemaRegistryServiceImpl("http://localhost:8081")
  service.copyAvroSchemaToHdfs[Person]("/metadata/schemas/person")
}
```

## Create Elasticsearch index

```scala
import com.datawizards.dmg.service.ElasticsearchServiceImpl

case class Person(name: String, age: Int)

object CreateElasticsearchIndex extends App {
  val service = new ElasticsearchServiceImpl("http://localhost:9200")
  service.createIndex[Person]("person")

  println("Index:")
  println(service.getIndexSettings("person"))
}
```

## Create Elasticsearch template

```scala
import com.datawizards.dmg.examples.TestModel.PersonWithMultipleEsAnnotations
import com.datawizards.dmg.service.ElasticsearchServiceImpl

object CreateElasticsearchTemplate extends App {
  val service = new ElasticsearchServiceImpl("http://localhost:9200")
  service.updateTemplate[PersonWithMultipleEsAnnotations]("people")

  println("Template:")
  println(service.getTemplate("people"))
}
```

## Create Hive table

```scala
import com.datawizards.dmg.service.HiveServiceImpl

case class Person(name: String, age: Int)

HiveServiceImpl.createHiveTable[Person]()
```

# Extracting class metadata

To extract class metadata you can use method `MetaDataWithDialectExtractor.extractClassMetaDataForDialect`. Example:
```scala
import com.datawizards.dmg.dialects
import com.datawizards.dmg.dialects.MetaDataWithDialectExtractor

case class Person(name: String, age: Int)

MetaDataWithDialectExtractor.extractClassMetaDataForDialect[Person](Some(dialects.HiveDialect))
```

# Customizations

## Custom column name

```scala
import com.datawizards.dmg.annotations._
import com.datawizards.dmg.{DataModelGenerator, dialects}

case class Person(
  @column(name="personName")
  name: String,
  age: Int
)

DataModelGenerator.generate[Person](dialects.H2Dialect)
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
import com.datawizards.dmg.{DataModelGenerator, dialects}

case class Person(
  @column(name="NAME")
  @column(name="personName", dialects.ElasticsearchDialect)
  name: String,
  @column(name="AGE")
  @column(name="personAge", dialects.ElasticsearchDialect)
  age: Int
)

DataModelGenerator.generate[Person](dialects.H2Dialect)
DataModelGenerator.generate[Person](dialects.ElasticsearchDialect)
```

```sql
CREATE TABLE PEOPLE(
   NAME VARCHAR,
   AGE INT
);
```

```json
{
   "mappings" : {
      "person" : {
         "personName" : {"type" : "string"},
         "personAge" : {"type" : "integer"}
      }
   }
}
```

## Custom table name

```scala
import com.datawizards.dmg.annotations._
import com.datawizards.dmg.{DataModelGenerator, dialects}

@table("PEOPLE")
case class Person(
  name: String,
  age: Int
)

DataModelGenerator.generate[Person](dialects.H2Dialect)
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
import com.datawizards.dmg.{DataModelGenerator, dialects}

@table("PEOPLE")
@table("person", dialects.ElasticsearchDialect)
case class Person(
  name: String,
  age: Int
)

DataModelGenerator.generate[Person](dialects.H2Dialect)
DataModelGenerator.generate[Person](dialects.ElasticsearchDialect)
```

```sql
CREATE TABLE PEOPLE(
   name VARCHAR,
   age INT
);
```

```json
{
   "mappings" : {
      "person" : {
         "name" : {"type" : "string"},
         "age" : {"type" : "integer"}
      }
   }
}
```

## Placeholders

data-model-generator supports placeholder variables when generating data model.
Placeholder variables can be used in any annotation.

Example use case for placeholder variables is to use them for generating table name dependent on environment.
For example, each environment has dedicated DB schema e.g. development, uat, production.

```scala
import com.datawizards.dmg.annotations._
import com.datawizards.dmg.service.TemplateHandler
import com.datawizards.dmg.{DataModelGenerator, dialects}

@table("${environment}.people")
case class Person(
    name: String,
    age: Int
)

TemplateHandler.inflate(DataModelGenerator.generate[Person](dialects.H2Dialect), Map("environment" -> "development"))

TemplateHandler.inflate(DataModelGenerator.generate[Person](dialects.H2Dialect), Map("environment" -> "production"))
```

Generates:

```sql
CREATE TABLE development.people(
   name VARCHAR,
   age INT
);
```

```sql
CREATE TABLE production.people(
   name VARCHAR,
   age INT
);
```

## Documentation comments

```scala
import com.datawizards.dmg.annotations._

@comment("People data")
case class PersonWithComments(
    @comment("Person name") name: String,
    age: Int
)
```

### H2

```scala
import com.datawizards.dmg.annotations._
import com.datawizards.dmg.{DataModelGenerator, dialects}

DataModelGenerator.generate[PersonWithComments](dialects.H2Dialect)
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
import com.datawizards.dmg.annotations._
import com.datawizards.dmg.generator.HiveGenerator
import com.datawizards.dmg.DataModelGenerator

DataModelGenerator.generate[PersonWithComments](new HiveGenerator)
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
import com.datawizards.dmg.annotations._
import com.datawizards.dmg.{DataModelGenerator, dialects}

DataModelGenerator.generate[PersonWithComments](dialects.RedshiftDialect)
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
import com.datawizards.dmg.annotations._
import com.datawizards.dmg.{DataModelGenerator, dialects}

DataModelGenerator.generate[PersonWithComments](dialects.AvroSchemaDialect)
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
import com.datawizards.dmg.{DataModelGenerator, dialects}


case class Person(
  @length(1000) name: String,
  age: Int
)

DataModelGenerator.generate[Person](dialects.H2Dialect)
```

```sql
CREATE TABLE PEOPLE(
   name VARCHAR(1000),
   age INT
);
```

## Not null

```scala
import com.datawizards.dmg.annotations._
import com.datawizards.dmg.{DataModelGenerator, dialects}

case class Person(
  @notNull name: String,
  age: Int
)

DataModelGenerator.generate[Person](dialects.H2Dialect)
DataModelGenerator.generate[Person](dialects.RedshiftDialect)
DataModelGenerator.generate[Person](dialects.AvroSchemaDialect)
```

### H2 - not null

```sql
CREATE TABLE PersonWithNull(
   name VARCHAR NOT NULL,
   age INT
);
```

### Redshift - not null

```sql
CREATE TABLE PersonWithNull(
   name VARCHAR NOT NULL,
   age INTEGER
);
```

### Avro schema - not null

```json
{
   "namespace": "com.datawizards.dmg",
   "type": "record",
   "name": "PersonWithNull",
   "fields": [
      {"name": "name", "type": "string"},
      {"name": "age", "type": ["null", "int"]}
   ]
}
```

## Underscore

Convert table and column names for selected dialect to underscore convention.

```scala
import com.datawizards.dmg.annotations._
import com.datawizards.dmg.{DataModelGenerator, dialects}

@underscore(dialect=dialects.H2Dialect)
case class PersonWithUnderscore(
    personName: String,
    personAge: Int
)
```

```sql
CREATE TABLE person_with_underscore(
   person_name VARCHAR,
   person_age INT
);
```

## Hive customizations

### Hive external table

```scala
import com.datawizards.dmg.annotations._
import com.datawizards.dmg.annotations.hive._
import com.datawizards.dmg.generator.HiveGenerator
import com.datawizards.dmg.{DataModelGenerator, dialects}

@hiveExternalTable(location="hdfs:///data/people")
case class Person(name: String, age: Int)

DataModelGenerator.generate[Person](new HiveGenerator)
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
import com.datawizards.dmg.annotations._
import com.datawizards.dmg.annotations.hive._
import com.datawizards.dmg.generator.HiveGenerator
import com.datawizards.dmg.{DataModelGenerator, dialects}

@hiveRowFormatSerde(format="org.apache.hadoop.hive.serde2.avro.AvroSerDe")
case class Person(name: String, age: Int)

DataModelGenerator.generate[Person](new HiveGenerator)
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
import com.datawizards.dmg.annotations._
import com.datawizards.dmg.annotations.hive._
import com.datawizards.dmg.generator.HiveGenerator
import com.datawizards.dmg.{DataModelGenerator, dialects}

@hiveStoredAs(format="PARQUET")
case class Person(name: String, age: Int)

DataModelGenerator.generate[Person](new HiveGenerator)
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
import com.datawizards.dmg.annotations._
import com.datawizards.dmg.annotations.hive._
import com.datawizards.dmg.generator.HiveGenerator
import com.datawizards.dmg.{DataModelGenerator, dialects}

@hiveTableProperty("key1", "value1")
@hiveTableProperty("key2", "value2")
@hiveTableProperty("key3", "value3")
case class Person(name: String, age: Int)

DataModelGenerator.generate[Person](new HiveGenerator)
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
import com.datawizards.dmg.annotations._
import com.datawizards.dmg.annotations.hive._
import com.datawizards.dmg.generator.HiveGenerator
import com.datawizards.dmg.{DataModelGenerator, dialects}

@hiveTableProperty("avro.schema.url", "hdfs:///metadata/person.avro")
case class Person(name: String, age: Int)

DataModelGenerator.generate[Person](new HiveGenerator)
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
import com.datawizards.dmg.annotations._
import com.datawizards.dmg.annotations.hive._
import com.datawizards.dmg.generator.HiveGenerator
import com.datawizards.dmg.{DataModelGenerator, dialects}

case class ClicksPartitioned(
    time: java.sql.Timestamp,
    event: String,
    user: String,
    @hivePartitionColumn
    year: Int,
    @hivePartitionColumn
    month: Int,
    @hivePartitionColumn
    day: Int
)

DataModelGenerator.generate[ClicksPartitioned](new HiveGenerator)
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
import com.datawizards.dmg.annotations._
import com.datawizards.dmg.annotations.hive._
import com.datawizards.dmg.generator.HiveGenerator
import com.datawizards.dmg.{DataModelGenerator, dialects}

case class ClicksPartitionedWithOrder(
    time: java.sql.Timestamp,
    event: String,
    user: String,
    @hivePartitionColumn(order=3)
    day: Int,
    @hivePartitionColumn(order=1)
    year: Int,
    @hivePartitionColumn(order=2)
    month: Int
)

DataModelGenerator.generate[ClicksPartitionedWithOrder](new HiveGenerator)
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
import com.datawizards.dmg.annotations._
import com.datawizards.dmg.annotations.hive._
import com.datawizards.dmg.generator.HiveGenerator
import com.datawizards.dmg.{DataModelGenerator, dialects}

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
    time: java.sql.Timestamp,
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

DataModelGenerator.generate[ParquetTableWithManyAnnotations](new HiveGenerator)
```

```sql
DROP TABLE IF EXISTS CUSTOM_TABLE_NAME;
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
import com.datawizards.dmg.annotations._
import com.datawizards.dmg.annotations.hive._
import com.datawizards.dmg.generator.HiveGenerator
import com.datawizards.dmg.{DataModelGenerator, dialects}

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
    time: java.sql.Timestamp,
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

DataModelGenerator.generate[AvroTableWithManyAnnotations](new HiveGenerator)
```

```sql
DROP TABLE IF EXISTS CUSTOM_TABLE_NAME;
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

### Skip table generation if it is unchanged

Sometimes a case class (one of many case classes) is not modified. 
Also it takes long to drop that table and re-create it (because it contains many partitions and many files).
In such case we can override `HiveGenerator` and add custom logic that fetches table property from Hive metastore.
Then, that property is compared against a hash calculated from case class metadata.
This guaranetees that once any property is changed, table is re-created.
If case class remains unchanged from previous table creation, then the code generated is being commented out.

```scala
import com.datawizards.dmg.annotations._
import com.datawizards.dmg.annotations.hive._
import com.datawizards.dmg.generator.HiveGenerator
import com.datawizards.dmg.metadata.ClassTypeMetaData
import com.datawizards.dmg.{DataModelGenerator, dialects}

case class Person(name: String, age: Int)

DataModelGenerator.generate[Person](new HiveGenerator(){
        override def getHashFromTableDefinition(metadata: ClassTypeMetaData): Option[Long] = {
          // TODO: connect to Hive and fetch table property named MODEL_GENERATOR_METADATA_HASH
          Some(877255039)
        }
      })
```

In case `getHashFromTableDefinition` returns `877255039`, code generated by this is:

```sql
--Not re-creating table for class Person because it was not modified.
--CREATE TABLE Person(
--  name STRING,
--  age INT
--)
--TBLPROPERTIES(   'MODEL_GENERATOR_METADATA_HASH' = '877255039')
--;

```

In case `getHashFromTableDefinition` returns something different than `877255039`, code generated by this is (so not re-creating the table):

```sql
 CREATE TABLE Person(
   name STRING,
   age INT
)
TBLPROPERTIES(   'MODEL_GENERATOR_METADATA_HASH' = '877255039')
;
```

## Elasticsearch customizations

### index settings

```scala
import com.datawizards.dmg.annotations._
import com.datawizards.dmg.annotations.es._
import com.datawizards.dmg.{DataModelGenerator, dialects}


@esSetting("number_of_shards", 1)
@esSetting("number_of_replicas", 3)
@esSetting("blocks.read_only", true)
@esSetting("codec", "best_compression")
case class Person(name: String, age: Int)

DataModelGenerator.generate[Person](dialects.ElasticsearchDialect)
```

```json
{
   "settings" : {
      "number_of_shards" : 1,
      "number_of_replicas" : 3,
      "blocks.read_only" : "true",
      "codec" : "best_compression"
   },
   "mappings" : {
      "Person" : {
         "properties" : {
            "name" : {"type" : "string"},
            "age" : {"type" : "integer"}
         }
      }
   }
}
```

### index parameter

Index parameter: https://www.elastic.co/guide/en/elasticsearch/reference/current/mapping-index.html

```scala
import com.datawizards.dmg.annotations._
import com.datawizards.dmg.annotations.es._
import com.datawizards.dmg.{DataModelGenerator, dialects}

case class Person(
    @esIndex("not_analyzed") name: String,
    age: Int
)

DataModelGenerator.generate[Person](dialects.ElasticsearchDialect)
```

```json
{
   "mappings" : {
      "PersonEsIndexSettings" : {
         "properties" : {
            "name" : {"type" : "string", "index" : "not_analyzed"},
            "age" : {"type" : "integer"}
         }
      }
   }
}
```

### format parameter

Date format parameter: https://www.elastic.co/guide/en/elasticsearch/reference/current/mapping-date-format.html

```scala
import com.datawizards.dmg.annotations._
import com.datawizards.dmg.annotations.es._
import com.datawizards.dmg.{DataModelGenerator, dialects}

case class Person(
    name: String,
    @esFormat("yyyy-MM-dd") birthday: Date
)

DataModelGenerator.generate[Person](dialects.ElasticsearchDialect)
```

```json
{
   "mappings" : {
      "Person" : {
         "properties" : {
            "name" : {"type" : "string"},
            "birthday" : {"type" : "date", "format" : "yyyy-MM-dd"}
         }
      }
   }
}
```

## Elasticsearch template

https://www.elastic.co/guide/en/elasticsearch/reference/current/indices-templates.html

```scala
import com.datawizards.dmg.annotations._
import com.datawizards.dmg.annotations.es._
import com.datawizards.dmg.{DataModelGenerator, dialects}

@esTemplate("people*")
case class PersonWithEsTemplate(name: String, age: Int)

DataModelGenerator.generate[PersonWithEsTemplate](dialects.ElasticsearchDialect)
```

```json
{
   "template" : "people*",
   "mappings" : {
      "PersonWithEsTemplate" : {
         "properties" : {
            "name" : {"type" : "string"},
            "age" : {"type" : "integer"}
         }
      }
   }
}
```

## Elasticsearch multiple annotations

```scala
import com.datawizards.dmg.annotations._
import com.datawizards.dmg.annotations.es._
import com.datawizards.dmg.{DataModelGenerator, dialects}

@table("people")
@esTemplate("people*")
@esSetting("number_of_shards", 1)
@esSetting("number_of_replicas", 3)
case class PersonWithMultipleEsAnnotations(
    @esIndex("not_analyzed")
    @column("personName")
    name: String,
    @column("personBirthday")
    @esFormat("yyyy-MM-dd")
    birthday: java.sql.Date
)

DataModelGenerator.generate[PersonWithMultipleEsAnnotations](dialects.ElasticsearchDialect)
```

```json
{
   "template" : "people*",
   "settings" : {
      "number_of_shards" : 1,
      "number_of_replicas" : 3
   },
   "mappings" : {
      "people" : {
         "properties" : {
            "personName" : {"type" : "string", "index" : "not_analyzed"},
            "personBirthday" : {"type" : "date", "format" : "yyyy-MM-dd"}
         }
      }
   }
}
```
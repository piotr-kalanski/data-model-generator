package com.datawizards.dmg.dialects

import com.datawizards.dmg.TestModel._
import com.datawizards.dmg.{DataModelGenerator, DataModelGeneratorBaseTest}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GenerateJavaClassTest extends DataModelGeneratorBaseTest {

  test("Simple model") {
    val expected =
      """public class Person {
        |   private String name;
        |   private Integer age;
        |
        |   public Person() {}
        |
        |   public Person(String name, Integer age) {
        |      this.name = name;
        |      this.age = age;
        |   }
        |
        |   public String getName() {
        |      return name;
        |   }
        |
        |   public void setName(String name) {
        |      this.name = name;
        |   }
        |
        |   public Integer getAge() {
        |      return age;
        |   }
        |
        |   public void setAge(Integer age) {
        |      this.age = age;
        |   }
        |}""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[Person](JavaDialect)
    }
  }

  test("ClassWithAllSimpleTypes") {
    val expected =
      """public class ClassWithAllSimpleTypes {
        |   private String strVal;
        |   private Integer intVal;
        |   private Long longVal;
        |   private Double doubleVal;
        |   private Float floatVal;
        |   private Short shortVal;
        |   private Boolean booleanVal;
        |   private Byte byteVal;
        |   private java.util.Date dateVal;
        |   private java.sql.Timestamp timestampVal;
        |
        |   public ClassWithAllSimpleTypes() {}
        |
        |   public ClassWithAllSimpleTypes(String strVal, Integer intVal, Long longVal, Double doubleVal, Float floatVal, Short shortVal, Boolean booleanVal, Byte byteVal, java.util.Date dateVal, java.sql.Timestamp timestampVal) {
        |      this.strVal = strVal;
        |      this.intVal = intVal;
        |      this.longVal = longVal;
        |      this.doubleVal = doubleVal;
        |      this.floatVal = floatVal;
        |      this.shortVal = shortVal;
        |      this.booleanVal = booleanVal;
        |      this.byteVal = byteVal;
        |      this.dateVal = dateVal;
        |      this.timestampVal = timestampVal;
        |   }
        |
        |   public String getStrVal() {
        |      return strVal;
        |   }
        |
        |   public void setStrVal(String strVal) {
        |      this.strVal = strVal;
        |   }
        |
        |   public Integer getIntVal() {
        |      return intVal;
        |   }
        |
        |   public void setIntVal(Integer intVal) {
        |      this.intVal = intVal;
        |   }
        |
        |   public Long getLongVal() {
        |      return longVal;
        |   }
        |
        |   public void setLongVal(Long longVal) {
        |      this.longVal = longVal;
        |   }
        |
        |   public Double getDoubleVal() {
        |      return doubleVal;
        |   }
        |
        |   public void setDoubleVal(Double doubleVal) {
        |      this.doubleVal = doubleVal;
        |   }
        |
        |   public Float getFloatVal() {
        |      return floatVal;
        |   }
        |
        |   public void setFloatVal(Float floatVal) {
        |      this.floatVal = floatVal;
        |   }
        |
        |   public Short getShortVal() {
        |      return shortVal;
        |   }
        |
        |   public void setShortVal(Short shortVal) {
        |      this.shortVal = shortVal;
        |   }
        |
        |   public Boolean getBooleanVal() {
        |      return booleanVal;
        |   }
        |
        |   public void setBooleanVal(Boolean booleanVal) {
        |      this.booleanVal = booleanVal;
        |   }
        |
        |   public Byte getByteVal() {
        |      return byteVal;
        |   }
        |
        |   public void setByteVal(Byte byteVal) {
        |      this.byteVal = byteVal;
        |   }
        |
        |   public java.util.Date getDateVal() {
        |      return dateVal;
        |   }
        |
        |   public void setDateVal(java.util.Date dateVal) {
        |      this.dateVal = dateVal;
        |   }
        |
        |   public java.sql.Timestamp getTimestampVal() {
        |      return timestampVal;
        |   }
        |
        |   public void setTimestampVal(java.sql.Timestamp timestampVal) {
        |      this.timestampVal = timestampVal;
        |   }
        |}""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClassWithAllSimpleTypes](JavaDialect)
    }
  }

  test("Array type") {
    val expected =
      """public class CV {
        |   private java.util.List<String> skills;
        |   private java.util.List<Integer> grades;
        |
        |   public CV() {}
        |
        |   public CV(java.util.List<String> skills, java.util.List<Integer> grades) {
        |      this.skills = skills;
        |      this.grades = grades;
        |   }
        |
        |   public java.util.List<String> getSkills() {
        |      return skills;
        |   }
        |
        |   public void setSkills(java.util.List<String> skills) {
        |      this.skills = skills;
        |   }
        |
        |   public java.util.List<Integer> getGrades() {
        |      return grades;
        |   }
        |
        |   public void setGrades(java.util.List<Integer> grades) {
        |      this.grades = grades;
        |   }
        |}""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[CV](JavaDialect)
    }
  }

  test("Nested array type") {
    val expected =
      """public class NestedArray {
        |   private java.util.List<java.util.List<String>> nested;
        |   private java.util.List<java.util.List<java.util.List<Integer>>> nested3;
        |
        |   public NestedArray() {}
        |
        |   public NestedArray(java.util.List<java.util.List<String>> nested, java.util.List<java.util.List<java.util.List<Integer>>> nested3) {
        |      this.nested = nested;
        |      this.nested3 = nested3;
        |   }
        |
        |   public java.util.List<java.util.List<String>> getNested() {
        |      return nested;
        |   }
        |
        |   public void setNested(java.util.List<java.util.List<String>> nested) {
        |      this.nested = nested;
        |   }
        |
        |   public java.util.List<java.util.List<java.util.List<Integer>>> getNested3() {
        |      return nested3;
        |   }
        |
        |   public void setNested3(java.util.List<java.util.List<java.util.List<Integer>>> nested3) {
        |      this.nested3 = nested3;
        |   }
        |}""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[NestedArray](JavaDialect)
    }
  }

  test("Struct types") {
    val expected =
      """public class Book {
        |   private String title;
        |   private Integer year;
        |   private com.datawizards.dmg.Person owner;
        |   private java.util.List<com.datawizards.dmg.Person> authors;
        |
        |   public Book() {}
        |
        |   public Book(String title, Integer year, com.datawizards.dmg.Person owner, java.util.List<com.datawizards.dmg.Person> authors) {
        |      this.title = title;
        |      this.year = year;
        |      this.owner = owner;
        |      this.authors = authors;
        |   }
        |
        |   public String getTitle() {
        |      return title;
        |   }
        |
        |   public void setTitle(String title) {
        |      this.title = title;
        |   }
        |
        |   public Integer getYear() {
        |      return year;
        |   }
        |
        |   public void setYear(Integer year) {
        |      this.year = year;
        |   }
        |
        |   public com.datawizards.dmg.Person getOwner() {
        |      return owner;
        |   }
        |
        |   public void setOwner(com.datawizards.dmg.Person owner) {
        |      this.owner = owner;
        |   }
        |
        |   public java.util.List<com.datawizards.dmg.Person> getAuthors() {
        |      return authors;
        |   }
        |
        |   public void setAuthors(java.util.List<com.datawizards.dmg.Person> authors) {
        |      this.authors = authors;
        |   }
        |}""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[Book](JavaDialect)
    }
  }

  test("Map type") {
    val expected =
      """public class ClassWithMap {
        |   private java.util.Map<Integer, Boolean> map;
        |
        |   public ClassWithMap() {}
        |
        |   public ClassWithMap(java.util.Map<Integer, Boolean> map) {
        |      this.map = map;
        |   }
        |
        |   public java.util.Map<Integer, Boolean> getMap() {
        |      return map;
        |   }
        |
        |   public void setMap(java.util.Map<Integer, Boolean> map) {
        |      this.map = map;
        |   }
        |}""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClassWithMap](JavaDialect)
    }
  }

  test("ClassWithArrayByte") {
    val expected =
      """public class ClassWithArrayByte {
        |   private java.util.List<Byte> arr;
        |
        |   public ClassWithArrayByte() {}
        |
        |   public ClassWithArrayByte(java.util.List<Byte> arr) {
        |      this.arr = arr;
        |   }
        |
        |   public java.util.List<Byte> getArr() {
        |      return arr;
        |   }
        |
        |   public void setArr(java.util.List<Byte> arr) {
        |      this.arr = arr;
        |   }
        |}""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClassWithArrayByte](JavaDialect)
    }
  }

}
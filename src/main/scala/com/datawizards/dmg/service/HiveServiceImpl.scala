package com.datawizards.dmg.service

import com.datawizards.dmg.metadata.MetaDataExtractor
import com.datawizards.dmg.{DataModelGenerator, dialects}
import org.apache.log4j.Logger

import scala.reflect.ClassTag
import scala.reflect.runtime.universe.TypeTag

object HiveServiceImpl extends HiveService {
  private val log = Logger.getLogger(getClass.getName)

  override def createHiveTable[T: ClassTag: TypeTag](variables: Map[String, String] = Map.empty): Unit = {
    val classTypeMetaData = MetaDataExtractor.extractClassMetaDataForDialect(Some(dialects.HiveDialect))
    val tableName = classTypeMetaData.typeName
    val createTableExpression = DataModelGenerator.generate[T](dialects.HiveDialect, classTypeMetaData)
    val dropTableExpression = s"DROP TABLE $tableName;\n"
    executeHiveScript(TemplateHandler.inflate(dropTableExpression + createTableExpression, variables))
  }

  override def createHiveTableIfNotExists[T: ClassTag: TypeTag](variables: Map[String, String] = Map.empty): Unit = {
    val createTableExpression = DataModelGenerator.generate[T](dialects.HiveDialect)
    executeHiveScript(TemplateHandler.inflate(createTableExpression, variables))
  }

  private def executeHiveScript(sql: String): Unit = {
    val file = "/tmp/dmg_" + java.util.UUID.randomUUID().toString
    val pw = new java.io.PrintWriter(file)
    pw.write(sql)
    pw.close()
    val command = s"hive -f $file"
    ConsoleCommandExecutor.execute(command)
  }

}

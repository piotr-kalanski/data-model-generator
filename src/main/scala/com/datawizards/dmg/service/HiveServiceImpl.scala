package com.datawizards.dmg.service

import com.datawizards.dmg.metadata.MetaDataExtractor
import com.datawizards.dmg.{DataModelGenerator, dialects}
import org.apache.log4j.Logger

import scala.reflect.ClassTag
import scala.reflect.runtime.universe.TypeTag

object HiveServiceImpl extends HiveService {
  private val log = Logger.getLogger(getClass.getName)

  class BatchCreateTable {
    val sqlScriptBuilder = new StringBuilder
    def createTable[T: ClassTag: TypeTag]: BatchCreateTable = {
      sqlScriptBuilder ++= buildCreateTableTemplate[T]()
      sqlScriptBuilder ++= "\n\n"
      this
    }

    def getScript: String = sqlScriptBuilder.toString()

    def execute(variables: Map[String, String] = Map.empty): Unit = {
      executeHiveScript(TemplateHandler.inflate(getScript, variables))
    }
  }

  def batchCreateTable(): BatchCreateTable = new BatchCreateTable()

  override def createHiveTable[T: ClassTag: TypeTag](variables: Map[String, String] = Map.empty): Unit = {
    executeHiveScript(TemplateHandler.inflate(buildCreateTableTemplate[T](), variables))
  }

  override def createHiveTableIfNotExists[T: ClassTag: TypeTag](variables: Map[String, String] = Map.empty): Unit = {
    val createTableExpression = DataModelGenerator.generate[T](dialects.HiveDialect)
    executeHiveScript(TemplateHandler.inflate(createTableExpression, variables))
  }

  private def buildCreateTableTemplate[T: ClassTag: TypeTag](): String = {
    val classTypeMetaData = MetaDataExtractor.extractClassMetaDataForDialect[T](Some(dialects.HiveDialect))
    val tableName = classTypeMetaData.typeName
    val createTableExpression = DataModelGenerator.generate[T](dialects.HiveDialect, classTypeMetaData)
    val dropTableExpression = s"DROP TABLE IF EXISTS $tableName;\n"
    dropTableExpression + createTableExpression
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

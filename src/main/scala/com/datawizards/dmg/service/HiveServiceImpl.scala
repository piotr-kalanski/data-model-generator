package com.datawizards.dmg.service

import com.datawizards.dmg.dialects.MetaDataWithDialectExtractor
import com.datawizards.dmg.generator.HiveGenerator
import com.datawizards.dmg.{DataModelGenerator, dialects}
import org.apache.log4j.Logger

import scala.reflect.ClassTag
import scala.reflect.runtime.universe.TypeTag

object HiveServiceImpl extends HiveService {
  private val log = Logger.getLogger(getClass.getName)

  class BatchCreateTable(val hiveGenerator: HiveGenerator) {
    val sqlScriptBuilder = new StringBuilder
    def createTable[T: ClassTag: TypeTag]: BatchCreateTable = {
      sqlScriptBuilder ++= buildCreateTableTemplate[T](hiveGenerator)
      sqlScriptBuilder ++= "\n\n"
      this
    }

    def getScript: String = sqlScriptBuilder.toString()

    def execute(variables: Map[String, String] = Map.empty): Unit = {
      executeHiveScript(TemplateHandler.inflate(getScript, variables))
    }
  }

  def batchCreateTable()(implicit hiveGenerator: HiveGenerator): BatchCreateTable = new BatchCreateTable(hiveGenerator)

  override def createHiveTable[T: ClassTag: TypeTag](variables: Map[String, String] = Map.empty)(implicit hiveGenerator: HiveGenerator): Unit = {
    executeHiveScript(TemplateHandler.inflate(buildCreateTableTemplate[T](hiveGenerator), variables))
  }

  private def buildCreateTableTemplate[T: ClassTag: TypeTag](hiveGenerator: HiveGenerator): String = {
    val classTypeMetaData = MetaDataWithDialectExtractor.extractClassMetaDataForDialect[T](Some(dialects.HiveDialect))
    DataModelGenerator.generate[T](hiveGenerator, classTypeMetaData)
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

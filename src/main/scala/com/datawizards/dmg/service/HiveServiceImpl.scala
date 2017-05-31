package com.datawizards.dmg.service

import com.datawizards.dmg.{DataModelGenerator, dialects}
import org.apache.log4j.Logger

import sys.process._
import scala.reflect.ClassTag
import scala.reflect.runtime.universe.TypeTag

object HiveServiceImpl extends HiveService {
  private val log = Logger.getLogger(getClass.getName)

  override def createHiveTable[T: ClassTag: TypeTag](): Unit = {
    val tableName = DataModelGenerator.getTargetNameForClass[T](dialects.Hive)
    val createTableExpression = DataModelGenerator.generate[T](dialects.Hive)
    val dropTableExpression = s"DROP TABLE $tableName;\n"
    executeHiveScript(dropTableExpression + createTableExpression)
  }

  override def createHiveTableIfNotExists[T: ClassTag: TypeTag](): Unit = {
    val createTableExpression = DataModelGenerator.generate[T](dialects.Hive)
    executeHiveScript(createTableExpression)
  }

  private def executeHiveScript(sql: String): Unit = {
    val file = "/tmp/dmg_" + java.util.UUID.randomUUID().toString
    val pw = new java.io.PrintWriter(file)
    pw.write(sql)
    pw.close()
    val command = s"hive -f $file"
    log.info(s"Executing hive command: [$command]")
    val out: String = command.!!
    log.info("Execute hive command. Output:\n" + out)
  }

}

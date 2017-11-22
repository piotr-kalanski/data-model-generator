package com.datawizards.dmg.service

import org.apache.log4j.Logger
import sys.process._

object ConsoleCommandExecutor {
  private val log = Logger.getLogger(getClass.getName)

  def execute(command: String): Unit = {
    log.info(s"Executing command: [$command]")
    val returnCode: Int = command.!<(ProcessLogger(outLine => log.info(outLine)))
    log.info(s"Command finished. Return code ${returnCode}")
    if(returnCode != 0){
      throw new RuntimeException(s"Failed executing command: ${command}")
    }
  }
}

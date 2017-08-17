package com.datawizards.dmg.service

import org.apache.log4j.Logger
import sys.process._

object ConsoleCommandExecutor {
  private val log = Logger.getLogger(getClass.getName)

  def execute(command: String): Unit = {
    log.info(s"Executing command: [$command]")
    val out: String = command.!!
    log.info("Execute command. Output:\n" + out)
  }
}

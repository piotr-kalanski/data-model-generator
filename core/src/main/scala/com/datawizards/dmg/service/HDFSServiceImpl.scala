package com.datawizards.dmg.service

object HDFSServiceImpl extends HDFSService {
  override def copyLocalFileToHDFS(localFilePath: String, hdfsPath: String): Unit = {
    val command = s"hdfs dfs -copyFromLocal $localFilePath $hdfsPath"
    ConsoleCommandExecutor.execute(command)
  }
}

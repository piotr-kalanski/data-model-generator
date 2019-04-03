package com.datawizards.dmg.service

trait HDFSService {
  def copyLocalFileToHDFS(localFilePath: String, hdfsPath: String): Unit
}

package com.datawizards.dmg.service

import com.datawizards.dmg.repository.{AvroSchemaRegistryRepository, AvroSchemaRegistryRepositoryImpl}

class AvroSchemaRegistryServiceImpl(url: String) extends AvroSchemaRegistryService {
  override protected val repository: AvroSchemaRegistryRepository = new AvroSchemaRegistryRepositoryImpl(url)
  override protected val hdfsService: HDFSService = HDFSServiceImpl
}

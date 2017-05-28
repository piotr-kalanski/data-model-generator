package com.datawizards.dmg.service

import com.datawizards.dmg.repository.{ElasticsearchRepository, ElasticsearchRepositoryImpl}

class ElasticsearchServiceImpl(url: String) extends ElasticsearchService {
  override protected val repository: ElasticsearchRepository = new ElasticsearchRepositoryImpl(url)
}

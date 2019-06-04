package io.shalastra.searchengine;

import io.shalastra.searchengine.repositories.IndexedWordsRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SearchEngineConfiguration {

  @Bean
  public IndexedWordsRepository indexedWordsRepository() {
    return new IndexedWordsRepository();
  }
}

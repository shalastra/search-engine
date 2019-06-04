package io.shalastra.searchengine;

import io.shalastra.searchengine.repositories.IndexedWordDocumentsRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SearchEngineConfiguration {

  @Bean
  public IndexedWordDocumentsRepository indexedWordsRepository() {
    return new IndexedWordDocumentsRepository();
  }
}

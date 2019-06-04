package io.shalastra.searchengine;

import io.shalastra.searchengine.repositories.IndexedWordDocumentsRepository;
import io.shalastra.searchengine.repositories.WordFrequenciesRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SearchEngineConfiguration {

  @Bean
  public IndexedWordDocumentsRepository indexedWordDocumentsRepository() {
    return new IndexedWordDocumentsRepository();
  }

  @Bean
  public WordFrequenciesRepository wordFrequenciesRepository() {
    return new WordFrequenciesRepository();
  }
}

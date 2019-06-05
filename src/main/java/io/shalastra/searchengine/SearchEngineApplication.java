package io.shalastra.searchengine;

import io.shalastra.searchengine.models.Document;
import io.shalastra.searchengine.repositories.DocumentRepository;
import io.shalastra.searchengine.services.SearchEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SearchEngineApplication implements CommandLineRunner {

  @Autowired
  private SearchEngine searchEngine;

  public static void main(String[] args) {
    SpringApplication.run(SearchEngineApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    Document doc1 = new Document("the brown fox jumped over the brown dog");
    Document doc2 = new Document("the lazy brown dog sat in the corner");
    Document doc3 = new Document("the red fox bit the lazy dog");

    searchEngine.updateInvertedIndex(doc1);
    searchEngine.updateInvertedIndex(doc2);
    searchEngine.updateInvertedIndex(doc3);
  }
}

package io.shalastra.searchengine;

import java.util.LinkedHashSet;

import io.shalastra.searchengine.models.Document;
import io.shalastra.searchengine.models.Word;
import io.shalastra.searchengine.repositories.IndexedWordDocumentsRepository;
import io.shalastra.searchengine.repositories.WordFrequenciesRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SearchEngineTests {

  @Autowired
  private IndexedWordDocumentsRepository indexedWordDocumentsRepository;

  @Autowired
  private WordFrequenciesRepository wordFrequenciesRepository;

  @Test
  public void calculateWordFrequencyInGivenDocument_ShouldReturnFrequencyNumber() {
    Word word = new Word("brown");
    int expectedFrequency = 2;
    Document document = new Document("the brown fox jumped over the brown dog");

    LinkedHashSet<Document> documents = new LinkedHashSet<>();
    documents.add(document);

    indexedWordDocumentsRepository.initializeInvertedIndex(documents);

    assertEquals(expectedFrequency, wordFrequenciesRepository.get(word).get(document).intValue());
  }
}

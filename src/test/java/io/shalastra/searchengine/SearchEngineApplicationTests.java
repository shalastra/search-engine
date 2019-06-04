package io.shalastra.searchengine;

import java.util.LinkedHashSet;

import io.shalastra.searchengine.models.Document;
import io.shalastra.searchengine.models.Word;
import io.shalastra.searchengine.repositories.IndexedWordsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SearchEngineApplicationTests {

  @Autowired
  private IndexedWordsRepository indexedWordsRepository;

  @Test
  public void saveDocumentsByContainingGivenWord() {
    Document doc1 = new Document("the brown fox jumped over the brown dog");
    Document doc2 = new Document("the lazy brown dog sat in the corner");
    Document doc3 = new Document("the red fox bit the lazy dog");

    LinkedHashSet<Document> documents = new LinkedHashSet<>();
    documents.add(doc1);
    documents.add(doc2);
    documents.add(doc3);

    Word word = new Word("dog");

    indexedWordsRepository.put(word, documents);

    assertEquals(indexedWordsRepository.get(word), documents);
  }
}

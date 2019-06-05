package io.shalastra.searchengine;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;

import io.shalastra.searchengine.models.Document;
import io.shalastra.searchengine.models.Word;
import io.shalastra.searchengine.services.SearchEngine;
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
  private SearchEngine searchEngine;

  @Test
  public void saveDocumentsByContainingGivenWord() {
    Word word = new Word("dog");

    Document doc1 = new Document("the brown fox jumped over the brown dog");
    Document doc2 = new Document("the lazy brown dog sat in the corner");
    Document doc3 = new Document("the red fox bit the lazy dog");

    LinkedHashSet<Document> documents = new LinkedHashSet<>();
    documents.add(doc1);
    documents.add(doc2);
    documents.add(doc3);

    searchEngine.getInvertedIndex().put(word, documents);

    assertEquals(searchEngine.getInvertedIndex().get(word).size(), documents.size());
  }

  @Test
  public void saveDocuments_ShouldReturnInvertedIndex() {
    Word word = new Word("dog");

    Document doc1 = new Document("the brown fox jumped over the brown dog");
    Document doc2 = new Document("the lazy brown dog sat in the corner");
    Document doc3 = new Document("the red fox bit the lazy dog");

    LinkedHashSet<Document> documents = new LinkedHashSet<>();
    documents.add(doc1);
    documents.add(doc2);
    documents.add(doc3);

    searchEngine.updateInvertedIndex(doc1);
    searchEngine.updateInvertedIndex(doc2);
    searchEngine.updateInvertedIndex(doc3);

    //Assertion is valid since all documents contain the word "dog"
    assertEquals(documents, searchEngine.getInvertedIndex().get(word));
  }

  @Test
  public void calculateWordFrequencyInGivenDocument_ShouldReturnFrequencyNumber() {
    Word word = new Word("brown");
    int expectedFrequency = 2;
    Document document = new Document("the brown fox jumped over the brown dog");

    searchEngine.updateInvertedIndex(document);

    assertEquals(expectedFrequency, searchEngine.getWordFrequencies().get(word).get(document).intValue());
  }

  @Test
  public void getWordFrequencyInDocuments_ShouldReturnNumberOfDocuments() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Word word = new Word("brown");
    int expectedFrequency = 2;

    Document doc1 = new Document("the brown fox jumped over the brown dog");
    Document doc2 = new Document("the lazy brown dog sat in the corner");
    Document doc3 = new Document("the red fox bit the lazy dog");

    searchEngine.updateInvertedIndex(doc1);
    searchEngine.updateInvertedIndex(doc2);
    searchEngine.updateInvertedIndex(doc3);

    Method getWordFrequencyInDocumentsMethod = SearchEngine.class.getDeclaredMethod("getWordFrequencyInDocuments", Word.class);
    getWordFrequencyInDocumentsMethod.setAccessible(true);

    int documentsNumber = (int) getWordFrequencyInDocumentsMethod.invoke(searchEngine, word);

    assertEquals(expectedFrequency, documentsNumber);
  }

  @Test
  public void updateInvertedIndex_ShouldReturnDifferentValueAfterEachUpdate() {
    Document doc1 = new Document("the brown fox jumped over the brown dog");
    Document doc2 = new Document("the lazy brown dog sat in the corner");

    Word word1 = new Word("jumped");
    Word word2 = new Word("brown");

    HashMap<Word, LinkedHashSet<Document>> expectedInvertedIndex = new HashMap<>();
    expectedInvertedIndex.put(word1, new LinkedHashSet<>(Collections.singleton(doc1)));
    expectedInvertedIndex.put(word2, new LinkedHashSet<>(Collections.singletonList(doc1)));

    searchEngine.updateInvertedIndex(doc1);

    assertEquals(expectedInvertedIndex.get(word1), searchEngine.getInvertedIndex().get(word1));
    assertEquals(expectedInvertedIndex.get(word2), searchEngine.getInvertedIndex().get(word2));

    expectedInvertedIndex.get(word2).add(doc2);

    searchEngine.updateInvertedIndex(doc2);

    assertEquals(expectedInvertedIndex.get(word1), searchEngine.getInvertedIndex().get(word1));
    assertEquals(expectedInvertedIndex.get(word2), searchEngine.getInvertedIndex().get(word2));
  }
}

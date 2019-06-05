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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SearchEngineTests {

  @Autowired
  private SearchEngine searchEngine;

  private static final Document doc1 = new Document("the brown fox jumped over the brown dog");
  private static final Document doc2 = new Document("the lazy brown dog sat in the corner");
  private static final Document doc3 = new Document("the red fox bit the lazy dog");

  @Test
  public void saveDocumentsByContainingGivenWord() {
    Word word = new Word("dog");

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

  @Test
  public void calculateTFIDFForWordInSingleDocument_ShouldReturnDoubleValue() throws NoSuchMethodException,
      InvocationTargetException, IllegalAccessException {
    Word word = new Word("brown");

    int wordFrequency = 2;
    int documentLength = 8;
    int documentNumber = 1;
    int documentNumberContainingWord = 1;

    double expectedTF = (double) wordFrequency / documentLength;
    double expectedIDF = Math.log10((double) documentNumber / (1 + documentNumberContainingWord));

    double expectedTFIDF = expectedTF * expectedIDF;

    Method calculateTfIDFMethod = SearchEngine.class.getDeclaredMethod("calculateTFIDF", Word.class, Document.class);
    calculateTfIDFMethod.setAccessible(true);

    searchEngine.updateInvertedIndex(doc1);

    double actualTFIDF = (double) calculateTfIDFMethod.invoke(searchEngine, word, doc1);

    assertEquals(expectedTFIDF, actualTFIDF, 0);
  }
}

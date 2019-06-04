package io.shalastra.searchengine;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;

import io.shalastra.searchengine.models.Document;
import io.shalastra.searchengine.models.Word;
import io.shalastra.searchengine.repositories.IndexedWordDocumentsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IndexedWordDocumentsRepositoryTest {

  @Autowired
  private IndexedWordDocumentsRepository indexedWordDocumentsRepository;

  private LinkedHashSet<Document> documents;

  @Before
  public void setUp() {
    Document doc1 = new Document("the brown fox jumped over the brown dog");
    Document doc2 = new Document("the lazy brown dog sat in the corner");
    Document doc3 = new Document("the red fox bit the lazy dog");

    documents = new LinkedHashSet<>();

    documents.add(doc1);
    documents.add(doc2);
    documents.add(doc3);
  }

  @Test
  public void saveDocumentsByContainingGivenWord() {
    Word word = new Word("dog");

    indexedWordDocumentsRepository.put(word, documents);

    assertEquals(indexedWordDocumentsRepository.get(word).size(), documents.size());
  }

  @Test
  public void saveDocuments_ShouldReturnInvertedIndex() {
    Word word = new Word("dog");

    indexedWordDocumentsRepository.initializeInvertedIndex(documents);

    //Assertion is valid since all documents contain the word "dog"
    assertEquals(documents, indexedWordDocumentsRepository.get(word));
  }

  @Test
  public void getDocumentLength_ShouldReturnNumberOfWords() {
    Document document = documents.iterator().next();
    int expectedLength = document.splitDocument().size();

    int wordsNumber = document.getDocumentLength();

    assertEquals(expectedLength, wordsNumber);
  }

  @Test
  public void getWordFrequencyInDocuments_ShouldReturnNumberOfDocuments() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Word word = new Word("brown");
    int expectedFrequency = 2;

    indexedWordDocumentsRepository.initializeInvertedIndex(documents);

    Method getWordFrequencyInDocumentsMethod = IndexedWordDocumentsRepository.class.getDeclaredMethod("getWordFrequencyInDocuments", Word.class);
    getWordFrequencyInDocumentsMethod.setAccessible(true);

    int documentsNumber = (int) getWordFrequencyInDocumentsMethod.invoke(indexedWordDocumentsRepository, word);

    assertEquals(expectedFrequency, documentsNumber);
  }
}

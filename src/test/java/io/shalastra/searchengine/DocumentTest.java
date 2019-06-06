package io.shalastra.searchengine;

import java.lang.reflect.Field;

import io.shalastra.searchengine.models.Document;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DocumentTest {

  @After
  public void setUp() throws IllegalAccessException, NoSuchFieldException {
    Field counterField = Document.class.getDeclaredField("counter");
    counterField.setAccessible(true);

    counterField.set(null, 1);
  }

  @Test
  public void getDocumentLength_ShouldReturnNumberOfWords() {
    Document document = new Document("the brown fox jumped over the brown dog");
    int expectedLength = document.splitText().size();

    int wordsNumber = document.getDocumentLength();

    assertEquals(expectedLength, wordsNumber);
  }
}

package io.shalastra.searchengine;

import io.shalastra.searchengine.models.Document;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DocumentTest {

  @Test
  public void getDocumentLength_ShouldReturnNumberOfWords() {
    Document document = new Document("the brown fox jumped over the brown dog");
    int expectedLength = document.splitDocument().size();

    int wordsNumber = document.getDocumentLength();

    assertEquals(expectedLength, wordsNumber);
  }
}

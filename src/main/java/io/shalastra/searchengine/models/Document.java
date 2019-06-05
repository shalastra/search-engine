package io.shalastra.searchengine.models;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Data;

@Data
public class Document {

  public static final String SPLIT_REGEX = "\\P{L}+";
  private static final String DOC = "document ";

  private static int counter = 0;

  private String filename;
  private String document;

  public Document(String document) {
    this.document = document;
    this.filename = DOC + counter++;
  }

  /**
   * Split given document by words for indexing
   * @return
   */
  public List<Word> splitDocument() {
    return Stream.of(document.split(SPLIT_REGEX)).map(Word::new).collect(Collectors.toList());
  }

  /**
   * @return number of words in the document
   */
  public int getDocumentLength() {
    return splitDocument().size();
  }
}

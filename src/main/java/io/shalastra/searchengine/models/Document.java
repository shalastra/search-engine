package io.shalastra.searchengine.models;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Data;

/**
 * Document Model class storing text and filename of document
 */
@Data
public class Document {

  public static final String SPLIT_REGEX = "\\P{L}+";
  private static final String DOC = "document ";

  /**
   * Static field for increasing document filename number
   */
  private static int counter = 1;

  private final String filename;
  private String text;

  public Document(String text) {
    this.text = text;
    this.filename = DOC + counter;

    counter++;
  }

  /**
   * Split given text by words for indexing
   *
   * @return list of single words, wrapped in {@link Word} object
   */
  public List<Word> splitText() {
    return Stream.of(text.split(SPLIT_REGEX)).map(Word::new).collect(Collectors.toList());
  }

  /**
   * @return number of words in the text
   */
  public int getDocumentLength() {
    return splitText().size();
  }
}

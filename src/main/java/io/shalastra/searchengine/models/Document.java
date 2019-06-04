package io.shalastra.searchengine.models;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Document {

  public static final String SPLIT_REGEX = "\\P{L}+";

  private String document;

  public List<Word> splitDocument() {
    return Stream.of(document.split(SPLIT_REGEX)).map(Word::new).collect(Collectors.toList());
  }

  public int getDocumentLength() {
    return splitDocument().size();
  }
}

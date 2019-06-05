package io.shalastra.searchengine.models;

import lombok.Data;

@Data
public class Word {

  private String word;

  public Word(String word) {
    this.word = word.toLowerCase();
  }
}

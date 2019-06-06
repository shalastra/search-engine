package io.shalastra.searchengine.models;

import lombok.Data;

/**
 * Single word wrapper with lowercase operation
 */
@Data
public class Word {

  private String word;

  public Word(String word) {
    this.word = word.toLowerCase();
  }
}

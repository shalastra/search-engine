package io.shalastra.searchengine.repositories;

import java.util.HashMap;

import io.shalastra.searchengine.models.Document;
import io.shalastra.searchengine.models.Word;

public class WordFrequenciesRepository extends HashMap<Word, HashMap<Document, Integer>> {

  public void calculateWordFrequency(Word word, Document document) {
    HashMap<Document, Integer> wordFrequency = get(word);

    if (wordFrequency == null) {
      wordFrequency = new HashMap<>();
      wordFrequency.put(document, 1);
    } else {
      wordFrequency.merge(document, 1, Integer::sum);
    }

    put(word, wordFrequency);
  }
}

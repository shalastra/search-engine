package io.shalastra.searchengine.repositories;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;

import io.shalastra.searchengine.models.Document;
import io.shalastra.searchengine.models.Word;

public class IndexedWordsRepository extends HashMap<Word, LinkedHashSet<Document>> {

  private static final String SPLIT_REGEX = "\\P{L}+";

  public void initializeInvertedIndex(LinkedHashSet<Document> documents) {
    for (Document document : documents) {
      Arrays.asList(document.getDocument().split(SPLIT_REGEX))
          .forEach(stringWord -> {
                Word word = new Word(stringWord.toLowerCase());
                LinkedHashSet<Document> documentsContainingGivenWord = get(word);

                if (documentsContainingGivenWord == null || documentsContainingGivenWord.isEmpty()) {
                  documentsContainingGivenWord = new LinkedHashSet<>();
                }

                documentsContainingGivenWord.add(document);

                put(word, documentsContainingGivenWord);
              }
          );
    }
  }
}

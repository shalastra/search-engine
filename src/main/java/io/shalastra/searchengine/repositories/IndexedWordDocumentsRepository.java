package io.shalastra.searchengine.repositories;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import io.shalastra.searchengine.models.Document;
import io.shalastra.searchengine.models.Word;

public class IndexedWordDocumentsRepository extends HashMap<Word, LinkedHashSet<Document>> {


  public void initializeInvertedIndex(Set<Document> documents) {
    for (Document document : documents) {
      document.splitDocument().forEach(word -> {
        LinkedHashSet<Document> documentsContainingGivenWord = get(word);

        if (documentsContainingGivenWord == null || documentsContainingGivenWord.isEmpty()) {
          documentsContainingGivenWord = new LinkedHashSet<>();
        }

        documentsContainingGivenWord.add(document);

        put(word, documentsContainingGivenWord);
      });
    }
  }

  private int getWordFrequencyInDocuments(Word word) {
    return get(word).size();
  }
}

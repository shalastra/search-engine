package io.shalastra.searchengine.repositories;

import java.util.*;
import java.util.stream.Collectors;

import io.shalastra.searchengine.models.Document;
import io.shalastra.searchengine.models.Word;

public class IndexedWordDocumentsRepository extends HashMap<Word, LinkedHashSet<Document>> {

  private static final String SPLIT_REGEX = "\\P{L}+";

  public void initializeInvertedIndex(Set<Document> documents) {
    for (Document document : documents) {
      splitDocument(document).forEach(word -> {
        LinkedHashSet<Document> documentsContainingGivenWord = get(word);

        if (documentsContainingGivenWord == null || documentsContainingGivenWord.isEmpty()) {
          documentsContainingGivenWord = new LinkedHashSet<>();
        }

        documentsContainingGivenWord.add(document);

        put(word, documentsContainingGivenWord);
      });
    }
  }

  private List<Word> splitDocument(Document document) {
    return Arrays.stream(document.getDocument().split(SPLIT_REGEX)).map(Word::new).collect(Collectors.toList());
  }
}

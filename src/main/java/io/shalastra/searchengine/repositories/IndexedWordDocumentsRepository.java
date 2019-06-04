package io.shalastra.searchengine.repositories;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.shalastra.searchengine.models.Document;
import io.shalastra.searchengine.models.Word;

public class IndexedWordDocumentsRepository extends HashMap<Word, LinkedHashSet<Document>> {

  public static final String SPLIT_REGEX = "\\P{L}+";

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

  private int getDocumentLength(Document document) {
    return splitDocument(document).size();
  }

  private int getWordFrequencyInDocuments(Word word) {
    return get(word).size();
  }

  private List<Word> splitDocument(Document document) {
    return Stream.of(document.getDocument().split(SPLIT_REGEX)).map(Word::new).collect(Collectors.toList());
  }
}

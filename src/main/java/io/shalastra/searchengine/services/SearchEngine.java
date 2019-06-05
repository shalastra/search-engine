package io.shalastra.searchengine.services;

import java.util.HashMap;
import java.util.LinkedHashSet;

import javax.annotation.PostConstruct;

import io.shalastra.searchengine.models.Document;
import io.shalastra.searchengine.models.Word;
import io.shalastra.searchengine.repositories.DocumentRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchEngine {

  @Autowired
  private DocumentRepository documents;

  @Getter
  private HashMap<Word, HashMap<Document, Integer>> wordFrequencies;

  @Getter
  private HashMap<Word, LinkedHashSet<Document>> invertedIndex;

  @PostConstruct
  private void initialize() {
    wordFrequencies = new HashMap<>();
    invertedIndex = new HashMap<>();
  }

  public void updateInvertedIndex(Document document) {
    documents.saveDocument(document);

    document.splitDocument().forEach(word -> {
      calculateWordFrequency(word, document);

      LinkedHashSet<Document> documentsContainingGivenWord = invertedIndex.get(word);

      if (documentsContainingGivenWord == null || documentsContainingGivenWord.isEmpty()) {
        documentsContainingGivenWord = new LinkedHashSet<>();
      }

      documentsContainingGivenWord.add(document);

      invertedIndex.put(word, documentsContainingGivenWord);
    });
  }

  public void calculateWordFrequency(Word word, Document document) {
    HashMap<Document, Integer> wordFrequency = wordFrequencies.get(word);

    if (wordFrequency == null) {
      wordFrequency = new HashMap<>();
      wordFrequency.put(document, 1);
    } else {
      wordFrequency.merge(document, 1, Integer::sum);
    }

    wordFrequencies.put(word, wordFrequency);
  }

  private int getWordFrequencyInDocuments(Word word) {
    return invertedIndex.get(word).size();
  }
}

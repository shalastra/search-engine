package io.shalastra.searchengine.services;

import java.util.*;
import java.util.stream.Collectors;

import io.shalastra.searchengine.models.Document;
import io.shalastra.searchengine.models.Word;
import io.shalastra.searchengine.repositories.DocumentRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchEngine {

  private DocumentRepository documents;

  @Getter
  private HashMap<Word, HashMap<Document, Integer>> wordFrequencies;

  @Getter
  private HashMap<Word, LinkedHashSet<Document>> invertedIndex;

  @Autowired
  public SearchEngine(DocumentRepository documentRepository) {
    this.documents = documentRepository;

    this.wordFrequencies = new HashMap<>();
    this.invertedIndex = new HashMap<>();
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

  private double calculateTFIDF(Word word, Document document) {
    double tf = (double) wordFrequencies.get(word).get(document) / document.getDocumentLength();
    double idf = Math.log10((double) documents.size() / (1 + getWordFrequencyInDocuments(word)));

    return tf * idf;
  }

  private List<String> sortByTFIDF(Word word, Set<Document> documents) {
    List<Document> sorted = new ArrayList<>(documents);

    sorted.sort((doc1, doc2) -> {
      double doc1TFIDF = calculateTFIDF(word, doc1);
      double doc2TFIDF = calculateTFIDF(word, doc2);

      return Double.compare(doc1TFIDF, doc2TFIDF);
    });

    return sorted.stream().map(Document::getFilename).collect(Collectors.toList());
  }
}

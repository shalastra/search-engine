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

  private DocumentRepository documentRepository;

  @Getter
  private HashMap<Word, HashMap<Document, Integer>> wordFrequencies;

  @Getter
  private HashMap<Word, LinkedHashSet<Document>> invertedIndex;

  @Autowired
  public SearchEngine(DocumentRepository documentRepository) {
    this.documentRepository = documentRepository;

    this.wordFrequencies = new HashMap<>();
    this.invertedIndex = new HashMap<>();
  }

  public void updateInvertedIndex(Document document) {
    documentRepository.add(document);

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

  public List<String> findByWord(Word word) {
    Set<Document> documentsContainingWord = invertedIndex.get(word);

    if (documentsContainingWord != null) {
      List<Document> results = new ArrayList<>(documentsContainingWord);
      if (documentsContainingWord.size() == 1) {
        return getDocumentFilenames(results);
      } else {
        return sortByTFIDF(word, results);
      }
    }

    return new ArrayList<>();
  }

  private void calculateWordFrequency(Word word, Document document) {
    HashMap<Document, Integer> wordFrequency = wordFrequencies.get(word);

    if (wordFrequency == null) {
      wordFrequency = new HashMap<>();
      wordFrequency.put(document, 1);
    } else {
      wordFrequency.merge(document, 1, Integer::sum);
    }

    wordFrequencies.put(word, wordFrequency);
  }

  private List<String> sortByTFIDF(Word word, List<Document> documents) {
    List<Document> sortedDocuments = new ArrayList<>(documents);
    sortedDocuments.sort((doc1, doc2) -> {
      double doc1TFIDF = calculateTFIDF(word, doc1);
      double doc2TFIDF = calculateTFIDF(word, doc2);

      return Double.compare(doc1TFIDF, doc2TFIDF);
    });

    return getDocumentFilenames(sortedDocuments);
  }

  private List<String> getDocumentFilenames(List<Document> sorted) {
    return sorted.stream().map(Document::getFilename).collect(Collectors.toList());
  }

  private double calculateTFIDF(Word word, Document document) {
    double tf = (double) wordFrequencies.get(word).get(document) / document.getDocumentLength();
    double idf = Math.log10((double) documentRepository.size() / (1 + getWordFrequencyInDocuments(word)));

    return tf * idf;
  }

  private int getWordFrequencyInDocuments(Word word) {
    return invertedIndex.get(word).size();
  }
}

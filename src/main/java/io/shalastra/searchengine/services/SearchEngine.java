package io.shalastra.searchengine.services;

import java.util.*;
import java.util.stream.Collectors;

import io.shalastra.searchengine.models.Document;
import io.shalastra.searchengine.models.Word;
import io.shalastra.searchengine.repositories.DocumentRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Class responsible for indexing and saving documents
 */
@Service
public class SearchEngine {

  private DocumentRepository documentRepository;

  /**
   * Store all words frequencies in each document
   */
  @Getter
  private HashMap<Word, HashMap<Document, Integer>> wordFrequencies;

  /**
   * Store all words with set of documents word appears
   */
  @Getter
  private HashMap<Word, LinkedHashSet<Document>> invertedIndex;

  @Autowired
  public SearchEngine(DocumentRepository documentRepository) {
    this.documentRepository = documentRepository;

    this.wordFrequencies = new HashMap<>();
    this.invertedIndex = new HashMap<>();
  }

  /**
   * Update index with new Document
   * More can be found: {https://en.wikipedia.org/wiki/Inverted_index}
   *
   * @param document - new document to add to the repository and the index
   */
  public void updateInvertedIndex(Document document) {
    // Add new document to the document repository
    documentRepository.add(document);

    document.splitText().forEach(word -> {
      calculateWordFrequency(word, document);

      LinkedHashSet<Document> documentsContainingGivenWord = invertedIndex.get(word);

      if (documentsContainingGivenWord == null || documentsContainingGivenWord.isEmpty()) {
        documentsContainingGivenWord = new LinkedHashSet<>();
      }

      documentsContainingGivenWord.add(document);

      invertedIndex.put(word, documentsContainingGivenWord);
    });
  }

  /**
   * Perform search on the index for a given word
   *
   * @param word - query in form of single word
   * @return list of sorted by TF-IDF fielnames or an empty list if word is not in the index
   */
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

  /**
   * Count word frequency in a given document
   *
   * @param word to look for in the document
   * @param document given during indexing
   */
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

  /**
   * Perform sorting by TF-IDF on the list of documents containing searched word
   *
   * @param word - given word to search
   * @param documents - list of documents containing the word
   * @return list of filenames from sorted documents
   */
  private List<String> sortByTFIDF(Word word, List<Document> documents) {
    List<Document> sortedDocuments = new ArrayList<>(documents);
    sortedDocuments.sort((doc1, doc2) -> {
      double doc1TFIDF = calculateTFIDF(word, doc1);
      double doc2TFIDF = calculateTFIDF(word, doc2);

      return Double.compare(doc1TFIDF, doc2TFIDF);
    });

    return getDocumentFilenames(sortedDocuments);
  }

  /**
   * Return final output of search for a given word
   *
   * @param sorted list of documents
   * @return filenames in form of List of Strings
   */
  private List<String> getDocumentFilenames(List<Document> sorted) {
    return sorted.stream().map(Document::getFilename).collect(Collectors.toList());
  }

  /**
   * Calculate TF-IDF for a given word in a given document
   * More can be found: {https://en.wikipedia.org/wiki/Tf%E2%80%93idf}
   * @param word
   * @param document -
   * @return TF-IDF value for a given word
   */
  private double calculateTFIDF(Word word, Document document) {
    double tf = (double) wordFrequencies.get(word).get(document) / document.getDocumentLength();
    double idf = Math.log10((double) documentRepository.size() / (1 + getWordFrequencyInDocuments(word)));

    return tf * idf;
  }

  private int getWordFrequencyInDocuments(Word word) {
    return invertedIndex.get(word).size();
  }
}

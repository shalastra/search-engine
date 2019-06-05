package io.shalastra.searchengine.repositories;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import io.shalastra.searchengine.models.Document;
import org.springframework.stereotype.Component;

@Component
public class DocumentRepository extends HashMap<Document, String> {

  private static final String DOC = "Document";

  private final AtomicInteger counter;

  public DocumentRepository() {
    counter = new AtomicInteger();
  }

  public void saveDocument(Document document) {
    put(document, DOC + counter.incrementAndGet());
  }
}

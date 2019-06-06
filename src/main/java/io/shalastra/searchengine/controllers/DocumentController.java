package io.shalastra.searchengine.controllers;

import java.util.List;

import io.shalastra.searchengine.models.Document;
import io.shalastra.searchengine.repositories.DocumentRepository;
import io.shalastra.searchengine.services.SearchEngine;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class DocumentController {

  private final SearchEngine searchEngine;

  private final DocumentRepository documentRepository;

  @GetMapping("/documents")
  public ResponseEntity<List<Document>> getAllDocuments() {
    return new ResponseEntity<>(documentRepository, HttpStatus.OK);
  }

  @PostMapping("/documents/new")
  public ResponseEntity<Document> saveNewDocument(@RequestBody Document document) {
    searchEngine.updateInvertedIndex(document);

    return new ResponseEntity<>(document, HttpStatus.CREATED);
  }

  @PostMapping("/documents")
  public ResponseEntity<List<Document>> saveDocumentsList(@RequestBody List<Document> documents) {
    documents.forEach(searchEngine::updateInvertedIndex);

    return new ResponseEntity<>(documents, HttpStatus.CREATED);
  }
}

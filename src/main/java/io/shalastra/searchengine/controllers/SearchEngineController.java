package io.shalastra.searchengine.controllers;

import java.util.List;

import io.shalastra.searchengine.models.Word;
import io.shalastra.searchengine.services.SearchEngine;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class SearchEngineController {

  private final SearchEngine searchEngine;

  @GetMapping("/search")
  public ResponseEntity<List<String>> findDocumentsByWord(@RequestParam String query) {
    Word word = new Word(query);

    return new ResponseEntity<>(searchEngine.findByWord(word), HttpStatus.OK);
  }
}

package io.shalastra.searchengine.repositories;

import java.util.HashMap;
import java.util.LinkedHashSet;

import io.shalastra.searchengine.models.Document;
import io.shalastra.searchengine.models.Word;

public class IndexedWordsRepository extends HashMap<Word, LinkedHashSet<Document>> {}

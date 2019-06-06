package io.shalastra.searchengine.repositories;

import java.util.ArrayList;

import io.shalastra.searchengine.models.Document;
import org.springframework.stereotype.Repository;

/**
 * Repository for storing all added documents
 */
@Repository
public class DocumentRepository extends ArrayList<Document> { }

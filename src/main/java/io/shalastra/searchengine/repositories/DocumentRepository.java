package io.shalastra.searchengine.repositories;

import java.util.ArrayList;

import io.shalastra.searchengine.models.Document;
import org.springframework.stereotype.Component;

/**
 * Repository for storing all added documents
 */
@Component
public class DocumentRepository extends ArrayList<Document> { }

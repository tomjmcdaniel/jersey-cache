package com.example.dao;

import com.example.data.Document;
import com.google.common.base.Optional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Persists store of Documents in volatile memory
 */
public class VolatileDocumentStore implements GenericStore<Document> {

    /**
     * Eagerly initialized in-memory hashmap for persisting documents
     */
    private static volatile Map<UUID, Document> documents = new HashMap<>();

    @Override
    public void put(UUID key, Document document) {
        assert key.equals(document.getUid());
        documents.put(key, document);
    }

    @Override
    public Optional<Document> get(UUID key) {
        return Optional.fromNullable(documents.get(key));
    }
}

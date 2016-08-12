package com.example.dao;

import com.example.data.Document;
import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Volatile cache using Google's more feature-rich CacheBuilder
 */
public class GuavaCacheDocumentStore implements GenericStore<Document> {

    private static final int MAX_SIZE = 5;
    private static final long TTL_AFTER_READ = 60; // sec

    private static volatile Cache<UUID, Document> documents = CacheBuilder.newBuilder()
        .maximumSize(MAX_SIZE)
        .expireAfterAccess(TTL_AFTER_READ, TimeUnit.SECONDS)
        .build();


    @Override
    public void put(UUID key, Document document) {
        documents.put(key, document);
    }

    @Override
    public Optional<Document> get(UUID key) {
        return Optional.fromNullable(documents.getIfPresent(key));
    }
}

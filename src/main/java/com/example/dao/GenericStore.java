package com.example.dao;

import com.google.common.base.Optional;

import java.util.UUID;

/**
 * A generic data store, indexing objects by a UUID
 *
 * Allows us to swap out implementation (eg. in-memory vs. persisted) with a consistent interface
 */
public interface GenericStore<T> {
    /**
     * Write to the store
     */
    void put(UUID key, T document);

    /**
     * Read from the store, returning Null on cache-miss
     */
    Optional<T> get(UUID key);
}

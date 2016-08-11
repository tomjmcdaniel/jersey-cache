package com.example.dao;

import javax.annotation.Nullable;
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
     * Read from the store
     *
     * NOTE: would prefer Optional<T> over Nullable return, but entire guava library is a bit much for single use
     */
    @Nullable
    T get(UUID key);
}

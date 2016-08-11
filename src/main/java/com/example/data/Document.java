package com.example.data;

import org.joda.time.DateTime;

import javax.annotation.Nonnull;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.UUID;

/**
 * Document allows us to encode supplemental information along with the cached HttpResponse, like expiration time, etc.
 */
public class Document {
    // clearly we have some extraneous metadata, but prefer to be explicit i/o premature optimization
    private final UUID uid;
    private final URI url;
    private final Response response;
    private final DateTime expiresAt;

    private static final long TTL = 60 * 1000; // ms

    public Document(String url, @Nonnull Response response) {
        this(UUID.randomUUID(), url, response);
    }

    private Document(UUID uid, String url, @Nonnull Response response) {
        this.uid = uid;
        this.url = URI.create(url);
        this.response = response;
        this.expiresAt = DateTime.now().plus(TTL);
    }

    public UUID getUid() {
        return uid;
    }

    public URI getUrl() {
        return url;
    }

    @Nonnull
    public Response getResponse() {
        return response;
    }

    public DateTime getExpiresAt() {
        return expiresAt;
    }
}

package com.example.controller;

import com.example.dao.GenericStore;
import com.example.dao.VolatileDocumentStore;
import com.example.data.Document;

import javax.ws.rs.*;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.UUID;

/**
 * Write endpoint
 */
@Path("cache")
public class DocumentController {

    // for mocking
    static GenericStore<Document> dao() {
        return new VolatileDocumentStore();
    }

    /**
     * Read endpoint
     */
    @GET
    @Path("{uid}")
    public Response get(
        @PathParam("uid") String key
    ) throws IOException {
        Document cacheHit = dao().get(UUID.fromString(key));

        // yuck: null check, prefer Optional
        if (cacheHit != null) {
            return cacheHit.getResponse();

        } else {
            // NOTE: we're conflating cache miss with a hit on upstream 404
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    /**
     * Write endpoint
     */
    @POST
    public String put(
        @QueryParam("url") String url
    ) throws IOException {
        // perform GET & construct cache entry
        Document document = getDocument(url);

        // write to cache
        dao().put(document.getUid(), document);

        // return entry's key
        return document.getUid().toString();
    }

    /**
     * Perform an HTTP GET to arbitrary URL and construct a Document from the Response
     *
     * This should really reside in a dedicated Client, but it is painfully simple and mockable as-is. Avoid premature
     * optimization
     */
    private Document getDocument(String url) {
        return new Document(
            url,
            ClientBuilder.newClient().target(url).request().get()
        );
    }
}

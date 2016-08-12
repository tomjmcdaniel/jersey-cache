package com.example.data;

import org.apache.commons.io.IOUtils;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Document allows us to encode supplemental information along with the cached HttpResponse, like expiration time, etc.
 */
public class Document {
    // clearly we have some extraneous metadata, but prefer to be explicit i/o premature optimization
    private final UUID uid;
    private final URI url;

    // these are the components of the original resposne, from which we can reconstruct it for idempotent reads
    private final int statusCode;
    private final MultivaluedMap<String, String> headers;
    private final byte[] body;

    public Document(String url, Response response) throws IOException {
        this(UUID.randomUUID(), url, response);
    }

    private Document(UUID uid, String url, Response response) throws IOException {
        this.uid = uid;
        this.url = URI.create(url);

        // FIXME: I don't really understand why the buffered stream is needed, instead of simply converting the original
        //  InputStream directly to byte[]
        InputStream buffered = IOUtils.toBufferedInputStream(
                response.readEntity(InputStream.class)
        );
        this.body = IOUtils.toByteArray(buffered);

        this.statusCode = response.getStatus();
        this.headers = response.getStringHeaders();
    }

    public UUID getUid() {
        return uid;
    }

    public URI getUrl() {
        return url;
    }

    public Response getResponse() throws IOException {
        Response.ResponseBuilder builder = Response.status(statusCode)
                .entity(body);

        // this is dumb, but ResponseBuilder doesn't expose a putAll method for its headers...
        for (Map.Entry<String, List<String>> obj : headers.entrySet()) {
            for (String value : obj.getValue()) {
                builder.header(obj.getKey(), value);
            }
        }

        return builder.build();
    }
}

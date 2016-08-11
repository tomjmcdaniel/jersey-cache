# jersey-cache

Painfully simple proxy cache

* MVP does not maintain a durable store, holding cache in volatile memory.

## Endpoints

```
POST /cache?url={url}
```

Executes a `GET` request to the specified HTTP `url` and will cache the response, returning a generated uid.

```
GET /cache/{uid}
```

Reads a previously-cached response with the associated uid.  
* Cache-hit will return the original Response (status, headers, body...)
* Cache-miss will return a 404 Not Found

## Build

```
mvn clean test exec:java
```

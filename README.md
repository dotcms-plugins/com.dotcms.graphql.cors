# Cors headers for GraphQL

This plugin adds an interceptor that reads cors headers from the `dotmarketing-config.properties` and adds them to graphql api calls.

You can override specific headers by adding them to the properties file, e.g.
```
api.cors.graphql.access-control-expose-headers=*
api.cors.graphql.access-control-allow-methods: GET,PUT,POST,DELETE,HEAD,OPTIONS,PATCH

```

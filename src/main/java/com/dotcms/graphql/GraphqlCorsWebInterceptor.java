package com.dotcms.graphql;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.dotcms.filters.interceptor.Result;
import com.dotcms.filters.interceptor.WebInterceptor;
import com.dotmarketing.util.Config;
import com.dotmarketing.util.UtilMethods;
import io.vavr.Function0;

public class GraphqlCorsWebInterceptor implements WebInterceptor {


    private static final String API_CALL = "/api/v1/graphql";


    final private static String CORS_PREFIX = "api.cors";
    final private static String CORS_DEFAULT = "default";
    final private static String CORS_GRAPHQL = "graphql";

    @Override
    public String[] getFilters() {
        return new String[] {API_CALL + "*"};
    }

    @Override
    public Result intercept(final HttpServletRequest request, final HttpServletResponse response) throws IOException {



        Map<String, String> heads = corsHeaders.apply();

        heads.entrySet().stream().forEach(e -> response.setHeader(e.getKey(), e.getValue()));

        return Result.NEXT;
    }


    /**
     * header list is computed but once. It first reads all values set as default, e.g.
     * api.cors.default.access-control-allow-headers that start with api.cors.default. It then overrides
     * those with the specific ones for graphql, api.cors.graphql.access-control-allow-headers
     * 
     */
    Function0<HashMap<String, String>> corsHeaders = Function0.of(() -> {

        final HashMap<String, String> headers = new HashMap<>();
        // load defaults
        Config.subset(CORS_PREFIX + "." + CORS_DEFAULT).forEachRemaining(k -> {
            final String prop = Config.getStringProperty(CORS_PREFIX + "." + CORS_DEFAULT + "." + k, null);
            if (UtilMethods.isSet(prop)) {
                headers.put(k.toLowerCase(), prop);
            } 

        });
        // then override with graph
        Config.subset(CORS_PREFIX + "." + CORS_GRAPHQL).forEachRemaining(k -> {
            final String prop = Config.getStringProperty(CORS_PREFIX + "." + CORS_GRAPHQL + "." + k, null);
            if (UtilMethods.isSet(prop)) {
                headers.put(k.toLowerCase(), prop);
            } else {
                headers.remove(k.toLowerCase());
            }

        });



        return headers;


    }).memoized();



}

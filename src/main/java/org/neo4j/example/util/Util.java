package org.neo4j.example.util;

import org.codehaus.jackson.map.ObjectMapper;
import org.neo4j.helpers.collection.IteratorWrapper;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * @author Michael Hunger @since 22.10.13
 */
public class Util {
    public static final String DEFAULT_URL = "http://localhost:7474";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public static final String WEBAPP_LOCATION = "src/main/webapp/";

    public static String toCypherUri(String baseUri) {
        try {
            return new URL(new URL(baseUri), "/db/data/transaction/commit").toString();
        } catch (Exception e) {
            throw new RuntimeException("Error constructing cypher uri from " + baseUri, e);
        }
    }

    @SuppressWarnings("unchecked")
    public static Map createPostData(String statement, Map<String, Object> params) {
        Map postData = new LinkedHashMap();
        postData.put("statement", statement);
        postData.put("parameters", params == null ? Collections.EMPTY_MAP : params);
        return Collections.singletonMap("statements",Arrays.asList(postData));
    }

    public static String toJson(Map postData) {
        try {
            return OBJECT_MAPPER.writeValueAsString(postData);
        } catch (IOException e) {
            throw new RuntimeException("Can't convert " + postData + " to json", e);
        }
    }

    // :POST /db/data/transaction/commit {"statements":[{"statement":"return {n} as n","parameters":{"n":42}}]}
    // {"results":[{"columns":["n"],"data":[{"row":[42]}]}],"errors":[]}
    @SuppressWarnings("unchecked")
    public static Iterator<Map<String,Object>> toResult(int status, String resultString) {
        if (status != 200) throw new IllegalStateException("Response status " + status + "\n" + resultString);
        Map map = toMap(resultString);

        List<Map> results = (List<Map>) map.get("results");
        if (results.isEmpty()) return Collections.emptyIterator();
        Map result = results.get(0);
        final List<String> columns = (List<String>) result.get("columns");
        List rows = (List) result.get("data");
        return new IteratorWrapper<Map<String, Object>,Map<String, List<Object>>>(rows.iterator()) {
            @Override
            protected Map<String, Object> underlyingObjectToObject(Map<String, List<Object>> entry) {
                Map<String, Object> result = new LinkedHashMap<>();
                List<Object> row = entry.get("row");
                for (int i = 0; i < columns.size(); i++) {
                    result.put(columns.get(i),row.get(i));
                }
                return result;
            }
        };
    }

    private static Map toMap(String value) {
        try {
            return OBJECT_MAPPER.readValue(value, Map.class);
        } catch (IOException e) {
            throw new RuntimeException("Can't deserialize to Map:\n"+value,e);
        }
    }

    public static int getWebPort() {
        String webPort = System.getenv("PORT");
        if(webPort == null || webPort.isEmpty()) {
            return 8080;
        }
        return Integer.parseInt(webPort);
    }

    public static String getNeo4jUrl() {
        String urlVar = System.getenv("NEO4J_URL");
        if (urlVar==null) urlVar = "NEO4J_REST_URL";
        String url =  System.getenv(urlVar);
        if(url == null || url.isEmpty()) {
            return DEFAULT_URL;
        }
        return url;
    }
}

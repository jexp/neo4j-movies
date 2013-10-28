package org.neo4j.example.executor;

import org.neo4j.example.executor.CypherExecutor;
import org.neo4j.rest.graphdb.RestAPI;
import org.neo4j.rest.graphdb.RestAPIFacade;
import org.neo4j.rest.graphdb.util.DefaultConverter;

import java.util.Iterator;
import java.util.Map;

/**
 * @author Michael Hunger @since 22.10.13
 */
public class RestApiCypherExecutor implements CypherExecutor {

    private final RestAPI restAPI;

    public RestApiCypherExecutor(String url, String user, String password) {
        restAPI = new RestAPIFacade(url+"/db/data/", user, password);
    }
    public RestApiCypherExecutor(String url) {
        restAPI = new RestAPIFacade(url+"/db/data/");
    }

    @Override
    public Iterator<Map<String, Object>> query(String statement, Map<String, Object> params) {
        return restAPI.query(statement,params, new DefaultConverter()).iterator();
    }
}

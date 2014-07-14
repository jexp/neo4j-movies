package org.neo4j.example.movies;

import org.neo4j.example.executor.CypherExecutor;
import org.neo4j.example.executor.JavaLiteCypherExecutor;
import org.neo4j.example.executor.JdbcCypherExecutor;
import org.neo4j.example.executor.RestApiCypherExecutor;
import org.neo4j.helpers.Pair;
import org.neo4j.helpers.collection.IteratorUtil;
import org.neo4j.rest.graphdb.RestAPI;
import org.neo4j.rest.graphdb.RestAPIFacade;
import org.neo4j.rest.graphdb.query.RestCypherQueryEngine;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

import static org.neo4j.helpers.collection.MapUtil.map;

/**
 * @author mh
 * @since 30.05.12
 */
public class MovieService {

    private final CypherExecutor cypher;

    public MovieService(String uri) {
        cypher = createCypherExecutor(uri);
    }

    private CypherExecutor createCypherExecutor(String uri) {
        URL url = null;
        try {
            url = new URL(uri);
            String auth = url.getUserInfo();
            if (auth != null) {
                String[] parts = auth.split(":");
                return new RestApiCypherExecutor(uri, parts[0], parts[1]);
            }
            return new JdbcCypherExecutor(uri);
//        return new JavaLiteCypherExecutor(uri);
//        return new RestApiCypherExecutor(uri);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid Neo4j-ServerURL " + uri);
        }
    }

    public Map findMovie(String id) {
        if (id==null) return Collections.emptyMap();
        return IteratorUtil.singleOrNull(cypher.query(
                "MATCH (movie:Movie)<-[:ACTED_IN]-(actor)\n" +
                        " WHERE movie.id = {1}\n" +
                        " WITH movie, collect(actor.name) as cast\n" +
                        " RETURN { title: movie.title, cast: cast} as movie",
                map("1", id)));
    }

    @SuppressWarnings("unchecked")
    public Iterable<Map<String,Object>> search(String query) {
        if (query==null || query.trim().isEmpty()) return Collections.emptyList();
        return IteratorUtil.asCollection(cypher.query(
                "MATCH (movie:Movie)<-[:ACTED_IN]-(actor)\n" +
                    " WHERE movie.title =~ {1}\n" +
                    " WITH movie, collect(actor.name) as cast\n" +
                    " RETURN { id: movie.id, title: movie.title, cast: cast} as movie",
                map("1", "(?i).*"+query+".*")));
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> graph(int limit) {
        Iterator<Map<String,Object>> result = cypher.query(
                "MATCH (m:Movie)<-[:ACTED_IN]-(a:Person) " +
                " RETURN m.title as movie, collect(a.name) as cast " +
                " LIMIT {1}", map("1",limit));
        List nodes = new ArrayList();
        List rels= new ArrayList();
        int i=0;
        while (result.hasNext()) {
            Map<String, Object> row = result.next();
            nodes.add(map("title",row.get("movie"),"label","movie"));
            int target=i;
            i++;
            for (Object name : (Collection) row.get("cast")) {
                Map<String, Object> actor = map("title", name,"label","actor");
                int source = nodes.indexOf(actor);
                if (source == -1) {
                    nodes.add(actor);
                    source = i++;
                }
                rels.add(map("source",source,"target",target));
            }
        }
        return map("nodes", nodes, "links", rels);
    }
}

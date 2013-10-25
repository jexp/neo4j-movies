package org.neo4j.example.executor;

import org.javalite.http.Http;
import org.javalite.http.Post;
import org.neo4j.example.util.Util;

import java.util.Iterator;
import java.util.Map;

/**
 * @author Michael Hunger @since 22.10.13
 */
public class JavaLiteCypherExecutor implements CypherExecutor {
    private final String uri;

    public JavaLiteCypherExecutor(String uri) {
        this.uri = Util.toCypherUri(uri);
    }
    @Override
    public Iterator<Map<String, Object>> query(String statement, Map<String, Object> params) {
        String content = Util.toJson(Util.createPostData(statement, params));
        Post post = Http.post(uri, content).header("accept","application/json").header("content-type","application/json").header("X-Stream","true");
        return Util.toResult(post.responseCode(),post.text());
    }
}

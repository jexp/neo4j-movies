package org.neo4j.example.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.ResponseTransformerRoute;

/**
 * @author Michael Hunger @since 25.10.13
 */
public abstract class JsonTransformerRoute extends ResponseTransformerRoute {

    private Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    public JsonTransformerRoute(String path) {
        super(path, "application/json");
    }

    @Override
    public String render(Object model) {
        return gson.toJson(model);
    }

}

package org.neo4j.example.movies;

import static org.neo4j.helpers.collection.MapUtil.map;
import static spark.Spark.get;

import org.neo4j.example.util.JsonTransformerRoute;
import org.slf4j.Logger;

import spark.Request;
import spark.Response;
import spark.servlet.SparkApplication;

public class MovieApplication implements SparkApplication {

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(MovieApplication.class);
    private MovieService service;

    public MovieApplication(MovieService service) {
        this.service = service;
    }

    @Override
    public void init() {
        get(new JsonTransformerRoute("/movie/:id") {
            public Object handle(Request request, Response response) {
                return service.findMovie(request.params("id"));
            }
        });
        get(new JsonTransformerRoute("/search") {
            @Override
            public Object handle(Request request, Response response) {
                return service.search(request.queryParams("q"));
            }
        });
        get(new JsonTransformerRoute("/graph") {
            @Override
            public Object handle(Request request, Response response) {
                int limit = request.queryParams("limit") != null ? Integer.valueOf(request.queryParams("limit")) : 100;
                return service.graph(limit);
            }
        });
    }
}

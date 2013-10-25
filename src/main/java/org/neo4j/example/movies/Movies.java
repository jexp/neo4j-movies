package org.neo4j.example.movies;

import org.neo4j.example.util.Util;
import org.slf4j.Logger;

import static spark.Spark.externalStaticFileLocation;
import static spark.Spark.setPort;

/**
 * @author Michael Hunger @since 22.10.13
 */
public class Movies {

    public static void main(String[] args) {
        setPort(Util.getWebPort());
        externalStaticFileLocation("src/main/webapp");
        final MovieService service = new MovieService(Util.getNeo4jUrl());
        new MovieApplication(service).init();
    }
}

# Neo4j Movies Demo Application
    
Heroku App: http://neo4j-movies.herokuapp.com


### Endpoints:

Get Movie
````
// JSON object for single movie
curl http://neo4j-movies.herokuapp.com/movie/10

// list of JSON objects for movie search results
curl http://neo4j-movies.herokuapp.com/search?q=matrix

// JSON object for whole graph viz (nodes, links - arrays)
curl http://neo4j-movies.herokuapp.com/graph

### Run locally:

Start local Neo4j Server

````
mvn exec:java
````

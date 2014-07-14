# Neo4j Movies Demo Application
    
Heroku App: http://neo4j-movies.herokuapp.com

This is a minimalistic Graph Application using [Neo4j](http://neo4j.org), [Spark-Java](http://www.sparkjava.com/) and [d3.js](http://d3js.org/).

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

Start your local Neo4j Server ([Download & Install](http://neo4j.org/download)), open the [Neo4j Browser](http://localhost:7474).
Then install the Movies dataset with `:play movies`, click and hit the triangular "Run" button.

Then run this setup query to generate some id's on the movies and actor nodes.

````
CREATE (n {id:0}) WITH n
MATCH (m)
WHERE m <> n
SET m.id = n.id SET n.id = n.id + 1 WITH n,count(*) as updates
DELETE n RETURN updates
````

Start the Application with:

````
mvn exec:java
````

Go to http://localhost:8080

You can search for movies by title or hit the "Viz" tab for some basic graph visualization.



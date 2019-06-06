# SEARCH ENGINE

### HOW TO RUN IT

Before running, please check if you have Java installed. Then, execute this command in your terminal:

    mvn clean install spring-boot:run

### HOW IT WORKS

All documents are stored in non-persistent repository, which means data is lost after stopping the server. 

Basic operations are provided in form of REST API. Support operations:

- `add new Document`
- `add list of Documents`
- `get list of all Documents`
- `search for a given word in the inverted index`

####COMMANDS EXAMPLES
You need a running application to execute commands.

To add a new document with text _"the red fox bit the lazy dog"_ following command has to be executed:    
    
    curl -d '"the red fox bit the lazy dog"' -H 'Content-Type: application/json' http://localhost:8080/api/documents/new

To add a list of documents _["the brown fox jumped over the brown dog", "the lazy brown dog sat in the corner", "the red fox bit the lazy dog"]_ following command has to be executed:

    curl -d '["the brown fox jumped over the brown dog", "the lazy brown dog sat in the corner", "the red fox bit the lazy dog"]' -H 'Content-Type: application/json' http://localhost:8080/api/documents/

To get a list of stored documents, the following command has to be executed:

    curl -v http://localhost:8080/api/documents

To search for a word _"dog"_ following command has to be executed:

    curl -X GET 'http://localhost:8080/api/search?query=dog'

### TECH STACK

 - Java 8
 - Maven 3.6.0
 - Spring Boot 2.1.5.RELEASE
 
 
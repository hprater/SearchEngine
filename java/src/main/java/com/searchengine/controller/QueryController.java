package com.searchengine.controller;

import com.searchengine.indexer.IndexHelper;
import com.searchengine.indexer.Lexer;
import com.searchengine.model.Query;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QueryController {
    private final IndexHelper indexHelper;

    public QueryController(@NotNull IndexHelper indexHelper) {
        this.indexHelper = indexHelper;
    }

    @CrossOrigin
    @RequestMapping(path = "/question", method = RequestMethod.POST)
    private String runQuery(@Valid @RequestBody Query queryTerms) throws IOException {
        // Create an instance of the Query object
        Query query = new Query();

        // Call the lexer class and pass it the query object to access methods
        Lexer queryLexer = new Lexer(new StringReader(queryTerms.getQueryWords()), null, query);
        queryLexer.yylex();

        // Open files for processing
        indexHelper.openForUpdate();

        // Accumulator
        Map<Integer, Float> accumulator = query.processQuery(indexHelper);

        StringBuilder resultStringBuilder = new StringBuilder();
        // Menu Messages
        if (accumulator.isEmpty())
            resultStringBuilder.append(" *** Term(s) not in the Dictionary. Please try a different query. *** ");
        else {
            accumulator.entrySet().stream()
                    .sorted(Map.Entry.<Integer, Float>comparingByValue().reversed())
                    .limit(10)  // Limit to the top 10 results
                    .forEach(entry -> {
                        try {
                            // Grab the document name with the document ID.
                            String docName = indexHelper.readMapRecord(entry.getKey());
                            String[] docNameParts = docName.split("\\|");

                            // Append the result to the string builder
                            resultStringBuilder.append("DOC ID: ").append(entry.getKey())
                                    .append(" | DOC NAME: ").append(docNameParts[1].trim())
                                    .append(" | WEIGHT: ").append(entry.getValue())
                                    .append("\n");

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }

        return resultStringBuilder.toString();
    }
}

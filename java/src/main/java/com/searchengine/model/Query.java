package com.searchengine.model;
/*
Author: Hayden Prater
Date: Nov 5, 2023
Assignment: HW#4
Query Class - Houses all methods needed for Query Processing of the search engine
 */

import com.searchengine.indexer.IndexHelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Query {

    private int id;

    private boolean type;

    private String queryWords;
    private List<String> queryTokens;
    private Map<Integer, Float> accumulator;

    public Query(){
        this.queryTokens = new ArrayList<>();
    }

    public Query(int id, boolean type, String queryWords) {
        this.id = id;
        this.type = type;
        this.queryWords = queryWords;
        this.queryTokens = new ArrayList<>();
    }

    public Map<Integer, Float> processQuery(IndexHelper indexHelper) throws IOException {
        this.accumulator = new HashMap<>();

        for (String term : getQueryTokenizedTerms()) {

            // Find term in the dictionary file
            String[] record = indexHelper.findByTermInDictionary(term);

            if (record != null) {
                int numDoc = Integer.parseInt(record[1].trim());
                int start = Integer.parseInt(record[2].trim());

                for(int i = start; i < (numDoc + start); i++){
                    // Grab the docId and weight from the post record
                    String posting = indexHelper.readPostRecord(i);
                    String[] postingParts = posting.split("\\|");
                    int docId = Integer.parseInt(postingParts[0].trim());
                    float weight = Float.parseFloat(postingParts[1].trim());

                    // Add the weight to the accumulator for the document
                    accumulator.merge(docId, weight, Float::sum);
                }
            } else {
                System.out.println("Error: Term '" + term + "' not found in the dictionary.\n");
            }
        }
        return accumulator;
    }

    /**
     * Method that returns the results of query
     *
     * @param userInput
     * @param responses
     * @return
     * @throws IOException
     */

    public String getResponse(String userInput, Map<String, String> responses) throws IOException {

        // Loop through sentences.
        String category = "chat-info";
        System.out.println(userInput);
        // Get predefined answer from given category & add to answer.
        return " " + responses.get(category);
    }

    /**
     * Used in jflex to add the tokenized words to list
     *
     * @param token
     */
    public void addToTokenizedQueryList(String token) {
        queryTokens.add(token.toLowerCase());
    }

    /**
     * Getter for tokenized query words
     *
     * @return
     */
    public List<String> getQueryTokenizedTerms() {
        return queryTokens;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public void setQueryWords(String queryWords) {
        this.queryWords = queryWords;
    }

    public int getId() {
        return id;
    }

    public boolean isType() {
        return type;
    }

    public String getQueryWords() {
        return queryWords;
    }
}

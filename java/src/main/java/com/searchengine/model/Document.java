package com.searchengine.model;
/*
Author: Hayden Prater
Date: Oct 25, 2023
Assignment: HW#4
Document Class - Contains all methods that act on a Document.
 */


import java.util.Hashtable;

public class Document {
    // Used to assign document id to input file
    private int docId = 0;
    // Tracks all tokens in the document
    private int totalTokens = 0;
    // Document hashtable
    private final Hashtable<String, Float> documentHashtable = new Hashtable<>();
    // Stop List
    private Hashtable<String, Boolean> stopList;

    /**
     * Constructor -> fills stopList words on creation
     */
    public Document(){
        // Initialize the stopList with common filler words and additional high-frequency words
        stopList = new Hashtable<>();
        String[] fillerWords = {
                "the", "of", "and", "to", "in", "is", "or", "you", "that", "he",
                "was", "for", "on", "are", "as", "with", "his", "they", "at", "be",
                "this", "have", "from", "or", "one", "had", "by", "word", "but",
                "not", "what", "all", "were", "we", "when", "your", "can", "said",
                "there", "use", "an", "each", "which", "she", "do", "how", "their",
                "if", "will", "up", "other", "about", "out", "many", "then", "them",
                "these", "so", "some", "her", "would", "make", "like", "him", "into",
                "time", "has", "look", "two", "more", "write", "go", "see", "number",
                "no", "way", "could", "people", "my", "than", "first", "water", "been",
                "call", "who", "oil", "its", "now", "find", "long", "down", "day",
                "did", "get", "come", "made", "off", "may", "com", "edu", "e-mail",
                "email"
        };
        for (String filler : fillerWords) {
            stopList.put(filler.toLowerCase(), true);
        }
    }
    /**
     * Increments the total token value for file
     */
    public void incrementTotalTokens() {
        totalTokens++;
    }

    /**
     * Adds a token to the Hashtable
     * Houses the frequency for each token
     *
     * @param token
     */
    public void addToDocumentHashtable(String token) {
        documentHashtable.put(token.toLowerCase(), documentHashtable.getOrDefault(token.toLowerCase(), (float) 0) + 1);
    }

    /**
     * Calculates the RTF and replaces the TF with the RTF in the documentHashtable
     */
    public void calculateRelativeTermFreq(){
        for (String token : documentHashtable.keySet()) {

            // Get the term frequency (TF) for the token
            float termFrequency = documentHashtable.getOrDefault(token, 0.0f);

            // Calculate the RTF by dividing the TF by the total terms
            float RTF = Float.parseFloat(String.format("%.10f", termFrequency / getTotalTokens()));

            // Update the document hashtable with the calculated RTF
            documentHashtable.put(token, RTF);
        }
    }

    /**
     * Used in Jflex rules to eliminate single letter words and stop words.
     * StopList structure (word, true)
     * If key found (word) return value is true and jflex will not include the word
     *
     * @param word
     * @return
     */
    public boolean isStopOrSingleLetterWord(String word) {
        // Check if the word is in the stopList or has length 1
        return stopList.containsKey(word.toLowerCase()) || word.length() == 1;
    }

    /**
     * Getter for total tokens
     *
     * @return total tokens
     */
    public int getTotalTokens() {
        return totalTokens;
    }

    /**
     * Getter for Hashtable
     *
     * @return Hashtable
     */
    public Hashtable<String, Float> getDocumentHashtable() {
        return documentHashtable;
    }

    /**
     * Increment the docId
     */
    public void incrementDocId() {
        this.docId++;
    }

    /**
     * Getter for docId
     *
     * @return
     */
    public int getCurrentDocId() {
        return docId;

    }

    /**
     * Clears the content of the current document
     */
    public void clear(){
        totalTokens = 0;
        documentHashtable.clear();
    }

    /**
     * Prints total tokens and total number of unique tokens for each file
     * Debugging Function
     */
    public void printDocumentStats() {
        System.out.println("Number of tokens in current document: " + totalTokens + "\n");
        System.out.println("Number of unique tokens in current document: " + documentHashtable.size() + "\n");
        System.out.println("Frequency of tokens in current document: \n" + documentHashtable + "\n");
    }

}

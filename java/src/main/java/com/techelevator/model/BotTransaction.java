package com.techelevator.model;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;



public class BotTransaction {
    private int id;
    private boolean type;
    private String questionOrResponse;

    public BotTransaction(){}
    public BotTransaction(int id, boolean type, String questionOrResponse) {
        this.id = id;
        this.type = type;
        this.questionOrResponse = questionOrResponse;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public String getQuestionOrResponse() {
        return questionOrResponse;
    }

    public void setQuestionOrResponse(String questionOrResponse) {
        this.questionOrResponse = questionOrResponse;
    }

    public String getResponse(String userInput, Map<String, String> responses) throws IOException {

        // Loop through sentences.
        String category = "chat-info";
        // Get predefined answer from given category & add to answer.
        return " " + responses.get(category);
    }


}

package com.searchengine.model;
/*
Author: Hayden Prater
Date: Oct 25, 2023
Assignment: HW#4
Posting Class - Structure of a Posting
 */
public class Posting {

    private int docId;
    private float freq;

    /**
     * Constructor
     *
     * @param docId
     * @param freq
     */
    public Posting(int docId, float freq) {
        this.docId = docId;
        this.freq = freq;
    }

    /**
     * Getter docId
     * @return
     */
    public int getDocId() {
        return docId;
    }

    /**
     * Setter docId
     * @param docId
     */
    public void setDocId(int docId) {
        this.docId = docId;
    }

    /**
     * Getter freq
     * @return
     */
    public float getFreq() {
        return freq;
    }

    /**
     * Setter freq
     * @param freq
     */
    public void setFreq(float freq) {
        this.freq = freq;
    }
}

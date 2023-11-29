package com.searchengine.model;
/**
 * Filename: HashTabe.java
 * Author: Susan Gauch, converted to java by Matt Miller, debugged by Patrick Anderson.
 *
 * Altered by Hayden Prater
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class CustomHashTable
{
    private int size;
    private long used;
    private long collision;
    private long lookups;
    private Node[] hashtable;
    
    /**
     * Initializes a hashtable with size 3 times the size given.
     * @param size One third of the hashtable size;
     */
    public CustomHashTable(int size)
    {
        this.size=size*3;
        init();
    }
    
    /**
     * Method to be called by constructors.
     * Also an easy way to reset a already made hashtable.
     * Requires that size already be set.
     */
    public void init()
    {
        used=0;
        collision=0;
        lookups=0;
        hashtable=new Node[this.size];
		
			//initialize the hashtable
        for(int i=0;i<this.size;i++)
            hashtable[i]=new Node("",-1, new LinkedList<>());
    }
    
    /**
     * Prints the non empty contents of the hashtable to the file given.
     * @param filename String
     */
    public void print(String filename)
    {
        try
        {
            FileWriter fwriter=new FileWriter(filename);
            BufferedWriter out=new BufferedWriter(fwriter);
            
            for(int i=0;i<size;i++)
            {
                if(!hashtable[i].getTerm().equals("")) //if address is not empty string
                    out.write(hashtable[i].getTerm()+" "+hashtable[i].getNumDoc()+"\n");
            }
            out.close();
        }
        catch(IOException e)
        {
            System.err.println("IOEror: "+e.getMessage());
        } 
        
        System.out.println("Collisions: "+collision+" Used: "+used+" Lookups: "+lookups);
    }
    
    /**
     * Insert term, int numDoc, and LinkedList composed of Posting Objects into hashtable, hashes on term.
     * @param term String to be hashed.
     * @param numDoc int
     */
    // Need to add the Linked List
    public void insert(String term, int numDoc, LinkedList<Posting> posting)
    {
        int index = find(term);
        
			//if term not already in the table, insert it
        if(hashtable[index].getTerm().equals(""))
        {
            hashtable[index].setTerm(term);
            hashtable[index].setNumDoc(numDoc);
            hashtable[index].setPostings(posting);
            used++;
        }
			//else do nothing
    }
    
    /**
     * Returns the index of the word in the table, or the index of a free space
     * in the table.
     * @param str String to hash.
     * @return index of the word, or of free space in which to place the word.
     */
    public int find(String str)
    {
        long sum=0;
        long index;
        
			//add all the characters of the string together
        for(int i=0;i<str.length();i++)
            sum=(sum*19)+str.charAt(i); //multiply sum by 19 and add byte value of char
        
		if(sum < 0)				// If calculation of sum was negative, make it positive
			sum = sum * -1;
		
        index= sum%size;
        int index2 = (int) index;
        
        /*
         * check to see if the word is in that location
         * if not there, do linear probing until word is found\
         * or empty location found
         */
        while(!hashtable[index2].getTerm().equals(str) && !hashtable[index2].getTerm().isEmpty())
        {
            index2++;
            collision++;
            if(index2 >= size)
                index2 = 0;
        }
        
        return index2;
    }

    /**
     * Get the entry based on the term
     *
     * @param term
     * @return
     */
    public Node getEntryByTerm(String term) {
        int index = find(term);

        // Check if the term exists in the hashtable
        if (hashtable[index].getTerm().equals(term)) {
            return hashtable[index];
        }

        // Return null if the term doesn't exist
        return null;
    }
    
    /**
     * Returns the numDoc at the hashed location of term.
     * @param term String to be hashed.
     * @return numDoc in the table at the location of term.
     */
    public int getNumDoc(String term)
    {
        int index=find(term);
        lookups++;
        return hashtable[index].getNumDoc();
    }
    
    /**
     * Get the three statistics as a string.  Used, Collisions, and Lookups.
     * @return Used, Collisions, and Lookups as a string.
     */
    public String getUsage()
    {
        return "Used: "+used+" Collisions: "+collision+" Lookups: "+lookups;
    }
    
    /**
     * Get the amount in the table.
     * @return How full the table is. long
     */
    public long getUsed()
    {
        return used;
    }
    
    /**
     * Get the number of collisions.
     * @return How much you need to improve your hash function. long
     */
    public long getCollisions()
    {
        return collision;
    }
    
    /**
     * The number of lookups made.
     * @return long
     */
    public long getLookups()
    {
        return lookups;
    }
    
    /**
     * Gets the size of the array.
     * @return size, long
     */
    public int getSize()
    {
        return size;
    }
    
    /**
     * Returns Node at location index.
     * @param index location in hashtable
     * @return Node at location index.
     */
    public Node getNode(int index)
    {
        return hashtable[index];
    }
    
    
    /**
     * Private class node to whole the actual data stored in the hashtable.
     * Provides standard accessor and mutator methods.
     */
    public static class Node
    {
        private String term;
        private int numDoc = 0;

        private LinkedList<Posting> postings;
        
        public Node(String term, int numDoc, LinkedList<Posting> postings)
        {
            this.term=term;
            this.numDoc=numDoc;
            this.postings = postings;

        }
        
        public String getTerm()
        {
            return term;
        }
        
        public int getNumDoc()
        {
            return numDoc;
        }
        
        public void setTerm(String term)
        {
            this.term=term;
        }
        
        public void setNumDoc(int numDoc)
        {
            this.numDoc=numDoc;
        }

        public LinkedList<Posting> getPostings() {
            return postings;
        }

        public void setPostings(LinkedList<Posting> postings) {
            this.postings = postings;
        }

        public void incrementNumDoc() {
            numDoc++;
        }
    }
}

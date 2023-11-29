package com.searchengine.indexer;
/*
Author: Hayden Prater
Date: Oct 25, 2023
Assignment: HW#4
InvertedFile  Class -  Builds a trio of files, (dict, post, and map) that have fixed length records in them
 */


import com.searchengine.model.CustomHashTable;
import com.searchengine.model.DataFiles;
import com.searchengine.model.Posting;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;


public class IndexHelper {
    //constant int for size of map record
    private static final int mapRecordSize = 19;
    private static final int mapDocIdSize = 5;
    private static final int mapFileNameSize = 12;

    //constant int for size of dict record
    private static final int dictRecordSize = 30;
    private static final int dictTermSize = 14;
    private static final int dictNumDocSize = 6;
    private static final int dictStartSize = 7;

    //constant int for size of post record
    private static final int postRecordSize = 13;
    private static final int postDocIdSize = 5;
    private static final int postWeightSize = 6;

    //number of records in the map file
    private int numRecordsMapFile;
    //number of records in the dict file
    private int numRecordsDictFile;
    //number of records in the post file
    private int numRecordsPostFile;

    //constant string's holding the filenames
    private final String configFileName = "config.txt";
    private final String dictFileName = "dict.dat";
    private final String postFileName = "post.dat";
    private final String mapFileName = "map.dat";

    private File outputDirectory;
    private DataFiles dataFiles;
    // Global hashtable (25000) or 15
    private CustomHashTable globalHashTable = new CustomHashTable(25000);


    /**
     * Constructor
     * Sets the streams to NULL
     * sets num records to -1 for dict, post, and map
     *
     * @param dataFiles
     */
    public IndexHelper(DataFiles dataFiles) throws IOException {
        this.numRecordsMapFile = -1;
        this.numRecordsDictFile = -1;
        this.numRecordsPostFile = -1;
        this.dataFiles = dataFiles;

        // Create the output directory if it doesn't exist
        outputDirectory = Files.createTempDirectory("output").toFile();
        if (!outputDirectory.exists() && !outputDirectory.mkdirs()) {
            System.err.println("Failed to create the output directory.");
            System.exit(1);
        }
    }

    /**
     * Sets num records for post and map to 0
     * removes any old post and map files
     * sets num records for dict to the parameter passed in
     * opens dict file for writing
     * write dictFileSize empty records to the file in sequence
     * closes dict file
     * creates config file and writes num records for each of the 3 files to it
     * creates an empty map and post file
     *
     * @param dictRecordSize
     */
    public void create(final int dictRecordSize) throws IOException {
        // Set num records
        numRecordsMapFile = 0;
        numRecordsDictFile = dictRecordSize;
        numRecordsPostFile = 0;

        // Remove any old files in the output directory
        deleteFilesInDirectory(outputDirectory);

        // Open dict file for writing
        dataFiles.setDict(new RandomAccessFile(outputDirectory + File.separator + dictFileName, "rw"));

        // Write dictFileSize empty records to the file in sequence
        for (int i = 0; i < dictRecordSize; i++) {
            writeDictRecord("empty", -1, -1);
            //globalHashTable.put("empty", new LinkedList<>()); //(IDK if we have to initialize the hashTable to empty records)
        }

        // Create config file and write num records for each of the 3 files to it
        dataFiles.setConfig(new File(outputDirectory + File.separator + configFileName));
        try (BufferedWriter configWriter = new BufferedWriter(new FileWriter(dataFiles.getConfig().getAbsolutePath()))) {
            configWriter.write("numRecordsMapFile=" + numRecordsMapFile + "\n");
            configWriter.write("numRecordsDictFile=" + numRecordsDictFile + "\n");
            configWriter.write("numRecordsPostFile=" + numRecordsPostFile + "\n");
            configWriter.write("outputDirectory=" + outputDirectory + "\n");
        }

        // Create empty map and post files in the output directory
        dataFiles.setMap(new RandomAccessFile(Files.createFile(Paths.get(outputDirectory + File.separator + mapFileName)).toString(), "rw"));
        dataFiles.setPost(new RandomAccessFile(Files.createFile(Paths.get(outputDirectory + File.separator + postFileName)).toString(), "rw"));
    }

    /**
     * Removes any old files
     *
     * @param dirToClean
     */
    private void deleteFilesInDirectory(File dirToClean) throws IOException {
        File[] files = dirToClean.listFiles();
        assert files != null;
        for (File file : files) {
            if (!file.delete()) {
                System.out.printf("Unable to delete %s%n", file.getAbsolutePath());
            }
        }
    }

    /**
     * Opens an existing inverted file
     * opens the configuration file
     * reads from it to set the number of records variables for the map, dict, and post files
     * closes the config file
     * opens the map and post and dict in read/write mode and sets the stream variables
     *
     * @throws IOException
     */
    public void openForUpdate() throws IOException {
        try (BufferedReader configReader = new BufferedReader(new FileReader(dataFiles.getConfig().getAbsolutePath()))) {
            // Using Java Stream to read from config file to set the number of records variables for the map, dict, and post files
            Map<String, String> configMap = configReader.lines()
                    .map(line -> line.split("="))
                    .filter(substring -> substring.length == 2)
                    .collect(Collectors.toMap(
                            substring -> substring[0].trim(),
                            substring -> substring[1].trim()
                    ));

            numRecordsMapFile = Integer.parseInt(configMap.getOrDefault("numRecordsMapFile", "0"));
            numRecordsDictFile = Integer.parseInt(configMap.getOrDefault("numRecordsDictFile", "0"));
            numRecordsPostFile = Integer.parseInt(configMap.getOrDefault("numRecordsPostFile", "0"));
        }

    }

    /**
     * Opens the configuration file
     * writes the number of records variables for the map, dict, and post files
     * closes the config file
     * closes the dict, post, and map files
     * sets num records to -1 for dict post and map
     * sets the streams to NULL
     *
     * @throws IOException
     */
    public void close() throws IOException {
        // Open the configuration file for writing
        try (BufferedWriter configWriter = new BufferedWriter(new FileWriter(dataFiles.getConfig().getAbsolutePath()))) {
            // Write the updated number of records to the config file
            configWriter.write("numRecordsMapFile=" + numRecordsMapFile + "\n");
            configWriter.write("numRecordsDictFile=" + numRecordsDictFile + "\n");
            configWriter.write("numRecordsPostFile=" + numRecordsPostFile + "\n");
            configWriter.write("outputDirectory=" + outputDirectory + "\n");
        }


        // Close the dictionary, post, and map files and set num records to -1
        dataFiles.getDict().close();
        numRecordsDictFile = -1;


        dataFiles.getPost().close();
        numRecordsPostFile = -1;


        dataFiles.getMap().close();
        numRecordsMapFile = -1;
    }

    /**
     * Update the global hash table with the term and frequencies from the document
     *
     * @param docHashTable
     */
    public void updateGlobalHashTable(Hashtable<String, Float> docHashTable, int docId) {
        for (Map.Entry<String, Float> entry : docHashTable.entrySet()) {
            String term = entry.getKey();
            float rtf = entry.getValue();

            // Find the HashTable.Node for the term
            CustomHashTable.Node entryData = globalHashTable.getEntryByTerm(term);

            // If term does not exist, add an entry to the global hashtable
            if (entryData == null) {
                // Insert new entry
                globalHashTable.insert(term, 0, new LinkedList<>());
                // Assign the new created entry
                entryData = globalHashTable.getEntryByTerm(term);
            }

            // Increment numDoc for this term
            entryData.incrementNumDoc();

            // Create a new posting
            Posting posting = new Posting(docId, rtf);

            // Add the posting to the entry postings list
            entryData.getPostings().add(posting);
        }
    }

    /**
     * Writes the post and dict file
     * Done in same method to keep track of "Start" position shared amongst the two
     * NOTE:
     * RTF = (term_freq_in_doc/total_terms_in_doc) <-- Already stored in documentHashtable
     * IDF = (log(N/n)+1) N = total doc in collection, n = num of docs that contain the term
     * weight = RTF * IDF
     */
    public void writeDictAndPostRecords(int totalDoc) {
        int recordNum = 0;
        int priorNum = 0;
        int currentPosition = 0;
        for (int i = 0; i < globalHashTable.getSize(); i++) {
            CustomHashTable.Node node = globalHashTable.getNode(i);
            String term = node.getTerm();
            LinkedList<Posting> postings = node.getPostings();

            // Skips Low Frequency Words and Empty records when writing to dict file
            if (term.isEmpty() || node.getNumDoc() == 1) {
                // Still increment the record number
                recordNum++;
                continue;
            }

            // Calculate starting position based on currentPosition for the dictionary insert
            int startPosition = currentPosition;

            for (Posting posting : postings) {
                int docId = posting.getDocId();
                // Method call to calculate the weight & truncate before writing to posting
                float weight = getTruncatedWeight(posting, node, totalDoc);

                // Write to postings
                writePostRecord(docId, weight);
                currentPosition++;
            }

            // Truncates the dict term size and ensures fixed length records
            if (term.length() > dictTermSize) {
                term = term.substring(0, dictTermSize);
            }

            // Checks to see if the last insert to posting was inserted right before current posting insert (Determines method used)
            if ((recordNum - 1) == priorNum)
                // Use normal method when records are inserted right after each other
                writeDictRecord(term, node.getNumDoc(), startPosition);
            else
                // Use offset method to insert at correct spot
                writeDictRecord(recordNum, term, node.getNumDoc(), startPosition);

            priorNum = recordNum;
            recordNum++; // Increment recordNum for the next term
        }
    }

    /**
     * Calculates the weight and truncates its value to ensure records are fixed length
     *
     * @param posting
     * @param node
     * @param totalDoc
     * @return
     */
    private float getTruncatedWeight(Posting posting, CustomHashTable.Node node, int totalDoc) {
        // Retrieve RTF from posting
        float RTF = posting.getFreq();

        // Calculate IDK
        float IDF = (float) (Math.log10((double) totalDoc / node.getNumDoc()) + 1);

        // Calculate Weight
        float weight = (RTF * IDF);

        // This is for getTruncatedWeight() -> Setup for checking calculation
//        System.out.println("DocID: " +  posting.getDocId() + " | " + "Term: " + node.getTerm() + " | " + "RTF " + RTF +
//                " | " + "TotalDoc: " + totalDoc  + " | "  + " NumDoc: " + node.getNumDoc() + " | " + " IDF: " + IDF +
//                " | " + " weight: " + weight);

        // Calculate a factor to round to the desired decimal places
        float factor = (float) Math.pow(10, (postWeightSize - 2)); // -2 to account for: leading 0, decimal point

        // Truncate the float to the specified decimal places (To ensure fixed length)
        return (float) (Math.floor(weight * factor) / factor);
    }

    /**
     * Writes a formatted record in the current write location
     *
     * @param term
     * @param numDocs
     * @param start
     */
    public void writeDictRecord(String term, int numDocs, int start) {
        // Formatted Record
        String recordFormatted = String.format("%-" + dictTermSize + "s|%" +
                dictNumDocSize + "s|%" + dictStartSize + "s\n", term, numDocs, start);

        // Write at current head
        try {
            dataFiles.getDict().writeBytes(recordFormatted);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Writes a formatted record in the given location
     *
     * @param recordNum
     * @param term
     * @param numDocs
     * @param start
     */
    public void writeDictRecord(int recordNum, String term, int numDocs, int start) {
        // Offset
        long offset = recordNum * (long) dictRecordSize;

        // Formatted Record
        String recordFormatted = String.format("%-" + dictTermSize + "s|%" +
                dictNumDocSize + "s|%" + dictStartSize + "s\n", term, numDocs, start);

        try {
            // Move to the write head
            dataFiles.getDict().seek(offset);
            // Write the record
            dataFiles.getDict().writeBytes(recordFormatted);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Writes a formatted record at the current write location
     *
     * @param docId
     * @param filename
     */
    public void writeMapRecord(int docId, String filename) {

        String recordFormatted = String.format("%-" + mapDocIdSize + "s|%" +
                mapFileNameSize + "s\n", docId, filename);

        try {
            dataFiles.getMap().writeBytes(recordFormatted);
            numRecordsMapFile++;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Writes a formatted record at the current write location
     *
     * @param docId
     * @param freq
     */
    public void writePostRecord(int docId, float freq) {
        String recordFormatted = String.format("%-" + postDocIdSize + "s|%" +
                postWeightSize + "s\n", docId, freq);

        try {
            dataFiles.getPost().writeBytes(recordFormatted);
            numRecordsPostFile++;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads the record at location recordNum and fills in/returns the information
     *
     * @param recordNum
     */
    public String readMapRecord(int recordNum) throws IOException {
        if (recordNum >= 0) {
            try {
                // Calculate the offset for the given recordNum
                long offset = (long) recordNum * mapRecordSize;
                // Seek to the specified position in the map file
                dataFiles.getMap().seek(offset);
                // Return the record un-split (Print method will format)
                return dataFiles.getMap().readLine();
            } catch (NullPointerException e) {
                System.out.println("Please open the file before trying to read map records\n");
            }
        }
        return null;
    }

    /**
     * Reads the record at location recordNum and fills in/returns the information
     *
     * @param recordNum
     */
    public String readDictRecord(int recordNum) throws IOException {
        if (recordNum >= 0) {
            try {
                // Calculate the offset for the given recordNum
                long offset = (long) recordNum * dictRecordSize;
                // Seek to the specified position in the map file
                dataFiles.getDict().seek(offset);
                /// Return the record un-split (Print method will format)
                return dataFiles.getDict().readLine();
            } catch (NullPointerException e) {
                System.out.println("Please open the file before trying to read dictionary records\n");
            }
        }
        return null;
    }

    /**
     * Reads the record at location recordNum and fills in/returns the information
     *
     * @param recordNum
     */
    public String readPostRecord(int recordNum) throws IOException {
        if (recordNum >= 0) {
            try {
                // Calculate the offset for the given recordNum
                long offset = (long) recordNum * postRecordSize;
                // Seek to the specified position in the map file
                dataFiles.getPost().seek(offset);
                // Return the record un-split (Print method will format)
                return dataFiles.getPost().readLine();
            } catch (NullPointerException e) {
                System.out.println("Please open the file before trying to read post records\n");
            }
        }
        return null;
    }

    /**
     * Finds the recordNum for the given String term
     *
     * @param term
     * @return String[]
     * @throws IOException
     */
    public String[] findByTermInDictionary(String term) throws IOException {
        long sum = 0;
        long index;

        //add all the characters of the string together
        for (int i = 0; i < term.length(); i++)
            //multiply sum by 19 and add byte value of char
            sum = (sum * 19) + term.charAt(i);

        // If calculation of sum was negative, make it positive
        if (sum < 0)
            sum = sum * -1;

        index = sum % numRecordsDictFile;
        int recordNum = (int) index;

        // Iterate through dictionary records
        while (true) {
            String record = readDictRecord(recordNum);

            if (record == null) {
                // End of file reached, term not found
                return null;
            }

            // Split the record
            String[] parts = record.split("\\|");
            String postingTerm = parts[0].trim();

            if (postingTerm.length() > dictTermSize) {
                term = term.substring(0, dictTermSize);
            }

            if (parts.length > 1 && postingTerm.equals(term)) {
                // Term found, return the record number
                return parts;
            }

            // Move to the next record
            recordNum++;
        }
    }



    /**
     * Displays the contents of the globalHashTable & data needed for dict / post files
     * Debugging Function
     */
    public void displayGlobalHashTable() {
        for (int i = 0; i < globalHashTable.getSize(); i++) {
            CustomHashTable.Node entry = globalHashTable.getNode(i);
            String term = entry.getTerm();
            LinkedList<Posting> postings = entry.getPostings(); //This needs all the postings for the record
            System.out.println(postings.size());

            System.out.print("Term: " + term + " = ");
            for (Posting posting : postings) {
                int docId = posting.getDocId();
                double tf = posting.getFreq();
                System.out.print("[" + docId + ", " + tf + "," + "] ");
            }
            System.out.println("\nNumDoc=" + entry.getNumDoc() + "\n");
        }
    }
}
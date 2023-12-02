package com.searchengine.indexer;

import com.searchengine.model.DataFiles;
import com.searchengine.model.Document;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

@Component
public class Indexer {
    private final IndexHelper indexHelper;

    public Indexer(IndexHelper indexHelper) {
        this.indexHelper = indexHelper;
    }

    /**
     * Handle Indexing - Runs once after application startup
     *
     * @throws IOException
     */
    @EventListener(ApplicationReadyEvent.class)
    private void runIndexing() throws IOException {

        // Create the files and initialize the dictionary file size 75000 or 45
        indexHelper.create(75000);
        // Open files for processing
        indexHelper.openForUpdate();

        // Grab the files in the specified directory and put into array for processing
        File processDir = ResourceUtils.getFile("classpath:static/files/");
        File[] files = processDir.listFiles();

        // Document Object
        Document document = new Document();

        // Takes the user specified directory and iterates over all files in it.
        for (File inputFile : Objects.requireNonNull(files)) {
            if (inputFile.isFile() && inputFile.getName().endsWith(".html")) {

                // Write document ID and filename to map file
                indexHelper.writeMapRecord(document.getCurrentDocId(), inputFile.getName());

                // Call the lexer class and pass it the document object to access methods
                Lexer lexer = new Lexer(new FileReader(inputFile), document, null);
                lexer.yylex();

                // Updates the document hashtable tf with rtf values
                document.calculateRelativeTermFreq();

                // Update the global hash table with the document's term frequencies (term={docId=frequency, ect.}
                indexHelper.updateGlobalHashTable(document.getDocumentHashtable(), document.getCurrentDocId());

                // Increment the Document ID for processing of the next document
                document.incrementDocId();

                //Clear the contents of the previous document for processing of the next.
                document.clear();
            }
        }
        // Write content to the dict and post file.
        indexHelper.writeDictAndPostRecords(document.getCurrentDocId());

    }
}

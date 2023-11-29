package com.searchengine.config;

import com.searchengine.indexer.IndexHelper;
import com.searchengine.model.DataFiles;
import java.io.IOException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Bean
    public DataFiles dataFiles(){
        return new DataFiles();
    }

    @Bean
    public IndexHelper indexHelper(DataFiles dataFiles) throws IOException {
        return new IndexHelper(dataFiles);
    }
}

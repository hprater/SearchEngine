package com.searchengine.model;

import java.io.File;
import java.io.RandomAccessFile;


public class DataFiles {
    private RandomAccessFile dict;
    private RandomAccessFile post;
    private RandomAccessFile map;
    private File config;

    public RandomAccessFile getDict() {
        return dict;
    }

    public void setDict(RandomAccessFile dict) {
        this.dict = dict;
    }

    public RandomAccessFile getPost() {
        return post;
    }

    public void setPost(RandomAccessFile post) {
        this.post = post;
    }

    public RandomAccessFile getMap() {
        return map;
    }

    public void setMap(RandomAccessFile map) {
        this.map = map;
    }

    public File getConfig() {
        return config;
    }

    public void setConfig(File config) {
        this.config = config;
    }
}

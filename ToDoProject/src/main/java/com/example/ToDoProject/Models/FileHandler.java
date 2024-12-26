package com.example.ToDoProject.Models;

import java.util.List;

public abstract class FileHandler<T> {
    protected String filepath;

    public FileHandler(String filepath) {
        this.filepath = filepath;
    }

    public abstract void saveToFile(List<T> items);

    public abstract List<T> readFromFile();
}

package com.example.ToDoProject.Models;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ToDoFileHandler extends FileHandler<ToDo> {

    public ToDoFileHandler(String filepath) {
        super(filepath);
    }

    @Override
    public void saveToFile(List<ToDo> items) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filepath))) {
            oos.writeObject(items);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save tasks to file: " + filepath, e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ToDo> readFromFile() {
        File file = new File(filepath);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filepath))) {
            Object obj = ois.readObject();
            if (obj instanceof List<?>) {
                List<?> tempList = (List<?>) obj;
                if (!tempList.isEmpty() && !(tempList.get(0) instanceof ToDo)) {
                    throw new IOException("Data in file is not a valid List<ToDo>");
                }
                return (List<ToDo>) tempList;
            } else {
                throw new IOException("Data in file is not a valid List<ToDo>");
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to read tasks from file: " + filepath, e);
        }
    }
}

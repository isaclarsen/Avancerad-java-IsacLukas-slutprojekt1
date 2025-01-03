package com.example.ToDoProject.Models;

import java.io.Serializable;

public class ToDo implements Serializable {
    private int id;
    private String title;
    private String description;

    public ToDo(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }
    //Getter för id
    public int getId(){
        return id;
    }

    //Setter för id
    public void setId(int id){
        this.id = id;
    }

    //Getter för title
    public String getTitle() {
        return title;
    }

    //Setter för title
    public void setTitle(String title) {
        this.title = title;
    }

    //Getter för description
    public String getDescription() {
        return description;
    }

    //Setter för description
    public void setDescription(String description) {
        this.description = description;
    }
}

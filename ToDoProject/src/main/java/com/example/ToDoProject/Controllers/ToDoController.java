package com.example.ToDoProject.Controllers;

import com.example.ToDoProject.Models.FileHandler;
import com.example.ToDoProject.Models.ToDo;
import com.example.ToDoProject.Models.ToDoFileHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")

public class ToDoController {
    //Lägg in datastrukut (Arraylist tror jag)
    private List<ToDo> toDos = new ArrayList<>();
    private ToDoFileHandler fileHandler;

    public ToDoController() {
        this.fileHandler = new ToDoFileHandler("todos.dat");
        this.toDos = fileHandler.readFromFile();
    }

    //Get för alla tasks
    @GetMapping
    public List<ToDo> getAllTasks(){
        return toDos;
    }

    //Get för tasks med ID
    @GetMapping("/{id}")
    public ResponseEntity<ToDo> getTaskById(@PathVariable int id){
        for (ToDo toDo : toDos) {
            if (toDo.getId() == id) {
                return new ResponseEntity<>(toDo, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    //Post tasks
    @PostMapping
    public ResponseEntity<ToDo> createTask(@RequestBody ToDo toDo){
        if (toDo.getId() != 0) {
            toDos.add(toDo);
            fileHandler.saveToFile(toDos);
            System.out.println("Task created: " + toDo);
            return new ResponseEntity<>(toDo, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    //Put för tasks
    @PutMapping("/{id}")
    public ResponseEntity<ToDo> updateTask(@PathVariable int id, @RequestBody ToDo updatedTask){
        for (ToDo toDo : toDos) {
            if (toDo.getId() == id) {
                toDo.setTitle(updatedTask.getTitle());
                toDo.setDescription(updatedTask.getDescription());
                return ResponseEntity.ok(toDo);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //Delete för tasks
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTaskById(@PathVariable int id){
        boolean removed = false;
        for(int i = 0; i < toDos.size(); i++) {
            if (toDos.get(i).getId() == id){
                toDos.remove(i);
                removed = true;
            }
        }
        if (removed) {
            fileHandler.saveToFile(toDos);
            return ResponseEntity.ok("Task with ID: " + id + " was deleted!");
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task with ID: " + id + " was not deleted!");
        }
    }
}

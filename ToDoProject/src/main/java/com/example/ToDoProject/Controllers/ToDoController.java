package com.example.ToDoProject.Controllers;

import com.example.ToDoProject.Models.ToDo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")

public class ToDoController {
    //Lägg in datastrukut (Arraylist tror jag)
    private List<ToDo> toDos = new ArrayList<>();

    public ToDoController() {
    toDos.add(new ToDo(1, "Buy ice cream", "Go to store"));
    toDos.add(new ToDo(2, "Read a book", "Read 50 pages of Harry Potter"));
    }

    //Get för alla todos
    @GetMapping
    public List<ToDo> getAllToDos(){
        return toDos;
    }

    //Get för todos med ID
    @GetMapping("/{id}")
    public ResponseEntity<ToDo> getToDoById(@PathVariable int id){
        for (ToDo toDo : toDos) {
            if (toDo.getId() == id) {
                return new ResponseEntity<>(toDo, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    //Post todos
    @PostMapping
    public ResponseEntity<ToDo> createToDo(@RequestBody ToDo toDo){
        if (toDo.getId() != 0) {
            toDos.add(toDo);
            return new ResponseEntity<>(toDo, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}

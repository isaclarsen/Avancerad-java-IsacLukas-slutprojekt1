package org.example.todofe;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

import java.awt.event.ActionEvent;

public class HelloController {


    @FXML
    private TextField input_Description;

    @FXML
    private TableColumn<?, ?> input_TableDesc;

    @FXML
    private TableColumn<?, ?> input_TableID;

    @FXML
    private TableColumn<?, ?> input_TableTaskName;

    @FXML
    private TextField input_Task;

    @FXML
    private TextField input_TaskID;

    @FXML
    private TextField input_TaskID2;

    @FXML
    void addTask(ActionEvent event) {

        try {
            String taskName = input_Task.getText();
            String description = input_Description.getText();
            int taskId = Integer.parseInt(input_TaskID.getText());

            if (taskName.isEmpty() || description.isEmpty()) {
                input_Description.setText("Please fill in both the task name and description. ");
            }
            // Ska lägga till mer Kod här
        } catch (Exception e) {
            input_TaskID.setText("Error " + e.getMessage());
        }

    }

    @FXML
    void deleteTask(ActionEvent event) {


    }

    @FXML
    void editTask(ActionEvent event) {

    }

}


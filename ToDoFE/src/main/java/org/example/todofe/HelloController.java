package org.example.todofe;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
        void addTask(javafx.event.ActionEvent actionEvent) {

                try {
                    String taskName = input_Task.getText();
                    String description = input_Description.getText();
                    int taskId = Integer.parseInt(input_TaskID.getText());

                    if (taskName.isEmpty() || description.isEmpty()) {
                        input_Description.setText("Please fill in both the task name and description. ");
                        return;

                    }
                    URL url = new URL("http://localhost:8080/api/tasks");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Accept", "application/json");
                    connection.setDoOutput(true);
                    String toDoJson = String.format("{\"id\":\"%s\",\"name\":\"%s\",\"description\":\"%s\"}", taskId, taskName, description);

                    try (OutputStream os = connection.getOutputStream()) {
                        os.write(toDoJson.getBytes());
                        os.flush();

                    }

                } catch (Exception e) {
                    input_TaskID.setText("Error " + e.getMessage());
                }

            }


        @FXML
        void deleteTask(javafx.event.ActionEvent actionEvent) {

        }

        @FXML
        void editTask(javafx.event.ActionEvent actionEvent ) {

        }


}



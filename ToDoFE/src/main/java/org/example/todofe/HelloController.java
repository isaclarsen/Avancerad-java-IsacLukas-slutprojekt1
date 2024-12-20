package org.example.todofe;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HelloController {

        @FXML
        private TextField input_Description;

        @FXML
        private TableView<Task> taskTable;

        @FXML
        private TableColumn<Task, String> columnTaskDescription;

        @FXML
        private TableColumn<Task, Integer> columnTaskId;

        @FXML
        private TableColumn<Task, String> columnTaskName;

        @FXML
        private TextField input_Task;

        @FXML
        private TextField input_TaskID;

        @FXML
        private TextField input_TaskID2;

        private ObservableList<Task> taskList = FXCollections.observableArrayList();


        @FXML
        void addTask(javafx.event.ActionEvent actionEvent) {

                try {
                    String taskName = input_Task.getText();
                    String description = input_Description.getText();
                    int taskId = Integer.parseInt(input_TaskID.getText());

                    if (taskName.isEmpty() || description.isEmpty()) {
                        showErrorMessage("Please fill in both the task name and description. ");
                        return;
                    }

                    URL url = new URL("http://localhost:8080/api/tasks");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Accept", "application/json");
                    connection.setDoOutput(true);

                    String toDoJson = String.format(
                            "{\"id\":\"%s\",\"name\":\"%s\",\"description\":\"%s\"}",
                            taskId, taskName, description);

                    try (OutputStream os = connection.getOutputStream()) {
                        os.write(toDoJson.getBytes());
                        os.flush();

                    }

                    if (connection.getResponseCode() == 200 && connection.getResponseCode() < 300) {
                        taskList.add(new Task(taskId, taskName, description));

                        input_Task.clear();
                        input_TaskID.clear();
                        input_Description.clear();

                    } else {
                        String errorResponse = readResponse(connection);

                        showErrorMessage("Failed to add task: " + errorResponse);
                    }

                } catch (NumberFormatException e) {
                    showErrorMessage("Task Id must be a valid number. ");
                } catch (Exception e) {
                    showErrorMessage("Error " + e.getMessage());
                }
            }


        @FXML
        void deleteTask(javafx.event.ActionEvent actionEvent) {


        }

        @FXML
        void editTask(javafx.event.ActionEvent actionEvent ) {

        }

        private String readResponse(HttpURLConnection connection) throws IOException {
            BufferedReader reader;
            if (connection.getResponseCode() == 200 && connection.getResponseCode() < 300) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            return response.toString();
        }


        @FXML
        public void initialize(){ // Fick kolla på en video för att implementera denna metod för taskTable

            columnTaskId.setCellValueFactory(new PropertyValueFactory<>("id"));
            columnTaskDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
            columnTaskName.setCellValueFactory(new PropertyValueFactory<>("name"));
            taskTable.setItems(taskList);

        }

        private void showErrorMessage(String message) {
            System.err.println(message);
        }


}



package org.example.todofe;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

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
        private TableColumn<Task, String> columnTaskTitle;

        @FXML
        private TextArea input_errorWindow;

        @FXML
        private TextField input_Task;

        @FXML
        private TextField input_TaskID;


        private ObservableList<Task> taskList = FXCollections.observableArrayList();


        // Metod för att lägga till en task i tabellen.
        @FXML
        void addTask(javafx.event.ActionEvent actionEvent) {

                try {// Hämta data från input fält
                    String taskTitle = input_Task.getText();
                    String description = input_Description.getText();
                    int taskId = Integer.parseInt(input_TaskID.getText());

                    //Kontrollera att nödvändiga fält är ifyllda
                    if (taskTitle.isEmpty() || description.isEmpty()) {
                        showErrorMessage("Please fill in both the task name and description. ");
                        input_errorWindow.setText("Please fill in both the task name and description. ");
                        return;
                    }

                    // Skapa och konfigurera en HTTP POST-anslutning
                    URL url = new URL("http://localhost:8080/api/tasks");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Accept", "application/json");
                    connection.setDoOutput(true);

                    // Konvertera till JSON och skicka till backend
                    String toDoJson = String.format(
                            "{\"id\":%d,\"title\":\"%s\",\"description\":\"%s\"}",
                            taskId, taskTitle, description);
                    System.out.println("JSON to send: " + toDoJson);

                    try (OutputStream os = connection.getOutputStream()) {
                        os.write(toDoJson.getBytes());
                        os.flush();
                    }

                    // Hantera serverns svar
                    if (connection.getResponseCode() == 201) {
                        try (InputStream is = connection.getInputStream()) {
                            String response2 = new BufferedReader(new InputStreamReader(is))
                                    .lines()
                                    .reduce("", (acc, line) -> acc + line + "\n");

                            if (!response2.isEmpty()) {
                                System.out.println("Response body: " + response2);

                                // Lägger till tasken i tabellen lokalt
                                Task newTask = new Task(taskId, taskTitle, description);
                                taskList.add(newTask);
                            } else {
                                Task newTask = new Task(taskId, taskTitle, description);
                                taskList.add(newTask);
                                System.out.println("No response body but task added locally");
                            }
                            input_Task.clear();
                            input_TaskID.clear();
                            input_Description.clear();
                        }
                    } else {
                        InputStream errorStream = connection.getErrorStream();
                        String errorResponse = errorStream != null ? readResponse(connection) : "No response body available.";
                        showErrorMessage("Failed to add task: " + errorResponse);
                        input_errorWindow.setText("Failed to add task: " + errorResponse);
                    }
                    if (connection.getResponseCode() == 200 && connection.getResponseCode() < 300) {
                        Task newTask = new Task(taskId, taskTitle, description);
                        taskList.add(newTask);
                        System.out.println("Added task: " + newTask.getTitle());
                        input_errorWindow.setText("Added task: " + newTask.getTitle());

                        input_Task.clear();
                        input_TaskID.clear();
                        input_Description.clear();

                    }

                } catch (NumberFormatException e) {
                    showErrorMessage("Task Id must be a valid number. ");
                    input_errorWindow.setText("Task Id must be a valid number.");
                    e.printStackTrace();
                } catch (IOException e) {
                    showErrorMessage("Network error " + e.getMessage());
                    input_errorWindow.setText("Network error " + e.getMessage());
                    e.printStackTrace();
                } catch (Exception e) {
                    showErrorMessage("Unexpected error " + e.getMessage());
                    input_errorWindow.setText("Unexpected error " + e.getMessage());
                    e.printStackTrace();
                }
            }


        @FXML // Isac som implementerade metoden, fick massor av konflikter så jag försöker pusha iställlet.
        void deleteTask(javafx.event.ActionEvent actionEvent) {

            //Kolla vilken task som är markerad
            Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
            if (selectedTask == null) {
                showErrorMessage("No task selected. Please select a task to delete.");
                input_errorWindow.setText("No task selected. Please select a task to delete.");
                return;
            }
            //Skapa en variabel som sparar ID från tasken man har markerat
            int taskId = selectedTask.getId();

            try {
                URL url = new URL("http://localhost:8080/api/tasks/" + taskId);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("DELETE");

                if (connection.getResponseCode() == 200 || connection.getResponseCode() == 204) {
                    taskList.remove(selectedTask);
                    System.out.println("Task with ID: " + taskId + " deleted successfully.");
                    input_errorWindow.setText("Task with ID: " + taskId + " deleted successfully.");
                } else {
                    String errorResponse = readResponse(connection);
                    showErrorMessage("Failed to delete task. Error: " + errorResponse);
                    input_errorWindow.setText("Failed to delete task. Error: " + errorResponse);
                    input_errorWindow.setText("Failed to delete task. Error: " + errorResponse);
                }

            } catch (IOException e) {
                showErrorMessage("Network error while deleting task: " + e.getMessage());
                input_errorWindow.setText("Network error while deleting task: " + e.getMessage());
            }
        }

        @FXML
        void editTask(javafx.event.ActionEvent actionEvent) {
            try {
                Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
                if (selectedTask == null) {
                    showErrorMessage("Please select a task.");
                    input_errorWindow.setText("Please select a task.");
                    return;
                }
                String updatedTaskJson = String.format(
                        "{\"id\":%d,\"title\":\"%s\",\"description\":\"%s\"}",
                        selectedTask.getId(), selectedTask.getTitle(), selectedTask.getDescription());

                URL url = new URL("http://localhost:8080/api/tasks/" + selectedTask.getId());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);

                try (OutputStream os = connection.getOutputStream()) {
                    os.write(updatedTaskJson.getBytes());
                    os.flush();
                }
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    taskTable.refresh();
                    System.out.println("Task updated successfully");
                    input_errorWindow.setText("Task updated successfully.");
                } else {
                    String errorResponse = readResponse(connection);
                    showErrorMessage("Failed to update task: " + errorResponse);
                    input_errorWindow.setText("Failed to update task. Error: " + errorResponse);
                }

            } catch (IOException e){
                showErrorMessage("Network error " + e.getMessage());
                input_errorWindow.setText("Network error " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {{
                showErrorMessage("Unexpected error " + e.getMessage());
                input_errorWindow.setText("Unexpected error " + e.getMessage());
                e.printStackTrace();
            }}
        }


        private String readResponse(HttpURLConnection connection) throws IOException {
            InputStream stream = connection.getInputStream();
            if (stream == null) {
                return "";
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                return response.toString();
            }
        }

        // Initialiserar tabellen vid start
        @FXML
        public void initialize(){ // Fick kolla på en video för att implementera denna metod för taskTable

            System.out.println("Initializing...");
            columnTaskId.setCellValueFactory(new PropertyValueFactory<>("id"));
            columnTaskDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
            columnTaskTitle.setCellValueFactory(new PropertyValueFactory<>("title"));

            columnTaskId.setEditable(false);

            columnTaskTitle.setCellFactory(TextFieldTableCell.forTableColumn());
            columnTaskTitle.setOnEditCommit(event -> {
                Task editedTask = event.getRowValue();
                editedTask.setTitle(event.getNewValue());

            });

            columnTaskDescription.setCellFactory(TextFieldTableCell.forTableColumn());
            columnTaskDescription.setOnEditCommit(event -> {
                Task editedTask = event.getRowValue();
                editedTask.setDescription(event.getNewValue());

            });
            taskTable.setItems(taskList);
            taskTable.setEditable(true);
            System.out.println("Task list initialized with " + taskList.size() + " tasks.");

            loadTasksFromBackend();

        }

    private void loadTasksFromBackend() {
        try {
            URL url = new URL("http://localhost:8080/api/tasks");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            if (connection.getResponseCode() == 200) {
                try (InputStream is = connection.getInputStream();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    String json = response.toString();
                    ObjectMapper objectMapper = new ObjectMapper(); // använder Jackson library
                    List<Task> tasks = objectMapper.readValue(json, new TypeReference<List<Task>>() {});
                    taskList.setAll(tasks);
                    System.out.println("Tasks loaded: " + tasks.size());
                }
            } else {
                showErrorMessage("Failed to fetch tasks: HTTP " + connection.getResponseCode());
            }
        } catch (IOException e) {
            showErrorMessage("Error loading tasks: " + e.getMessage());
            e.printStackTrace();
        }
    }

        private void showErrorMessage(String message) {
            System.err.println(message);
        }
}

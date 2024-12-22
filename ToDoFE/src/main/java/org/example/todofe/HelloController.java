package org.example.todofe;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.io.*;
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
        private TableColumn<Task, String> columnTaskTitle;

        @FXML
        private TextField input_Task;

        @FXML
        private TextField input_TaskID;

        @FXML
        private TextArea input_textArea;

        @FXML
        private TextField input_TaskID2;



        private ObservableList<Task> taskList = FXCollections.observableArrayList();


        @FXML
        void addTask(javafx.event.ActionEvent actionEvent) {

                try {
                    String taskTitle = input_Task.getText();
                    String description = input_Description.getText();
                    int taskId = Integer.parseInt(input_TaskID.getText());

                    if (taskTitle.isEmpty() || description.isEmpty()) {
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
                            "{\"id\":%d,\"title\":\"%s\",\"description\":\"%s\"}",
                            taskId, taskTitle, description);
                    System.out.println("JSON to send: " + toDoJson);

                    try (OutputStream os = connection.getOutputStream()) {
                        os.write(toDoJson.getBytes());
                        os.flush();
                    }
//                    int responseCode = connection.getResponseCode();
//                    String responseMessage = connection.getResponseMessage();
////                    System.out.println("Response Code: " + responseCode);
////                    System.out.println("Response Message: " + responseMessage);

                    if (connection.getResponseCode() == 201) {
                        try (InputStream is = connection.getInputStream()) {
                            String response2 = new BufferedReader(new InputStreamReader(is))
                                    .lines()
                                    .reduce("", (acc, line) -> acc + line + "\n");

                            if (!response2.isEmpty()) {
                                System.out.println("Response body: " + response2);

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
                    }
                    if (connection.getResponseCode() == 200 && connection.getResponseCode() < 300) {
                        Task newTask = new Task(taskId, taskTitle, description);
                        taskList.add(newTask);
                        System.out.println("Added task: " + newTask.getTitle());

                        input_Task.clear();
                        input_TaskID.clear();
                        input_Description.clear();

//                    } else {
//                        InputStream errorStream = connection.getErrorStream();
//                        String errorResponse = errorStream != null ? readResponse(connection) : "No response body available.";
//                        showErrorMessage("Failed to add task: " + errorResponse);
                    }

                } catch (NumberFormatException e) {
                    showErrorMessage("Task Id must be a valid number. ");
                    e.printStackTrace();
                } catch (IOException e) {
                    showErrorMessage("Network error " + e.getMessage());
                    e.printStackTrace();
                } catch (Exception e) {
                    showErrorMessage("Unexpected error " + e.getMessage());
                    e.printStackTrace();
                }
            }


    @FXML
    void deleteTask(javafx.event.ActionEvent actionEvent) {
        //Kolla vilken task som är markerad
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        if (selectedTask == null) {
            showErrorMessage("No task selected. Please select a task to delete.");
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
            } else {
                String errorResponse = readResponse(connection);
                showErrorMessage("Failed to delete task. Error: " + errorResponse);
            }

        } catch (IOException e) {
            showErrorMessage("Network error while deleting task: " + e.getMessage());
        }
    }


    @FXML
        void editTask(javafx.event.ActionEvent actionEvent ) {





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


//            BufferedReader reader;
//            if (connection.getResponseCode() == 200 && connection.getResponseCode() < 300) {
//                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            } else {
//                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
//            }
//            StringBuilder response = new StringBuilder();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                response.append(line);
//            }
//            reader.close();
//            return response.toString();
        }


        @FXML
        public void initialize(){ // Fick kolla på en video för att implementera denna metod för taskTable

            System.out.println("Initializing...");
            columnTaskId.setCellValueFactory(new PropertyValueFactory<>("id"));
            columnTaskDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
            columnTaskTitle.setCellValueFactory(new PropertyValueFactory<>("title"));

//      Lade in detta efter jag blev klar med addTask metoden
//            columnTaskTitle.setCellFactory(TextFieldTableCell.forTableColumn());
//            columnTaskTitle.setOnEditCommit(event -> {
//                Task editedTask = event.getRowValue();
//                editedTask.setTitle(event.getNewValue());
//                editTask(editedTask);
//            });
//
//            columnTaskDescription.setCellFactory(TextFieldTableCell.forTableColumn());
//            columnTaskDescription.setOnEditCommit(event -> {
//                Task editedTask = event.getRowValue();
//                editedTask.setDescription(event.getNewValue());
//                editTask(editedTask);
//            });
            taskTable.setItems(taskList);
            System.out.println("Task list initialized with " + taskList.size() + " tasks.");

        }

        private void showErrorMessage(String message) {
            System.err.println(message);
        }


}



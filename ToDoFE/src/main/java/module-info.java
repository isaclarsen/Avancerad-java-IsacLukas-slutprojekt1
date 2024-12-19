module org.example.todofe {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens org.example.todofe to javafx.fxml;
    exports org.example.todofe;
}
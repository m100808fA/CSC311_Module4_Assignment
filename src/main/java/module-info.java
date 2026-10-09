module com.example.card24 {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.card24 to javafx.fxml;
    opens com.example.card24.controller to javafx.fxml;
    exports com.example.card24;
    exports com.example.card24.model;
}
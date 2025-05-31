module main.application {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires java.validation;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;


    opens main.application to javafx.fxml;
    exports main.application;
    exports main.application.controller;
    opens main.application.controller to javafx.fxml;

    opens main.application.service to javafx.fxml; // Adicione esta linha

    exports main.application.model;
    exports main.application.model.enums;
}
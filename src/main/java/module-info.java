module bombermanfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.media;
    requires com.fasterxml.jackson.annotation;    // <--- Ajouté pour la lecture audio

    opens controller to javafx.fxml;    // pour que le contrôleur soit accessible au FXML
    opens bombermanfx to javafx.fxml;

    exports controller;    // à exporter si utilisé ailleurs
    exports entities;      // optionnel, si utilisé dans d'autres modules
    exports utils;         // optionnel, idem
    exports bombermanfx;
    opens utils to javafx.fxml;
}
module bombermanfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens ui to javafx.fxml;                 // pour charger Game.fxml
    opens controller to javafx.fxml;         // pour que le contrôleur soit accessible au FXML

    exports controller;                      // à exporter si utilisé ailleurs
    exports entities;                        // optionnel, si utilisé dans d'autres modules
    exports utils;                           // optionnel, idem

    exports bombermanfx;
    opens bombermanfx to javafx.fxml;
}

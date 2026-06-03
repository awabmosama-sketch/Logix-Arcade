module com.example.oopprojectjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;

    // Export all game packages
    exports com.example.oopprojectjavafx.HangMan;
    exports com.example.oopprojectjavafx.FlappyBird;
    exports com.example.oopprojectjavafx.TicTacToe;

    // Also open them for FXML (if needed)
    opens com.example.oopprojectjavafx.HangMan to javafx.fxml;
    opens com.example.oopprojectjavafx.FlappyBird to javafx.fxml;
    opens com.example.oopprojectjavafx.TicTacToe to javafx.fxml;
}
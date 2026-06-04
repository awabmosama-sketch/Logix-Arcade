module com.example.oopprojectjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;

    // Export all game packages
    exports com.example.oopprojectjavafx.HangMan;
    exports com.example.oopprojectjavafx.FlappyBird;
    exports com.example.oopprojectjavafx.TicTacToe;
    exports com.example.oopprojectjavafx.MainMenu;
    exports com.example.oopprojectjavafx.RockPaperScisscors;
    exports com.example.oopprojectjavafx.QuizGame;
    exports com.example.oopprojectjavafx.snake_game;
    exports com.example.oopprojectjavafx.Timer_Game;

    // Also open them for FXML (if needed)
    opens com.example.oopprojectjavafx.HangMan to javafx.fxml;
    opens com.example.oopprojectjavafx.FlappyBird to javafx.fxml;
    opens com.example.oopprojectjavafx.TicTacToe to javafx.fxml;
    opens com.example.oopprojectjavafx.RockPaperScisscors to javafx.fxml;
    opens com.example.oopprojectjavafx to javafx.fxml;
    opens com.example.oopprojectjavafx.QuizGame to javafx.fxml;
    opens com.example.oopprojectjavafx.snake_game to javafx.fxml;
    opens com.example.oopprojectjavafx.Timer_Game to javafx.fxml;

}
module com.example.oopprojectjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.graphics;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires jdk.jfr;
    requires java.desktop;

    // These lines allow the FXML loader to inject data into your private @FXML fields
    opens com.example.oopprojectjavafx to javafx.fxml;
    opens com.example.oopprojectjavafx.chess to javafx.fxml;
    opens com.example.oopprojectjavafx.FlappyBird to javafx.fxml;

    // These lines allow other parts of your code to access your classes
    exports com.example.oopprojectjavafx;
    exports com.example.oopprojectjavafx.chess;
    exports com.example.oopprojectjavafx.FlappyBird;
}
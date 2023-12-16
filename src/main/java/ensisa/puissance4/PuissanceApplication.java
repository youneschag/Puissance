package ensisa.puissance4;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class PuissanceApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PuissanceApplication.class.getResource("main-view.fxml"));
        Screen screen = Screen.getPrimary();
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        double screenWidth = screen.getBounds().getWidth();
        double screenHeight = screen.getBounds().getHeight();
        MainController controller = fxmlLoader.getController();
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        controller.initialiserJeu();
        controller.setScene(scene);
        stage.setTitle("Puissance 4");
        stage.setScene(scene);
        stage.setWidth(screenWidth);
        stage.setHeight(screenHeight);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}


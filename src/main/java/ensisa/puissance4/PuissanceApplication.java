package ensisa.puissance4;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PuissanceApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PuissanceApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 800);
        MainController controller = fxmlLoader.getController();
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        controller.initialiserJeu();
        controller.setScene(scene);
        
        stage.setTitle("Puissance 4");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}


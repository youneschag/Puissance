package ensisa.puissance4;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class MatchHistoryWindow {

    public static void display(List<MainController.Match> matchList) {
        Stage window = new Stage();

        // Configure la fenêtre
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Historique des matchs");

        // Crée le contenu de la fenêtre
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        // Crée la liste des matchs avec des séparateurs
        ListView<String> listView = new ListView<>();
        for (MainController.Match match : matchList) {
            // Ajoute le match à la liste avec la couleur appropriée
            String matchText = match.toString();
            Label matchLabel = new Label(matchText);
            if ("Gagné".equals(match.getResult())) {
                matchLabel.setTextFill(Color.GREEN);
            } else if ("Perdu".equals(match.getResult())) {
                matchLabel.setTextFill(Color.RED);
            } else {
                matchLabel.setTextFill(Color.BLACK);
            }

            // Ajoute le label à la liste
            listView.getItems().add(matchLabel.getText());

            // Ajoute une ligne de séparation
            Separator separator = new Separator();
            layout.getChildren().addAll(separator, matchLabel);
        }

        layout.getChildren().add(listView);

        // Affiche la fenêtre
        Scene scene = new Scene(layout, 300, 200);
        window.setScene(scene);
        window.showAndWait();
    }
}




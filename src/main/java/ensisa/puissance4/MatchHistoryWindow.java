package ensisa.puissance4;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.List;

public class MatchHistoryWindow {

    public static void display(List<Match> matchList) {
        Stage window = new Stage();
        window.setTitle("Historique des matchs");

        // Create a ListView to display match history
        ListView<String> listView = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList();

        // Add each match result to the ListView
        for (Match match : matchList) {
            items.add(match.getMatchResult());
        }
        listView.setItems(items);
        // Add the ListView to the scene
        Scene scene = new Scene(listView, 500, 400);
        window.setScene(scene);
        // Show the window
        window.show();
    }
}

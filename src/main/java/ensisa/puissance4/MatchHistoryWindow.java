package ensisa.puissance4;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.List;

public class MatchHistoryWindow {

    public static void display(List<Match> matchList, String dernierGagnant) {
        Stage window = new Stage();
        window.setTitle("Historique des matchs");

        // Create a ListView to display match history
        ListView<String> listView = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList();

        // Add each match result to the ListView


        listView.setItems(items);

        // Add the ListView to the scene
        Scene scene = new Scene(listView, 400, 300);
        window.setScene(scene);

        // Show the window
        window.show();
    }
}

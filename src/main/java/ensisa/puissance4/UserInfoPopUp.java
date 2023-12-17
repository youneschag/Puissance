package ensisa.puissance4;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Optional;

public class UserInfoPopUp {

    public static UserInfo obtenirInfosUtilisateur() {
        // Afficher une boîte de dialogue pour le nom d'utilisateur
        TextInputDialog firstusernameDialog = new TextInputDialog();
        firstusernameDialog.setTitle("Nom d'utilisateur");
        firstusernameDialog.setHeaderText(null);
        firstusernameDialog.setContentText("Entrez votre nom d'utilisateur :");
        Optional<String> firstusernameResult = firstusernameDialog.showAndWait();

        // Afficher une boîte de dialogue pour le choix du jeton
        List<String> choices = List.of("YELLOW", "RED"); // Ajoutez d'autres choix au besoin
        ChoiceDialog<String> tokenDialog = new ChoiceDialog<>(choices.get(0), choices);
        tokenDialog.setTitle("Choix du jeton");
        tokenDialog.setHeaderText(null);
        tokenDialog.setContentText("Choisissez votre jeton :");
        Optional<String> tokenResult = tokenDialog.showAndWait();

        // Afficher une boîte de dialogue pour le choix du mode de jeu
        List<String> gameModes = List.of("Human vs Human", "Human vs Computer"); // Ajoutez d'autres modes au besoin
        ChoiceDialog<String> gameModeDialog = new ChoiceDialog<>(gameModes.get(0), gameModes);
        gameModeDialog.setTitle("Mode de jeu");
        gameModeDialog.setHeaderText(null);
        gameModeDialog.setContentText("Choisissez le mode de jeu :");
        Optional<String> gameModeResult = gameModeDialog.showAndWait();

        // Si le mode de jeu est "Human vs Human", demandez le nom du deuxième utilisateur
        String secondUsername = "";
        if ("Human vs Human".equals(gameModeResult.orElse(""))) {
            TextInputDialog secondUsernameDialog = new TextInputDialog();
            secondUsernameDialog.setTitle("Nom d'utilisateur du deuxième joueur ");
            secondUsernameDialog.setHeaderText(null);
            secondUsernameDialog.setContentText("Entrez le nom d'utilisateur du deuxième joueur :");
            Optional<String> secondUsernameResult = secondUsernameDialog.showAndWait();
            secondUsername = secondUsernameResult.orElse("");
        }
        // Boîte de dialogue pour demander l'ordre de jeu
        List<String> orderChoices = List.of("Premier", "Deuxième"); // Ajoutez d'autres choix au besoin
        ChoiceDialog<String> orderDialog = new ChoiceDialog<>(orderChoices.get(0), orderChoices);
        orderDialog.setTitle("Ordre de jeu");
        orderDialog.setHeaderText(null);
        orderDialog.setContentText("Choisissez l'ordre de jeu :");
        Optional<String> orderResult = orderDialog.showAndWait();

        // Boîte de dialogue pour demander si l'utilisateur veut limiter le temps
        TextInputDialog timeLimitDialog = new TextInputDialog("0"); // Valeur par défaut, 0 signifie pas de limite
        timeLimitDialog.setTitle("Limite de temps");
        timeLimitDialog.setHeaderText(null);
        timeLimitDialog.setContentText("Entrez la limite de temps en secondes (0 pour pas de limite) :");
        Optional<String> timeLimitResult = timeLimitDialog.showAndWait();
        int selectedTimeLimit = Integer.parseInt(timeLimitResult.orElse("0"));

        // Retourne les informations de l'utilisateur
        return new UserInfo(firstusernameResult.orElse(""), tokenResult.orElse(""), gameModeResult.orElse(""), secondUsername, orderResult.orElse(""), selectedTimeLimit);
    }

    public static void afficherInstructions() {
        // Afficher les instructions
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Instructions du jeu");
        alert.setHeaderText(null);
        alert.setContentText("Insérez ici les instructions du jeu.");
        alert.showAndWait();
    }
}

package ensisa.puissance4;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

import java.util.List;
import java.util.Optional;

public class UserInfoPopUp {

    public static UserInfo obtenirInfosUtilisateur() {
        // Créer une GridPane pour organiser les champs
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Ajouter le champ pour le nom d'utilisateur
        TextField usernameField = new TextField();
        gridPane.add(new Label("Nom d'utilisateur:"), 0, 0);
        gridPane.add(usernameField, 1, 0);

        // Ajouter le champ pour le choix du jeton
        List<String> choices = List.of("YELLOW", "RED");
        ChoiceBox<String> tokenChoice = new ChoiceBox<>();
        tokenChoice.getItems().addAll(choices);
        tokenChoice.setValue(choices.get(0));
        gridPane.add(new Label("Choix du jeton:"), 0, 1);
        gridPane.add(tokenChoice, 1, 1);

        // Ajouter le champ pour le choix du mode de jeu
        List<String> gameModes = List.of("Human vs Human", "Human vs Computer");
        ChoiceBox<String> gameModeChoice = new ChoiceBox<>();
        gameModeChoice.getItems().addAll(gameModes);
        gameModeChoice.setValue(gameModes.get(0));
        gridPane.add(new Label("Mode de jeu:"), 0, 2);
        gridPane.add(gameModeChoice, 1, 2);

        // Ajouter le champ pour l'ordre de jeu
        List<String> orderChoices = List.of("Premier", "Deuxième");
        ChoiceBox<String> orderChoice = new ChoiceBox<>();
        orderChoice.getItems().addAll(orderChoices);
        orderChoice.setValue(orderChoices.get(0));
        gridPane.add(new Label("Ordre de jeu:"), 0, 3);
        gridPane.add(orderChoice, 1, 3);

        // Ajouter le champ pour la limite de temps
        TextField timeLimitField = new TextField("0");
        int timeLimit = Integer.parseInt(timeLimitField.getText());
        gridPane.add(new Label("Limite de temps en secondes (0 : pas de limite):"), 0, 4);
        gridPane.add(timeLimitField, 1, 4);

        // Ajouter le champ pour le deuxième utilisateur (visible seulement si "Human vs Human" est sélectionné)
        TextField secondUsernameField = new TextField();
        secondUsernameField.setVisible(false);
        gridPane.add(new Label("Nom d'utilisateur du deuxième joueur:"), 0, 5);
        gridPane.add(secondUsernameField, 1, 5);

        // Écouter les changements dans le choix du mode de jeu
        gameModeChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            secondUsernameField.setVisible("Human vs Human".equals(newValue));
        });

        // Boîte de dialogue principale
        Dialog<UserInfo> dialog = new Dialog<>();
        dialog.setTitle("Informations de l'utilisateur");
        dialog.setHeaderText(null);
        dialog.getDialogPane().setContent(gridPane);

        // Ajouter les boutons OK et Annuler
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        // Configurer la conversion des résultats
        dialog.setResultConverter(new Callback<ButtonType, UserInfo>() {
            @Override
            public UserInfo call(ButtonType buttonType) {
                if (buttonType == okButtonType) {
                    return new UserInfo(
                            usernameField.getText(),
                            tokenChoice.getValue(),
                            gameModeChoice.getValue(),
                            secondUsernameField.getText(),
                            orderChoice.getValue(),
                            timeLimit
                    );
                }
                return null;
            }
        });

        // Afficher la boîte de dialogue et récupérer les résultats
        Optional<UserInfo> result = dialog.showAndWait();

        // Retourner les informations de l'utilisateur si elles sont présentes
        return result.orElse(null);
    }

    public static void afficherInstructions() {
        // Afficher les instructions
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Instructions du jeu");
        alert.setHeaderText(null);
        alert.setContentText("Insérez ici les instructions du jeu.");
        alert.showAndWait();
    }
}


package ensisa.puissance4;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class UserInfoPopUp {
    private static Label labelSecondUsername = new Label("Nom du deuxième joueur:");
    private static StringProperty selectedGameMode = new SimpleStringProperty("");

    public static UserInfo obtenirInfosUtilisateur() {
        // Créer une GridPane pour organiser les champs
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Ajouter le champ pour le nom d'utilisateur
        TextField usernameField = new TextField();
        usernameField.setPromptText("Nom d'utilisateur");
        gridPane.add(new Label("Nom d'utilisateur:"), 0, 0);
        gridPane.add(usernameField, 1, 0);

        // Ajouter le champ pour le choix du jeton
        List<String> choices = List.of("YELLOW", "RED");
        ChoiceBox<String> tokenChoice = new ChoiceBox<>();
        tokenChoice.getItems().addAll(choices);
        tokenChoice.setValue(choices.get(0));
        gridPane.add(new Label("Choix du jeton:"), 0, 1);
        gridPane.add(tokenChoice, 1, 1);

        // Créer une ToggleGroup pour les boutons radio
        ToggleGroup gameModeToggleGroup = new ToggleGroup();

        // Créer les boutons radio
        RadioButton humanVsHumanRadioButton = new RadioButton("Human vs Human");
        humanVsHumanRadioButton.setToggleGroup(gameModeToggleGroup);

        RadioButton humanVsComputerRadioButton = new RadioButton("Human vs Computer");
        humanVsComputerRadioButton.setToggleGroup(gameModeToggleGroup);

        // Ajouter les boutons radio à la GridPane
        gridPane.add(new Label("Mode de jeu:"), 0, 2);
        gridPane.add(humanVsHumanRadioButton, 1, 2);
        gridPane.add(humanVsComputerRadioButton, 2, 2);

        // Champ pour le deuxième utilisateur
        TextField secondUsernameField = new TextField();
        secondUsernameField.setVisible(false);
        gridPane.add(labelSecondUsername, 0, 5);
        gridPane.add(secondUsernameField, 1, 5);

        // Ajouter un ChangeListener pour détecter le choix de l'utilisateur
        gameModeToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Mettre à jour le contenu en fonction du bouton radio sélectionné
                selectedGameMode.set(((RadioButton) newValue).getText());

                // Mettre à jour la visibilité du champ du deuxième utilisateur en fonction du choix du mode de jeu
                secondUsernameField.setVisible("Human vs Human".equals(selectedGameMode.get()));
                // Mettre à jour la visibilité du label "Nom d'utilisateur du deuxième joueur:"
                labelSecondUsername.setVisible("Human vs Human".equals(selectedGameMode.get()));
            }
        });
        // Définir le bouton radio initialement sélectionné (par exemple, "Human vs Computer")
        humanVsComputerRadioButton.setSelected(true);

        // Ajouter le champ pour l'ordre de jeu
        List<String> orderChoices = List.of("Premier", "Deuxième");
        ChoiceBox<String> orderChoice = new ChoiceBox<>();
        orderChoice.getItems().addAll(orderChoices);
        orderChoice.setValue(orderChoices.get(0));
        gridPane.add(new Label("Ordre de jeu:"), 0, 3);
        gridPane.add(orderChoice, 1, 3);

        // Ajouter le champ pour la limite de temps
        TextField timeLimitField = new TextField("");
        timeLimitField.setPromptText("0 : pas de limite");
        gridPane.add(new Label("Limite de temps(en s):"), 0, 4);
        gridPane.add(timeLimitField, 1, 4);

        // Boîte de dialogue principale
        Dialog<UserInfo> dialog = new Dialog<>();
        dialog.setTitle("Informations de l'utilisateur");
        dialog.setHeaderText(null);
        dialog.getDialogPane().setContent(gridPane);

        // Ajouter les boutons OK et Annuler
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        // Configurer la conversion des résultats
        dialog.setResultConverter(buttonType -> {
            if (buttonType == okButtonType) {
                return new UserInfo(
                        usernameField.getText(),
                        tokenChoice.getValue(),
                        selectedGameMode.get(),
                        secondUsernameField.getText(),
                        orderChoice.getValue(),
                        Integer.parseInt(timeLimitField.getText())
                );
            }
            return null;
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


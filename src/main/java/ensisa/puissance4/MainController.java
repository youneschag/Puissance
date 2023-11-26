package ensisa.puissance4;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.css.Match;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainController {
    @FXML
    private void handleExitButtonAction(ActionEvent event) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Voulez-vous vraiment quitter l'application?");

        // Ajouter les boutons "Oui" et "Annuler"
        confirmationAlert.getButtonTypes().setAll(ButtonType.YES, ButtonType.CANCEL);

        // Afficher la boîte de dialogue et attendre la réponse de l'utilisateur
        Stage stage = (Stage) confirmationAlert.getDialogPane().getScene().getWindow();
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        // Vérifier la réponse de l'utilisateur
        if (result.isPresent() && result.get() == ButtonType.YES) {
            // Si l'utilisateur a cliqué sur "Oui", quitter l'application
            Platform.exit();
            System.exit(0);
        }
        // Sinon, l'utilisateur a annulé l'action, rien ne se passe
    }
    private boolean premierClic = true;
    @FXML
    private TextField textField;
    @FXML
    private TextField gameTypeTextField;
    @FXML
    private Label usernameLabelJoueur1;
    @FXML
    private Label usernameLabelJoueur2;
    private String nomJoueur1;
    private String nomJoueur2;
    private String selectedGameMode; // Choix du mode de jeu
    private Color selectedTokenColor; // Choix de la couleur du jeton
    private String selectedOrdre;
    private Timeline timeline;
    private boolean demarrerTemps = false;
    @FXML
    private GridPane gridPane;
    private Scene scene;
    private boolean isDarkTheme = false;
    @FXML
    private Circle themeCircle;
    @FXML
    private VBox matchHistoryContainer;

    // Méthode pour initialiser les éléments de jeu
    @FXML
    public void initialiserJeu() {
        // Afficher les règles
        ReglesDuJeuPopUp.afficherRegles();

        // Obtenir les informations de l'utilisateur
        UserInfo userInfo = UserInfoPopUp.obtenirInfosUtilisateur();

        // Mettre à jour le texte et la couleur du jeton en fonction des informations du joueur
        mettreAJourTypePartie(userInfo.getSelectedGameMode());
        selectedGameMode = userInfo.getSelectedGameMode();
        selectedOrdre = userInfo.getSelectedOrdre();

        // Déterminer la couleur du premier jeton en fonction du choix de l'utilisateur et de l'ordre de jeu
        if ("Premier".equals(selectedOrdre)) {
            selectedTokenColor = ("YELLOW".equals(userInfo.getSelectedToken())) ? Color.YELLOW : Color.RED;
        } else if ("Deuxième".equals(selectedOrdre)) {
            selectedTokenColor = ("YELLOW".equals(userInfo.getSelectedToken())) ? Color.RED : Color.YELLOW;
        }

        // Vérifier l'ordre de jeu et mettre à jour le texte en conséquence
        if ("Premier".equals(selectedOrdre)) {
            textField.setText("C'est le tour de " + userInfo.getUsername());
        } else if ("Deuxième".equals(selectedOrdre)) {
            if ("Human vs Human".equals(selectedGameMode)) {
                textField.setText("C'est le tour de " + userInfo.getSecondusername());
            } else {
                textField.setText("C'est le tour du Computer");
            }
        }
        // Mise à jour du label avec le nom d'utilisateur en fonction du choix du jeton et de l'ordre
        if ("Human vs Computer".equals(selectedGameMode)) {
            if ("YELLOW".equals(userInfo.getSelectedToken())) {
                usernameLabelJoueur1.setText(userInfo.getUsername());
                usernameLabelJoueur2.setText("Computer");
            } else {
                usernameLabelJoueur1.setText("Computer");
                usernameLabelJoueur2.setText(userInfo.getUsername());
            }
        };
        if ("Human vs Human".equals(selectedGameMode)) {
            if ("YELLOW".equals(userInfo.getSelectedToken())) {
                usernameLabelJoueur1.setText(userInfo.getUsername());
                usernameLabelJoueur2.setText(userInfo.getSecondusername());
            } else {
                usernameLabelJoueur1.setText(userInfo.getSecondusername());
                usernameLabelJoueur2.setText(userInfo.getUsername());
            }
        };
        if (timeline != null) {
            timeline.stop(); // Arrêter la timeline si elle est en cours
        }
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), this::updateDuration));
        timeline.setCycleCount(Timeline.INDEFINITE);

        themeCircle.setOnMouseClicked(this::handleThemeSwitch);
    }


    @FXML
    private Label durationLabel;
    private int secondsElapsed = 0;

    // Méthode appelée à chaque intervalle de la Timeline pour mettre à jour la durée
    private void updateDuration(ActionEvent event) {
        int minutes = secondsElapsed / 60;
        int seconds = secondsElapsed % 60;
        durationLabel.setText(String.format("Durée: %02d:%02d", minutes, seconds));

        // Incrémenter les secondes après la mise à jour de l'affichage
        secondsElapsed++;
    }

    @FXML
    private void handleColumnClick(MouseEvent event) {
        if (premierClic) {
            // Réinitialiser le jeu avant de placer le premier jeton
            reinitialiserJeu();

            // Initialiser les noms des joueurs
            nomJoueur1 = usernameLabelJoueur1.getText();
            nomJoueur2 = usernameLabelJoueur2.getText();

            textField.setText("C'est le tour de " + nomJoueur1);
            premierClic = false;
            demarrerTemps = true; // Démarrer le temps lorsque le premier jeton est posé
        }

        Circle clickedCircle = (Circle) event.getSource();
        int columnIndex = GridPane.getColumnIndex(clickedCircle);
        // Logique pour placer le jeton dans la colonne columnIndex
        placerJeton(columnIndex);
    }

    @FXML
    private VBox yellowMovesHistory;

    @FXML
    private VBox redMovesHistory;

    public void placerJeton(int columnIndex) {
        // Remove existing circles in the clicked column and store them
        List<Node> circlesToRemove = supprimerCercles(columnIndex);

        int row = trouverLigneVide(columnIndex);

        // Check if the column is not full
        if (row != -1) {
            // Place the token on the grid with the current color
            Jeton token = new Jeton(25, selectedTokenColor);
            GridPane.setColumnIndex(token, columnIndex);
            GridPane.setRowIndex(token, row);
            GridPane.setHalignment(token, HPos.CENTER);
            GridPane.setValignment(token, VPos.CENTER);
            gridPane.getChildren().add(token);

            // Alternating the token color for the next player
            selectedTokenColor = (selectedTokenColor == Color.YELLOW) ? Color.RED : Color.YELLOW;

            // Update the text according to the current player
            textField.setText("C'est le tour de " + (selectedTokenColor == Color.YELLOW ? nomJoueur1 : nomJoueur2));

            // Start or stop the timeline based on demarrerTemps
            if (demarrerTemps) {
                // Start the timeline
                if (timeline != null && timeline.getStatus() != Timeline.Status.RUNNING) {
                    timeline.play();
                }
            }
            // Check if the grid is full
            if (gridPane.getChildren().size() == gridPane.getRowCount() * gridPane.getColumnCount()) {
                // Stop the timeline if the grid is full
                if (timeline != null) {
                    timeline.stop();
                }
            }

            // Remove the circles that were taken out from the grid, except the one placed at the token position
            circlesToRemove.removeIf(node -> GridPane.getColumnIndex(node) == columnIndex && GridPane.getRowIndex(node) == row);
            gridPane.getChildren().addAll(circlesToRemove);


            // Déterminer le nom du joueur en fonction de la couleur du jeton
            String playerName = (selectedTokenColor == Color.RED) ? nomJoueur1 : nomJoueur2;
            VBox movesHistory = (selectedTokenColor == Color.RED) ? yellowMovesHistory : redMovesHistory;

            // Inverser l'affectation des noms de joueurs si la couleur du jeton est jaune
            if (selectedTokenColor == Color.YELLOW) {
                playerName = nomJoueur2; // Le joueur jaune est nomJoueur1, donc nomJoueur2 ici
                movesHistory = redMovesHistory; // Historique rouge pour le joueur jaune
            } else if (selectedTokenColor == Color.RED) {
                playerName = nomJoueur1; // Le joueur rouge est nomJoueur2, donc nomJoueur1 ici
                movesHistory = yellowMovesHistory; // Historique jaune pour le joueur rouge
            }

            // Création du label pour le mouvement
            Label moveDetails = new Label(" " + playerName + ", " + " Ligne " + (row) + ", " + " Colonne " + (columnIndex) + ", " + " Durée: " + secondsElapsed + " secondes.\n\n");
            // Couleur du texte selon la couleur du jeton
            Color textColor = (selectedTokenColor == Color.YELLOW) ? Color.DARKRED : Color.YELLOW;
            moveDetails.setTextFill(textColor);
            movesHistory.getChildren().add(moveDetails); // Ajout du label à l'historique
        }
    }

    // Modifiez la méthode supprimerCercles pour supprimer les instances de Cercle
    private List<Node> supprimerCercles(int columnIndex) {
        List<Node> cerclesASupprimer = new ArrayList<>();
        // Recherchez les cercles existants dans la colonne
        for (Node node : gridPane.getChildren()) {
            if (node instanceof Cercle && GridPane.getColumnIndex(node) == columnIndex) {
                cerclesASupprimer.add(node);
            }
        }

        // Supprimez les cercles de la grille
        gridPane.getChildren().removeAll(cerclesASupprimer);

        return cerclesASupprimer; // Retourne la liste des cercles supprimés
    }

    private int trouverLigneVide(int columnIndex) {
        for (int row = 6; row >= 1; row--) { // Commencez depuis la ligne 6
            Jeton jeton = (Jeton) getJeton(columnIndex, row);
            if (jeton == null) {
                return row;
            }
        }
        return -1; // La colonne est pleine
    }

    private Node getJeton(int columnIndex, int rowIndex) {
        for (Node node : gridPane.getChildren()) {
            Integer nodeColumnIndex = GridPane.getColumnIndex(node);
            Integer nodeRowIndex = GridPane.getRowIndex(node);

            // Vérifier si les indices de colonne et de ligne sont non nuls et correspondent
            if (nodeColumnIndex != null && nodeRowIndex != null
                    && nodeColumnIndex == columnIndex && nodeRowIndex == rowIndex) {
                // Assurez-vous que le nœud est du type attendu (peut être un Circle ou un autre type de jeton)
                if (node instanceof Cercle || node instanceof Jeton) {
                    return node;
                }
            }
        }
        return null; // Aucun jeton trouvé dans cette case
    }

    // Méthode pour réinitialiser la partie
    private void reinitialiserGrille() {
        // Supprimez tous les cercles de la grille
        gridPane.getChildren().removeIf(node -> node instanceof Jeton);
    }

    // Méthode pour réinitialiser la durée
    private void reinitialiserDuree() {
        secondsElapsed = 0;
        updateDuration(null); // Mettez à jour la durée à zéro
    }

    // Méthode pour réinitialiser le texte du champ de texte
    private void reinitialiserTextField() {
        premierClic = true;
        textField.setText("Que la partie commence !");
    }

    @FXML
    private void handleHumanVsHuman(ActionEvent event) {
        reinitialiserJeu();
        // Vérifier si le type de jeu a changé
        if (!"Human vs Human".equals(gameTypeTextField.getText())) {
            // Si le type de jeu a changé, demander les informations à l'utilisateur
            TextInputDialog firstUsernameDialog = new TextInputDialog();
            firstUsernameDialog.setTitle("Nom d'utilisateur");
            firstUsernameDialog.setHeaderText(null);
            firstUsernameDialog.setContentText("Entrez votre nom d'utilisateur :");
            Optional<String> firstUsernameResult = firstUsernameDialog.showAndWait();

            TextInputDialog secondUsernameDialog = new TextInputDialog();
            secondUsernameDialog.setTitle("Nom d'utilisateur du deuxième joueur ");
            secondUsernameDialog.setHeaderText(null);
            secondUsernameDialog.setContentText("Entrez le nom d'utilisateur du deuxième joueur :");
            Optional<String> secondUsernameResult = secondUsernameDialog.showAndWait();

            ChoiceDialog<String> ordreDialog = new ChoiceDialog<>("Premier", "Premier", "Deuxième");
            ordreDialog.setTitle("Ordre de jeu");
            ordreDialog.setHeaderText(null);
            ordreDialog.setContentText("Choisissez l'ordre de jeu :");
            Optional<String> ordreResult = ordreDialog.showAndWait();

            List<String> colorNames = List.of("YELLOW", "RED");
            ChoiceDialog<String> couleurDialog = new ChoiceDialog<>(colorNames.get(0), colorNames);
            couleurDialog.setTitle("Couleur du jeton");
            couleurDialog.setHeaderText(null);
            couleurDialog.setContentText("Choisissez la couleur de votre jeton :");
            Optional<String> colorNameResult = couleurDialog.showAndWait();
            Optional<Color> couleurResult = colorNameResult.map(colorName -> {
                return switch (colorName) {
                    case "YELLOW" -> Color.YELLOW;
                    case "RED" -> Color.RED;
                    // Add more color cases if needed
                    default -> null; // Handle the case when an invalid color is selected
                };
            });

            // Si l'utilisateur a saisi des informations, mettez à jour le jeu
            if (firstUsernameResult.isPresent() && couleurResult.isPresent() && ordreResult.isPresent() && secondUsernameResult.isPresent()) {
                // Mettez à jour le texte et la couleur du jeton en fonction des informations du joueur
                mettreAJourTypePartie("Human vs Computer");
                selectedGameMode = "Human vs Computer";
                selectedOrdre = ordreResult.get();
                selectedTokenColor = couleurResult.get();

                // Mettez à jour les labels avec le nom d'utilisateur en fonction du choix du jeton et de l'ordre
                if (Color.YELLOW.equals(selectedTokenColor)) {
                    nomJoueur1 = firstUsernameResult.get();
                    nomJoueur2 = secondUsernameResult.get();
                } else {
                    nomJoueur1 = secondUsernameResult.get();
                    nomJoueur2 = firstUsernameResult.get();
                }

                usernameLabelJoueur1.setText(nomJoueur1);
                usernameLabelJoueur2.setText(nomJoueur2);

                // Afficher le premier joueur dans le texte en fonction de l'ordre choisi
                if ("Premier".equals(selectedOrdre) && Color.YELLOW.equals(selectedTokenColor)) {
                    textField.setText("C'est le tour de " + nomJoueur1);
                } else if ("Premier".equals(selectedOrdre) && Color.RED.equals(selectedTokenColor)) {
                    textField.setText("C'est le tour de " + nomJoueur2);
                } else if ("Deuxième".equals(selectedOrdre) && Color.YELLOW.equals(selectedTokenColor)) {
                    textField.setText("C'est le tour de " + nomJoueur2);
                } else if ("Deuxième".equals(selectedOrdre) && Color.RED.equals(selectedTokenColor)) {
                    textField.setText("C'est le tour de " + nomJoueur1);
                }
                // Réinitialiser le jeu sans démarrer le temps
                reinitialiserJeu();
            }
        }
    }

    @FXML
    private void handleHumanVsComputer(ActionEvent event) {
        reinitialiserJeu();
        // Vérifier si le type de jeu a changé
        if (!"Human vs Computer".equals(gameTypeTextField.getText())) {
            // Si le type de jeu a changé, demander les informations à l'utilisateur
            TextInputDialog firstUsernameDialog = new TextInputDialog();
            firstUsernameDialog.setTitle("Nom d'utilisateur");
            firstUsernameDialog.setHeaderText(null);
            firstUsernameDialog.setContentText("Entrez votre nom d'utilisateur :");
            Optional<String> firstUsernameResult = firstUsernameDialog.showAndWait();

            ChoiceDialog<String> ordreDialog = new ChoiceDialog<>("Premier", "Premier", "Deuxième");
            ordreDialog.setTitle("Ordre de jeu");
            ordreDialog.setHeaderText(null);
            ordreDialog.setContentText("Choisissez l'ordre de jeu :");
            Optional<String> ordreResult = ordreDialog.showAndWait();

            List<String> colorNames = List.of("YELLOW", "RED");
            ChoiceDialog<String> couleurDialog = new ChoiceDialog<>(colorNames.get(0), colorNames);
            couleurDialog.setTitle("Couleur du jeton");
            couleurDialog.setHeaderText(null);
            couleurDialog.setContentText("Choisissez la couleur de votre jeton :");
            Optional<String> colorNameResult = couleurDialog.showAndWait();
            Optional<Color> couleurResult = colorNameResult.map(colorName -> {
                return switch (colorName) {
                    case "YELLOW" -> Color.YELLOW;
                    case "RED" -> Color.RED;
                    // Add more color cases if needed
                    default -> null; // Handle the case when an invalid color is selected
                };
            });

            // Si l'utilisateur a saisi des informations, mettez à jour le jeu
            if (firstUsernameResult.isPresent() && couleurResult.isPresent() && ordreResult.isPresent()) {
                // Mettez à jour le texte et la couleur du jeton en fonction des informations du joueur
                mettreAJourTypePartie("Human vs Computer");
                selectedGameMode = "Human vs Computer";
                selectedOrdre = ordreResult.get();
                selectedTokenColor = couleurResult.get();

                // Mettez à jour les labels avec le nom d'utilisateur en fonction du choix du jeton et de l'ordre
                if (Color.YELLOW.equals(selectedTokenColor)) {
                    nomJoueur1 = firstUsernameResult.get();
                    nomJoueur2 = "Computer";
                } else {
                    nomJoueur1 = "Computer";
                    nomJoueur2 = firstUsernameResult.get();
                }

                usernameLabelJoueur1.setText(nomJoueur1);
                usernameLabelJoueur2.setText(nomJoueur2);

                // Afficher le premier joueur dans le texte en fonction de l'ordre choisi
                if ("Premier".equals(selectedOrdre) && Color.YELLOW.equals(selectedTokenColor)) {
                    textField.setText("C'est le tour de " + nomJoueur1);
                } else if ("Premier".equals(selectedOrdre) && Color.RED.equals(selectedTokenColor)) {
                    textField.setText("C'est le tour de " + nomJoueur2);
                } else if ("Deuxième".equals(selectedOrdre) && Color.YELLOW.equals(selectedTokenColor)) {
                    textField.setText("C'est le tour de " + nomJoueur2);
                } else if ("Deuxième".equals(selectedOrdre) && Color.RED.equals(selectedTokenColor)) {
                    textField.setText("C'est le tour de " + nomJoueur1);
                }
                // Réinitialiser le jeu sans démarrer le temps
                reinitialiserJeu();
            }
        }
    }

    // Méthode pour réinitialiser l'ensemble du jeu
    public void reinitialiserJeu() {
        reinitialiserGrille();
        reinitialiserDuree();
        reinitialiserTextField();

        // Assurez-vous que demarrerTemps est réinitialisé à false
        demarrerTemps = false;

        // Arrêtez la timeline s'il est en cours
        if (timeline != null && timeline.getStatus() == Timeline.Status.RUNNING) {
            timeline.stop();
        }
    }

    public void mettreAJourTypePartie(String typePartie) {
        gameTypeTextField.setText(typePartie);
    }

    @FXML
    private void handleHelpButtonAction(ActionEvent event) {
        // Afficher les règles
        ReglesDuJeuPopUp.afficherRegles();
    }

    // Méthode pour injecter la scène
    public void setScene(Scene scene) {
        this.scene = scene;
    }

    @FXML
    private void handleThemeSwitch(MouseEvent event) {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.3), themeCircle);

        if (themeCircle.getTranslateX() == -15) {
            // Cercle à gauche, activer le thème sombre
            scene.getStylesheets().add(getClass().getResource("/dark-theme.css").toExternalForm());
            transition.setToX(15); // Déplace le cercle à droite
        } else {
            // Cercle à droite, activer le thème clair
            scene.getStylesheets().remove(getClass().getResource("/dark-theme.css").toExternalForm());
            transition.setToX(-15); // Déplace le cercle à gauche
        }

        transition.play();
    }

    private List<Match> getMatchList() {
        // Exemple : retourne une liste statique de matchs pour la démo
        List<Match> matchList = new ArrayList<>();
        matchList.add(new Match("Joueur1", "Joueur2", "Gagné"));
        matchList.add(new Match("Joueur3", "Joueur4", "Perdu"));
        // Ajoutez d'autres matchs au besoin

        return matchList;
    }

    public class Match {
        private String player1;
        private String player2;
        private String result;

        public Match(String joueur1, String joueur2, String resultat) {
            this.player1 = joueur1;
            this.player2 = joueur2;
            this.result = resultat;
        }

        public String getPlayer1() {
            return player1;
        }

        public String getPlayer2() {
            return player2;
        }

        public String getResult() {
            return result;
        }

        @Override
        public String toString() {
            return player1 + "," + player2 + "," + result;
        }
    }

    @FXML
    private void showMatchHistory(ActionEvent event) {
        // Exemple : retourne une liste statique de matchs pour la démo
        List<Match> matchList = getMatchList(); // Méthode à implémenter

        // Utilisez la nouvelle classe MatchHistoryWindow pour afficher la fenêtre
        MatchHistoryWindow.display(matchList);
    }
}

package ensisa.puissance4;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

import javafx.scene.control.Label;


public class MainController {
    private boolean premierClic = true;
    @FXML
    private TextField textField;
    @FXML
    private TextField gameTypeTextField;
    @FXML
    private Label usernameLabelJoueur1;
    @FXML
    private Label usernameLabelJoueur2;
    private static String nomJoueur1;
    private static String nomJoueur2;
    private String selectedGameMode; // Choix du mode de jeu
    private Color selectedTokenColor; // Choix de la couleur du jeton
    private String selectedOrdre;
    private Timeline timeline;
    private boolean demarrerTemps = false;
    @FXML
    private GridPane gridPane;
    private Scene scene;
    @FXML
    private Circle themeCircle;
    static String dernierGagnant = "";
    public static int scoreJoueur1 = 0;
    public static int scoreJoueur2 = 0;
    @FXML
    private Label scoreLabel;
    private List<Match> matchList = new ArrayList<>();
    @FXML
    private Label durationLabel;
    public static int secondsElapsed = 0;
    private int currentGameDuration = 0;
    @FXML
    private VBox yellowMovesHistory;
    @FXML
    private VBox redMovesHistory;
    private int nombreToken = 0;
    private int selectedTimeLimit;
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

        // Stockez la limite de temps dans votre MainController
        selectedTimeLimit = userInfo.getSelectedTimeLimit();

        // Déterminer la couleur du premier jeton en fonction du choix de l'utilisateur et de l'ordre de jeu
        if ("Premier".equals(selectedOrdre)) {
            selectedTokenColor = ("YELLOW".equals(userInfo.getSelectedToken())) ? Color.YELLOW : Color.RED;
        } else if ("Deuxième".equals(selectedOrdre)) {
            selectedTokenColor = ("YELLOW".equals(userInfo.getSelectedToken())) ? Color.RED : Color.YELLOW;
            if ("Human vs Computer".equals(selectedGameMode)) {
                handleComputerMove();
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
        }

        if ("Human vs Human".equals(selectedGameMode)) {
            if ("YELLOW".equals(userInfo.getSelectedToken())) {
                usernameLabelJoueur1.setText(userInfo.getUsername());
                usernameLabelJoueur2.setText(userInfo.getSecondusername());
            } else {
                usernameLabelJoueur1.setText(userInfo.getSecondusername());
                usernameLabelJoueur2.setText(userInfo.getUsername());
            }
        }
        if (timeline != null) {
            timeline.stop(); // Arrêter la timeline si elle est en cours
        }
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), this::updateDuration));
        timeline.setCycleCount(Timeline.INDEFINITE);

        themeCircle.setOnMouseClicked(this::handleThemeSwitch);
    }

    // Méthode appelée à chaque intervalle de la Timeline pour mettre à jour la durée
    private void updateDuration(ActionEvent event) {
        int minutes = secondsElapsed / 60;
        int seconds = secondsElapsed % 60;

        // Vérifier si une limite de temps est définie
        if (selectedTimeLimit > 0) {
            // Calculer le temps restant
            int timeRemaining = selectedTimeLimit - currentGameDuration;

            // Mettre à jour le libellé avec le temps restant
            durationLabel.setText(String.format("Temps restant: %02d:%02d", timeRemaining / 60, timeRemaining % 60));

            // Vérifier si le temps est écoulé
            if (timeRemaining <= 0) {
                // Arrêter le jeu, car la limite de temps est atteinte
                stopGameDueToTimeLimit();
                return; // Pour éviter d'incrémenter les secondes après l'arrêt du jeu
            }
        } else {
            // Si aucune limite de temps n'est définie, afficher la durée normale
            durationLabel.setText(String.format("Durée: %02d:%02d", minutes, seconds));
        }

        // Incrémenter les secondes après la mise à jour de l'affichage
        secondsElapsed++;
        currentGameDuration++;
    }

    private void stopGameDueToTimeLimit() {
        if (timeline != null) {
            timeline.stop();
        }
        Platform.runLater(() -> {
            showAlerte("Fin du temps réglementaire", "Le temps imparti est écoulé. La partie se termine avec une égalité.");
        });
    }

    @FXML
    private void handleColumnClick(MouseEvent event) {
        demarrerTemps = true;

        // Initialiser les noms des joueurs
        nomJoueur1 = usernameLabelJoueur1.getText();
        nomJoueur2 = usernameLabelJoueur2.getText();

        if ("Human vs Human".equals(selectedGameMode)) {
            if (premierClic) {
                // Réinitialiser le jeu avant de placer le premier jeton
                reinitialiserJeu();

                textField.setText("C'est le tour de " + nomJoueur1);
                premierClic = false;
                demarrerTemps = true;
            }
        }
        // Logique pour placer le jeton seulement si c'est le tour de l'humain
        Circle clickedCircle = (Circle) event.getSource();
        int columnIndex = GridPane.getColumnIndex(clickedCircle);
        placerJeton(columnIndex);
    }
    private void handleComputerMove() {
        demarrerTemps = true;
        // Initialiser les noms des joueurs
        nomJoueur1 = usernameLabelJoueur1.getText();
        nomJoueur2 = usernameLabelJoueur2.getText();
        if (("Computer".equals(nomJoueur1)) || ("Computer".equals(nomJoueur2))) {
            // Sélectionnez une colonne aléatoire
            Random random = new Random();
            int columnIndex = random.nextInt(7) + 1;
            // Placez un jeton dans la colonne sélectionnée
            placerJeton(columnIndex);
        }
    }
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
            nombreToken++;

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
            if (nombreToken == 42) {
                if (timeline != null) {
                    timeline.stop();
                }
                showAlerte("Egalité !", "Le match a fini par une égalité.");
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
            if (selectedTokenColor == Color.YELLOW) {
                moveDetails.setTextFill(Color.RED); // Changer la couleur du texte à l'or
            } else {
                // Si la couleur n'est pas jaune, utilisez une autre couleur pour le texte
                Color textColor = (selectedTokenColor == Color.RED) ? Color.DARKGOLDENROD : Color.YELLOW;
                moveDetails.setTextFill(textColor);
            }
            movesHistory.getChildren().add(moveDetails); // Ajout du label à l'historique

            // Vérifier si le joueur actuel a gagné après avoir placé le jeton
            if (aGagne()) {
                if (timeline != null) {
                    timeline.stop();
                }
                dernierGagnant = playerName;
                showAlerte("Victoire !", "Le joueur " + playerName + " a gagné !");
            }
            // Check if it's the computer's turn
            if (textField.getText().equals("C'est le tour de Computer")) {
                handleComputerMove();
            }
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
    private void handleHumanVsHuman() {
        reinitialiserScore();
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
                mettreAJourTypePartie("Human vs Human");
                selectedGameMode = "Human vs Human";
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
    private void handleHumanVsComputer() {
        reinitialiserJeu();
        reinitialiserScore();
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

    private void effacerHistorique() {
        yellowMovesHistory.getChildren().clear();
        redMovesHistory.getChildren().clear();
    }
    // Méthode pour réinitialiser l'ensemble du jeu
    public void reinitialiserJeu() {
        reinitialiserGrille();
        reinitialiserDuree();
        reinitialiserTextField();
        effacerHistorique();

        // Assurez-vous que demarrerTemps est réinitialisé à false
        demarrerTemps = false;
        nombreToken = 0;

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
    public void addMatchToHistory() {
        String joueurPerdant = (dernierGagnant.equals(nomJoueur1)) ? nomJoueur2 : nomJoueur1;
        // Ajoute le match actuel à la liste globale
        if (joueurPerdant != null && !dernierGagnant.isEmpty()) {
            // Ajoute le match actuel à la liste globale
            matchList.add(0, new Match(dernierGagnant, joueurPerdant, currentGameDuration));
        }
    }
    @FXML
    private void showMatchHistory(ActionEvent event) {
        // Affiche l'historique des matchs
        MatchHistoryWindow.display(matchList);
    }
    private boolean aGagne() {
        // Vérifie les horizontales ( - )
        for (int ligne = 0; ligne < 7; ligne++) {
            if (countJetonsAlignes(0, ligne, 1, 0)) {
                return true;
            }
        }

        // Vérifie les verticales ( ¦ )
        for (int col = 0; col < 8; col++) {
            if (countJetonsAlignes(col, 0, 0, 1)) {
                return true;
            }
        }

        // Diagonales (cherche depuis la ligne du bas)
        for (int col = 0; col < 8; col++) {
            // Première diagonale ( / )
            if (countJetonsAlignes(col, 0, 1, 1)) {
                return true;
            }
            // Deuxième diagonale ( \ )
            if (countJetonsAlignes(col, 0, -1, 1)) {
                return true;
            }
        }
        // Diagonales (cherche depuis les colonnes gauches et droites)
        for (int ligne = 0; ligne < 7; ligne++) {
            // Première diagonale ( / )
            if (countJetonsAlignes(0, ligne, 1, 1)) {
                return true;
            }
            // Deuxième diagonale ( \ )
            if (countJetonsAlignes(7, ligne, -1, 1)) {
                return true;
            }
        }
        return false;
    }

    private boolean countJetonsAlignes(int oCol, int oLigne, int dCol, int dLigne) {
        int count = 1;
        Color targetColor = selectedTokenColor;

        int curCol = oCol;
        int curRow = oLigne;

        while (count < 4 && curCol + dCol >= 0 && curCol + dCol < 8 && curRow + dLigne >= 0 && curRow + dLigne < 7) {
            curCol += dCol;
            curRow += dLigne;

            Jeton jeton = (Jeton) getJeton(curCol, curRow);

            if (jeton != null && memeCouleur(curCol, curRow, targetColor)) {
                // Si la couleur du jeton est celle recherchée, on l'incrémente
                count++;
            } else {
                // Si la couleur change ou le jeton est null, on réinitialise le compteur
                targetColor = (jeton != null) ? jeton.getCouleur() : null;
                count = 1;
            }
        }

        return count >= 4;
    }

    private boolean memeCouleur(int column, int row, Color targetColor) {
        Node node = getJeton(column, row);
        return node instanceof Jeton && ((Jeton) node).getCouleur() == targetColor;
    }

    private void showAlerte(String titre, String message) {
        // Créer une boîte de dialogue
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(titre);
        dialog.setHeaderText(null);
        dialog.setContentText(message);

        // Ajouter les boutons "Refaire une partie" et "Changer de mode"
        ButtonType refairePartieButton = new ButtonType("Refaire une partie", ButtonBar.ButtonData.OK_DONE);
        ButtonType changerModeButton = new ButtonType("Changer de mode", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(refairePartieButton, changerModeButton);

        // Afficher la boîte de dialogue et attendre que l'utilisateur clique sur l'un des boutons
        Optional<ButtonType> result = dialog.showAndWait();

        // Traiter la réponse de l'utilisateur
        result.ifPresent(buttonType -> {
            if (buttonType == refairePartieButton) {
                // L'utilisateur a cliqué sur "Refaire une partie"
                incrementerScore();
                reinitialiserJeu();
            } else if (buttonType == changerModeButton) {
                // L'utilisateur a cliqué sur "Changer de mode"
                // Afficher un nouveau dialogue avec les options "Human vs Human" et "Human vs Computer"
                ChoiceDialog<String> modeDialog = new ChoiceDialog<>("Human vs Human", "Human vs Computer");
                modeDialog.setTitle("Choisir un mode");
                modeDialog.setHeaderText(null);
                modeDialog.setContentText("Choisissez le mode de jeu :");

                Optional<String> modeResult = modeDialog.showAndWait();
                modeResult.ifPresent(selectedMode -> {
                    // L'utilisateur a choisi un mode, appeler les actions correspondantes
                    if ("Human vs Human".equals(selectedMode)) {
                        reinitialiserScore();
                        handleHumanVsHuman();
                    } else if ("Human vs Computer".equals(selectedMode)) {
                        reinitialiserScore();
                        handleHumanVsComputer();
                    }
                });
            }
        });
        // Maintenant, ajoutez le match à l'historique
        addMatchToHistory();
        currentGameDuration = 0;
    }
    private void incrementerScore() {
        if (nomJoueur1 == dernierGagnant) {
            scoreJoueur1++;
        } else if (nomJoueur2 == dernierGagnant) {
            scoreJoueur2++;
        }

        // Mettez à jour l'étiquette du score
        scoreLabel.setText("Score: " + scoreJoueur1 + ":" + scoreJoueur2);
    }
    private void reinitialiserScore() {
        scoreJoueur1 = 0;
        scoreJoueur2 = 0;

        // Mettez à jour l'étiquette du score
        scoreLabel.setText("Score: 0:0");
    }
}

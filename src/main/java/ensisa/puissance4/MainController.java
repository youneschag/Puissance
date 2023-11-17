package ensisa.puissance4;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class MainController {
    @FXML
    private void handleExitButtonAction(ActionEvent event) {
        Platform.exit();
        System.exit(0);
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
    private String selectedGameMode; // Choix du mode de jeu
    private Color selectedTokenColor; // Choix de la couleur du jeton
    private String selectedOrdre;
    private Timeline timeline;
    private boolean demarrerTemps = false;
    @FXML
    private GridPane gridPane;

    // Méthode pour initialiser les éléments de jeu
    @FXML
    public void initialiserJeu() {
        // Afficher les règles
        ReglesDuJeuPopUp.afficherRegles();

        // Obtenir les informations de l'utilisateur
        UserInfo userInfo = UserInfoPopUp.obtenirInfosUtilisateur();

        // Utilisez les informations de l'utilisateur comme nécessaire
        System.out.println("Nom d'utilisateur : " + userInfo.getUsername());
        System.out.println("Jeton choisi : " + userInfo.getSelectedToken());
        System.out.println("Mode de jeu choisi : " + userInfo.getSelectedGameMode());

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

            textField.setText("C'est le tour du Joueur 1/2");
            premierClic = false;
            demarrerTemps = true; // Démarrer le temps lorsque le premier jeton est posé
        }

        Circle clickedCircle = (Circle) event.getSource();
        int columnIndex = GridPane.getColumnIndex(clickedCircle);
        // Logique pour placer le jeton dans la colonne columnIndex
        placerJeton(columnIndex);
    }

    public void placerJeton(int columnIndex) {
        // Remove existing cercles in the clicked column and store them
        List<Node> cerclesASupprimer = supprimerCercles(columnIndex);

        int row = trouverLigneVide(columnIndex);

        // Vérifier si la colonne est pleine
        if (row != -1) {
            // Placer le jeton dans la grille avec la couleur actuelle
            Jeton jeton = new Jeton(25, selectedTokenColor);
            GridPane.setColumnIndex(jeton, columnIndex);
            GridPane.setRowIndex(jeton, row);
            GridPane.setHalignment(jeton, HPos.CENTER);
            GridPane.setValignment(jeton, VPos.CENTER);
            gridPane.getChildren().add(jeton);

            // Alterner la couleur du jeton pour le prochain joueur
            selectedTokenColor = (selectedTokenColor == Color.YELLOW) ? Color.RED : Color.YELLOW;

            // Mettre à jour le texte en fonction du joueur actuel
            textField.setText("C'est le tour du Joueur " + (selectedTokenColor == Color.YELLOW ? "1" : "2"));

            // Démarrer ou arrêter la timeline en fonction de demarrerTemps
            if (demarrerTemps) {
                // Démarrer la timeline
                if (timeline != null && timeline.getStatus() != Timeline.Status.RUNNING) {
                    timeline.play();
                }
            }

            // Vérifier si la grille est pleine
            if (gridPane.getChildren().size() == gridPane.getRowCount() * gridPane.getColumnCount()) {
                // Arrêter la timeline si la grille est pleine
                if (timeline != null) {
                    timeline.stop();
                }
            }

            // Ajouter les cercles supprimés à nouveau à la grille, sauf celui situé à la position du jeton
            cerclesASupprimer.removeIf(node -> GridPane.getColumnIndex(node) == columnIndex && GridPane.getRowIndex(node) == row);
            gridPane.getChildren().addAll(cerclesASupprimer);
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
        reinitialiserJeu(); // Appel de la méthode de réinitialisation sans démarrer le temps
        gameTypeTextField.setText("Human vs Human");
        // Ajoutez ici la logique pour le mode Human vs Human
    }

    @FXML
    private void handleHumanVsComputer(ActionEvent event) {
        reinitialiserJeu(); // Appel de la méthode de réinitialisation sans démarrer le temps
        gameTypeTextField.setText("Human vs Computer");
        // Ajoutez ici la logique pour le mode Human vs Computer
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
}

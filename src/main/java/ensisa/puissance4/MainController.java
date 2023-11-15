package ensisa.puissance4;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
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
    @FXML
    private Circle jetonJoueur1;

    @FXML
    private Circle jetonJoueur2;

    // Méthode pour placer le jeton du joueur 1
    public void placerJetonJoueur1() {
        // Logique pour placer le jeton du joueur 1
    }

    // Méthode pour placer le jeton du joueur 2
    public void placerJetonJoueur2() {
        // Logique pour placer le jeton du joueur 2
    }
    @FXML
    private Label labelJoueur1;
    @FXML
    private Label labelJoueur2;
    @FXML
    private GridPane gridPane;
    private Color couleurJetonActuel = Color.YELLOW;

    // Méthode pour initialiser les éléments de jeu
    @FXML
    public void initialiserJeu() {
        labelJoueur1.setText("Joueur 1 :");
        labelJoueur2.setText("Joueur 2 :");
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), this::updateDuration));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    @FXML
    private Label durationLabel;
    private int secondsElapsed = 0;

    // Méthode appelée à chaque intervalle de la Timeline pour mettre à jour la durée
    private void updateDuration(ActionEvent event) {
        secondsElapsed++;
        int minutes = secondsElapsed / 60;
        int seconds = secondsElapsed % 60;
        durationLabel.setText(String.format("Durée: %02d:%02d", minutes, seconds));
    }
    @FXML
    private void handleColumnClick(MouseEvent event) {
        Circle clickedCircle = (Circle) event.getSource();
        int columnIndex = GridPane.getColumnIndex(clickedCircle);

        // Logique pour placer le jeton dans la colonne columnIndex
        placerJeton(columnIndex);
    }
    public void placerJeton(int columnIndex) {
        // Remove existing circles in the clicked column and store them
        List<Node> cerclesASupprimer = supprimerCercles(columnIndex);

        int row = trouverLigneVide(columnIndex);

        // Vérifier si la colonne est pleine
        if (row != -1) {
            // Placer le jeton dans la grille avec la couleur actuelle
            Circle jeton = new Circle(25, couleurJetonActuel);
            GridPane.setColumnIndex(jeton, columnIndex);
            GridPane.setRowIndex(jeton, row);
            GridPane.setHalignment(jeton, HPos.CENTER);
            GridPane.setValignment(jeton, VPos.CENTER);
            gridPane.getChildren().add(jeton);

            // Alterner la couleur du jeton pour le prochain joueur
            couleurJetonActuel = (couleurJetonActuel == Color.YELLOW) ? Color.RED : Color.YELLOW;

            // Ajouter les cercles supprimés à nouveau à la grille, sauf celui situé à la position du jeton
            cerclesASupprimer.removeIf(node -> GridPane.getColumnIndex(node) == columnIndex && GridPane.getRowIndex(node) == row);
            gridPane.getChildren().addAll(cerclesASupprimer);
        }
    }

    // Modifiez la méthode supprimerCercles pour renvoyer la liste des cercles supprimés
    private List<Node> supprimerCercles(int columnIndex) {
        List<Node> cerclesASupprimer = new ArrayList<>();

        // Recherchez les cercles existants dans la colonne
        for (Node node : gridPane.getChildren()) {
            if (node instanceof Circle && GridPane.getColumnIndex(node) == columnIndex) {
                cerclesASupprimer.add(node);
            }
        }

        // Supprimez les cercles de la grille
        gridPane.getChildren().removeAll(cerclesASupprimer);

        return cerclesASupprimer; // Retourne la liste des cercles supprimés
    }


    private int trouverLigneVide(int columnIndex) {
        for (int row = 6; row >= 1; row--) { // Commencez depuis la ligne 6
            Circle jeton = getJeton(columnIndex, row);
            if (jeton == null) {
                return row; // La première case vide dans la colonne
            }
        }
        return -1; // La colonne est pleine
    }
    private Circle getJeton(int columnIndex, int rowIndex) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == columnIndex && GridPane.getRowIndex(node) == rowIndex) {
                return (Circle) node;
            }
        }
        return null; // Aucun jeton trouvé dans cette case
    }
}
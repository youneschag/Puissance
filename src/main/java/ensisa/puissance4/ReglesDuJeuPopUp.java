package ensisa.puissance4;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ReglesDuJeuPopUp {

    public static void afficherRegles() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Règles du jeu");
        alert.setHeaderText(null);
        alert.setContentText("Bienvenue dans le jeu Puissance 4!\n\n"
                + "Règles du jeu :\n"
                + "Le but du jeu est d'aligner 4 jetons de votre couleur (jaune ou rouge) "
                + "horizontalement, verticalement ou en diagonale sur le plateau.\n\n"
                + "Commandes :\n"
                + "Cliquez sur une colonne pour placer un jeton.\n\n"
                + "Modes de jeu :\n"
                + "Vous pouvez jouer en mode Joueur contre Joueur ou Joueur contre Ordinateur.\n\n"
                + "Amusez-vous bien!");

        alert.showAndWait();
    }
}

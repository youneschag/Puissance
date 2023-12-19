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
                + "Cliquez sur un cercle d'une colonne pour placer un jeton.\n\n"
                + "Vous pouvez choisir le light ou dark thème, changer de mode, ou meme refaire une partie; "
                + "tout cela en visualisant l'historique de chaque jeton durant la partie ou alors l'historique des matches "
                + " à chaque fin de partie, avec le vainqueur, et la durée de la partie à chaque fois.\n\n"
                + "Modes de jeu :\n"
                + "Vous pouvez jouer en mode 'Joueur contre Joueur' ou 'Joueur contre Ordinateur'.\n\n"
                + "Petites astuces: \n"
                + "Vous pouvez visualisez votre score contre votre adversaire, ainsi que le tour de quel joueur "
                + "à n'importe quel moment de la partie. \n\n"
                + "Amusez-vous bien!");

        alert.showAndWait();
    }
}

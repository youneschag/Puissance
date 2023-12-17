package ensisa.puissance4;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Match {
    private String joueur1;
    private String joueur2;
    private int duration;

    public Match(String joueur1, String joueur2, int duration) {
        this.joueur1 = joueur1;
        this.joueur2 = joueur2;
        this.duration = duration;
    }
    public String getMatchResult() {
        if (MainController.scoreJoueur1 == 0 && MainController.scoreJoueur2 == 0) {
            return "Aucun match n'a encore été joué.";
        } else {
            // Obtenir la date et l'heure actuelles
            LocalDateTime now = LocalDateTime.now();

            // Formater la date et l'heure
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm:ss");
            String formattedDateTime = now.format(formatter);

            // Retourner le résultat du match avec la date et l'heure
            return joueur1 + " a gagné face à " + joueur2 + " dans une durée de " + duration + " secondes,  le " + formattedDateTime;
        }
    }
}


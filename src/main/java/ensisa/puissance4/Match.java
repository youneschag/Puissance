package ensisa.puissance4;

import javafx.scene.paint.Color;

public class Match {
    private String joueur1;
    private String joueur2;
    private boolean resultat;
    private int duration;

    public Match(String joueur1, String joueur2, boolean resultat, int duration) {
        this.joueur1 = joueur1;
        this.joueur2 = joueur2;
        this.resultat = resultat;
        this.duration = duration;
    }

    public String getJoueur1() {
        return joueur1;
    }

    public String getJoueur2() {
        return joueur2;
    }

    public boolean getResultat() {
        return resultat;
    }

    public int getDuration() {
        return duration;
    }

    public String getMatchResult(Color selectedTokenColor) {
        if (duration == 0) {
            return "Aucun match n'a encore été joué.";
        } else {
            String winner = resultat ? getWinnerName(selectedTokenColor) : getLoserName(selectedTokenColor);
            String loser = resultat ? getLoserName(selectedTokenColor) : getWinnerName(selectedTokenColor);
            return winner + " a gagné face à " + loser + " dans une durée de " + duration + " secondes";
        }
    }

    private String getWinnerName(Color selectedTokenColor) {
        String nomJoueur1 = "";
        String nomJoueur2 = "";
        return (selectedTokenColor == Color.YELLOW) ? nomJoueur1 : nomJoueur2;
    }

    private String getLoserName(Color selectedTokenColor) {
        String nomJoueur2 = "";
        String nomJoueur1 = "";
        return (selectedTokenColor == Color.YELLOW) ? nomJoueur2 : nomJoueur1;
    }
}


package ensisa.puissance4;

public class Match {
    private String joueur1;
    private String joueur2;
    private boolean resultat;
    private static int secondsElapsed = 0;

    public Match(String joueur1, String joueur2, boolean resultat, int secondsElapsed) {
        this.joueur1 = joueur1;
        this.joueur2 = joueur2;
        this.resultat = resultat;
        this.secondsElapsed = secondsElapsed;
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

    public static int getSecondsElapsed() {
        return secondsElapsed;
    }

    public String getMatchResult() {
        if (secondsElapsed == 0) {
            return "Aucun match n'a encore été joué.";
        } else {
            String winner = resultat ? getWinnerName() : getLoserName();
            String loser = resultat ? getLoserName() : getWinnerName();
            return winner + " a gagné face à " + loser + " dans une durée de " + secondsElapsed + " secondes";
        }
    }

    private String getWinnerName() {
        return (resultat) ? joueur1 : joueur2;
    }

    private String getLoserName() {
        return (resultat) ? joueur2 : joueur1;
    }
}


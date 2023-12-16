package ensisa.puissance4;

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
            return joueur1 + " a gagné face à " + joueur2 + " dans une durée de " + duration+ " secondes";
        }
    }
}


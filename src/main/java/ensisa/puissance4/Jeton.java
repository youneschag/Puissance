package ensisa.puissance4;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Jeton extends Circle {
    private Color couleur;

    // Constructor that takes the color as a parameter
    public Jeton(double radius, Color couleur) {
        super(radius);
        this.couleur = couleur;
        this.setFill(couleur);
    }

    // Getter for couleur
    public Color getCouleur() {
        return couleur;
    }

    // Setter for couleur
    public void setCouleur(Color couleur) {
        this.couleur = couleur;
        this.setFill(couleur);
    }
}

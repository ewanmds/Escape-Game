package Enigme;

import Decor.Personnage;

public class Indice {
    private int x, y;
    private String message;

    public Indice(int x, int y, String message) {
        this.x = x;
        this.y = y;
        this.message = message;
    }

    //Vérifie si le personnage est proche de l'indice
    public boolean estProcheIndice(Personnage p) {
        int XcentrePerso = p.getX() + p.getLargeur() / 2;
        int YcentrePerso = p.getY() + p.getLargeur() / 2;
        double distance = Math.sqrt(Math.pow((XcentrePerso - (this.x - 100)), 2) + Math.pow((YcentrePerso - (this.y - 100)), 2));
        return distance < 200;
    }

    // Getters & Setters
    public int getX() { return x;}
    public int getY() { return y;}
    public String getMessage() { return message;}
}
package Enigme;

import Decor.Personnage;

import java.util.ArrayList;

public class EnigmePostePolice implements Enigmes {
    private int x, y;
    private String message;
    private ArrayList<Indice> indices = new ArrayList<>();

    public EnigmePostePolice(){
        this.x = 40;
        this.y = 120;
        this.message = "BONJOUR CACA \nBONJOUR CACA\n BONJOUR CACA";
    }

    public void ajouterIndice(Indice indice) {
        indices.add(indice);
    }

    public boolean estProcheMessage(Personnage p){
        int XcentrePerso = p.getX()+ p.getHauteur()/2 + p.getLargeur()/2;
        int YcentrePerso = p.getY() + p.getHauteur()/2 + p.getLargeur()/2;
        double distance = Math.sqrt(Math.pow(XcentrePerso - x,2) +Math.pow(YcentrePerso - x,2));
        return distance < 750;
    }

    public Indice estProcheIndice(Personnage p){
        int XcentrePerso = p.getX()  + p.getLargeur() / 2;
        int YcentrePerso = p.getY()  + p.getLargeur() / 2;
        for (Indice indice : indices) {
            double distance = Math.sqrt(Math.pow(XcentrePerso - indice.getX()-100, 2) + Math.pow(YcentrePerso - indice.getY()-100, 2));
            if (distance < 100) {
                return indice;
            }
        }
        return null;
    }

    public String getMessage() {
        return message;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public ArrayList<Indice> getIndices() {
        return indices;
    }

}

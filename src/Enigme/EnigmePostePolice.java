package Enigme;

import Decor.Personnage;

import java.util.ArrayList;

public class EnigmePostePolice implements Enigmes {
    private ArrayList<Indice> indices = new ArrayList<>();

    public EnigmePostePolice(){
    }

    public void ajouterIndice(Indice indice) {
        indices.add(indice);
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

    public ArrayList<Indice> getIndices() {
        return indices;
    }

}

package Enigme;

import Decor.Personnage;
import Decor.Piece;

import java.util.ArrayList;

public class EnigmePostePolice {
    private int x, y;
    private Piece piece;
    private String message;
    private ArrayList<String> indice;

    public EnigmePostePolice(int x, int y, String message, Piece piece){
        this.x = x;
        this.y = y;
        this.message = message;
        this.piece = piece;

    }

    public boolean estProcheMessage(Personnage p){
        int XcentrePerso = p.getX()+ p.getHauteur()/2 + p.getLargeur()/2;
        int YcentrePerso = p.getY() + p.getHauteur()/2 + p.getLargeur()/2;
        double distance = Math.sqrt(Math.pow(XcentrePerso - x,2) +Math.pow(YcentrePerso - x,2));
        return distance < 100;
    }

    public boolean estProcheIndice(Personnage p){

        int x=0;
        return false;
    }

}

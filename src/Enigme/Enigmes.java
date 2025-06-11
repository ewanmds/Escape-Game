package Enigme;

import Decor.Personnage;

import java.util.ArrayList;

public interface Enigmes {
    boolean estProcheMessage(Personnage p);
    Indice estProcheIndice(Personnage p);
    public void ajouterIndice(Indice indice);
    String getMessage();
    int getX();
    int getY();
    public ArrayList<Indice> getIndices();
}
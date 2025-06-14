package Enigme;

import Decor.Personnage;

import java.util.ArrayList;

public interface Enigmes {
    Indice estProcheIndice(Personnage p);
    public void ajouterIndice(Indice indice);
    public ArrayList<Indice> getIndices();
}
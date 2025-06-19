package Enigme;

import Jeu.Jeux;
import java.awt.event.*;

public class Souris extends MouseAdapter{
    Jeux d;
    public Souris (Jeux a){
        this.d = a;
    }

    public void mouseClicked(MouseEvent m) {
        int x = m.getX();
        int y = m.getY();
        //d.affect(x, y);
    }
}

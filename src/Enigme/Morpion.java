package Enigme;

import javax.swing.*;
import java.awt.*;

public class Morpion extends JFrame {
    public int x, y;
    public static int [][] grille = {{0,0,0},{0,0,0},{0,0,0}};

    public Morpion() {
        x = 0;
        y = 0;
    }

    public void paintComponent(Graphics g) {

    }

    public void clic(int x, int y){}

    public void dessiner(Graphics2D g, int x, int y){}

    public void Reset() {
    }
}

package Enigme;

import java.awt.*;

public class Morpion {
    public int x, y;
    public static int [][] grille = new int [3][3];

    public Morpion() {

    }

    public void affect(int x, int y, int largeur, int hauteur) {
        this.x = x;
        this.y = y;

        int x1 = 0;
        int y1 = 0;
        int L = largeur;
        int l = hauteur;

        int colonne = (x - x1) / (L / 3);
        int ligne = (y - y1) / (l / 3);

        if (x > x1 && x < x1 + L && y > y1 && y < y1 + l) {
            if (ligne >= 0 && ligne < 3 && colonne >= 0 && colonne < 3 && grille[ligne][colonne] == 0) {
                grille[ligne][colonne] = 1;
            }
        }
    }

    public void dessiner(Graphics2D g, int largeur, int hauteur){
        int x1 = 0;
        int y1 = 0;

        g.setColor(Color.blue);
        g.drawRect(x1,y1, largeur, hauteur);
        g.drawLine(x1+ largeur /3, y1, x1+ largeur /3, y1+ hauteur);
        g.drawLine(x1, y1+ hauteur /3, x1+ largeur, y1+ hauteur /3);
        g.drawLine(x1+ largeur *2/3, y1, x1+ largeur *2/3, y1+ hauteur);
        g.drawLine(x1, y1+ hauteur *2/3, x1+ largeur, y1+ hauteur *2/3);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Morpion", largeur/2, 50);


        for (int k = 0; k<3; k++) {
            for (int n = 0; n<3; n++) {
                if (grille[k][n] == 1) {
                    g.drawLine(x1+n* largeur /3,y1+k* hauteur /3,x1+(n+1)* largeur /3,y1+(k+1)* hauteur /3);
                    g.drawLine(x1+n* largeur /3,y1+(k+1)* hauteur /3,x1+(n+1)* largeur /3,y1+k* hauteur /3);
                }
                if (grille[k][n] == 2) {
                    g.drawOval(x1 + n* largeur /3, y1 + k* hauteur /3, largeur /3, hauteur /3);
                }
            }
        }
    }

    public void Reset() {
        grille = new int [3][3];
    }
    public static int detecterVictoire(int[][] grille) {

        for (int i = 0; i < 3; i++) {
            if (grille[i][0] != 0 && grille[i][0] == grille[i][1] && grille[i][1] == grille[i][2]) {
                return grille[i][0];
            }
        }
        for (int i = 0; i < 3; i++) {
            if (grille[0][i] != 0 && grille[0][i] == grille[1][i] && grille[1][i] == grille[2][i]) {
                return grille[0][i];
            }
        }
        if (grille[0][0] != 0 && grille[0][0] == grille[1][1] && grille[1][1] == grille[2][2]) {
            return grille[0][0];
        }
        if (grille[0][2] != 0 && grille[0][2] == grille[1][1] && grille[1][1] == grille[2][0]) {
            return grille[0][2];
        }
        return 0;
    }
}

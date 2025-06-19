package Enigme;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.Random;

public class Morpion {
    public int x, y;
    public static int [][] grille = new int [3][3];
    public static boolean tour = false;
    private static int i = 0;
    private static boolean victoire = false;
    private static boolean fin = false;
    public Morpion() {

    }
//test
    public void affect(int x, int y, int largeur, int hauteur) {
        this.x = x;
        this.y = y;

        int x1 = 0;
        int y1 = 0;
        int L = largeur;
        int h = hauteur;

        int colonne = (x - x1) / (L / 3);
        int ligne = (y - y1) / (h / 3);

        if (!tour){
            if (x > x1 && x < x1 + L && y > y1 && y < y1 + h) {
                if (ligne >= 0 && ligne < 3 && colonne >= 0 && colonne < 3 && grille[ligne][colonne] == 0) {
                    grille[ligne][colonne] = 1;
                    i++;
                    tour = true;
                }
            }
        }
        //empeche le bot de jouer si on gagne
        if (detecterVictoire(grille) == 1){
            fin = true;
        }
        if (tour && !fin) {
            //blocage en ligne
            for (int l = 0; l < 3; l++) {
                int nb = 0, nbVide = 0, caseVide = 0;
                for (int c = 0; c < 3; c++) {
                    if (grille[l][c] == 1) {
                        nb++;
                    }
                    else if (grille[l][c] == 0) {
                        nbVide++;
                        caseVide = c;
                    }
                }
                if (nb == 2 && nbVide == 1) {
                    grille[l][caseVide] = 2;
                    i++;
                    tour = false;
                }
            }
        }
        if (tour && !fin) {
            //blocage en colonne
            for (int c = 0; c < 3; c++) {
                int nb = 0, nbVide = 0, caseVide = 0;
                for (int l = 0; l < 3; l++) {
                    if (grille[l][c] == 1) {
                        nb++;
                    }
                    else if (grille[l][c] == 0) {
                        nbVide++;
                        caseVide = l;
                    }
                }
                if (nb == 2 && nbVide == 1) {
                    grille[caseVide][c] = 2;
                    i++;
                    tour = false;
                }
            }
        }
        if (tour && !fin) {
            //blocage en diagonale 1
            int nb = 0, nbVide = 0, caseVide = 0, l = 0;
            for (int c = 0; c < 3; c++) {
                if (grille[l][c] == 1) {
                    nb++;
                }
                else if (grille[l][c] == 0) {
                    nbVide++;
                    caseVide = l;
                }
                l++;
            }
            if (nb == 2 && nbVide == 1) {
                grille[caseVide][caseVide] = 2;
                i++;
                tour = false;
            }
        }
        if (tour && !fin) {
            //blocage en diagonale 1
            int nb = 0, nbVide = 0, caseVideLigne = 0, caseVideColonne = 0, l = 2;
            for (int c = 0; c < 3; c++) {
                if (grille[l][c] == 1) {
                    nb++;
                }
                else if (grille[l][c] == 0) {
                    nbVide++;
                    caseVideLigne = l;
                    caseVideColonne = c;
                }
                l--;
            }
            if (nb == 2 && nbVide == 1) {
                grille[caseVideLigne][caseVideColonne] = 2;
                i++;
                tour = false;
            }
        }
        Random rand = new Random();
        while (tour && i<9 && !fin) {
            int ligneAleatoire = rand.nextInt(3);
            int colonneAleatoire = rand.nextInt(3);
            if (grille[ligneAleatoire][colonneAleatoire] == 0) {
                grille[ligneAleatoire][colonneAleatoire] = 2;
                i++;
                tour = false;
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
        if (detecterVictoire(grille) == 1){
            g.setFont(new Font("Courrier New", Font.ITALIC, 70));
            g.setColor(Color.green);
            victoire = true;
            fin = true;
            drawMultilineString(g, "Tu as gagné !\nechap : sortir du morpion", largeur/2-150, hauteur /2-150, 70);

        }
        if (detecterVictoire(grille) == 2){
            g.setFont(new Font("Courrier New", Font.ITALIC, 70));
            g.setColor(Color.red);
            drawMultilineString(g, "Tu as perdu !\nR : recommencer\nechap : sortir du morpion", largeur/2-150, hauteur /2-150, 70);
            fin = true;
        }
        if (detecterVictoire(grille) == 0){
            g.setFont(new Font("Courrier New", Font.ITALIC, 70));
            g.setColor(Color.black);
            drawMultilineString(g, "Egalité !\nR : recommencer\nechap : sortir du morpion", largeur/2-100, hauteur /2-100, 70);
            fin = true;
        }
    }

    public void Reset() {
        grille = new int [3][3];
        i = 0;
        tour = false;
        fin = false;
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

        if (i == 9){
            return 0;
        }

        return -1;
    }

    public boolean getVictoire() {
        return victoire;
    }

    private void drawMultilineString(Graphics2D g2d, String text, int x, int y, int lineHeight) {
        if (text != null) {
            for (String line : text.split("\n")) {
                g2d.drawString(line, x, y);
                y += lineHeight;
            }
        }
    }
}

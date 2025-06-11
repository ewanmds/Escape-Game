package Decor;

import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;


public class Piece {
    private Image background;
    private String nom;
    private String cheminImage;
    private ArrayList<Obstacle> obstacles = new ArrayList<>();
    private int porteX, porteY, porteLargeur, porteHauteur;
    private Piece pieceDestination;

    public Piece(String nom, String cheminImage, Piece pieceDestination) {
        this.nom = nom;
        this.pieceDestination = pieceDestination;
        this.cheminImage = cheminImage;
        try {
            this.background = ImageIO.read(new File(cheminImage));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public Piece(String nom, String cheminImage) {
        this.nom = nom;
        this.cheminImage = cheminImage;
        try {
            this.background = ImageIO.read(new File(cheminImage));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    // Ajoute un obstacle à la pièce
    public void ajouterObstacle(int x, int y, int largeur, int hauteur) {
        obstacles.add(new Obstacle(x, y, largeur, hauteur));
    }

    // Vérifie les collisions entre le perso et obstacle
    public void verifierCollisions(Personnage perso) {
        for (Obstacle obs : obstacles) {
            if (perso.collision(obs) == true) {
                perso.annulerDeplacement();
                break;
            }
        }
    }

    // Configure la porte de la pièce
    public void Porte(int x, int y, int largeur, int hauteur, Piece destination) {
        this.porteX = x;
        this.porteY = y;
        this.porteLargeur = largeur;
        this.porteHauteur = hauteur;
        this.pieceDestination = destination;
    }

    // Vérifie si le personnage peut prendre la porte
    public boolean surPorte(Personnage perso) {
        if (porteLargeur == 0 || porteHauteur == 0) return false; // pas de porte définie
        return perso.getX() < porteX + porteLargeur &&
                perso.getX() + perso.getLargeur() > porteX &&
                perso.getY() < porteY + porteHauteur &&
                perso.getY() + perso.getHauteur() > porteY;
    }

    // Dessine la pièce
    public void dessiner(Graphics2D g) {
        g.drawImage(background, 0, 0, null);
    }

    // Getters & Setters
    public ArrayList<Obstacle> getObstacles() { return obstacles;}
    public Image getImage() { return background; }
    public String getNom() { return nom; }
    public String getCheminImage() { return cheminImage;}
    public Piece getPieceDestination() { return pieceDestination; }
    public int getLargeur() { return background.getWidth(null); }
    public int getHauteur() { return background.getHeight(null); }

}

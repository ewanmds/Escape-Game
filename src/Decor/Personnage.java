package Decor;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Personnage {
    private int x, y;
    private final int largeur = 205, hauteur = 500, vitesse = 20;
    private int dernierX, dernierY;
    private BufferedImage imageDos, imageFace, imageGauche, imageDroite, imageActuelle;
    private boolean haut, bas, gauche, droite, toucheE;

    public Personnage(int x, int y) {
        this.x = x;
        this.y = y;
        chargerImages();
        imageActuelle = imageDos;
    }

    //Gestion des images
    private void chargerImages() {
        String chemin = "ressources\\";
        try {
            imageDos = redimensionnerImage(ImageIO.read(new File(chemin + "perso_dos.png")));
            imageFace = redimensionnerImage(ImageIO.read(new File(chemin + "perso_face.png")));
            imageGauche = redimensionnerImage(ImageIO.read(new File(chemin + "perso_gauche.png")));
            imageDroite = redimensionnerImage(ImageIO.read(new File(chemin + "perso_droite.png")));
        } catch (IOException e) {
            System.err.println("Erreur de chargement d'une image: " + e.getMessage());
        }
    }


    //Redimensionne l'image à la taille du personnage
    private BufferedImage redimensionnerImage(BufferedImage originale) {
        if (originale == null) return null;
        Image tmp = originale.getScaledInstance(largeur, hauteur, Image.SCALE_SMOOTH); // redimesnionne l'image
        BufferedImage resized = new BufferedImage(largeur, hauteur, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }

    //Gère les actions losqjes les touches sont enfoncées
    public void toucheEnfoncee(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_Z:
                haut = true;
                bas = gauche = droite = false;
                break;
            case KeyEvent.VK_S:
                bas = true;
                haut = gauche = droite = false;
                break;
            case KeyEvent.VK_Q:
                gauche = true;
                haut = bas = droite = false;
                break;
            case KeyEvent.VK_D:
                droite = true;
                haut = bas = gauche = false;
                break;
            case KeyEvent.VK_E:
                toucheE = true;
        }
    }

    // Gère les actions lorsque des touches sont relâchées
    public void toucheRelachee(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_Z: haut = false; break;
            case KeyEvent.VK_S: bas = false; break;
            case KeyEvent.VK_Q: gauche = false; break;
            case KeyEvent.VK_D: droite = false; break;
            case KeyEvent.VK_E: toucheE = false;
        }
    }


    //Déplace le personnage en fonction des touches enfoncées
    public void deplacer() {
        dernierX = x;
        dernierY = y;

        if (haut) {
            y -= vitesse;
            imageActuelle = imageDos;
        }
        if (bas) {
            y += vitesse;
            imageActuelle = imageFace;
        }
        if (gauche) {
            x -= vitesse;
            imageActuelle = imageGauche;
        }
        if (droite) {
            x += vitesse;
            imageActuelle = imageDroite;
        }
    }

    // Annule le dernier déplacement du personnage
    public void annulerDeplacement() {
        x = dernierX;
        y = dernierY;
    }

    //Gestion des collisions du personnage avec un obstacle
    public boolean collision(Obstacle obs) {
        int piedY = this.y + this.hauteur;
        return this.x < obs.getX() + obs.getLargeur() &&
                this.x + this.largeur > obs.getX() &&
                piedY < obs.getY() + obs.getHauteur() &&
                piedY + 5 > obs.getY();
    }

    //Annule le dernier déplacement du personnage
    public void dessiner(Graphics2D g) {
        g.drawImage(imageActuelle, x, y, null);
    }


    //Getters et Setters
    public boolean toucheE() { return toucheE; }
    public int getX() { return x; }
    public int getY() { return y; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public int getLargeur() { return largeur; }
    public int getHauteur() { return hauteur; }
}
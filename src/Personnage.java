import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Personnage {
    private int x, y; // position du perso
    private int largeur = 205; // dimensions du perso
    private int hauteur = 500;
    private int vitesse = 15; //vitesse en pixel

    private boolean haut, bas, gauche, droite; //états des touches mouvements
    private int dernierX, dernierY; //position précédente (annulation dépclacement si collision)
    private BufferedImage imageDos, imageFace, imageGauche, imageDroite; //images du perso
    private BufferedImage imageActuelle; //image à l'instant t
    private String dernierDirection = "dos"; //firection du perso à l'instant t

    public Personnage(int x, int y) {
        this.x = x;
        this.y = y;
        chargerImage();
        imageActuelle = imageDos;
    }


    private void chargerImage() {
        try {
            String chemin = "C:\\Users\\ewanm\\Desktop\\P4\\Dev_App\\Escape-Game\\ressources\\";
            // On charger les 4 images de direction
            BufferedImage persoDos = ImageIO.read(new File(chemin + "perso_dos.png"));
            BufferedImage persoFace = ImageIO.read(new File(chemin + "perso_face.png"));
            BufferedImage persoGauche = ImageIO.read(new File(chemin + "perso_gauche.png"));
            BufferedImage persoDroite = ImageIO.read(new File(chemin + "perso_droite.png"));

            // On rdimensionner chaque image aux dimensions du perso
            imageDos = redimensionnerImage(persoDos);
            imageFace = redimensionnerImage(persoFace);
            imageGauche = redimensionnerImage(persoGauche);
            imageDroite = redimensionnerImage(persoDroite);

        } catch (IOException e) {
            System.err.println("Erreur de chargement d'une image: " + e.getMessage());
        }
    }

    private BufferedImage redimensionnerImage(BufferedImage originale) {
        if (originale == null) return null;

        Image tmp = originale.getScaledInstance(largeur, hauteur, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(largeur, hauteur, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }

    // Gère l'évènement de la touche enfoncée
    public void toucheEnfoncee(KeyEvent e) { //Gère les touches endoncées clavier
        switch (e.getKeyCode()) {
            case KeyEvent.VK_Z: haut = true; break;
            case KeyEvent.VK_S: bas = true; break;
            case KeyEvent.VK_Q: gauche = true; break;
            case KeyEvent.VK_D: droite = true; break;
        }
    }

    // Gère l'évènement de la touche relâchée
    public void toucheRelachee(KeyEvent e) {//Gère les touches relachées// clavier
        switch (e.getKeyCode()) {
            case KeyEvent.VK_Z: haut = false; break;
            case KeyEvent.VK_S: bas = false; break;
            case KeyEvent.VK_Q: gauche = false; break;
            case KeyEvent.VK_D: droite = false; break;
        }
    }

    // Déplace le perso en fonctions des touches enfoncées & màj l'image correspondante
    public void deplacer() {
        dernierX = x;
        dernierY = y;

        boolean aMouvementX = false;
        boolean aMouvementY = false;

        // Déplacement vertical vers le haut
        if (haut == true) {
            y -= vitesse;
            imageActuelle = imageDos;
            dernierDirection = "dos";
            aMouvementY = true;
        }

        // Déplacement vertical vers le bas
        if (bas == true) {
            y += vitesse;
            imageActuelle = imageFace;
            dernierDirection = "face";
            aMouvementY = true;
        }

        // Déplacement horizontal vers la gauche
        if (gauche == true) {
            x -= vitesse;
            imageActuelle = imageGauche;
            dernierDirection = "gauche";
            aMouvementX = true;
        }

        // Déplacement horizontal vers la droite
        if (droite == true) {
            x += vitesse;
            imageActuelle = imageDroite;
            dernierDirection = "droite";
            aMouvementX = true;
        }

        // Si on a deux mouvements simultanés, prioriser horizontal
        if (aMouvementX && aMouvementY) {
            if (dernierDirection.equals("gauche")) {
                imageActuelle = imageGauche;
            } else if (dernierDirection.equals("droite")) {
                imageActuelle = imageDroite;
            }
        }
    }

    // Annule le dernier déplacement en cas de collision
    public void annulerDeplacement() {
        x = dernierX;
        y = dernierY;
    }

    // Vérifie s'il y a collision entre les pieds du personnage et un obstacle
    public boolean collision(Obstacle obs) {
        // Coordonnées des pieds du personnage
        int piedY = this.y + this.hauteur;

        // Vérification de collision seulement pour les pieds
        return this.x < obs.getX() + obs.getLargeur() &&
                this.x + this.largeur > obs.getX() &&
                piedY < obs.getY() + obs.getHauteur() &&
                piedY + 5 > obs.getY();
    }

    // Dessine le personnage
    public void dessiner(Graphics2D g) {
        g.drawImage(imageActuelle, x, y, null);
    }

}
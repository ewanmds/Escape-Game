import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.ArrayList;


public class Jeux extends JPanel {
    private Image background;
    private Personnage perso;
    private ArrayList<Obstacle> obstacles = new ArrayList<>();

    public Jeux(String imagePath) {
        try {
            background = ImageIO.read(new File(imagePath)); // Lit l'image
            setPreferredSize(new Dimension(background.getWidth(null), background.getHeight(null))); // dimension back en fonction image
            perso = new Personnage(0, 0);

            obstacles.add(new Obstacle(0,410,1536,50)); // mur haut
            obstacles.add(new Obstacle(530, 535,500 , 320)); // bureau
            obstacles.add(new Obstacle(150, 530, 50, 350)); //commode
            obstacles.add(new Obstacle(1290, 490, 107, 420));//canapé
            setFocusable(true); // Fait liaison entre clavier et le Jpanel

            // Écoute les évènements du claviers (touches enfoncées ou relachées)
            addKeyListener(new KeyAdapter() {
                @Override public void keyPressed(KeyEvent e) { if(perso != null) perso.toucheEnfoncee(e); }
                @Override public void keyReleased(KeyEvent e) { if(perso != null) perso.toucheRelachee(e); }
            });

            new Timer(16, e -> { //màj le code toutes les 16 ms (environ 60 fps)
                if ( perso != null) {
                    perso.deplacer();
                    verifierCollisions();
                    repaint();
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Vérifie les collisions entre le perso et obstacle
    private void verifierCollisions() {
        for (Obstacle obs : obstacles)
            if (perso.collision(obs) == true) {
             perso.annulerDeplacement();
             break;
        }
    }

    // Dessine tout (fond,perso)
    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(background, 0, 0, this);
        perso.dessiner(g2d);
    }

    // Programme principale pour lancer le jeu
    public static void main(String[] args) {
        System.setProperty("sun.java2d.uiScale", "1.0"); // Même résolution et taille fenêtre sur différents écran
        JFrame frame = new JFrame("Poliscape"); // On créer la fenêtre


        frame.setContentPane(new Jeux("ressources\\temoin.png")); // Affiche le contenue du jeu
        frame.setResizable(false); // Interdir redimesnion fenêtre
        frame.pack(); // Ajuste la fenêtre avec le contenue ( background)
        frame.setLocationRelativeTo(null); // Centre la fenêtre au milieu de l'écran
        frame.setVisible(true); // Affiche la fenêtre
    }
}
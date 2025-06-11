package Jeu;

import Decor.*;
import Enigme.*;


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
    private ArrayList<Piece> pieces = new ArrayList<>();

    private static  int i = 0;
    private boolean Okmessage;
    private JTextField champTexte = new JTextField(20);
    private Indice indiceProche = null;


    private Morpion morpion = new Morpion();
    private enum ModeJeu { EXPLORATION, MORPION }
    private ModeJeu mode = ModeJeu.EXPLORATION;



    public Jeux() {
        try {
            /*


            champTexte.setVisible(true);
            setLayout(null);
            champTexte.setBounds(300, 300, 100, 70);
            champTexte.setFont(new Font("Arial", Font.PLAIN, 24));
            add(champTexte);

             */

            initialiserPieces();
            background = ImageIO.read(new File(pieces.get(i).getCheminImage())); // Lit l'image
            setPreferredSize(new Dimension(background.getWidth(null), background.getHeight(null))); // dimension back en fonction image
            perso = new Personnage(500, 500);
            obstacles.addAll(pieces.get(i).getObstacles());


            setFocusable(true); // Fait la liaison entre clavier et le Jpanel

            requestFocusInWindow();


            // Écoute les évènements du claviers (touches enfoncées ou relachées)
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (mode == ModeJeu.EXPLORATION) {

                        if (perso != null) perso.toucheEnfoncee(e);

                        if (e.getKeyCode() == KeyEvent.VK_M) {
                            mode = ModeJeu.MORPION;
                            morpion.Reset();
                            System.out.println("ca marche");
                            repaint();

                        }
                    } else if (mode == ModeJeu.MORPION) {
                        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                            mode = ModeJeu.EXPLORATION;
                        }
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    if (perso != null) perso.toucheRelachee(e);
                }
            });


            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent m) {
                    requestFocusInWindow();
                    if (mode == ModeJeu.MORPION) {
                        int x = m.getX();
                        int y = m.getY();
                        morpion.affect(m.getX(), m.getY(), getWidth(), getHeight());
                        repaint(); // pour voir le X ou O apparaître
                    }
                }
            });


            new Timer(16, e -> { //màj le code toutes les 16 ms (environ 60 fps)
                if (mode == ModeJeu.EXPLORATION){

                if ( perso != null) {
                    perso.deplacer();
                    verifierCollisions();
                    boolean surPorte = pieces.get(i).surPorte(perso);
                    if ((surPorte == true) && perso.EAppuyee()) {
                        i++;
                        perso.setX(500);
                        perso.setY(500);
                        obstacles.clear();
                        obstacles.addAll(pieces.get(i).getObstacles());
                    }


                    if (pieces.get(i).getEnigme() != null && pieces.get(i).getEnigme().estProcheMessage(perso)) {
                        Okmessage = true;
                    } else {
                        Okmessage = false;
                    }

                    if (pieces.get(i).getEnigme() != null && pieces.get(i).getEnigme().estProcheIndice(perso) != null) {
                        indiceProche = pieces.get(i).getEnigme().estProcheIndice(perso);
                    } else {
                        indiceProche = null;
                    }

                }

                    repaint();
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initialiserPieces() {
        // Création des pièces
        Piece pieceSecrete = new Piece("PIECE SECRETE", "ressources\\piece_secrete.png");
        Piece maisonTemoin = new Piece("MAISON DU TEMOIN", "ressources\\temoin.png",pieceSecrete);
        Piece laboAnalyse = new Piece("LABORATOIRE D'ANALYSE", "ressources\\labo_analyse.png",maisonTemoin);
        Piece sceneCrime = new Piece("SCENE DU CRIME", "ressources\\scene_crime.png",laboAnalyse);
        Piece postePolice = new Piece("POSTE DE POLICE", "ressources\\poste_police.png",sceneCrime);

        pieces.add(postePolice);
        pieces.add(sceneCrime);
        pieces.add(laboAnalyse);
        pieces.add(maisonTemoin);
        pieces.add(pieceSecrete);

        postePolice.setEnigme(new EnigmePostePolice());
        postePolice.getEnigme().ajouterIndice(new Indice(950,-20,"SALOPE"));
        postePolice.getEnigme().ajouterIndice(new Indice(480,110,"BATARD"));


        postePolice.Porte(0, 0, 200, 10, sceneCrime);
        sceneCrime.Porte(0, 0, 200, 10, laboAnalyse);
        laboAnalyse.Porte(0, 0, 200, 10, maisonTemoin);
        maisonTemoin.Porte(0, 0, 200, 10, pieceSecrete);

        // Obstacles poste Police
        postePolice.ajouterObstacle(0, 370, 1526, 50); // mur haut
        postePolice.ajouterObstacle(-180, 0, 100, 1500); // mur gauche
        postePolice.ajouterObstacle(-70, 1100, 1500, 100); //mur bas
        postePolice.ajouterObstacle(1620, 0, 100, 1500);// mur froite
        postePolice.ajouterObstacle(1190, 450, 150, 430); // canapé
        postePolice.ajouterObstacle(500, 350, 330, 240); // bureau
        postePolice.ajouterObstacle(650, 550, 10, 110); // chaise bureau
        postePolice.ajouterObstacle(160, 450, 70, 330); // meuble

        // Obstacles scène Crime
        sceneCrime.ajouterObstacle(0, 400, 1526, 50); // mur haut
        sceneCrime.ajouterObstacle(-180, 0, 100, 1500); // mur gauche
        sceneCrime.ajouterObstacle(-70, 1100, 1500, 100); //mur bas
        sceneCrime.ajouterObstacle(1620, 0, 100, 1500);// mur froite
        sceneCrime.ajouterObstacle(990, 200, 330, 350); //bibliothèque
        sceneCrime.ajouterObstacle(100, 700, 60, 250); //chaise
        sceneCrime.ajouterObstacle(1150, 575, 90, 275); //chaise


        // Obstacles maison Témoin
        maisonTemoin.ajouterObstacle(530, 535,520 , 320); // bureau
        maisonTemoin.ajouterObstacle(150, 490, 50, 350); // commode
        maisonTemoin.ajouterObstacle(1290, 490, 115, 420); // canapé
        maisonTemoin.ajouterObstacle(725, 420, 95, 300); //témoin
        maisonTemoin.ajouterObstacle(0, 410, 1526, 50); // mur haut
        maisonTemoin.ajouterObstacle(-180, 0, 100, 1500); // mur gauche
        maisonTemoin.ajouterObstacle(-70, 1100, 1500, 100); //mur bas
        maisonTemoin.ajouterObstacle(1630, 0, 100, 1500);// mur froite

        // Obstacles labo analyse
        laboAnalyse.ajouterObstacle(0, 335, 1526, 50); // mur haut
        laboAnalyse.ajouterObstacle(-180, 0, 100, 1500); // mur gauche
        laboAnalyse.ajouterObstacle(-70, 1100, 1500, 100); //mur bas
        laboAnalyse.ajouterObstacle(1620, 0, 100, 1500);// mur droite
        laboAnalyse.ajouterObstacle(0, 150, 1360, 305); // armoire + grand bureau
        laboAnalyse.ajouterObstacle(520, 400, 470, 400); // burea
        laboAnalyse.ajouterObstacle(700, 700, 50, 200); // chaise
        laboAnalyse.ajouterObstacle(1280, 485, 120, 315); // table roulette


        // Obstacles pièce secrète
        pieceSecrete.ajouterObstacle(0, 410, 1526, 50); // mur haut
        pieceSecrete.ajouterObstacle(-180, 0, 100, 1500); // mur gauche
        pieceSecrete.ajouterObstacle(-70, 1100, 1500, 100); //mur bas
        pieceSecrete.ajouterObstacle(1620, 0, 100, 1500);// mur froite

    }

    // Vérifie les collisions entre le perso et obstacle
    private void verifierCollisions() {
        for (Obstacle obs : obstacles) {
            if (perso.collision(obs) == true) {
                perso.annulerDeplacement();
                break;
            }
        }
    }
    private void drawMultilineString(Graphics2D g2d, String text, int x, int y, int lineHeight) {
        for (String line : text.split("\n")) {
            g2d.drawString(line, x, y);
            y += lineHeight;
        }
    }

    // Dessine tout (fond,perso)
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(pieces.get(i).getImage(), 0, 0, this);


        String texte = pieces.get(i).getNom();
        g2d.setFont(new Font("Courrier New", Font.ITALIC, 70));
        g2d.setColor(Color.LIGHT_GRAY);  // Ombre
        g2d.drawString(texte, 453, 1003);
        g2d.setColor(Color.darkGray); // Texte principal
        g2d.drawString(texte, 450, 1000);
        if (perso != null) {
            perso.dessiner(g2d);


        if (mode == ModeJeu.EXPLORATION) {
            g2d.drawImage(pieces.get(i).getImage(), 0, 0, this);


            String texte = pieces.get(i).getNom();
            g2d.setFont(new Font("Courrier New", Font.ITALIC, 70));
            // Ombre
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.drawString(texte, 453, 1003);
            // Texte principal
            g2d.setColor(Color.darkGray);
            g2d.drawString(texte, 450, 1000);
            if (perso != null) {
                perso.dessiner(g2d);
            }
        }
        else if (mode == ModeJeu.MORPION) {
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            morpion.dessiner(g2d, getWidth(), getHeight());

        }



        if (Okmessage) {
            Image img = Toolkit.getDefaultToolkit().getImage("ressources\\bulle_message.png");
            g2d.drawImage(img, pieces.get(i).getEnigme().getX(), pieces.get(i).getEnigme().getY(), this);
            g2d.setFont(new Font("Arial", Font.BOLD, 15));
            g2d.setColor(Color.BLACK);
            drawMultilineString(g2d, pieces.get(i).getEnigme().getMessage(), pieces.get(i).getEnigme().getX() + 70, pieces.get(i).getEnigme().getY() + 100, 20);
        }

        if (indiceProche != null) {
            Image img = Toolkit.getDefaultToolkit().getImage("ressources\\bulle_message.png");
            g2d.drawImage(img,indiceProche.getX(),indiceProche.getY(), this);
            g2d.setFont(new Font("Arial", Font.BOLD, 15));
            g2d.setColor(Color.BLACK);
            drawMultilineString(g2d, indiceProche.getMessage(), indiceProche.getX() + 70, indiceProche.getY() + 100, 20);
        }


    }

    // Programme principale pour lancer le jeu
    public static void main(String[] args) {
        System.setProperty("sun.java2d.uiScale", "1.0"); // Même résolution et taille fenêtre sur différents écran
        JFrame frame = new JFrame("Poliscape"); // On créer la fenêtre
        frame.setContentPane(new Jeux()); // Affiche le contenue du jeu
        frame.setResizable(false); // Interdir redimesnion fenêtre
        frame.pack(); // Ajuste la fenêtre avec le contenue ( background)
        frame.setLocationRelativeTo(null); // Centre la fenêtre au milieu de l'écran
        frame.setVisible(true); // Affiche la fenêtre




    }
}
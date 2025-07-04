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
    private static  int i = 0; //Numéro de la pièce
    private Indice indiceProche = null;
    private Morpion morpion = new Morpion();
    private enum ModeJeu { EXPLORATION, MORPION}
    private ModeJeu mode = ModeJeu.EXPLORATION;
    private int xMorpion = 600, yMorpion = 300;

    public Jeux() {
        try {
            initialiserPieces();
            background = ImageIO.read(new File(pieces.get(i).getCheminImage())); // Lit l'image
            setPreferredSize(new Dimension(background.getWidth(null), background.getHeight(null))); // dimension background en fonction image
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

                        if (e.getKeyCode() == KeyEvent.VK_E  && i == 3 && estProche(perso.getX(),perso.getY(),xMorpion,yMorpion,100)) {
                            mode = ModeJeu.MORPION;
                            morpion.Reset();
                            repaint();
                        }

                    } else if (mode == ModeJeu.MORPION) {
                        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                            mode = ModeJeu.EXPLORATION;
                        }
                        if (e.getKeyCode() == KeyEvent.VK_R){
                            morpion.Reset();
                            repaint();
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

            //Timer du jeu
            new Timer(16, e -> { //màj le code toutes les 16 ms (environ 60 fps)
                if (mode == ModeJeu.EXPLORATION && perso != null){
                    perso.deplacer();
                    verifierCollisions();

                    if (i == 1 || i == 2) {
                        Enigmes enigme = pieces.get(i).getEnigme();
                        if (enigme instanceof EnigmeSceneCrimeOuLaboAnalyse esc) {
                            esc.verifierEtAfficher(this, perso); // on délègue à l'énigme
                        }
                    }

                    if (i == 4) {
                        Enigmes enigme = pieces.get(i).getEnigme();
                        if (enigme instanceof EnigmePieceSecrete epc) {
                            epc.verifierEtAfficher(this, perso); // on délègue à l'énigme
                        }
                    }

                    boolean surPorte = pieces.get(i).surPorte(perso);
                    boolean appuieE = perso.toucheE();
                    boolean peutChanger = false;

                    if (surPorte && appuieE) {
                        if(i != 3) {
                            Enigmes enigme = pieces.get(i).getEnigme();

                            if ((i == 0 && ((EnigmePostePolice) enigme).estResolue())
                                    || (i == 1 && ((EnigmeSceneCrimeOuLaboAnalyse) enigme).estResolue())
                                    || (i == 2 && ((EnigmeSceneCrimeOuLaboAnalyse) enigme).estResolue())
                                    || (i == 4  && ((EnigmePieceSecrete) enigme).estResolue())){
                                peutChanger = true;
                            }
                        } else if (morpion.getVictoire()) {
                            peutChanger = true;
                        }

                        if (peutChanger) {
                            i++;
                            perso.setX(500);
                            perso.setY(500);
                            obstacles.clear();
                            obstacles.addAll(pieces.get(i).getObstacles());
                        }
                    }

                    // Gestion spécifique à la pièce 0 (poste de police)
                    if (i == 0) {
                        Enigmes enigme = pieces.get(i).getEnigme();
                        if (enigme instanceof EnigmePostePolice enigmePP) {
                            if (enigmePP.estProcheCoffres(perso)) {
                                enigmePP.afficherBoutons(this);
                            } else {
                                enigmePP.retirerBoutons(this);
                            }
                        }
                    }

                    if (i == 0) {
                        Enigmes enigme = pieces.get(i).getEnigme();
                        if (enigme instanceof EnigmePostePolice) {
                            EnigmePostePolice enigmePP = (EnigmePostePolice) enigme;

                            if (enigmePP.estProcheCoffres(perso)) {
                                enigmePP.afficherBoutons(this);
                            } else {
                                enigmePP.retirerBoutons(this);
                            }
                        }
                    }

                    //Gestion de la proximité d’un indice
                    Enigmes enigmeActuelle = pieces.get(i).getEnigme();
                    if (enigmeActuelle != null) {
                        indiceProche = enigmeActuelle.estProcheIndice(perso);
                    } else {
                        indiceProche = null;
                    }

                    repaint();
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initialiserPieces() {
        //Création des pièces
        Piece pieceSecrete = new Piece("PIECE SECRETE", "ressources\\piece_secrete.png");
        Piece maisonTemoin = new Piece("MAISON DU TEMOIN", "ressources\\temoin.png",pieceSecrete);
        Piece laboAnalyse = new Piece("LABORATOIRE D'ANALYSE", "ressources\\labo_analyse.png",maisonTemoin);
        Piece sceneCrime = new Piece("SCENE DU CRIME", "ressources\\scene_crime.png",laboAnalyse);
        Piece postePolice = new Piece("POSTE DE POLICE", "ressources\\poste_police.png",sceneCrime);
        Piece theEnd = new Piece("THE END","ressources\\the_end.png");

        //Ajout des pièces
        pieces.add(postePolice);
        pieces.add(sceneCrime);
        pieces.add(laboAnalyse);
        pieces.add(maisonTemoin);
        pieces.add(pieceSecrete);
        pieces.add(theEnd);

        //Ajout des enigmes et des indices
        postePolice.setEnigme(new EnigmePostePolice());
        postePolice.getEnigme().ajouterIndice(new Indice(950,-20,"Coffre 1 dit :\n“La vérité est dans\nce coffre.” \n"));
        postePolice.getEnigme().ajouterIndice(new Indice(480,110,"Coffre 2 dit :\n“Le coffre 3 ment.” "));
        postePolice.getEnigme().ajouterIndice(new Indice(775,-20,"Coffre 3 dit :\n“Coffre 1 ment.” "));
        postePolice.getEnigme().ajouterIndice(new Indice(150,270,"Un seul coffre\ncontient la vérité.\n (un seul des indices\n est vrai.) \n” "));

        sceneCrime.setEnigme(new EnigmeSceneCrimeOuLaboAnalyse("32113312",210,200)); // MDP = 32113312
        sceneCrime.getEnigme().ajouterIndice(new Indice(550,550," ■ ● ▲ ▲ "));
        sceneCrime.getEnigme().ajouterIndice(new Indice(200,250," ■ ■ ▲ ● "));
        sceneCrime.getEnigme().ajouterIndice(new Indice(1100,70,"▲ = 1\n ● = 2\n ■ = 3\n"));

        laboAnalyse.setEnigme(new EnigmeSceneCrimeOuLaboAnalyse("L'ADN EST UNE CLEF",100,200)); // MDP = L'ADN EST UNE CLEF
        laboAnalyse.getEnigme().ajouterIndice(new Indice(1200,-30,"César +3"));
        laboAnalyse.getEnigme().ajouterIndice(new Indice(800,250,"I'XAK BPQ RKB ZIBC"));

        maisonTemoin.setEnigme(new EnigmeTemoin());
        maisonTemoin.getEnigme().ajouterIndice(new Indice(xMorpion,yMorpion,"Pour un indice,\ngagne contre moi\n(appuyer sur E)"));

        pieceSecrete.setEnigme(new EnigmePieceSecrete("3",150,180)); // MDP = 32
        pieceSecrete.getEnigme().ajouterIndice(new Indice(120, 30, "Combien y a-t-il\nde canapés\n(en tout) ?"));
        pieceSecrete.getEnigme().ajouterIndice(new Indice(0, 250, "Cherchez dans\nle plafond.")); //Ok
        pieceSecrete.getEnigme().ajouterIndice(new Indice(100, 500, "Regardez sous\nla table.")); //OK
        pieceSecrete.getEnigme().ajouterIndice(new Indice(500, 450, "Le mot de passe\nest '1234'.")); //OK
        pieceSecrete.getEnigme().ajouterIndice(new Indice(1100, 500, "Le coffre\nest vide.")); // OK
        pieceSecrete.getEnigme().ajouterIndice(new Indice(700, 50, "La lampe est\nla solution.")); //OK
        pieceSecrete.getEnigme().ajouterIndice(new Indice(1100, 50, "Le tableau contient\nun indice.")); //OK

        //Création des portes
        postePolice.Porte(20, 0, 250, 10, sceneCrime);
        sceneCrime.Porte(20, 0, 250, 10, laboAnalyse);
        laboAnalyse.Porte(20, 0, 250, 10, maisonTemoin);
        maisonTemoin.Porte(20, 0, 250, 10, pieceSecrete);
        pieceSecrete.Porte(20, 0, 250, 10, theEnd);

        // Obstacles poste Police
        postePolice.ajouterObstacle(0, 370, 1526, 50); // mur haut
        postePolice.ajouterObstacle(-180, 0, 100, 1500); // mur gauche
        postePolice.ajouterObstacle(-70, 1100, 1500, 100); //mur bas
        postePolice.ajouterObstacle(1620, 0, 100, 1500);// mur droite
        postePolice.ajouterObstacle(1215, 450, 150, 430); // canapé
        postePolice.ajouterObstacle(500, 350, 330, 240); // bureau
        postePolice.ajouterObstacle(650, 550, 10, 110); // chaise bureau
        postePolice.ajouterObstacle(160, 450, 70, 445); // meuble
        postePolice.ajouterObstacle(900, 650, 70, 275); // meuble droite

        // Obstacles scène Crime
        sceneCrime.ajouterObstacle(0, 400, 1526, 50); // mur haut
        sceneCrime.ajouterObstacle(-180, 0, 100, 1500); // mur gauche
        sceneCrime.ajouterObstacle(-70, 1100, 1500, 100); //mur bas
        sceneCrime.ajouterObstacle(1620, 0, 100, 1500);// mur droite
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
        maisonTemoin.ajouterObstacle(1630, 0, 100, 1500);// mur droite

        // Obstacles labo analyse
        laboAnalyse.ajouterObstacle(0, 335, 1526, 50); // mur haut
        laboAnalyse.ajouterObstacle(-180, 0, 100, 1500); // mur gauche
        laboAnalyse.ajouterObstacle(-70, 1100, 1500, 100); //mur bas
        laboAnalyse.ajouterObstacle(1620, 0, 100, 1500);// mur droite
        laboAnalyse.ajouterObstacle(0, 150, 1360, 305); // armoire + grand bureau
        laboAnalyse.ajouterObstacle(520, 400, 470, 380); // bureau
        laboAnalyse.ajouterObstacle(650, 700, 50, 200); // chaise
        laboAnalyse.ajouterObstacle(1280, 485, 120, 315); // table roulette


        // Obstacles pièce secrète
        pieceSecrete.ajouterObstacle(0, 380, 1526, 50); // mur haut
        pieceSecrete.ajouterObstacle(-180, 0, 100, 1500); // mur gauche
        pieceSecrete.ajouterObstacle(-70, 1100, 1500, 100); //mur bas
        pieceSecrete.ajouterObstacle(1620, 0, 100, 1500);// mur droite

        pieceSecrete.ajouterObstacle(210, 645, 220, 300);// table gauche
        pieceSecrete.ajouterObstacle(600, 620, 240, 300);// table milieu
        pieceSecrete.ajouterObstacle(720, 920, 35, 70);// tabouret milieu
        pieceSecrete.ajouterObstacle(1120, 646, 245, 290);// table droite
        pieceSecrete.ajouterObstacle(1120, 280, 245, 290);// canapé
        pieceSecrete.ajouterObstacle(820, 280, 245, 230);// meuble lampe
        pieceSecrete.ajouterObstacle(500, 280, 70, 290);// meuble livres
        pieceSecrete.ajouterObstacle(50, 460, 60, 170);// plante

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

    //Affiche le texte à la ligen en présence d'un \n
    private void drawMultilineString(Graphics2D g2d, String text, int x, int y, int lineHeight) {
        if (text != null) {
            for (String line : text.split("\n")) {
                g2d.drawString(line, x, y);
                y += lineHeight;
            }
        }
    }

    public boolean estProche(int x1,int y1,int x2,int y2, int distance){
        return (Math.abs(x1-x2) < distance) && (Math.abs(y1-y2) < distance);
    }

    // Dessine tout
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (mode == ModeJeu.EXPLORATION) {
            g2d.drawImage(pieces.get(i).getImage(), 0, 0, this);

            if (i != 5) { // Vérifie si ce n'est pas la dernière pièce
                String texte = pieces.get(i).getNom();
                g2d.setFont(new Font("Courrier New", Font.ITALIC, 70));
                // Ombre
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.drawString(texte, 453, 1003);
                // Texte principal
                g2d.setColor(Color.darkGray);
                g2d.drawString(texte, 450, 1000);
            }

            if (perso != null) {
                perso.dessiner(g2d);
            }

            if (indiceProche != null) {
                Image img = Toolkit.getDefaultToolkit().getImage("ressources\\bulle_message.png");
                g2d.drawImage(img, indiceProche.getX(), indiceProche.getY(), this);
                g2d.setFont(new Font("Arial", Font.BOLD, 20));
                g2d.setColor(Color.BLACK);
                drawMultilineString(g2d, indiceProche.getMessage(), indiceProche.getX() + 30, indiceProche.getY() + 70, 35);
            }

        } else if (mode == ModeJeu.MORPION) {
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            morpion.dessiner(g2d, getWidth(), getHeight());
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
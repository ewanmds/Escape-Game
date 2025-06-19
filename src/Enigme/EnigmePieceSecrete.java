package Enigme;

import Decor.Personnage;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class EnigmePieceSecrete implements Enigmes {
    private ArrayList<Indice> indices = new ArrayList<>();
    private boolean resolue = false;
    private String motDePasse ;
    private JTextField champTexte = new JTextField(10);
    private boolean champAjoute = false;
    private boolean listenerAjoute = false;
    int xZone, yZone;


    public EnigmePieceSecrete(String motDePasse, int xZone, int yZone){
        this.motDePasse = motDePasse;
        this.xZone = xZone;
        this.yZone = yZone;
    }

    //Ajoute un indice à l'énigme
    public void ajouterIndice(Indice indice) {
        indices.add(indice);
    }

    //Vérifie si le personnage est proche d'un indice
    public Indice estProcheIndice(Personnage p) {
        int XcentrePerso = p.getX() + p.getLargeur() / 2;
        int YcentrePerso = p.getY() + p.getHauteur() / 2;
        for (Indice indice : indices) {
            double distance = Math.sqrt(Math.pow(XcentrePerso - indice.getX() - 100, 2) + Math.pow(YcentrePerso - indice.getY() - 100, 2));
            if (distance < 200) {
                return indice;
            }
        }
        return null;
    }

    //Renvoir si l'énigme est résolue
    public boolean estResolue() {
        return resolue;
    }

    // Vérifie si le personnage est dans la zone de l'énigme et affiche un champ texte pour entrer le mot de passe
    public void verifierEtAfficher(JPanel panel, Personnage p) {

        int xPerso = p.getX() + p.getLargeur() / 2;
        int yPerso = p.getY() + p.getHauteur() / 2;
        double distance = Math.sqrt(Math.pow(xPerso - xZone, 2) + Math.pow(yPerso - yZone, 2));

        if (distance < 100 && !champAjoute && !resolue) {
            champTexte.setBounds(xZone, yZone, 180, 30);
            champTexte.setFont(new Font("Arial", Font.BOLD, 17));

            if (!listenerAjoute) {
                champTexte.addActionListener(e -> {
                    if (champTexte.getText().trim().equals(motDePasse)) {
                        resolue = true;
                        JOptionPane.showMessageDialog(panel, "Mot de passe correct !");
                        champTexte.setVisible(false);
                    } else {
                        JOptionPane.showMessageDialog(panel, "Mot de passe incorrect !");
                    }
                    champTexte.setText("");
                });
                listenerAjoute = true;
            }

            panel.add(champTexte);
            champTexte.setVisible(true);
            champAjoute = true;
            panel.repaint();
        }


        if (distance >= 100 && champAjoute) {
            panel.remove(champTexte);
            champTexte.setVisible(false);
            champAjoute = false;
            panel.repaint();
        }
    }

    //Getters & Setters
    public ArrayList<Indice> getIndices() {
        return indices;
    }
}
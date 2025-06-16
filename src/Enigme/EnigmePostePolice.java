package Enigme;

import Decor.Personnage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class EnigmePostePolice implements Enigmes {
    private String reponseCorrecte = "Coffre 2";
    private boolean resolue = false;
    private ArrayList<Indice> indices = new ArrayList<>();
    private JButton bouton1, bouton2, bouton3;

    public boolean verifierReponse(String reponse) {
        boolean correcte = reponseCorrecte.equalsIgnoreCase(reponse.trim());
        if (correcte) {
            resolue = true;
        }
        return correcte;
    }

    public String getEnonce() {
        return "Un seul coffre contient la vérité.\nMais attention : un seul de ces messages est vrai.";
    }


    public Indice estProcheIndice(Personnage p){
        int XcentrePerso = p.getX()  + p.getLargeur() / 2;
        int YcentrePerso = p.getY()  + p.getLargeur() / 2;
        for (Indice indice : indices) {
            double distance = Math.sqrt(Math.pow(XcentrePerso - indice.getX()-100, 2) + Math.pow(YcentrePerso - indice.getY()-100, 2));
            if (distance < 100) {
                return indice;
            }
        }
        return null;
    }

    public boolean estProcheCoffres(Personnage p) {
        int xCoffres = 150;
        int yCoffres = 480;

        int XcentrePerso = p.getX() + p.getLargeur() / 2;
        int YcentrePerso = p.getY() + p.getHauteur() / 2;

        double d = Math.sqrt(Math.pow(XcentrePerso - xCoffres, 2) + Math.pow(YcentrePerso - yCoffres, 2));
        return d < 260;
    }

    public void ajouterIndice(Indice indice) {
        indices.add(indice);
    }

    public void afficherBoutons(JPanel panel) {
        if (bouton1 != null && bouton1.isDisplayable()) return; // déjà créés => on sort

        bouton1 = new JButton("Coffre 1");
        bouton2 = new JButton("Coffre 2");
        bouton3 = new JButton("Coffre 3");

        bouton1.setBounds(130, 480, 120, 40);
        bouton2.setBounds(130, 550, 120, 40);
        bouton3.setBounds(130, 620, 120, 40);

        ActionListener listener = e -> {
            String choix = ((JButton) e.getSource()).getText();
            boolean res = verifierReponse(choix);
            JOptionPane.showMessageDialog(panel, res ? "Bonne réponse !" : "Mauvaise réponse !");
        };

        bouton1.addActionListener(listener);
        bouton2.addActionListener(listener);
        bouton3.addActionListener(listener);

        panel.add(bouton1);
        panel.add(bouton2);
        panel.add(bouton3);

        bouton1.setVisible(true);
        bouton2.setVisible(true);
        bouton3.setVisible(true);

        panel.repaint();
    }

    public void retirerBoutons(JPanel panel) {
        if (bouton1 != null) panel.remove(bouton1);
        if (bouton2 != null) panel.remove(bouton2);
        if (bouton3 != null) panel.remove(bouton3);

        bouton1 = bouton2 = bouton3 = null;

        panel.repaint();
    }

    public ArrayList<Indice> getIndices() {
        return null;
    }
    public boolean estResolue() {
        return resolue;
    }
}

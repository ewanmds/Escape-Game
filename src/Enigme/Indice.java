package Enigme;

public class Indice {
    private int x, y;
    private String message;

    public Indice (int x, int y, String message ){
        this.x = x;
        this.y = y;
        this.message = message;
    }


    // Getters & Setters
    public int getX() { return x;}
    public int getY() {return y;}
    public String getMessage() { return message; }
}

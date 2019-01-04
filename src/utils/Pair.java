package utils;

public class Pair {
    public float x;
    public float y;

    public Pair(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void divide(Pair divider) {
        this.x = this.x / divider.x;
        this.y = this.y / divider.y;
    }
}
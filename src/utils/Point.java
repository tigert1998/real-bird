package utils;

public class Point {
    public float x;
    public float y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void divide(Point divider) {
        this.x /= divider.x;
        this.y /= divider.y;
    }

    public float cross(Point point) {
        return this.x * point.y - this.y * point.x;
    }
}
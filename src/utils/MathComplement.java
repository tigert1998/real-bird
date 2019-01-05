package utils;

public class MathComplement {
    public static Point rotate(Point p, Point o, float theta) {
        double cos = Math.cos(theta), sin = Math.sin(theta);
        double x = (p.x - o.x) * cos - (p.y - o.y) * sin + o.x;
        double y = (p.x - o.x) * sin + (p.y - o.y) * cos + o.y;
        return new Point((float) x, (float) y);
    }

    public static Point minus(Point a, Point b) {
        return new Point(a.x - b.x, a.y - b.y);
    }

    public static Point add(Point a, Point b) {
        return new Point(a.x + b.x, a.y + b.y);
    }
}

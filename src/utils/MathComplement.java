package utils;

public class MathComplement {
    public static Pair rotate(Pair p, Pair o, float theta) {
        double cos = Math.cos(theta), sin = Math.sin(theta);
        double x = (p.x - o.x) * cos - (p.y - o.y) * sin + o.x;
        double y = (p.x - o.x) * sin + (p.y - o.y) * cos + o.y;
        return new Pair((float) x, (float) y);
    }
}

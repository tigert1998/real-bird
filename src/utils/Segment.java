package utils;

public class Segment {
    public Point source, direction;

    private static boolean unidirectionalIntersects(Segment a, Segment b) {
        Point u = MathComplement.minus(b.source, a.source);
        Point v = MathComplement.minus(b.destination(), a.source);
        float x = Math.signum(u.cross(a.direction));
        float y = Math.signum(a.direction.cross(v));
        return x * y >= 0;
    }

    public Segment(Point a, Point b) {
        source = a;
        direction = MathComplement.minus(b, a);
    }

    public Point destination() {
        return MathComplement.add(source, direction);
    }

    public boolean intersects(Segment segment) {
        return unidirectionalIntersects(this, segment) && unidirectionalIntersects(segment, this);
    }
}

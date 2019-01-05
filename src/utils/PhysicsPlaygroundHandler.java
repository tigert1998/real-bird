package utils;

import java.util.*;

public class PhysicsPlaygroundHandler {
    public List<Point> convexPoints;

    static private boolean pointIsInner(Point point, PhysicsPlaygroundHandler handler) {
        int size = handler.convexPoints.size();
        for (int i = 0; i < handler.convexPoints.size(); i++) {
            Point a = MathComplement.minus(handler.convexPoints.get(i), point);
            Point b = MathComplement.minus(handler.convexPoints.get((i + 1) % size), point);
            Point c = MathComplement.minus(handler.convexPoints.get((i + 2) % size), point);
            float x = Math.signum(a.cross(b));
            float y = Math.signum(b.cross(c));
            if (x * y < 0) return false;
        }
        return true;
    }

    public boolean isInner(PhysicsPlaygroundHandler handler) {
        for (Point point : convexPoints) {
            if (!pointIsInner(point, handler)) return false;
        }
        return true;
    }

    public boolean intersects(PhysicsPlaygroundHandler handler) {
        int aSize = convexPoints.size();
        int bSize = handler.convexPoints.size();
        for (int i = 0; i < aSize; i++) {
            Segment aSegment = new Segment(convexPoints.get(i), convexPoints.get((i + 1) % aSize));
            for (int j = 0; j < bSize; j++) {
                Segment bSegment = new Segment(handler.convexPoints.get(j), convexPoints.get((j + 1) % bSize));
                if (aSegment.intersects(bSegment)) return true;
            }
        }
        return false;
    }
}

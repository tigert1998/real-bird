package utils;

import java.util.*;

public class PhysicsPlayground {
    public static PhysicsPlayground shared = new PhysicsPlayground();

    private List<PhysicsPlaygroundHandler> sensitiveHandlers = new ArrayList<>();
    private List<PhysicsPlaygroundHandler> numbHandlers = new ArrayList<>();

    public boolean hit() {
        for (int i = 0; i < sensitiveHandlers.size(); i++) {
            for (int j = i + 1; j < sensitiveHandlers.size(); j++)
                if (sensitiveHandlers.get(i).intersects(sensitiveHandlers.get(j)))
                    return true;
            for (PhysicsPlaygroundHandler numbHandler : numbHandlers) {
                if (numbHandler.intersects(sensitiveHandlers.get(i)))
                    return true;
            }
        }
        return false;
    }

    public PhysicsPlaygroundHandler addHandler(boolean isSensitive, List<Point> convexPoints) {
        PhysicsPlaygroundHandler handler = new PhysicsPlaygroundHandler();
        handler.convexPoints = convexPoints;
        if (isSensitive)
            sensitiveHandlers.add(handler);
        else
            numbHandlers.add(handler);
        return handler;
    }
}

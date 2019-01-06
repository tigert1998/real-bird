package utils;

import java.awt.*;
import java.io.*;
import java.nio.file.*;

import org.w3c.dom.*;
import javax.xml.parsers.*;

public class Settings {
    private static Rectangle skylinePosition = null;
    private static Rectangle[] birdPosturePositions = null;
    private static Rectangle groundPosition = null;
    private static Rectangle pipeDownPosition = null;
    private static Rectangle pipeUpPosition = null;
    private static Rectangle[] bigNumberPositions = null;

    public static Rectangle getSkylinePosition() {
        return skylinePosition;
    }

    public static Rectangle[] getBirdPosturePositions() {
        return birdPosturePositions;
    }

    public static Rectangle getGroundPosition() {
        return groundPosition;
    }

    public static Rectangle getPipeDownPosition() {
        return pipeDownPosition;
    }

    public static Rectangle getPipeUpPosition() {
        return pipeUpPosition;
    }

    public static Rectangle[] getBigNumberPositions() {
        return bigNumberPositions;
    }

    static {
        InputStream inputStream = UniformReader.getInputStream(Paths.get("resources", "settings.xml"));
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputStream);

            skylinePosition = getPositionByName(doc, "skyline");
            groundPosition = getPositionByName(doc, "ground");
            pipeUpPosition = getPositionByName(doc, "pipe-up");
            pipeDownPosition = getPositionByName(doc, "pipe-down");

            birdPosturePositions = getPositionsByName(doc, "bird-posture");
            bigNumberPositions = getPositionsByName(doc, "big-number");
        } catch (Exception exception) {
            System.out.println("Oops! An exception happened when loading \"settings.xml:\"");
            exception.printStackTrace();
            System.exit(1);
        }
    }

    static private Rectangle getPositionByElement(Element element) {
        int x = Integer.valueOf(element.getElementsByTagName("x").item(0).getTextContent());
        int y = Integer.valueOf(element.getElementsByTagName("y").item(0).getTextContent());
        int width = Integer.valueOf(element.getElementsByTagName("width").item(0).getTextContent());
        int height = Integer.valueOf(element.getElementsByTagName("height").item(0).getTextContent());
        return new Rectangle(x, y, width, height);
    }

    static private Rectangle getPositionByName(Document document, String name) {
        Element element = (Element) document.getElementsByTagName(name).item(0);
        return getPositionByElement(element);
    }

    static private Rectangle[] getPositionsByName(Document document, String name) {
        NodeList nodeList = document.getElementsByTagName(name);
        Rectangle[] res = new Rectangle[nodeList.getLength()];
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            res[i] = getPositionByElement(element);
        }
        return res;
    }

    public static final float BIRD_RELATIVE_WIDTH =
            2.f * getBirdPosturePositions()[0].width / getSkylinePosition().width;
    public static final float BIRD_RELATIVE_HEIGHT =
            2.f * getBirdPosturePositions()[0].height / getSkylinePosition().height;
    public final static float PIPE_RELATIVE_WIDTH =
            2.f * getPipeUpPosition().width / getSkylinePosition().width;
    public final static float PIPE_DOWN_RELATIVE_HEIGHT =
            2.f * getPipeDownPosition().height / getSkylinePosition().height;
    public final static float PIPE_UP_RELATIVE_HEIGHT =
            2.f * getPipeUpPosition().height / getSkylinePosition().height;
    public final static float GROUND_RELATIVE_HEIGHT =
            2.f * getGroundPosition().height / getSkylinePosition().height;
    public final static float BIG_NUMBER_RELATIVE_WIDTH =
            2.f * getBigNumberPositions()[0].width / getSkylinePosition().width;
    public final static float BIG_NUMBER_RELATIVE_HEIGHT =
            2.f * getBigNumberPositions()[0].height / getSkylinePosition().height;
}

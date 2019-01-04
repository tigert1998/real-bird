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

    static {
        Path settingFilePath = Paths.get(System.getProperty("user.dir"), "resources", "settings.xml");
        File xmlFile = settingFilePath.toFile();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            skylinePosition = getPositionByName(doc, "skyline");
            groundPosition = getPositionByName(doc, "ground");
            pipeUpPosition = getPositionByName(doc, "pipe-up");
            pipeDownPosition = getPositionByName(doc, "pipe-down");

            NodeList nodeList = doc.getElementsByTagName("bird-posture");
            birdPosturePositions = new Rectangle[nodeList.getLength()];
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                birdPosturePositions[i] = getPositionByElement(element);
            }
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
}

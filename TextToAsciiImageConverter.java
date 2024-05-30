import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class TextToAsciiImageConverter {

    public static void main(String[] args) {
        String inputFilePath = "Gemeinschaftsordnung.txt";
        String outputImagePath = "Gemeinschaftsordnung.bmp";

        try {
            // Schritt 1: Text aus Datei lesen
            String text = readTextFromFile(inputFilePath);

            // Schritt 2: Bildgröße berechnen und Bild erstellen
            int[] dimensions = calculateImageDimensions(text.length());
            int width = dimensions[0];
            int height = dimensions[1];
            BufferedImage image = createImageFromText(text, width, height, inputFilePath);

            // Schritt 3: Bild speichern
            saveImage(image, outputImagePath);
            System.out.println("Image creation completed. Output saved to " + outputImagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readTextFromFile(String filePath) throws IOException {
        StringBuilder textBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                textBuilder.append(line).append("\n");
            }
        }
        return textBuilder.toString();
    }

    public static int[] calculateImageDimensions(int textLength) {
        int height = (int) Math.sqrt((10.0 / 7.0) * textLength);
        int width = (int) Math.ceil((double) textLength / height);
        
        // Sicherstellen, dass das Bildverhältnis 6:10 bleibt
        if (width * 10 != height * 7) {
            height = (int) Math.sqrt((10.0 / 7.0) * textLength);
            width = (int) Math.ceil((double) textLength / height);
        }
        
        return new int[]{width, height};
    }

    public static BufferedImage createImageFromText(String text, int width, int height, String fileName) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // Hintergrundfarbe festlegen
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);

        int index = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (index < text.length()) {
                    char character = text.charAt(index);
                    int asciiValue = (int) character;
                    Color color = asciiToColor(asciiValue);
                    image.setRGB(x, y, color.getRGB());
                    index++;
                } else {
                    // Wenn der Text kürzer ist als die Bildgröße, füllen wir den Rest mit Weiß
                    image.setRGB(x, y, Color.BLACK.getRGB());
                }
            }
        }

        // Dateiname oben links zeichnen
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 8));
        g.drawString(fileName, 2, 8); // 5 Pixel von links, 15 Pixel von oben
        g.dispose();

        return image;
    }

    public static Color asciiToColor(int asciiValue) {
        // Beispiel für eine einfache Farbumwandlung
        int r = (asciiValue * 123) % 256;
        int g = (asciiValue * 231) % 256;
        int b = (asciiValue * 321) % 256;
        return new Color(r, g, b);
    }

    public static void saveImage(BufferedImage image, String outputPath) throws IOException {
        File outputFile = new File(outputPath);
        ImageIO.write(image, "bmp", outputFile);
    }
}

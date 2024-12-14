package GUI;

import Piece.Piece;
import Piece.PieceType;
import Piece.PieceColor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class PieceImage {

    private static final String url = "/GUI/images/";

    private static String getStringImage(Piece piece){
        return url + piece.getColor() + "-" + piece.getType() + ".png";
    }
    public static BufferedImage getImage(Piece piece) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(Objects.requireNonNull(PieceImage.class.getResourceAsStream(getStringImage(piece))));
        } catch (IOException e) {
            System.err.println("Error loading image for piece: " + getStringImage(piece));
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("Resource not found: " + getStringImage(piece));
        }
        return image;
    }

    public static BufferedImage getPieceImage(PieceType type, PieceColor pieceColor) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(Objects.requireNonNull(PieceImage.class.getResourceAsStream(url + pieceColor + "-" + type + ".png")));
        } catch (IOException e) {
            System.err.println("Error loading image for piece: " + pieceColor + "-" + type + ".png");
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("Resource not found: " + pieceColor + "-" + type + ".png");
        }
        return image;
    }
}

package StaticField;

import Piece.Pieces;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class ButtonProperties {
    private static final String URL = "/Assets/Buttons";

    public static class Play{
        private static final String PLAY_URL = "/PlayButton";
        public static final String DEFAULT = PLAY_URL + "/Default.png";
        public static final String HOVER = PLAY_URL + "/Hover.png";
    }

    public static class Piece{
        private static final String PIECES_URL = "/Pieces";
        private static final String WHITE = PIECES_URL +  "/White";
        private static final String BLACK = PIECES_URL + "/Black";

        private static final String PAWN = "/Pawn.png";
        private static final String KNIGHT = "/Knight.png";
        private static final String ROOK = "/Rook.png";
        private static final String QUEEN = "/Queen.png";
        private static final String KING = "/King.png";
        private static final String BISHOP = "/Bishop.png";

        public static final String WHITE_PAWN = WHITE + PAWN;
        public static final String WHITE_KNIGHT = WHITE + KNIGHT;
        public static final String WHITE_ROOK = WHITE + ROOK;
        public static final String WHITE_QUEEN = WHITE + QUEEN;
        public static final String WHITE_KING = WHITE + KING;
        public static final String WHITE_BISHOP = WHITE + BISHOP;

        public static final String BLACK_PAWN = BLACK + PAWN;
        public static final String BLACK_KNIGHT = BLACK + KNIGHT;
        public static final String BLACK_ROOK = BLACK + ROOK;
        public static final String BLACK_QUEEN = BLACK + QUEEN;
        public static final String BLACK_KING = BLACK + KING;
        public static final String BLACK_BISHOP = BLACK + BISHOP;

        public static BufferedImage getPieceImage(int piece) {
            int color = Pieces.getColorValue(piece);
            int type = Pieces.getTypeValue(piece);

            return getButtonImg(getPieceUrl(color, type));
        }

        private static String getPieceUrl(int color, int type) {
            String c = "";
            String t = "";
            switch (color) {
                case 0 -> c = WHITE;
                case 8 -> c = BLACK;
                default -> c = "";
            }
            switch (type) {
                case 1 -> t = PAWN;
                case 2 -> t = KNIGHT;
                case 3 -> t = BISHOP;
                case 4 -> t = ROOK;
                case 5 -> t = QUEEN;
                case 6 -> t = KING;
                default -> t = "";
            }
            return c + t;
        }
    }

    public static BufferedImage getButtonImg(String name){
        BufferedImage img = null;
        String src = URL + name;
        InputStream inputStream = ButtonProperties.class.getResourceAsStream(src);
        try {
            img = ImageIO.read(Objects.requireNonNull(inputStream));
        } catch (IOException e) {
            System.err.println("Error loading image");
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("Resource not found");
        }
        return img;
    }
}

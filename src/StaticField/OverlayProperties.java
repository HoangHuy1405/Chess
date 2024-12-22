package StaticField;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class OverlayProperties {
    private static final String URL = "/Assets/OverLayouts/";

    public static final String PROMOTE_LAYOUT = "PromoteLayout.png";

    public static BufferedImage getLayoutImg(String name){
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

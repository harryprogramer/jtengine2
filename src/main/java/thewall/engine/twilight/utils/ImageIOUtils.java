package thewall.engine.twilight.utils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class ImageIOUtils {
    public static ImageReader checkImageFormat(String filePath) throws IOException {
        File file = new File(filePath);
        ImageInputStream iis = ImageIO.createImageInputStream(file);
        Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
        if (!iter.hasNext()) {
            throw new IOException("No readers found!");
        }
        ImageReader reader = iter.next();
        iis.close();
        return reader;
    }
}

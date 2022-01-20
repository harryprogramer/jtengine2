package jte2.engine.twilight.display;

import jte2.engine.twilight.texture.Picture;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;
import jte2.engine.twilight.Area;

import java.awt.image.BufferedImage;

public interface Display {
    String getTitle();

    void setTitle(String name);

    void show();

    void hide();

    void setSize(Area area);

    boolean isFocus();

    void focus();

    void fullscreen();

    void iconify();

    void maximize();

    void setMinimumSize(Area area);

    void setMaximumSize(Area area);

    void setLocation(Area area);

    void setLocation(int x, int y);

    Area getLocation();

    void setIcon(Picture image);

    void sendAttention();

    void setTransparency(float transparency);

    float getTransparency();

    void sizeChangedListener(DisplayResizeCallback callback);

    void setVSync(boolean vSync);
}

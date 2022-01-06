package thewall.engine.twilight.display;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import thewall.engine.twilight.Area;

import java.awt.image.BufferedImage;

public interface Display {
    String getTitle();

    void setTitle(String name);

    void show();

    void hide();

    void setSize(int x, int y);

    void setSize(Area area);

    boolean isFocus();

    void focus();

    void fullscreen();

    void iconify();

    void maximize();

    @Deprecated
    void setResizeLimit(int xMin, int yMin, int xMax, int yMax);

    void setMinimumSize(Area area);

    void setMaximumSize(Area area);

    @Deprecated(forRemoval = true)
    default void setResizeLimit(@NotNull Vector2i min, @NotNull Vector2i max){
        setResizeLimit(min.x, min.y, max.x, max.y);
    }

    /**
     * @deprecated
     *  {@link #setLocation(Area)}
     *  {@link #setLocation(int, int)}
     */
    @Deprecated(forRemoval = true)
    void setPosition(int x, int y);

    @Deprecated(forRemoval = true)
    Vector2i getResizeLimit();

    void setLocation(Area area);

    void setLocation(int x, int y);

    Area getLocation();

    void setIcon(BufferedImage bufferedImage);

    void setIcon(String file);

    void sendAttention();

    void setTransparency(float transparency);

    float getTransparency();

    void sizeChangedListener(DisplayResizeCallback callback);

    void setVSync(boolean vSync);
}

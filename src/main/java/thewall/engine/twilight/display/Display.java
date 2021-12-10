package thewall.engine.twilight.display;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.awt.image.BufferedImage;

public interface Display {
    String getTitleName();

    void setTitle(String name);

    void showWindow();

    void hide();

    void setSize(int x, int y);

    default void setSize(@NotNull Vector2i vector2i){
        setSize(vector2i.x, vector2i.y);
    }

    @Deprecated
    Vector2i getSize();

    boolean isFocus();

    void requestFocus();

    void switchFullscreen();

    void iconify();

    void maximize();

    void setResizeLimit(int xMin, int yMin, int xMax, int yMax);

    default void setResizeLimit(@NotNull Vector2i min, @NotNull Vector2i max){
        setResizeLimit(min.x, min.y, max.x, max.y);
    }

    @Deprecated
    Vector2i getResizeLimit();

    void setPosition(int x, int y);

    @Deprecated
    default void setPosition(@NotNull Vector2i vector2i){
        setPosition(vector2i.x, vector2i.y);
    }

    void setIcon(BufferedImage bufferedImage);

    void setIcon(String file);

    void attentionRequest();

    void setTransparency(float transparency);

    float getTransparency();

    void setVSync(boolean vSync);

    void setContentScale(float x, float y);

    default void getContentScale(@NotNull Vector3f vector3f){
        setContentScale(vector3f.x, vector3f.y);
    }

    Vector2f getContentScale();
}

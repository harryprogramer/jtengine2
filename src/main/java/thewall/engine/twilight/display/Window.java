package thewall.engine.twilight.display;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.awt.image.BufferedImage;

public interface Window {
    String getWindowName();

    void setWindowName(String name);

    void showWindow();

    void hideWindow();

    void setWindowSize(int x, int y);

    default void setWindowSize(@NotNull Vector2i vector2i){
        setWindowSize(vector2i.x, vector2i.y);
    }

    @Deprecated
    Vector2i getWindowSize();

    boolean isWindowFocus();

    void requestWindowFocus();

    void switchFullScreen();

    void iconifyWindow();

    void maximizeWindow();

    void destroyWindow();

    void setWindowSizeLimit(int xMin, int yMin, int xMax, int yMax);

    default void setWindowSizeLimit(@NotNull Vector2i min, @NotNull Vector2i max){
        setWindowSizeLimit(min.x, min.y, max.x, max.y);
    }

    Vector2i getWindowSizeLimit();

    void setWindowPosition(int x, int y);

    @Deprecated
    default void setWindowPosition(@NotNull Vector2i vector2i){
        setWindowPosition(vector2i.x, vector2i.y);
    }

    void setWindowIcon(BufferedImage bufferedImage);

    void setWindowIcon(String file);

    void sendWindowAttentionRequest();

    void setWindowTransparency(float transparency);

    float getWindowTransparency();

    void setWindowContentScale(float x, float y);

    default void getWindowContentScale(@NotNull Vector3f vector3f){
        setWindowContentScale(vector3f.x, vector3f.y);
    }

    Vector2f getWindowContentScale();
}

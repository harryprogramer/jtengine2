package thewall.engine.twilight.gui;

import lombok.Data;
import org.joml.Vector2f;

@Data
@Deprecated
public class GuiTexture {
    private final int texture;

    private final Vector2f position;

    private final Vector2f scale;
}

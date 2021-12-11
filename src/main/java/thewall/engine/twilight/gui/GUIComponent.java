package thewall.engine.twilight.gui;

import com.google.common.primitives.Floats;
import org.joml.Vector2f;
import thewall.engine.twilight.material.Material;
import thewall.engine.twilight.models.Mesh;
import thewall.engine.twilight.spatials.Spatial;
import thewall.engine.twilight.spatials.Spatial2D;

public abstract class GUIComponent extends Spatial2D {
    public GUIComponent(){
        Mesh mesh = new Mesh();
        mesh.setVertices(Floats.asList(-1, 1, -1, -1, 1, 1, 1, -1));
        setMesh(mesh);
    }

    @Override
    public Material getMaterial() {
        return getContent();
    }

    public abstract Material getContent();
}

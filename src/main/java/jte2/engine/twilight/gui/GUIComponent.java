package jte2.engine.twilight.gui;

import com.google.common.primitives.Floats;
import jte2.engine.twilight.material.Material;
import jte2.engine.twilight.models.Mesh;
import jte2.engine.twilight.spatials.Spatial2D;

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

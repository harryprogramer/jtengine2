package thewall.engine.twilight.entity;

import org.joml.Vector3f;
import thewall.engine.twilight.material.Material;
import thewall.engine.twilight.models.Mesh;
import thewall.engine.twilight.utils.Colour;
import thewall.engine.twilight.utils.Validation;

/**
 * Class to represent a some spatial object, which can be rendered
 */
public abstract class Spatial {
    private static int SPATIAL_INDEX = 0;
    private final Vector3f transformation = new Vector3f(0, 0, 0);
    private final Vector3f rotation = new Vector3f();
    private Material material;
    private float scale = 1;
    private String name;
    private Mesh mesh;

    /**
     * Spatial
     */
    public Spatial(){
        this.name = "Spatial-" + ++SPATIAL_INDEX;
    }

    /**
     * Set new transformation to this object
     *
     * @param transformation new object transformation
     *
     * @return modifed {@link Spatial}
     */
    public Spatial setTransformation(Vector3f transformation){
        this.transformation.set(transformation);
        return this;
    }

    // TODO: docs
    public void addTransformation(Vector3f transformation){
        this.transformation.add(transformation);
    }

    public void addTransformation(float x, float y, float z){
        this.transformation.add(x, y, z);
    }

    /**
     * Set transformation of this object
     * @param x x position
     * @param y y position
     * @param z z position
     */
    public void setTransformation(float x, float y, float z){
        this.transformation.set(x, y, z);
    }

    /**
     * Set name of this spatial
     * @param name name
     */
    public void setName(String name){
        Validation.checkNull(name);
        this.name = name;
    }

    /**
     * Get name of this spatial
     * @return spatial name
     */
    public String getName() {
        return name;
    }

    /**
     * Set rotation of this spatial
     * @param rotation float vector of rotation
     * @return modified spatial
     */
    public Spatial setRotation(Vector3f rotation){
        this.rotation.set(rotation);
        return this;
    }

    /**
     * Set rotation of this spatial
     * @param x rotation x
     * @param y rotation y
     * @param z rotation z
     * @return modified spatial
     */
    public Spatial setRotation(float x, float y, float z){
        this.rotation.set(x, y, z);
        return this;
    }

    /**
     * Get rotation of this spatial
     * @return vector of spatial
     */
    public Vector3f getRotation(){
        return rotation;
    }

    /**
     * Get transformation of this spatial
     * @return transformation vector
     */
    public Vector3f getTransformation(){
        return transformation;
    }

    /**
     * Set scale of this spatial
     * @param scale spatial scale
     */
    public void setScale(float scale){
        this.scale = scale;
    }

    /**
     * Get scale of this spatial
     * @return spatial size
     */
    public float getScale(){
        return scale;
    }

    /**
     * Set spatial material
     *
     * @param material spatial material
     */
    public void setMaterial(Material material){
        Validation.checkNull(material);
        this.material = material;
    }

    /**
     * Get spatial material
     * @return spatial material
     */
    public Material getMaterial(){
        if(material == null){
            return new Material("Default Material").loadColour(Colour.WHITE);
        }
        return material;
    }

    public void setMesh(Mesh mesh){
        Validation.checkNull(mesh);
        this.mesh = mesh;
    }

    public Mesh getMesh(){
        return mesh;
    }


    @Override
    public String toString() {
        return "Spatial{" +
                "transformation=" + transformation +
                ", rotation=" + rotation +
                ", scale=" + scale +
                ", name='" + name + '\'' +
                '}';
    }
}

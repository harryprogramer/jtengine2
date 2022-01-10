package jte2.engine.twilight.spatials;

import org.joml.Vector2f;
import jte2.engine.twilight.material.Colour;
import jte2.engine.twilight.material.Material;
import jte2.engine.twilight.models.Mesh;
import jte2.engine.twilight.utils.Validation;

public abstract class Spatial2D {
    private static int SPATIAL_INDEX = 0;
    private final Vector2f transformation = new Vector2f(0, 0);
    private final Vector2f scale = new Vector2f(1, 1);
    private final Vector2f rotation = new Vector2f();
    private Material material;
    private float size = 1;
    private String name;
    private Mesh mesh;


    public Vector2f getScale(){
        return scale;
    }

    public void setScale(Vector2f scale){
        if(scale == null){
            throw new NullPointerException("Scale is null");
        }

        this.scale.set(scale);
    }

    /**
     * Spatial2D
     */
    public Spatial2D(){
        this.name = "Spatial-" + ++SPATIAL_INDEX;
    }

    /**
     * Set new transformation to this object
     *
     * @param transformation new object transformation
     *
     * @return modifed {@link Spatial}
     */
    public Spatial2D setTransformation(Vector2f transformation){
        this.transformation.set(transformation);
        return this;
    }

    // TODO: docs
    public void addTransformation(Vector2f transformation){
        this.transformation.add(transformation);
    }

    public void addTransformation(float x, float y){
        this.transformation.add(x, y);
    }

    /**
     * Set transformation of this object
     * @param x x position
     * @param y y position
     */
    public void setTransformation(float x, float y){
        this.transformation.set(x, y);
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
    public Spatial2D setRotation(Vector2f rotation){
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
    public Spatial2D setRotation(float x, float y, float z){
        this.rotation.set(x, y);
        return this;
    }

    /**
     * Get rotation of this spatial
     * @return vector of spatial
     */
    public Vector2f getRotation(){
        return rotation;
    }

    /**
     * Get transformation of this spatial
     * @return transformation vector
     */
    public Vector2f getTransformation(){
        return transformation;
    }

    /**
     * Set scale of this spatial
     * @param scale spatial scale
     */
    public void setSize(float scale){
        this.size = scale;
    }

    /**
     * Get scale of this spatial
     * @return spatial size
     */
    public float getSize(){
        return size;
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
                ", scale=" + size +
                ", name='" + name + '\'' +
                '}';
    }
}

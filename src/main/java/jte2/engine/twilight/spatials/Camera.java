package jte2.engine.twilight.spatials;

import org.joml.Vector3f;
import jte2.engine.twilight.utils.Validation;

/**
 * Class to represent a Camera in Twilight Engine
 * @author many
 */
public class Camera extends Spatial {
    private static final int MAX_FAR_PLANE = 25600;

    private static final int MAX_FOV = 190;
    private float NEAR_PLANE = 0.1f;
    private float FAR_PLANE = 17000;
    private short fov = 90; /** fov =< MAX_FOV */

    public Camera() {
        setTransformation(0, -45, 0);
    }

    /**
     * Constructor of camera with transformation
     * @param transformation new camera transformation
     */
    public Camera(Vector3f transformation) {
        this();
        Validation.checkNull(transformation);
        setTransformation(transformation);
    }

    /**
     * Get camera FOV
     * @return camera fov
     */
    public short getFOV(){
        return fov;
    }

    /**
     * Set the FOV of camera
     * @param fov camera fov
     * @return modifed camera
     */
    public Camera setFOV(short fov){
        if(MAX_FOV < fov){
            throw new IllegalStateException("Max fov is: " + MAX_FOV);
        }
        this.fov = fov;
        return this;
    }

    /**
     * Get camera near plane
     * @return near plane
     */
    public float getNearPlane(){
        return NEAR_PLANE;
    }

    public float getFarPlane(){
        return FAR_PLANE;
    }

    /**
     * Set camera near plane
     * @param nearPlane camera near plane
     * @return modifed camera
     */
    public Camera setNearPlane(float nearPlane){
        this.NEAR_PLANE = nearPlane;
        return this;
    }

    public Camera setFarPlane(float farPlane){
        if(MAX_FAR_PLANE < farPlane){
            throw new IllegalStateException("Max near plane is: " + MAX_FAR_PLANE);
        }
        this.FAR_PLANE = farPlane;
        return this;
    }


}

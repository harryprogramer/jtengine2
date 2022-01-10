package jte2.engine.twilight;

import org.jetbrains.annotations.NotNull;
import jte2.engine.twilight.serialization.JTESerializable;

import java.io.Serializable;

public final class Area implements Serializable, JTESerializable  {
    private int width, height;

    public Area(){
        this.width = 0;
        this.height = 0;
    }

    /**
     * Area of 2 dimensions
     * @param width width
     * @param height height
     */
    public Area(int width, int height){
        this.width = width;
        this.height = height;
    }

    /**
     * Get area width
     * @return width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get area height
     * @return height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Set area height
     * @param height height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Set area width
     * @param width width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Set new area
     * @param area new area
     */
    public void setArea(@NotNull Area area){
        this.width = area.width;
        this.height = area.height;
    }

    @Override
    public String toString() {
        return "Area{" +
                "width=" + width +
                ", height=" + height +
                '}';
    }
}

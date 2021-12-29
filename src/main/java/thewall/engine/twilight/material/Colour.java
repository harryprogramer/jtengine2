package thewall.engine.twilight.material;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Range;
import org.joml.Vector3f;

import java.awt.*;

public class Colour {
    private final static short MAX_RGB = 255;

    /**
     * White colour
     */
    public final static Colour WHITE = new Colour(242, 235, 235);

    /**
     * Black colour
     */
    public final static Colour BLACK = new Colour(0, 0, 0);

    /**
     * Blue colour
     */
    public final static Colour BLUE = new Colour(75, 175, 242);

    /**
     * Yellow colour
     */
    public final static Colour YELLOW = new Colour(237, 232, 88);

    /**
     * Red colour
     */
    public final static Colour RED = new Colour(232, 73, 67);

    /**
     * Pink colour
     */
    public final static Colour PINK = new Colour(197, 79, 227);

    /**
     * Green colour
     */
    public final static Colour GREEN = new Colour(118, 235, 82);

    /**
     * Orange colour
     */
    public final static Colour ORANGE = new Colour(235, 169, 82);

    /**
     * Purple colour
     */
    public final static Colour PURPLE = new Colour(125, 82, 235);

    /**
     * Aqua colour
     */
    public final static Colour AQUA = new Colour(82, 235, 212);


    private int r, g, b;
    private int alpha = -1;

    public Colour(int r, int g, int b){
        if(r > MAX_RGB || g > MAX_RGB || b > MAX_RGB){
            throw new IllegalStateException("rgb value out of range");
        }

        this.r = r;
        this.g = g;
        this.b = b;
    }

    public Colour(int r, int g, int b, int alpha){
        if(r > MAX_RGB || g > MAX_RGB || b > MAX_RGB){
            throw new IllegalStateException("rgb value out of range");
        }

        this.r = r;
        this.g = g;
        this.b = b;
        this.alpha = alpha;
    }

    public String toHex(){
        return String.format("#%02X%02X%02X", r, g, b);
    }

    public void setRGB(int r, int g, int b){
        if(r > MAX_RGB || g > MAX_RGB || b > MAX_RGB){
            throw new IllegalStateException("rgb value out of range");
        }

        this.r = r;
        this.g = g;
        this.b = b;
    }

    public void setRGB(int r, int g, int b, int alpha){
        if(r > MAX_RGB || g > MAX_RGB || b > MAX_RGB){
            throw new IllegalStateException("rgb value out of range");
        }

        this.r = r;
        this.g = g;
        this.b = b;
        this.alpha = alpha;
    }

    public Vector3f getVectorColor(){
        return new Vector3f(r, g, b);
    }

    @Contract(pure = true)
    public int getRed(){
        return r;
    }

    @Contract(pure = true)
    public int getGreen() {
        return g;
    }

    @Contract(pure = true)
    public int getBlue() {
        return b;
    }

    @Contract(pure = true)
    public int getAlpha() {
        return alpha;
    }

    @Contract(pure = true)
    public Color getColor(){
        return new Color(r, g, b); // TODO: alpha support
    }

    @Contract(pure = true)
    public @Range(from = 0, to = 3) int[] getRGB(){
        return new int[]{r, g, b, alpha};
    }

    public Colour createColor(int r, int g, int b){
        return new Colour(r, g, b);
    }

    public Colour createColor(int r, int g, int b, int alpha){
        return new Colour(r, g, b, alpha);
    }

    public float getRedChannel(){
        return r / 255.0F;
    }

    public float getGreenChannel(){
        return g / 255.0F;
    }

    public float getBlueChannel(){
        return b / 255.0F;
    }

    @Override
    public String toString() {
        return "Colour{" +
                "r=" + r +
                ", g=" + g +
                ", b=" + b +
                ", alpha=" + alpha +
                '}';
    }
}

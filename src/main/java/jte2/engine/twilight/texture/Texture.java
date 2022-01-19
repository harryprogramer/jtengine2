package jte2.engine.twilight.texture;

import java.awt.image.BufferedImage;

/***
 * MIT License
 *
 * Copyright (c) 2018 Serhiy Boychenko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
public abstract class Texture {
    private final PixelFormat pixelFormat;
    private int textureAtlasSize = 1;
    private int textureAtlasIndex = 0;
    private int id = -1;

    private final int textureWidth, textureHeight;
    private boolean isTransparency = false, isFakeLighting = false;

    private float shineDamper, reflectivity;

    public Texture(PixelFormat pixelFormat, int width, int height){
        this.pixelFormat = pixelFormat;
        this.textureWidth = width;
        this.textureHeight = height;
    }

    public Texture(PixelFormat pixelFormat, int width, int height, int textureAtlasSize){
        this(pixelFormat, width, height);
        this.textureAtlasSize = textureAtlasSize;
    }

    /**
     * Converts source image to height map.
     *
     * @param image to be converted to height map.
     * @return resulting height map.
     */
    protected BufferedImage convertToHeight(BufferedImage image) {
        if (image == null) {
            throw new IllegalArgumentException("Source image cannot be null!");
        }

        BufferedImage greyscaleImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        for (int line = 0; line < image.getHeight(); line++) {
            for (int column = 0; column < image.getWidth(); column++) {
                int color = image.getRGB(column, line);
                int grey = (int) ((((color) & 0xFF) * .299f) + (((color >> 8) & 0xFF) * .587f) + ((color >> 16) & 0xFF) * .114f);
                int greyscaleColor = (((color >> 24) & 0xFF) << 24) + (grey << 16) + (grey << 8) + grey;
                greyscaleImage.setRGB(column, line, greyscaleColor);
            }
        }
        return greyscaleImage;
    }

    /**
     * Converts height map to normal map (with only four adjacent pixels).
     *
     * @param image corresponding to height map.
     * @param scale controlling the color transition smoothness (the lower the scale is the smoother transition will be).
     * @return resulting normal map.
     */
    protected BufferedImage convertToNormal(BufferedImage image, float scale) {
        if (image == null) {
            throw new IllegalArgumentException("Source image cannot be null!");
        }

        if (scale < 0) {
            throw new IllegalArgumentException("Scale must be positive!");
        }

        BufferedImage normalMap = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        float [][] pixelHeights = new float[image.getHeight()][image.getWidth()];
        for (int line = 0; line < image.getHeight(); line++) {
            for (int column = 0; column < image.getWidth(); column++) {
                pixelHeights[line][column] = pixelHeight(image.getRGB(column, line), scale);
            }
        }

        int left, right, up, down, normalColorRGB, normalColorRGBA;
        for (int line = 0; line < image.getHeight(); line++) {
            for (int column = 0; column < image.getWidth(); column++) {
                left = column - 1;
                right = column + 1;
                up = line - 1;
                down = line + 1;

                if( left < 0 ) {
                    left = image.getWidth() - 1;
                } else if( right >= image.getWidth()) {
                    right = 0;
                }

                if( up < 0 ) {
                    up = image.getHeight() - 1;
                } else if( down >= image.getHeight() ) {
                    down = 0;
                }

                normalColorRGB = getNormal(pixelHeights[line][left], pixelHeights[up][column], pixelHeights[line][right], pixelHeights[down][column]);
                normalColorRGBA = (((image.getRGB(column, line) >> 24) & 0xFF) << 24) + normalColorRGB;

                normalMap.setRGB(column, line, normalColorRGBA);
            }
        }

        return normalMap;
    }

    private static float pixelHeight(int color, float scale) {
        return (((color) & 0xFF) + ((color >> 8) & 0xFF) + ((color >> 16) & 0xFF)) / 765.0f * scale;
    }

    private static int getNormal(float left, float up, float right, float down) {
        float x = left - right;
        float y = down - up;
        float z = 1.0f;

        float val = (float) (1.0f / Math.sqrt( x * x + y * y + z * z ));

        x *= val;
        y *= val;
        z *= val;

        return ((int)((x + 1) * 127.5f) << 16) + ((int)((y + 1) * 127.5f) << 8) + (int) ((z + 1) * 127.5f);
    }

    public void setTextureAtlasIndex(int index){
        this.textureAtlasIndex = index;
    }

    public int getTextureAtlasSize(){
        return textureAtlasSize;
    }

    public int getTextureWidth(){
        return textureWidth;
    }

    public int getTextureHeight(){
        return textureHeight;
    }

    public PixelFormat getPixelFormat(){
        return pixelFormat;
    }

    public void setTransparency(boolean transparency){
        this.isTransparency = transparency;
    }

    public void setFakeLighting(boolean fakeLighting){
        this.isFakeLighting = fakeLighting;
    }

    public boolean isTransparency(){
        return isTransparency;
    }

    public boolean isFakeLighting(){
        return isFakeLighting;
    }

    public float getShineDamper(){
        return shineDamper;
    }

    public void setShineDamper(float shineDamper){
        this.shineDamper = shineDamper;
    }

    public float getReflectivity(){
        return reflectivity;
    }

    public void setReflectivity(float reflectivity){
        this.reflectivity = reflectivity;
    }

    public int getTextureAtlasIndex() {
        return textureAtlasIndex;
    }

    public void setID(int id) {
        if(this.id != -1){
            throw new IllegalArgumentException("id is already set");
        }

        this.id = id;
    }

    public int getID() {
        return id;
    }
}

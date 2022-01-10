package jte2.engine.twilight.renderer;

import org.joml.Matrix4f;
import jte2.engine.twilight.shaders.ShaderHandle;
import jte2.engine.twilight.shaders.gl.TerrainShader;
import jte2.engine.twilight.shaders.gl.UnshadedShader;
import jte2.engine.twilight.viewport.RenderQueue;
import jte2.engine.twilight.viewport.RenderQueue2D;
import jte2.engine.twilight.viewport.ViewPort;
import jte2.engine.twilight.renderer.opengl.GLRenderer;
import jte2.engine.twilight.material.Colour;
import jte2.engine.twilight.viewport.ViewPort2D;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

/**
 * Basis of the deepest level of this engine. This interface is mainly used to render the objects, gui and more.
 * @author many
 */
public interface Renderer {
    /**
     * Init the renderer
     *
     * @param viewPort initial viewport
     */
    void init(ViewPort viewPort);

    /**
     * Set the background color of the world universe
     * @param colour colour for the background
     */
    void setBackground(Colour colour);

    /**
     * Update the viewport
     * @param x viewport x
     * @param y viewport y
     * @param width screen width
     * @param height screen height
     */
    void setViewPort(int x, int y, int width, int height);

    /**
     * Get current projection matrix used by renderer
     * @return projection matrix
     */
    Matrix4f getProjectionMatrix();

    void hideSkybox();

    void showSkybox();

    /**
     * Change projection matrix
     * @param matrix matrix to change
     */
    void changeProjectionMatrix(Matrix4f matrix);

    /**
     * Prepare render queue for displaying later.
     * The function prepares the objects in the queue and saves them for rendering.
     * For example, the queue handled by {@link GLRenderer} prepares objects and generates a VAO for them.
     * @param renderQueue render queue to prepare
     */
    void prepareRenderQueue(RenderQueue renderQueue, RenderQueue2D renderQueue2D);

    /**
     *
     * This function renders a previously prepared queue for display using {@link ViewPort}.
     * @param viewPort current viewport
     * @param viewport2D viewport of 2d
     */
    void render(ViewPort viewPort, ViewPort2D viewport2D);

    /**
     * Set global renderer shader.
     * If no shader is set, renderer should be use a {@link UnshadedShader}
     * @param shader spatial shader
     */
    void setSpatialShader(ShaderHandle shader);

    /**
     * Set global renderer shader.
     * If no shader is set, renderer should be use a {@link TerrainShader}
     * @param shader terrain shader
     */
    void setTerrainShader(ShaderHandle shader);

    /**
     * Clean up the all loaded objects and clean the renderer
     */
    void cleanUp();

    /**
     * Take screenshot of screen to outputFile
     * @param outputFile output file
     */
    void takeScreenShot(String outputFile);

    /**
     * Take screenshot of screen to {@link BufferedImage} buffer
     * @param buffer image to write
     */
    void takeScreenShot(BufferedImage buffer);

    /**
     * Take screenshot of screen to {@link BufferedImage} buffer
     * @param buffer buffer to write
     */
    void takeScreenShot(ByteBuffer buffer);

    /**
     * Get name of renderer
     * @return name
     */
    String getName();
}

package thewall.engine.twilight.renderer;

import org.joml.Matrix4f;
import thewall.engine.twilight.RenderQueue;
import thewall.engine.twilight.ViewPort;
import thewall.engine.twilight.renderer.opengl.GLRenderer;
import thewall.engine.twilight.utils.Colour;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public interface Renderer {
    /**
     * Init the renderer
     *
     * @param viewPort current viewport
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
    void prepareRenderQueue(RenderQueue renderQueue);

    /**
     *
     * This function renders a previously prepared queue for display using {@link thewall.engine.twilight.ViewPort}.
     * @param viewPort current viewport
     */
    void render(ViewPort viewPort);

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

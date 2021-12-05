package thewall.engine.twilight.renderer.opengl;


import org.lwjgl.opengl.GL20C;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL40;
import org.lwjgl.system.NativeType;

import java.nio.FloatBuffer;

@SuppressWarnings("unused")
public interface GL2 extends GL{
    int GL_SHADING_LANGUAGE_VERSION = 0x8B8C;

    /** Accepted by the {@code pname} parameter of GetInteger. */
    int GL_CURRENT_PROGRAM = 0x8B8D;

    /** Accepted by the {@code pname} parameter of GetShaderiv. */
    int
            GL_SHADER_TYPE                 = 0x8B4F,
            GL_DELETE_STATUS               = 0x8B80,
            GL_COMPILE_STATUS              = 0x8B81,
            GL_LINK_STATUS                 = 0x8B82,
            GL_VALIDATE_STATUS             = 0x8B83,
            GL_INFO_LOG_LENGTH             = 0x8B84,
            GL_ATTACHED_SHADERS            = 0x8B85,
            GL_ACTIVE_UNIFORMS             = 0x8B86,
            GL_ACTIVE_UNIFORM_MAX_LENGTH   = 0x8B87,
            GL_ACTIVE_ATTRIBUTES           = 0x8B89,
            GL_ACTIVE_ATTRIBUTE_MAX_LENGTH = 0x8B8A,
            GL_SHADER_SOURCE_LENGTH        = 0x8B88;

    /** Returned by the {@code type} parameter of GetActiveUniform. */
    int
            GL_FLOAT_VEC2        = 0x8B50,
            GL_FLOAT_VEC3        = 0x8B51,
            GL_FLOAT_VEC4        = 0x8B52,
            GL_INT_VEC2          = 0x8B53,
            GL_INT_VEC3          = 0x8B54,
            GL_INT_VEC4          = 0x8B55,
            GL_BOOL              = 0x8B56,
            GL_BOOL_VEC2         = 0x8B57,
            GL_BOOL_VEC3         = 0x8B58,
            GL_BOOL_VEC4         = 0x8B59,
            GL_FLOAT_MAT2        = 0x8B5A,
            GL_FLOAT_MAT3        = 0x8B5B,
            GL_FLOAT_MAT4        = 0x8B5C,
            GL_SAMPLER_1D        = 0x8B5D,
            GL_SAMPLER_2D        = 0x8B5E,
            GL_SAMPLER_3D        = 0x8B5F,
            GL_SAMPLER_CUBE      = 0x8B60,
            GL_SAMPLER_1D_SHADOW = 0x8B61,
            GL_SAMPLER_2D_SHADOW = 0x8B62;

    /** Accepted by the {@code type} argument of CreateShader and returned by the {@code params} parameter of GetShaderiv. */
    int GL_VERTEX_SHADER = 0x8B31;

    /** Accepted by the {@code pname} parameter of GetBooleanv, GetIntegerv, GetFloatv, and GetDoublev. */
    int
            GL_MAX_VERTEX_UNIFORM_COMPONENTS    = 0x8B4A,
            GL_MAX_VARYING_FLOATS               = 0x8B4B,
            GL_MAX_VERTEX_ATTRIBS               = 0x8869,
            GL_MAX_TEXTURE_IMAGE_UNITS          = 0x8872,
            GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS   = 0x8B4C,
            GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS = 0x8B4D,
            GL_MAX_TEXTURE_COORDS               = 0x8871;

    /**
     * Accepted by the {@code cap} parameter of Disable, Enable, and IsEnabled, and by the {@code pname} parameter of GetBooleanv, GetIntegerv, GetFloatv, and
     * GetDoublev.
     */
    int
            GL_VERTEX_PROGRAM_POINT_SIZE = 0x8642,
            GL_VERTEX_PROGRAM_TWO_SIDE   = 0x8643;

    /** Accepted by the {@code pname} parameter of GetVertexAttrib{dfi}v. */
    int
            GL_VERTEX_ATTRIB_ARRAY_ENABLED    = 0x8622,
            GL_VERTEX_ATTRIB_ARRAY_SIZE       = 0x8623,
            GL_VERTEX_ATTRIB_ARRAY_STRIDE     = 0x8624,
            GL_VERTEX_ATTRIB_ARRAY_TYPE       = 0x8625,
            GL_VERTEX_ATTRIB_ARRAY_NORMALIZED = 0x886A,
            GL_CURRENT_VERTEX_ATTRIB          = 0x8626;

    /** Accepted by the {@code pname} parameter of GetVertexAttribPointerv. */
    int GL_VERTEX_ATTRIB_ARRAY_POINTER = 0x8645;

    /** Accepted by the {@code type} argument of CreateShader and returned by the {@code params} parameter of GetShaderiv. */
    int GL_FRAGMENT_SHADER = 0x8B30;

    /** Accepted by the {@code pname} parameter of GetBooleanv, GetIntegerv, GetFloatv, and GetDoublev. */
    int GL_MAX_FRAGMENT_UNIFORM_COMPONENTS = 0x8B49;

    /** Accepted by the {@code target} parameter of Hint and the {@code pname} parameter of GetBooleanv, GetIntegerv, GetFloatv, and GetDoublev. */
    int GL_FRAGMENT_SHADER_DERIVATIVE_HINT = 0x8B8B;

    /** Accepted by the {@code pname} parameters of GetIntegerv, GetFloatv, and GetDoublev. */
    int
            GL_MAX_DRAW_BUFFERS = 0x8824,
            GL_DRAW_BUFFER0     = 0x8825,
            GL_DRAW_BUFFER1     = 0x8826,
            GL_DRAW_BUFFER2     = 0x8827,
            GL_DRAW_BUFFER3     = 0x8828,
            GL_DRAW_BUFFER4     = 0x8829,
            GL_DRAW_BUFFER5     = 0x882A,
            GL_DRAW_BUFFER6     = 0x882B,
            GL_DRAW_BUFFER7     = 0x882C,
            GL_DRAW_BUFFER8     = 0x882D,
            GL_DRAW_BUFFER9     = 0x882E,
            GL_DRAW_BUFFER10    = 0x882F,
            GL_DRAW_BUFFER11    = 0x8830,
            GL_DRAW_BUFFER12    = 0x8831,
            GL_DRAW_BUFFER13    = 0x8832,
            GL_DRAW_BUFFER14    = 0x8833,
            GL_DRAW_BUFFER15    = 0x8834;

    /**
     * Accepted by the {@code cap} parameter of Enable, Disable, and IsEnabled, by the {@code pname} parameter of GetBooleanv, GetIntegerv, GetFloatv, and
     * GetDoublev, and by the {@code target} parameter of TexEnvi, TexEnviv, TexEnvf, TexEnvfv, GetTexEnviv, and GetTexEnvfv.
     */
    int GL_POINT_SPRITE = 0x8861;

    /**
     * When the {@code target} parameter of TexEnvf, TexEnvfv, TexEnvi, TexEnviv, GetTexEnvfv, or GetTexEnviv is POINT_SPRITE, then the value of
     * {@code pname} may be.
     */
    int GL_COORD_REPLACE = 0x8862;

    /** Accepted by the {@code pname} parameter of PointParameter{if}v. */
    int GL_POINT_SPRITE_COORD_ORIGIN = 0x8CA0;

    /** Accepted by the {@code param} parameter of PointParameter{if}v. */
    int
            GL_LOWER_LEFT = 0x8CA1,
            GL_UPPER_LEFT = 0x8CA2;

    /** Accepted by the {@code pname} parameter of GetBooleanv, GetIntegerv, GetFloatv, and GetDoublev. */
    int
            GL_BLEND_EQUATION_RGB   = 0x8009,
            GL_BLEND_EQUATION_ALPHA = 0x883D;

    /** Accepted by the {@code pname} parameter of GetIntegerv. */
    int
            GL_STENCIL_BACK_FUNC            = 0x8800,
            GL_STENCIL_BACK_FAIL            = 0x8801,
            GL_STENCIL_BACK_PASS_DEPTH_FAIL = 0x8802,
            GL_STENCIL_BACK_PASS_DEPTH_PASS = 0x8803,
            GL_STENCIL_BACK_REF             = 0x8CA3,
            GL_STENCIL_BACK_VALUE_MASK      = 0x8CA4,
            GL_STENCIL_BACK_WRITEMASK       = 0x8CA5;

    int GL_CURRENT_RASTER_SECONDARY_COLOR = 0x845F;

    /** Returned by the {@code type} parameter of GetActiveUniform. */
    int
            GL_FLOAT_MAT2x3 = 0x8B65,
            GL_FLOAT_MAT2x4 = 0x8B66,
            GL_FLOAT_MAT3x2 = 0x8B67,
            GL_FLOAT_MAT3x4 = 0x8B68,
            GL_FLOAT_MAT4x2 = 0x8B69,
            GL_FLOAT_MAT4x3 = 0x8B6A;

    /**
     * Accepted by the {@code target} parameters of BindBuffer, BufferData, BufferSubData, MapBuffer, UnmapBuffer, GetBufferSubData, GetBufferParameteriv, and
     * GetBufferPointerv.
     */
    int
            GL_PIXEL_PACK_BUFFER   = 0x88EB,
            GL_PIXEL_UNPACK_BUFFER = 0x88EC;

    /** Accepted by the {@code pname} parameter of GetBooleanv, GetIntegerv, GetFloatv, and GetDoublev. */
    int
            GL_PIXEL_PACK_BUFFER_BINDING   = 0x88ED,
            GL_PIXEL_UNPACK_BUFFER_BINDING = 0x88EF;

    /** Accepted by the {@code internalformat} parameter of TexImage1D, TexImage2D, TexImage3D, CopyTexImage1D, CopyTexImage2D. */
    int
            GL_SRGB                        = 0x8C40,
            GL_SRGB8                       = 0x8C41,
            GL_SRGB_ALPHA                  = 0x8C42,
            GL_SRGB8_ALPHA8                = 0x8C43,
            GL_SLUMINANCE_ALPHA            = 0x8C44,
            GL_SLUMINANCE8_ALPHA8          = 0x8C45,
            GL_SLUMINANCE                  = 0x8C46,
            GL_SLUMINANCE8                 = 0x8C47,
            GL_COMPRESSED_SRGB             = 0x8C48,
            GL_COMPRESSED_SRGB_ALPHA       = 0x8C49,
            GL_COMPRESSED_SLUMINANCE       = 0x8C4A,
            GL_COMPRESSED_SLUMINANCE_ALPHA = 0x8C4B;


    void glEnableVertexAttribArray(int index);

    void glVertexAttribPointer(int index, int size, int type, boolean normalized, int stride, long pointer);

    /**
     * Attaches a shader object to a program object.
     *
     * <p>In order to create a complete shader program, there must be a way to specify the list of things that will be linked together. Program objects provide
     * this mechanism. Shaders that are to be linked together in a program object must first be attached to that program object. glAttachShader attaches the
     * shader object specified by shader to the program object specified by program. This indicates that shader will be included in link operations that will
     * be performed on program.</p>
     *
     * <p>All operations that can be performed on a shader object are valid whether or not the shader object is attached to a program object. It is permissible to
     * attach a shader object to a program object before source code has been loaded into the shader object or before the shader object has been compiled. It
     * is permissible to attach multiple shader objects of the same type because each may contain a portion of the complete shader. It is also permissible to
     * attach a shader object to more than one program object. If a shader object is deleted while it is attached to a program object, it will be flagged for
     * deletion, and deletion will not occur until glDetachShader is called to detach it from all program objects to which it is attached.</p>
     *
     * @param program the program object to which a shader object will be attached
     * @param shader  the shader object that is to be attached
     *
     * @see <a target="_blank" href="http://docs.gl/gl4/glAttachShader">Reference Page</a>
     */
    void glAttachShader(int program, int shader);

    /**
     * Links a program object.
     *
     * @param program the program object to be linked
     *
     * @see <a target="_blank" href="http://docs.gl/gl4/glLinkProgram">Reference Page</a>
     */
    void glLinkProgram(int program);

    /**
     * Validates a program object.
     *
     * @param program the program object to be validated
     *
     * @see <a target="_blank" href="http://docs.gl/gl4/glValidateProgram">Reference Page</a>
     */
    void glValidateProgram(int program);

    /**
     * Creates a program object.
     *
     * @see <a target="_blank" href="http://docs.gl/gl4/glCreateProgram">Reference Page</a>
     */
    int glCreateProgram();

    /**
     * Returns the information log for a program object.
     *
     * @param program the program object whose information log is to be queried
     *
     * @see <a target="_blank" href="http://docs.gl/gl4/glGetProgramInfoLog">Reference Page</a>
     */
    String glGetProgramInfoLog(int program);

    /**
     * Detaches a shader object from a program object to which it is attached.
     *
     * @param program the program object from which to detach the shader object
     * @param shader  the shader object to be detached
     *
     * @see <a target="_blank" href="http://docs.gl/gl4/glDetachShader">Reference Page</a>
     */
    void glDetachShader(int program, int shader);

    /**
     * Deletes a shader object.
     *
     * @param shader the shader object to be deleted
     *
     * @see <a target="_blank" href="http://docs.gl/gl4/glDeleteShader">Reference Page</a>
     */
    void glDeleteShader(int shader);

    /**
     * Deletes a program object.
     *
     * @param program the program object to be deleted
     *
     * @see <a target="_blank" href="http://docs.gl/gl4/glDeleteProgram">Reference Page</a>
     */
    void glDeleteProgram(int program);

    /**
     * Returns the location of a uniform variable.
     *
     * @param program the program object to be queried
     * @param name    a null terminated string containing the name of the uniform variable whose location is to be queried
     *
     * @see <a target="_blank" href="http://docs.gl/gl4/glGetUniformLocation">Reference Page</a>
     */
    int glGetUniformLocation(int program, String name);

    /**
     * Specifies the value of a float uniform variable for the current program object.
     *
     * @param location the location of the uniform variable to be modified
     * @param v0       the uniform value
     *
     * @see <a target="_blank" href="http://docs.gl/gl4/glUniform">Reference Page</a>
     */
    void glUniform1f(int location, float v0);

    /**
     * Specifies the value of a vec3 uniform variable for the current program object.
     *
     * @param location the location of the uniform variable to be modified
     * @param x       the uniform x value
     * @param y       the uniform y value
     * @param z       the uniform z value
     *
     * @see <a target="_blank" href="http://docs.gl/gl4/glUniform">Reference Page</a>
     */
    void glUniform3f(int location, float x, float y, float z);

    /**
     * Specifies the value of an int uniform variable for the current program object.
     *
     * @param location the location of the uniform variable to be modified
     * @param v0       the uniform value
     *
     * @see <a target="_blank" href="http://docs.gl/gl4/glUniform">Reference Page</a>
     */
    void glUniform1i(int location, int v0);

    /**
     * Specifies the value of a single mat4 uniform variable or a mat4 uniform variable array for the current program object.
     *
     * @param location  the location of the uniform variable to be modified
     * @param transpose whether to transpose the matrix as the values are loaded into the uniform variable
     * @param value     a pointer to an array of {@code count} values that will be used to update the specified uniform variable
     *
     * @see <a target="_blank" href="http://docs.gl/gl4/glUniform">Reference Page</a>
     */
    void glUniformMatrix4fv(int location, boolean transpose, FloatBuffer value);

    /**
     * Specifies the value of a vec2 uniform variable for the current program object.
     *
     * @param location the location of the uniform variable to be modified
     * @param x       the uniform x value
     * @param y       the uniform y value
     *
     * @see <a target="_blank" href="http://docs.gl/gl4/glUniform">Reference Page</a>
     */
    void glUniform2f(int location, float x, float y);

    /**
     * Creates a shader object.
     *
     * @param type the type of shader to be created. One of:<br><table><tr><td>{@link GL20C#GL_VERTEX_SHADER VERTEX_SHADER}</td><td>{@link GL20C#GL_FRAGMENT_SHADER FRAGMENT_SHADER}</td><td>{@link GL32#GL_GEOMETRY_SHADER GEOMETRY_SHADER}</td><td>{@link GL40#GL_TESS_CONTROL_SHADER TESS_CONTROL_SHADER}</td></tr><tr><td>{@link GL40#GL_TESS_EVALUATION_SHADER TESS_EVALUATION_SHADER}</td></tr></table>
     *
     * @see <a target="_blank" href="http://docs.gl/gl4/glCreateShader">Reference Page</a>
     */
    int glCreateShader(int type);

    /**
     * Installs a program object as part of current rendering state.
     *
     * @param program the program object whose executables are to be used as part of current rendering state
     *
     * @see <a target="_blank" href="http://docs.gl/gl4/glUseProgram">Reference Page</a>
     */
    void glUseProgram(int program);

    /**
     * Sets the source code in {@code shader} to the source code in the array of strings specified by {@code strings}. Any source code previously stored in the
     * shader object is completely replaced. The number of strings in the array is specified by {@code count}. If {@code length} is {@code NULL}, each string is
     * assumed to be null terminated. If {@code length} is a value other than {@code NULL}, it points to an array containing a string length for each of the
     * corresponding elements of {@code strings}. Each element in the length array may contain the length of the corresponding string (the null character is not
     * counted as part of the string length) or a value less than 0 to indicate that the string is null terminated. The source code strings are not scanned or
     * parsed at this time; they are simply copied into the specified shader object.
     *
     * @param shader the shader object whose source code is to be replaced
     *
     * @see <a target="_blank" href="http://docs.gl/gl4/glShaderSource">Reference Page</a>
     */
    void glShaderSource(int shader, String readFromInputStream);

    /**
     * Compiles a shader object.
     *
     * @param shader the shader object to be compiled
     *
     * @see <a target="_blank" href="http://docs.gl/gl4/glCompileShader">Reference Page</a>
     */
    void glCompileShader(int shader);

    /**
     * Returns a parameter from a shader object.
     *
     * @param shader the shader object to be queried
     * @param pname  the object parameter. One of:<br><table><tr><td>{@link GL20C#GL_SHADER_TYPE SHADER_TYPE}</td><td>{@link GL20C#GL_DELETE_STATUS DELETE_STATUS}</td><td>{@link GL20C#GL_COMPILE_STATUS COMPILE_STATUS}</td><td>{@link GL20C#GL_INFO_LOG_LENGTH INFO_LOG_LENGTH}</td><td>{@link GL20C#GL_SHADER_SOURCE_LENGTH SHADER_SOURCE_LENGTH}</td></tr></table>
     *
     * @see <a target="_blank" href="http://docs.gl/gl4/glGetShader">Reference Page</a>
     */
    int glGetShaderi(int shader, int pname);

    /**
     * Returns the information log for a shader object.
     *
     * @param shader the shader object whose information log is to be queried
     *
     * @see <a target="_blank" href="http://docs.gl/gl4/glGetShaderInfoLog">Reference Page</a>
     */
    String glGetShaderInfoLog(int shader);

    /**
     * Associates a generic vertex attribute index with a named attribute variable.
     *
     * @param program the program object in which the association is to be made
     * @param index   the index of the generic vertex attribute to be bound
     * @param name    a null terminated string containing the name of the vertex shader attribute variable to which {@code index} is to be bound
     *
     * @see <a target="_blank" href="http://docs.gl/gl4/glBindAttribLocation">Reference Page</a>
     */
    void glBindAttribLocation(int program, int index, String name);
}

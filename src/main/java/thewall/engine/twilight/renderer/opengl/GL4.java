package thewall.engine.twilight.renderer.opengl;


import org.lwjgl.opengl.*;
import org.lwjgl.system.NativeType;

public interface GL4 extends GL3 {
    /**
     * Accepted by the {@code target} parameters of BindBuffer, BufferData, BufferSubData, MapBuffer, UnmapBuffer, GetBufferSubData, GetBufferPointerv,
     * MapBufferRange, FlushMappedBufferRange, GetBufferParameteriv, and CopyBufferSubData.
     */
    int GL_DRAW_INDIRECT_BUFFER = 0x8F3F;

    /** Accepted by the {@code value} parameter of GetIntegerv, GetBooleanv, GetFloatv, and GetDoublev. */
    int GL_DRAW_INDIRECT_BUFFER_BINDING = 0x8F43;

    /** Accepted by the {@code pname} parameter of GetProgramiv. */
    int GL_GEOMETRY_SHADER_INVOCATIONS = 0x887F;

    /** Accepted by the {@code pname} parameter of GetBooleanv, GetIntegerv, GetFloatv, GetDoublev, and GetInteger64v. */
    int
            GL_MAX_GEOMETRY_SHADER_INVOCATIONS    = 0x8E5A,
            GL_MIN_FRAGMENT_INTERPOLATION_OFFSET  = 0x8E5B,
            GL_MAX_FRAGMENT_INTERPOLATION_OFFSET  = 0x8E5C,
            GL_FRAGMENT_INTERPOLATION_OFFSET_BITS = 0x8E5D;

    /** Returned in the {@code type} parameter of GetActiveUniform, and GetTransformFeedbackVarying. */
    int
            GL_DOUBLE_VEC2   = 0x8FFC,
            GL_DOUBLE_VEC3   = 0x8FFD,
            GL_DOUBLE_VEC4   = 0x8FFE,
            GL_DOUBLE_MAT2   = 0x8F46,
            GL_DOUBLE_MAT3   = 0x8F47,
            GL_DOUBLE_MAT4   = 0x8F48,
            GL_DOUBLE_MAT2x3 = 0x8F49,
            GL_DOUBLE_MAT2x4 = 0x8F4A,
            GL_DOUBLE_MAT3x2 = 0x8F4B,
            GL_DOUBLE_MAT3x4 = 0x8F4C,
            GL_DOUBLE_MAT4x2 = 0x8F4D,
            GL_DOUBLE_MAT4x3 = 0x8F4E;

    /**
     * Accepted by the {@code cap} parameter of Enable, Disable, and IsEnabled, and by the {@code pname} parameter of GetBooleanv, GetIntegerv, GetFloatv, and
     * GetDoublev.
     */
    int GL_SAMPLE_SHADING = 0x8C36;

    /** Accepted by the {@code pname} parameter of GetBooleanv, GetDoublev, GetIntegerv, and GetFloatv. */
    int GL_MIN_SAMPLE_SHADING_VALUE = 0x8C37;

    /** Accepted by the {@code pname} parameter of GetProgramStageiv. */
    int
            GL_ACTIVE_SUBROUTINES                   = 0x8DE5,
            GL_ACTIVE_SUBROUTINE_UNIFORMS           = 0x8DE6,
            GL_ACTIVE_SUBROUTINE_UNIFORM_LOCATIONS  = 0x8E47,
            GL_ACTIVE_SUBROUTINE_MAX_LENGTH         = 0x8E48,
            GL_ACTIVE_SUBROUTINE_UNIFORM_MAX_LENGTH = 0x8E49;

    /** Accepted by the {@code pname} parameter of GetBooleanv, GetIntegerv, GetFloatv, GetDoublev, and GetInteger64v. */
    int
            GL_MAX_SUBROUTINES                  = 0x8DE7,
            GL_MAX_SUBROUTINE_UNIFORM_LOCATIONS = 0x8DE8;

    /** Accepted by the {@code pname} parameter of GetActiveSubroutineUniformiv. */
    int
            GL_NUM_COMPATIBLE_SUBROUTINES = 0x8E4A,
            GL_COMPATIBLE_SUBROUTINES     = 0x8E4B;

    /** Accepted by the {@code mode} parameter of Begin and all vertex array functions that implicitly call Begin. */
    int GL_PATCHES = 0xE;

    /** Accepted by the {@code pname} parameter of PatchParameteri, GetBooleanv, GetDoublev, GetFloatv, GetIntegerv, and GetInteger64v. */
    int GL_PATCH_VERTICES = 0x8E72;

    /** Accepted by the {@code pname} parameter of PatchParameterfv, GetBooleanv, GetDoublev, GetFloatv, and GetIntegerv, and GetInteger64v. */
    int
            GL_PATCH_DEFAULT_INNER_LEVEL = 0x8E73,
            GL_PATCH_DEFAULT_OUTER_LEVEL = 0x8E74;

    /** Accepted by the {@code pname} parameter of GetProgramiv. */
    int
            GL_TESS_CONTROL_OUTPUT_VERTICES = 0x8E75,
            GL_TESS_GEN_MODE                = 0x8E76,
            GL_TESS_GEN_SPACING             = 0x8E77,
            GL_TESS_GEN_VERTEX_ORDER        = 0x8E78,
            GL_TESS_GEN_POINT_MODE          = 0x8E79;

    /** Returned by GetProgramiv when {@code pname} is TESS_GEN_MODE. */
    int GL_ISOLINES = 0x8E7A;

    /** Returned by GetProgramiv when {@code pname} is TESS_GEN_SPACING. */
    int
            GL_FRACTIONAL_ODD  = 0x8E7B,
            GL_FRACTIONAL_EVEN = 0x8E7C;

    /** Accepted by the {@code pname} parameter of GetBooleanv, GetDoublev, GetFloatv, GetIntegerv, and GetInteger64v. */
    int
            GL_MAX_PATCH_VERTICES                              = 0x8E7D,
            GL_MAX_TESS_GEN_LEVEL                              = 0x8E7E,
            GL_MAX_TESS_CONTROL_UNIFORM_COMPONENTS             = 0x8E7F,
            GL_MAX_TESS_EVALUATION_UNIFORM_COMPONENTS          = 0x8E80,
            GL_MAX_TESS_CONTROL_TEXTURE_IMAGE_UNITS            = 0x8E81,
            GL_MAX_TESS_EVALUATION_TEXTURE_IMAGE_UNITS         = 0x8E82,
            GL_MAX_TESS_CONTROL_OUTPUT_COMPONENTS              = 0x8E83,
            GL_MAX_TESS_PATCH_COMPONENTS                       = 0x8E84,
            GL_MAX_TESS_CONTROL_TOTAL_OUTPUT_COMPONENTS        = 0x8E85,
            GL_MAX_TESS_EVALUATION_OUTPUT_COMPONENTS           = 0x8E86,
            GL_MAX_TESS_CONTROL_UNIFORM_BLOCKS                 = 0x8E89,
            GL_MAX_TESS_EVALUATION_UNIFORM_BLOCKS              = 0x8E8A,
            GL_MAX_TESS_CONTROL_INPUT_COMPONENTS               = 0x886C,
            GL_MAX_TESS_EVALUATION_INPUT_COMPONENTS            = 0x886D,
            GL_MAX_COMBINED_TESS_CONTROL_UNIFORM_COMPONENTS    = 0x8E1E,
            GL_MAX_COMBINED_TESS_EVALUATION_UNIFORM_COMPONENTS = 0x8E1F;

    /** Accepted by the {@code pname} parameter of GetActiveUniformBlockiv. */
    int
            GL_UNIFORM_BLOCK_REFERENCED_BY_TESS_CONTROL_SHADER    = 0x84F0,
            GL_UNIFORM_BLOCK_REFERENCED_BY_TESS_EVALUATION_SHADER = 0x84F1;

    /** Accepted by the {@code type} parameter of CreateShader and returned by the {@code params} parameter of GetShaderiv. */
    int
            GL_TESS_EVALUATION_SHADER = 0x8E87,
            GL_TESS_CONTROL_SHADER    = 0x8E88;

    /** Accepted by the {@code target} parameter of TexParameteri, TexParameteriv, TexParameterf, TexParameterfv, BindTexture, and GenerateMipmap. */
    int GL_TEXTURE_CUBE_MAP_ARRAY = 0x9009;

    /** Accepted by the {@code pname} parameter of GetBooleanv, GetDoublev, GetIntegerv and GetFloatv. */
    int GL_TEXTURE_BINDING_CUBE_MAP_ARRAY = 0x900A;

    /** Accepted by the {@code target} parameter of TexImage3D, TexSubImage3D, CompressedTeximage3D, CompressedTexSubImage3D and CopyTexSubImage3D. */
    int GL_PROXY_TEXTURE_CUBE_MAP_ARRAY = 0x900B;

    /** Returned by the {@code type} parameter of GetActiveUniform. */
    int
            GL_SAMPLER_CUBE_MAP_ARRAY              = 0x900C,
            GL_SAMPLER_CUBE_MAP_ARRAY_SHADOW       = 0x900D,
            GL_INT_SAMPLER_CUBE_MAP_ARRAY          = 0x900E,
            GL_UNSIGNED_INT_SAMPLER_CUBE_MAP_ARRAY = 0x900F;

    /** Accepted by the {@code pname} parameter of GetBooleanv, GetIntegerv, GetFloatv, and GetDoublev. */
    int
            GL_MIN_PROGRAM_TEXTURE_GATHER_OFFSET = 0x8E5E,
            GL_MAX_PROGRAM_TEXTURE_GATHER_OFFSET = 0x8E5F;

    /** Accepted by the {@code target} parameter of BindTransformFeedback. */
    int GL_TRANSFORM_FEEDBACK = 0x8E22;

    /** Accepted by the {@code pname} parameter of GetBooleanv, GetDoublev, GetIntegerv, and GetFloatv. */
    int
            GL_TRANSFORM_FEEDBACK_BUFFER_PAUSED = 0x8E23,
            GL_TRANSFORM_FEEDBACK_BUFFER_ACTIVE = 0x8E24,
            GL_TRANSFORM_FEEDBACK_BINDING       = 0x8E25;

    /** Accepted by the {@code pname} parameter of GetBooleanv, GetDoublev, GetIntegerv, and GetFloatv. */
    int
            GL_MAX_TRANSFORM_FEEDBACK_BUFFERS = 0x8E70,
            GL_MAX_VERTEX_STREAMS             = 0x8E71;

    /** Accepted by the {@code value} parameter of GetBooleanv, GetIntegerv, GetInteger64v, GetFloatv, and GetDoublev. */
    int
            GL_SHADER_COMPILER                  = 0x8DFA,
            GL_SHADER_BINARY_FORMATS            = 0x8DF8,
            GL_NUM_SHADER_BINARY_FORMATS        = 0x8DF9,
            GL_MAX_VERTEX_UNIFORM_VECTORS       = 0x8DFB,
            GL_MAX_VARYING_VECTORS              = 0x8DFC,
            GL_MAX_FRAGMENT_UNIFORM_VECTORS     = 0x8DFD,
            GL_IMPLEMENTATION_COLOR_READ_TYPE   = 0x8B9A,
            GL_IMPLEMENTATION_COLOR_READ_FORMAT = 0x8B9B;

    /** Accepted by the {@code type} parameter of VertexAttribPointer. */
    int GL_FIXED = 0x140C;

    /** Accepted by the {@code precisiontype} parameter of GetShaderPrecisionFormat. */
    int
            GL_LOW_FLOAT    = 0x8DF0,
            GL_MEDIUM_FLOAT = 0x8DF1,
            GL_HIGH_FLOAT   = 0x8DF2,
            GL_LOW_INT      = 0x8DF3,
            GL_MEDIUM_INT   = 0x8DF4,
            GL_HIGH_INT     = 0x8DF5;

    /** Accepted by the {@code format} parameter of most commands taking sized internal formats. */
    int GL_RGB565 = 0x8D62;

    /** Accepted by the {@code pname} parameter of ProgramParameteri and GetProgramiv. */
    int GL_PROGRAM_BINARY_RETRIEVABLE_HINT = 0x8257;

    /** Accepted by the {@code pname} parameter of GetProgramiv. */
    int GL_PROGRAM_BINARY_LENGTH = 0x8741;

    /** Accepted by the {@code pname} parameter of GetBooleanv, GetIntegerv, GetInteger64v, GetFloatv and GetDoublev. */
    int
            GL_NUM_PROGRAM_BINARY_FORMATS = 0x87FE,
            GL_PROGRAM_BINARY_FORMATS     = 0x87FF;

    /** Accepted by {@code stages} parameter to UseProgramStages. */
    int
            GL_VERTEX_SHADER_BIT          = 0x1,
            GL_FRAGMENT_SHADER_BIT        = 0x2,
            GL_GEOMETRY_SHADER_BIT        = 0x4,
            GL_TESS_CONTROL_SHADER_BIT    = 0x8,
            GL_TESS_EVALUATION_SHADER_BIT = 0x10,
            GL_ALL_SHADER_BITS            = 0xFFFFFFFF;

    /** Accepted by the {@code pname} parameter of ProgramParameteri and GetProgramiv. */
    int GL_PROGRAM_SEPARABLE = 0x8258;

    /** Accepted by {@code type} parameter to GetProgramPipelineiv. */
    int GL_ACTIVE_PROGRAM = 0x8259;

    /** Accepted by the {@code pname} parameter of GetBooleanv, GetIntegerv, GetInteger64v, GetFloatv, and GetDoublev. */
    int GL_PROGRAM_PIPELINE_BINDING = 0x825A;

    /** Accepted by the {@code pname} parameter of GetBooleanv, GetIntegerv, GetFloatv, GetDoublev and GetInteger64v. */
    int
            GL_MAX_VIEWPORTS                   = 0x825B,
            GL_VIEWPORT_SUBPIXEL_BITS          = 0x825C,
            GL_VIEWPORT_BOUNDS_RANGE           = 0x825D,
            GL_LAYER_PROVOKING_VERTEX          = 0x825E,
            GL_VIEWPORT_INDEX_PROVOKING_VERTEX = 0x825F;

    /** Returned in the {@code data} parameter from a Get query with a {@code pname} of LAYER_PROVOKING_VERTEX or VIEWPORT_INDEX_PROVOKING_VERTEX. */
    int GL_UNDEFINED_VERTEX = 0x8260;

    /** Renamed tokens. */
    int
            GL_COPY_READ_BUFFER_BINDING  = GL31.GL_COPY_READ_BUFFER,
            GL_COPY_WRITE_BUFFER_BINDING = GL31.GL_COPY_WRITE_BUFFER,
            GL_TRANSFORM_FEEDBACK_ACTIVE = GL40.GL_TRANSFORM_FEEDBACK_BUFFER_ACTIVE,
            GL_TRANSFORM_FEEDBACK_PAUSED = GL40.GL_TRANSFORM_FEEDBACK_BUFFER_PAUSED;

    /**
     * Accepted by the {@code internalformat} parameter of TexImage2D, TexImage3D, CopyTexImage2D, CopyTexImage3D, CompressedTexImage2D, and
     * CompressedTexImage3D and the {@code format} parameter of CompressedTexSubImage2D and CompressedTexSubImage3D.
     */
    int
            GL_COMPRESSED_RGBA_BPTC_UNORM         = 0x8E8C,
            GL_COMPRESSED_SRGB_ALPHA_BPTC_UNORM   = 0x8E8D,
            GL_COMPRESSED_RGB_BPTC_SIGNED_FLOAT   = 0x8E8E,
            GL_COMPRESSED_RGB_BPTC_UNSIGNED_FLOAT = 0x8E8F;

    /** Accepted by the {@code pname} parameter of PixelStore[fi], GetBooleanv, GetIntegerv, GetInteger64v, GetFloatv, and GetDoublev. */
    int
            GL_UNPACK_COMPRESSED_BLOCK_WIDTH  = 0x9127,
            GL_UNPACK_COMPRESSED_BLOCK_HEIGHT = 0x9128,
            GL_UNPACK_COMPRESSED_BLOCK_DEPTH  = 0x9129,
            GL_UNPACK_COMPRESSED_BLOCK_SIZE   = 0x912A,
            GL_PACK_COMPRESSED_BLOCK_WIDTH    = 0x912B,
            GL_PACK_COMPRESSED_BLOCK_HEIGHT   = 0x912C,
            GL_PACK_COMPRESSED_BLOCK_DEPTH    = 0x912D,
            GL_PACK_COMPRESSED_BLOCK_SIZE     = 0x912E;

    /** Accepted by the {@code target} parameter of BindBufferBase and BindBufferRange. */
    int GL_ATOMIC_COUNTER_BUFFER = 0x92C0;

    /**
     * Accepted by the {@code pname} parameter of GetBooleani_v, GetIntegeri_v, GetFloati_v, GetDoublei_v, GetInteger64i_v, GetBooleanv, GetIntegerv,
     * GetInteger64v, GetFloatv, GetDoublev, and GetActiveAtomicCounterBufferiv.
     */
    int GL_ATOMIC_COUNTER_BUFFER_BINDING = 0x92C1;

    /** Accepted by the {@code pname} parameter of GetIntegeri_64v. */
    int
            GL_ATOMIC_COUNTER_BUFFER_START = 0x92C2,
            GL_ATOMIC_COUNTER_BUFFER_SIZE  = 0x92C3;

    /** Accepted by the {@code pname} parameter of GetActiveAtomicCounterBufferiv. */
    int
            GL_ATOMIC_COUNTER_BUFFER_DATA_SIZE                            = 0x92C4,
            GL_ATOMIC_COUNTER_BUFFER_ACTIVE_ATOMIC_COUNTERS               = 0x92C5,
            GL_ATOMIC_COUNTER_BUFFER_ACTIVE_ATOMIC_COUNTER_INDICES        = 0x92C6,
            GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_VERTEX_SHADER          = 0x92C7,
            GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_TESS_CONTROL_SHADER    = 0x92C8,
            GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_TESS_EVALUATION_SHADER = 0x92C9,
            GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_GEOMETRY_SHADER        = 0x92CA,
            GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_FRAGMENT_SHADER        = 0x92CB;

    /** Accepted by the {@code pname} parameter of GetBooleanv, GetIntegerv, GetInteger64v, GetFloatv, and GetDoublev. */
    int
            GL_MAX_VERTEX_ATOMIC_COUNTER_BUFFERS          = 0x92CC,
            GL_MAX_TESS_CONTROL_ATOMIC_COUNTER_BUFFERS    = 0x92CD,
            GL_MAX_TESS_EVALUATION_ATOMIC_COUNTER_BUFFERS = 0x92CE,
            GL_MAX_GEOMETRY_ATOMIC_COUNTER_BUFFERS        = 0x92CF,
            GL_MAX_FRAGMENT_ATOMIC_COUNTER_BUFFERS        = 0x92D0,
            GL_MAX_COMBINED_ATOMIC_COUNTER_BUFFERS        = 0x92D1,
            GL_MAX_VERTEX_ATOMIC_COUNTERS                 = 0x92D2,
            GL_MAX_TESS_CONTROL_ATOMIC_COUNTERS           = 0x92D3,
            GL_MAX_TESS_EVALUATION_ATOMIC_COUNTERS        = 0x92D4,
            GL_MAX_GEOMETRY_ATOMIC_COUNTERS               = 0x92D5,
            GL_MAX_FRAGMENT_ATOMIC_COUNTERS               = 0x92D6,
            GL_MAX_COMBINED_ATOMIC_COUNTERS               = 0x92D7,
            GL_MAX_ATOMIC_COUNTER_BUFFER_SIZE             = 0x92D8,
            GL_MAX_ATOMIC_COUNTER_BUFFER_BINDINGS         = 0x92DC;

    /** Accepted by the {@code pname} parameter of GetProgramiv. */
    int GL_ACTIVE_ATOMIC_COUNTER_BUFFERS = 0x92D9;

    /** Accepted by the {@code pname} parameter of GetActiveUniformsiv. */
    int GL_UNIFORM_ATOMIC_COUNTER_BUFFER_INDEX = 0x92DA;

    /** Returned in {@code params} by GetActiveUniform and GetActiveUniformsiv. */
    int GL_UNSIGNED_INT_ATOMIC_COUNTER = 0x92DB;

    /** Accepted by the {@code value} parameter of GetTexParameter{if}v. */
    int GL_TEXTURE_IMMUTABLE_FORMAT = 0x912F;

    /** Accepted by the {@code pname} parameter of GetBooleanv, GetIntegerv, GetFloatv, GetDoublev, and GetInteger64v. */
    int
            GL_MAX_IMAGE_UNITS                               = 0x8F38,
            GL_MAX_COMBINED_IMAGE_UNITS_AND_FRAGMENT_OUTPUTS = 0x8F39,
            GL_MAX_IMAGE_SAMPLES                             = 0x906D,
            GL_MAX_VERTEX_IMAGE_UNIFORMS                     = 0x90CA,
            GL_MAX_TESS_CONTROL_IMAGE_UNIFORMS               = 0x90CB,
            GL_MAX_TESS_EVALUATION_IMAGE_UNIFORMS            = 0x90CC,
            GL_MAX_GEOMETRY_IMAGE_UNIFORMS                   = 0x90CD,
            GL_MAX_FRAGMENT_IMAGE_UNIFORMS                   = 0x90CE,
            GL_MAX_COMBINED_IMAGE_UNIFORMS                   = 0x90CF;

    /** Accepted by the {@code target} parameter of GetIntegeri_v and GetBooleani_v. */
    int
            GL_IMAGE_BINDING_NAME    = 0x8F3A,
            GL_IMAGE_BINDING_LEVEL   = 0x8F3B,
            GL_IMAGE_BINDING_LAYERED = 0x8F3C,
            GL_IMAGE_BINDING_LAYER   = 0x8F3D,
            GL_IMAGE_BINDING_ACCESS  = 0x8F3E,
            GL_IMAGE_BINDING_FORMAT  = 0x906E;

    /** Accepted by the {@code barriers} parameter of MemoryBarrier. */
    int
            GL_VERTEX_ATTRIB_ARRAY_BARRIER_BIT = 0x1,
            GL_ELEMENT_ARRAY_BARRIER_BIT       = 0x2,
            GL_UNIFORM_BARRIER_BIT             = 0x4,
            GL_TEXTURE_FETCH_BARRIER_BIT       = 0x8,
            GL_SHADER_IMAGE_ACCESS_BARRIER_BIT = 0x20,
            GL_COMMAND_BARRIER_BIT             = 0x40,
            GL_PIXEL_BUFFER_BARRIER_BIT        = 0x80,
            GL_TEXTURE_UPDATE_BARRIER_BIT      = 0x100,
            GL_BUFFER_UPDATE_BARRIER_BIT       = 0x200,
            GL_FRAMEBUFFER_BARRIER_BIT         = 0x400,
            GL_TRANSFORM_FEEDBACK_BARRIER_BIT  = 0x800,
            GL_ATOMIC_COUNTER_BARRIER_BIT      = 0x1000,
            GL_ALL_BARRIER_BITS                = 0xFFFFFFFF;

    /** Returned by the {@code type} parameter of GetActiveUniform. */
    int
            GL_IMAGE_1D                                = 0x904C,
            GL_IMAGE_2D                                = 0x904D,
            GL_IMAGE_3D                                = 0x904E,
            GL_IMAGE_2D_RECT                           = 0x904F,
            GL_IMAGE_CUBE                              = 0x9050,
            GL_IMAGE_BUFFER                            = 0x9051,
            GL_IMAGE_1D_ARRAY                          = 0x9052,
            GL_IMAGE_2D_ARRAY                          = 0x9053,
            GL_IMAGE_CUBE_MAP_ARRAY                    = 0x9054,
            GL_IMAGE_2D_MULTISAMPLE                    = 0x9055,
            GL_IMAGE_2D_MULTISAMPLE_ARRAY              = 0x9056,
            GL_INT_IMAGE_1D                            = 0x9057,
            GL_INT_IMAGE_2D                            = 0x9058,
            GL_INT_IMAGE_3D                            = 0x9059,
            GL_INT_IMAGE_2D_RECT                       = 0x905A,
            GL_INT_IMAGE_CUBE                          = 0x905B,
            GL_INT_IMAGE_BUFFER                        = 0x905C,
            GL_INT_IMAGE_1D_ARRAY                      = 0x905D,
            GL_INT_IMAGE_2D_ARRAY                      = 0x905E,
            GL_INT_IMAGE_CUBE_MAP_ARRAY                = 0x905F,
            GL_INT_IMAGE_2D_MULTISAMPLE                = 0x9060,
            GL_INT_IMAGE_2D_MULTISAMPLE_ARRAY          = 0x9061,
            GL_UNSIGNED_INT_IMAGE_1D                   = 0x9062,
            GL_UNSIGNED_INT_IMAGE_2D                   = 0x9063,
            GL_UNSIGNED_INT_IMAGE_3D                   = 0x9064,
            GL_UNSIGNED_INT_IMAGE_2D_RECT              = 0x9065,
            GL_UNSIGNED_INT_IMAGE_CUBE                 = 0x9066,
            GL_UNSIGNED_INT_IMAGE_BUFFER               = 0x9067,
            GL_UNSIGNED_INT_IMAGE_1D_ARRAY             = 0x9068,
            GL_UNSIGNED_INT_IMAGE_2D_ARRAY             = 0x9069,
            GL_UNSIGNED_INT_IMAGE_CUBE_MAP_ARRAY       = 0x906A,
            GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE       = 0x906B,
            GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE_ARRAY = 0x906C;

    /** Accepted by the {@code value} parameter of GetTexParameteriv, GetTexParameterfv, GetTexParameterIiv, and GetTexParameterIuiv. */
    int GL_IMAGE_FORMAT_COMPATIBILITY_TYPE = 0x90C7;

    /**
     * Returned in the {@code data} parameter of GetTexParameteriv, GetTexParameterfv, GetTexParameterIiv, and GetTexParameterIuiv when {@code value} is
     * IMAGE_FORMAT_COMPATIBILITY_TYPE.
     */
    int
            GL_IMAGE_FORMAT_COMPATIBILITY_BY_SIZE  = 0x90C8,
            GL_IMAGE_FORMAT_COMPATIBILITY_BY_CLASS = 0x90C9;

    /** Accepted by the {@code pname} parameter of GetInternalformativ. */
    int GL_NUM_SAMPLE_COUNTS = 0x9380;

    /** Accepted by the {@code pname} parameter of GetBooleanv, GetIntegerv, GetInteger64v, GetFloatv, and GetDoublev. */
    int GL_MIN_MAP_BUFFER_ALIGNMENT = 0x90BC;

    /** No. of supported Shading Language Versions. Accepted by the {@code pname} parameter of GetIntegerv. */
    int GL_NUM_SHADING_LANGUAGE_VERSIONS = 0x82E9;

    /** Vertex attrib array has unconverted doubles. Accepted by the {@code pname} parameter of GetVertexAttribiv. */
    int GL_VERTEX_ATTRIB_ARRAY_LONG = 0x874E;

    /** Accepted by the {@code internalformat} parameter of CompressedTexImage2D. */
    int
            GL_COMPRESSED_RGB8_ETC2                      = 0x9274,
            GL_COMPRESSED_SRGB8_ETC2                     = 0x9275,
            GL_COMPRESSED_RGB8_PUNCHTHROUGH_ALPHA1_ETC2  = 0x9276,
            GL_COMPRESSED_SRGB8_PUNCHTHROUGH_ALPHA1_ETC2 = 0x9277,
            GL_COMPRESSED_RGBA8_ETC2_EAC                 = 0x9278,
            GL_COMPRESSED_SRGB8_ALPHA8_ETC2_EAC          = 0x9279,
            GL_COMPRESSED_R11_EAC                        = 0x9270,
            GL_COMPRESSED_SIGNED_R11_EAC                 = 0x9271,
            GL_COMPRESSED_RG11_EAC                       = 0x9272,
            GL_COMPRESSED_SIGNED_RG11_EAC                = 0x9273;

    /** Accepted by the {@code target} parameter of Enable and Disable. */
    int GL_PRIMITIVE_RESTART_FIXED_INDEX = 0x8D69;

    /** Accepted by the {@code target} parameter of BeginQuery, EndQuery, GetQueryIndexediv and GetQueryiv. */
    int GL_ANY_SAMPLES_PASSED_CONSERVATIVE = 0x8D6A;

    /** Accepted by the {@code value} parameter of the GetInteger* functions. */
    int GL_MAX_ELEMENT_INDEX = 0x8D6B;

    /** Accepted by the {@code pname} parameters of GetTexParameterfv and  GetTexParameteriv. */
    int GL_TEXTURE_IMMUTABLE_LEVELS = 0x82DF;

    /** Accepted by the {@code type} parameter of CreateShader and returned in the {@code params} parameter by GetShaderiv. */
    int GL_COMPUTE_SHADER = 0x91B9;

    /** Accepted by the {@code pname} parameter of GetIntegerv, GetBooleanv, GetFloatv, GetDoublev and GetInteger64v. */
    int
            GL_MAX_COMPUTE_UNIFORM_BLOCKS              = 0x91BB,
            GL_MAX_COMPUTE_TEXTURE_IMAGE_UNITS         = 0x91BC,
            GL_MAX_COMPUTE_IMAGE_UNIFORMS              = 0x91BD,
            GL_MAX_COMPUTE_SHARED_MEMORY_SIZE          = 0x8262,
            GL_MAX_COMPUTE_UNIFORM_COMPONENTS          = 0x8263,
            GL_MAX_COMPUTE_ATOMIC_COUNTER_BUFFERS      = 0x8264,
            GL_MAX_COMPUTE_ATOMIC_COUNTERS             = 0x8265,
            GL_MAX_COMBINED_COMPUTE_UNIFORM_COMPONENTS = 0x8266,
            GL_MAX_COMPUTE_WORK_GROUP_INVOCATIONS      = 0x90EB;

    /** Accepted by the {@code pname} parameter of GetIntegeri_v, GetBooleani_v, GetFloati_v, GetDoublei_v and GetInteger64i_v. */
    int
            GL_MAX_COMPUTE_WORK_GROUP_COUNT = 0x91BE,
            GL_MAX_COMPUTE_WORK_GROUP_SIZE  = 0x91BF;

    /** Accepted by the {@code pname} parameter of GetProgramiv. */
    int GL_COMPUTE_WORK_GROUP_SIZE = 0x8267;

    /** Accepted by the {@code pname} parameter of GetActiveUniformBlockiv. */
    int GL_UNIFORM_BLOCK_REFERENCED_BY_COMPUTE_SHADER = 0x90EC;

    /** Accepted by the {@code pname} parameter of GetActiveAtomicCounterBufferiv. */
    int GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_COMPUTE_SHADER = 0x90ED;

    /** Accepted by the {@code target} parameters of BindBuffer, BufferData, BufferSubData, MapBuffer, UnmapBuffer, GetBufferSubData, and GetBufferPointerv. */
    int GL_DISPATCH_INDIRECT_BUFFER = 0x90EE;

    /** Accepted by the {@code value} parameter of GetIntegerv, GetBooleanv, GetInteger64v, GetFloatv, and GetDoublev. */
    int GL_DISPATCH_INDIRECT_BUFFER_BINDING = 0x90EF;

    /** Accepted by the {@code stages} parameter of UseProgramStages. */
    int GL_COMPUTE_SHADER_BIT = 0x20;

    /** Tokens accepted by the {@code target} parameters of Enable, Disable, and  IsEnabled. */
    int
            GL_DEBUG_OUTPUT             = 0x92E0,
            GL_DEBUG_OUTPUT_SYNCHRONOUS = 0x8242;

    /** Returned by GetIntegerv when {@code pname} is CONTEXT_FLAGS. */
    int GL_CONTEXT_FLAG_DEBUG_BIT = 0x2;

    /** Tokens accepted by the {@code value} parameters of GetBooleanv, GetIntegerv,  GetFloatv, GetDoublev and GetInteger64v. */
    int
            GL_MAX_DEBUG_MESSAGE_LENGTH         = 0x9143,
            GL_MAX_DEBUG_LOGGED_MESSAGES        = 0x9144,
            GL_DEBUG_LOGGED_MESSAGES            = 0x9145,
            GL_DEBUG_NEXT_LOGGED_MESSAGE_LENGTH = 0x8243,
            GL_MAX_DEBUG_GROUP_STACK_DEPTH      = 0x826C,
            GL_DEBUG_GROUP_STACK_DEPTH          = 0x826D,
            GL_MAX_LABEL_LENGTH                 = 0x82E8;

    /** Tokens accepted by the {@code pname} parameter of GetPointerv. */
    int
            GL_DEBUG_CALLBACK_FUNCTION   = 0x8244,
            GL_DEBUG_CALLBACK_USER_PARAM = 0x8245;

    /**
     * Tokens accepted or provided by the {@code source} parameters of DebugMessageControl, DebugMessageInsert and DEBUGPROC, and the {@code sources} parameter
     * of GetDebugMessageLog.
     */
    int
            GL_DEBUG_SOURCE_API             = 0x8246,
            GL_DEBUG_SOURCE_WINDOW_SYSTEM   = 0x8247,
            GL_DEBUG_SOURCE_SHADER_COMPILER = 0x8248,
            GL_DEBUG_SOURCE_THIRD_PARTY     = 0x8249,
            GL_DEBUG_SOURCE_APPLICATION     = 0x824A,
            GL_DEBUG_SOURCE_OTHER           = 0x824B;

    /**
     * Tokens accepted or provided by the {@code type} parameters of DebugMessageControl, DebugMessageInsert and DEBUGPROC, and the {@code types} parameter of
     * GetDebugMessageLog.
     */
    int
            GL_DEBUG_TYPE_ERROR               = 0x824C,
            GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR = 0x824D,
            GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR  = 0x824E,
            GL_DEBUG_TYPE_PORTABILITY         = 0x824F,
            GL_DEBUG_TYPE_PERFORMANCE         = 0x8250,
            GL_DEBUG_TYPE_OTHER               = 0x8251,
            GL_DEBUG_TYPE_MARKER              = 0x8268;

    /** Tokens accepted or provided by the {@code type} parameters of DebugMessageControl and DEBUGPROC, and the {@code types} parameter of GetDebugMessageLog. */
    int
            GL_DEBUG_TYPE_PUSH_GROUP = 0x8269,
            GL_DEBUG_TYPE_POP_GROUP  = 0x826A;

    /**
     * Tokens accepted or provided by the {@code severity} parameters of DebugMessageControl, DebugMessageInsert and DEBUGPROC callback functions, and the
     * {@code severities} parameter of GetDebugMessageLog.
     */
    int
            GL_DEBUG_SEVERITY_HIGH         = 0x9146,
            GL_DEBUG_SEVERITY_MEDIUM       = 0x9147,
            GL_DEBUG_SEVERITY_LOW          = 0x9148,
            GL_DEBUG_SEVERITY_NOTIFICATION = 0x826B;

    /** Tokens accepted or provided by the {@code identifier} parameters of ObjectLabel and GetObjectLabel. */
    int
            GL_BUFFER           = 0x82E0,
            GL_SHADER           = 0x82E1,
            GL_PROGRAM          = 0x82E2,
            GL_QUERY            = 0x82E3,
            GL_PROGRAM_PIPELINE = 0x82E4,
            GL_SAMPLER          = 0x82E6,
            GL_DISPLAY_LIST     = 0x82E7;

    /** Accepted by the {@code pname} parameter of GetBooleanv, GetIntegerv, GetFloatv, GetDoublev, and GetInteger64v. */
    int GL_MAX_UNIFORM_LOCATIONS = 0x826E;

    /**
     * Accepted by the {@code pname} parameter of FramebufferParameteri, GetFramebufferParameteriv, NamedFramebufferParameteriEXT, and
     * GetNamedFramebufferParameterivEXT.
     */
    int
            GL_FRAMEBUFFER_DEFAULT_WIDTH                  = 0x9310,
            GL_FRAMEBUFFER_DEFAULT_HEIGHT                 = 0x9311,
            GL_FRAMEBUFFER_DEFAULT_LAYERS                 = 0x9312,
            GL_FRAMEBUFFER_DEFAULT_SAMPLES                = 0x9313,
            GL_FRAMEBUFFER_DEFAULT_FIXED_SAMPLE_LOCATIONS = 0x9314;

    /** Accepted by the {@code pname} parameter of GetIntegerv, GetBooleanv, GetInteger64v, GetFloatv, and GetDoublev. */
    int
            GL_MAX_FRAMEBUFFER_WIDTH   = 0x9315,
            GL_MAX_FRAMEBUFFER_HEIGHT  = 0x9316,
            GL_MAX_FRAMEBUFFER_LAYERS  = 0x9317,
            GL_MAX_FRAMEBUFFER_SAMPLES = 0x9318;

    /** Accepted by the {@code pname} parameter of GetInternalformativ and GetInternalformati64v. */
    int
            GL_INTERNALFORMAT_SUPPORTED               = 0x826F,
            GL_INTERNALFORMAT_PREFERRED               = 0x8270,
            GL_INTERNALFORMAT_RED_SIZE                = 0x8271,
            GL_INTERNALFORMAT_GREEN_SIZE              = 0x8272,
            GL_INTERNALFORMAT_BLUE_SIZE               = 0x8273,
            GL_INTERNALFORMAT_ALPHA_SIZE              = 0x8274,
            GL_INTERNALFORMAT_DEPTH_SIZE              = 0x8275,
            GL_INTERNALFORMAT_STENCIL_SIZE            = 0x8276,
            GL_INTERNALFORMAT_SHARED_SIZE             = 0x8277,
            GL_INTERNALFORMAT_RED_TYPE                = 0x8278,
            GL_INTERNALFORMAT_GREEN_TYPE              = 0x8279,
            GL_INTERNALFORMAT_BLUE_TYPE               = 0x827A,
            GL_INTERNALFORMAT_ALPHA_TYPE              = 0x827B,
            GL_INTERNALFORMAT_DEPTH_TYPE              = 0x827C,
            GL_INTERNALFORMAT_STENCIL_TYPE            = 0x827D,
            GL_MAX_WIDTH                              = 0x827E,
            GL_MAX_HEIGHT                             = 0x827F,
            GL_MAX_DEPTH                              = 0x8280,
            GL_MAX_LAYERS                             = 0x8281,
            GL_MAX_COMBINED_DIMENSIONS                = 0x8282,
            GL_COLOR_COMPONENTS                       = 0x8283,
            GL_DEPTH_COMPONENTS                       = 0x8284,
            GL_STENCIL_COMPONENTS                     = 0x8285,
            GL_COLOR_RENDERABLE                       = 0x8286,
            GL_DEPTH_RENDERABLE                       = 0x8287,
            GL_STENCIL_RENDERABLE                     = 0x8288,
            GL_FRAMEBUFFER_RENDERABLE                 = 0x8289,
            GL_FRAMEBUFFER_RENDERABLE_LAYERED         = 0x828A,
            GL_FRAMEBUFFER_BLEND                      = 0x828B,
            GL_READ_PIXELS                            = 0x828C,
            GL_READ_PIXELS_FORMAT                     = 0x828D,
            GL_READ_PIXELS_TYPE                       = 0x828E,
            GL_TEXTURE_IMAGE_FORMAT                   = 0x828F,
            GL_TEXTURE_IMAGE_TYPE                     = 0x8290,
            GL_GET_TEXTURE_IMAGE_FORMAT               = 0x8291,
            GL_GET_TEXTURE_IMAGE_TYPE                 = 0x8292,
            GL_MIPMAP                                 = 0x8293,
            GL_MANUAL_GENERATE_MIPMAP                 = 0x8294,
            GL_AUTO_GENERATE_MIPMAP                   = 0x8295,
            GL_COLOR_ENCODING                         = 0x8296,
            GL_SRGB_READ                              = 0x8297,
            GL_SRGB_WRITE                             = 0x8298,
            GL_FILTER                                 = 0x829A,
            GL_VERTEX_TEXTURE                         = 0x829B,
            GL_TESS_CONTROL_TEXTURE                   = 0x829C,
            GL_TESS_EVALUATION_TEXTURE                = 0x829D,
            GL_GEOMETRY_TEXTURE                       = 0x829E,
            GL_FRAGMENT_TEXTURE                       = 0x829F,
            GL_COMPUTE_TEXTURE                        = 0x82A0,
            GL_TEXTURE_SHADOW                         = 0x82A1,
            GL_TEXTURE_GATHER                         = 0x82A2,
            GL_TEXTURE_GATHER_SHADOW                  = 0x82A3,
            GL_SHADER_IMAGE_LOAD                      = 0x82A4,
            GL_SHADER_IMAGE_STORE                     = 0x82A5,
            GL_SHADER_IMAGE_ATOMIC                    = 0x82A6,
            GL_IMAGE_TEXEL_SIZE                       = 0x82A7,
            GL_IMAGE_COMPATIBILITY_CLASS              = 0x82A8,
            GL_IMAGE_PIXEL_FORMAT                     = 0x82A9,
            GL_IMAGE_PIXEL_TYPE                       = 0x82AA,
            GL_SIMULTANEOUS_TEXTURE_AND_DEPTH_TEST    = 0x82AC,
            GL_SIMULTANEOUS_TEXTURE_AND_STENCIL_TEST  = 0x82AD,
            GL_SIMULTANEOUS_TEXTURE_AND_DEPTH_WRITE   = 0x82AE,
            GL_SIMULTANEOUS_TEXTURE_AND_STENCIL_WRITE = 0x82AF,
            GL_TEXTURE_COMPRESSED_BLOCK_WIDTH         = 0x82B1,
            GL_TEXTURE_COMPRESSED_BLOCK_HEIGHT        = 0x82B2,
            GL_TEXTURE_COMPRESSED_BLOCK_SIZE          = 0x82B3,
            GL_CLEAR_BUFFER                           = 0x82B4,
            GL_TEXTURE_VIEW                           = 0x82B5,
            GL_VIEW_COMPATIBILITY_CLASS               = 0x82B6;

    /** Returned as possible responses for various {@code pname} queries to GetInternalformativ and GetInternalformati64v. */
    int
            GL_FULL_SUPPORT              = 0x82B7,
            GL_CAVEAT_SUPPORT            = 0x82B8,
            GL_IMAGE_CLASS_4_X_32        = 0x82B9,
            GL_IMAGE_CLASS_2_X_32        = 0x82BA,
            GL_IMAGE_CLASS_1_X_32        = 0x82BB,
            GL_IMAGE_CLASS_4_X_16        = 0x82BC,
            GL_IMAGE_CLASS_2_X_16        = 0x82BD,
            GL_IMAGE_CLASS_1_X_16        = 0x82BE,
            GL_IMAGE_CLASS_4_X_8         = 0x82BF,
            GL_IMAGE_CLASS_2_X_8         = 0x82C0,
            GL_IMAGE_CLASS_1_X_8         = 0x82C1,
            GL_IMAGE_CLASS_11_11_10      = 0x82C2,
            GL_IMAGE_CLASS_10_10_10_2    = 0x82C3,
            GL_VIEW_CLASS_128_BITS       = 0x82C4,
            GL_VIEW_CLASS_96_BITS        = 0x82C5,
            GL_VIEW_CLASS_64_BITS        = 0x82C6,
            GL_VIEW_CLASS_48_BITS        = 0x82C7,
            GL_VIEW_CLASS_32_BITS        = 0x82C8,
            GL_VIEW_CLASS_24_BITS        = 0x82C9,
            GL_VIEW_CLASS_16_BITS        = 0x82CA,
            GL_VIEW_CLASS_8_BITS         = 0x82CB,
            GL_VIEW_CLASS_S3TC_DXT1_RGB  = 0x82CC,
            GL_VIEW_CLASS_S3TC_DXT1_RGBA = 0x82CD,
            GL_VIEW_CLASS_S3TC_DXT3_RGBA = 0x82CE,
            GL_VIEW_CLASS_S3TC_DXT5_RGBA = 0x82CF,
            GL_VIEW_CLASS_RGTC1_RED      = 0x82D0,
            GL_VIEW_CLASS_RGTC2_RG       = 0x82D1,
            GL_VIEW_CLASS_BPTC_UNORM     = 0x82D2,
            GL_VIEW_CLASS_BPTC_FLOAT     = 0x82D3;

    /**
     * Accepted by the {@code programInterface} parameter of GetProgramInterfaceiv, GetProgramResourceIndex, GetProgramResourceName, GetProgramResourceiv,
     * GetProgramResourceLocation, and GetProgramResourceLocationIndex.
     */
    int
            GL_UNIFORM                            = 0x92E1,
            GL_UNIFORM_BLOCK                      = 0x92E2,
            GL_PROGRAM_INPUT                      = 0x92E3,
            GL_PROGRAM_OUTPUT                     = 0x92E4,
            GL_BUFFER_VARIABLE                    = 0x92E5,
            GL_SHADER_STORAGE_BLOCK               = 0x92E6,
            GL_VERTEX_SUBROUTINE                  = 0x92E8,
            GL_TESS_CONTROL_SUBROUTINE            = 0x92E9,
            GL_TESS_EVALUATION_SUBROUTINE         = 0x92EA,
            GL_GEOMETRY_SUBROUTINE                = 0x92EB,
            GL_FRAGMENT_SUBROUTINE                = 0x92EC,
            GL_COMPUTE_SUBROUTINE                 = 0x92ED,
            GL_VERTEX_SUBROUTINE_UNIFORM          = 0x92EE,
            GL_TESS_CONTROL_SUBROUTINE_UNIFORM    = 0x92EF,
            GL_TESS_EVALUATION_SUBROUTINE_UNIFORM = 0x92F0,
            GL_GEOMETRY_SUBROUTINE_UNIFORM        = 0x92F1,
            GL_FRAGMENT_SUBROUTINE_UNIFORM        = 0x92F2,
            GL_COMPUTE_SUBROUTINE_UNIFORM         = 0x92F3,
            GL_TRANSFORM_FEEDBACK_VARYING         = 0x92F4;

    /** Accepted by the {@code pname} parameter of GetProgramInterfaceiv. */
    int
            GL_ACTIVE_RESOURCES               = 0x92F5,
            GL_MAX_NAME_LENGTH                = 0x92F6,
            GL_MAX_NUM_ACTIVE_VARIABLES       = 0x92F7,
            GL_MAX_NUM_COMPATIBLE_SUBROUTINES = 0x92F8;

    /** Accepted in the {@code props} array of GetProgramResourceiv. */
    int
            GL_NAME_LENGTH                          = 0x92F9,
            GL_TYPE                                 = 0x92FA,
            GL_ARRAY_SIZE                           = 0x92FB,
            GL_OFFSET                               = 0x92FC,
            GL_BLOCK_INDEX                          = 0x92FD,
            GL_ARRAY_STRIDE                         = 0x92FE,
            GL_MATRIX_STRIDE                        = 0x92FF,
            GL_IS_ROW_MAJOR                         = 0x9300,
            GL_ATOMIC_COUNTER_BUFFER_INDEX          = 0x9301,
            GL_BUFFER_BINDING                       = 0x9302,
            GL_BUFFER_DATA_SIZE                     = 0x9303,
            GL_NUM_ACTIVE_VARIABLES                 = 0x9304,
            GL_ACTIVE_VARIABLES                     = 0x9305,
            GL_REFERENCED_BY_VERTEX_SHADER          = 0x9306,
            GL_REFERENCED_BY_TESS_CONTROL_SHADER    = 0x9307,
            GL_REFERENCED_BY_TESS_EVALUATION_SHADER = 0x9308,
            GL_REFERENCED_BY_GEOMETRY_SHADER        = 0x9309,
            GL_REFERENCED_BY_FRAGMENT_SHADER        = 0x930A,
            GL_REFERENCED_BY_COMPUTE_SHADER         = 0x930B,
            GL_TOP_LEVEL_ARRAY_SIZE                 = 0x930C,
            GL_TOP_LEVEL_ARRAY_STRIDE               = 0x930D,
            GL_LOCATION                             = 0x930E,
            GL_LOCATION_INDEX                       = 0x930F,
            GL_IS_PER_PATCH                         = 0x92E7;

    /** Accepted by the {@code target} parameters of BindBuffer, BufferData, BufferSubData, MapBuffer, UnmapBuffer, GetBufferSubData, and GetBufferPointerv. */
    int GL_SHADER_STORAGE_BUFFER = 0x90D2;

    /**
     * Accepted by the {@code pname} parameter of GetIntegerv, GetIntegeri_v, GetBooleanv, GetInteger64v, GetFloatv, GetDoublev, GetBooleani_v, GetIntegeri_v,
     * GetFloati_v, GetDoublei_v, and GetInteger64i_v.
     */
    int GL_SHADER_STORAGE_BUFFER_BINDING = 0x90D3;

    /** Accepted by the {@code pname} parameter of GetIntegeri_v, GetBooleani_v, GetIntegeri_v, GetFloati_v, GetDoublei_v, and GetInteger64i_v. */
    int
            GL_SHADER_STORAGE_BUFFER_START = 0x90D4,
            GL_SHADER_STORAGE_BUFFER_SIZE  = 0x90D5;

    /** Accepted by the {@code pname} parameter of GetIntegerv, GetBooleanv, GetInteger64v, GetFloatv, and GetDoublev. */
    int
            GL_MAX_VERTEX_SHADER_STORAGE_BLOCKS          = 0x90D6,
            GL_MAX_GEOMETRY_SHADER_STORAGE_BLOCKS        = 0x90D7,
            GL_MAX_TESS_CONTROL_SHADER_STORAGE_BLOCKS    = 0x90D8,
            GL_MAX_TESS_EVALUATION_SHADER_STORAGE_BLOCKS = 0x90D9,
            GL_MAX_FRAGMENT_SHADER_STORAGE_BLOCKS        = 0x90DA,
            GL_MAX_COMPUTE_SHADER_STORAGE_BLOCKS         = 0x90DB,
            GL_MAX_COMBINED_SHADER_STORAGE_BLOCKS        = 0x90DC,
            GL_MAX_SHADER_STORAGE_BUFFER_BINDINGS        = 0x90DD,
            GL_MAX_SHADER_STORAGE_BLOCK_SIZE             = 0x90DE,
            GL_SHADER_STORAGE_BUFFER_OFFSET_ALIGNMENT    = 0x90DF;

    /** Accepted in the {@code barriers} bitfield in glMemoryBarrier. */
    int GL_SHADER_STORAGE_BARRIER_BIT = 0x2000;

    /** Alias for the existing token MAX_COMBINED_IMAGE_UNITS_AND_FRAGMENT_OUTPUTS. */
    int GL_MAX_COMBINED_SHADER_OUTPUT_RESOURCES = 0x8F39;

    /** Accepted by the {@code pname} parameter of TexParameter* and GetTexParameter*. */
    int GL_DEPTH_STENCIL_TEXTURE_MODE = 0x90EA;

    /** Accepted by the {@code pname} parameter of GetTexLevelParameter. */
    int
            GL_TEXTURE_BUFFER_OFFSET = 0x919D,
            GL_TEXTURE_BUFFER_SIZE   = 0x919E;

    /** Accepted by the {@code pname} parameter of GetBooleanv, GetIntegerv, GetFloatv, and GetDoublev. */
    int GL_TEXTURE_BUFFER_OFFSET_ALIGNMENT = 0x919F;

    /** Accepted by the {@code pname} parameters of GetTexParameterfv and  GetTexParameteriv. */
    int
            GL_TEXTURE_VIEW_MIN_LEVEL  = 0x82DB,
            GL_TEXTURE_VIEW_NUM_LEVELS = 0x82DC,
            GL_TEXTURE_VIEW_MIN_LAYER  = 0x82DD,
            GL_TEXTURE_VIEW_NUM_LAYERS = 0x82DE;

    /** Accepted by the {@code pname} parameter of GetVertexAttrib*v. */
    int
            GL_VERTEX_ATTRIB_BINDING         = 0x82D4,
            GL_VERTEX_ATTRIB_RELATIVE_OFFSET = 0x82D5;

    /** Accepted by the {@code target} parameter of GetBooleani_v, GetIntegeri_v, GetFloati_v, GetDoublei_v, and GetInteger64i_v. */
    int
            GL_VERTEX_BINDING_DIVISOR = 0x82D6,
            GL_VERTEX_BINDING_OFFSET  = 0x82D7,
            GL_VERTEX_BINDING_STRIDE  = 0x82D8,
            GL_VERTEX_BINDING_BUFFER  = 0x8F4F;

    /** Accepted by the {@code pname} parameter of GetIntegerv, .... */
    int
            GL_MAX_VERTEX_ATTRIB_RELATIVE_OFFSET = 0x82D9,
            GL_MAX_VERTEX_ATTRIB_BINDINGS        = 0x82DA;


    /** Implementation-dependent state which constrains the maximum value of stride parameters to vertex array pointer-setting commands. */
    int GL_MAX_VERTEX_ATTRIB_STRIDE = 0x82E5;

    /**
     * Implementations are not required to support primitive restart for separate patch primitives (primitive type PATCHES). Support can be queried by calling
     * GetBooleanv with the symbolic constant PRIMITIVE_RESTART_FOR_PATCHES_SUPPORTED. A value of FALSE indicates that primitive restart is treated as
     * disabled when drawing patches, no matter the value of the enables. A value of TRUE indicates that primitive restart behaves normally for patches.
     */
    int GL_PRIMITIVE_RESTART_FOR_PATCHES_SUPPORTED = 0x8221;

    /** Equivalent to {@link ARBTextureBufferObject#GL_TEXTURE_BUFFER_ARB TEXTURE_BUFFER_ARB} query, but named more consistently. */
    int GL_TEXTURE_BUFFER_BINDING = 0x8C2A;

    /** Accepted in the {@code flags} parameter of {@link #glBufferStorage BufferStorage} and {@link ARBBufferStorage#glNamedBufferStorageEXT NamedBufferStorageEXT}. */
    int
            GL_MAP_PERSISTENT_BIT  = 0x40,
            GL_MAP_COHERENT_BIT    = 0x80,
            GL_DYNAMIC_STORAGE_BIT = 0x100,
            GL_CLIENT_STORAGE_BIT  = 0x200;

    /** Accepted by the {@code pname} parameter of {@code GetBufferParameter&#123;i|i64&#125;v}. */
    int
            GL_BUFFER_IMMUTABLE_STORAGE = 0x821F,
            GL_BUFFER_STORAGE_FLAGS     = 0x8220;

    /** Accepted by the {@code barriers} parameter of {@link GL42C#glMemoryBarrier MemoryBarrier}. */
    int GL_CLIENT_MAPPED_BUFFER_BARRIER_BIT = 0x4000;

    /** Accepted by the {@code pname} parameter for {@link GL42C#glGetInternalformativ GetInternalformativ} and {@link GL43C#glGetInternalformati64v GetInternalformati64v}. */
    int GL_CLEAR_TEXTURE = 0x9365;

    /** Accepted in the {@code props} array of {@link GL43C#glGetProgramResourceiv GetProgramResourceiv}. */
    int
            GL_LOCATION_COMPONENT               = 0x934A,
            GL_TRANSFORM_FEEDBACK_BUFFER_INDEX  = 0x934B,
            GL_TRANSFORM_FEEDBACK_BUFFER_STRIDE = 0x934C;

    /** Accepted by the {@code pname} parameter of {@link GL15C#glGetQueryObjectiv GetQueryObjectiv}, {@link GL15C#glGetQueryObjectuiv GetQueryObjectuiv}, {@link GL33C#glGetQueryObjecti64v GetQueryObjecti64v} and {@link GL33C#glGetQueryObjectui64v GetQueryObjectui64v}. */
    int GL_QUERY_RESULT_NO_WAIT = 0x9194;

    /**
     * Accepted by the {@code target} parameter of {@link GL15C#glBindBuffer BindBuffer}, {@link GL15C#glBufferData BufferData}, {@link GL15C#glBufferSubData BufferSubData},
     * {@link GL15C#glMapBuffer MapBuffer}, {@link GL15C#glUnmapBuffer UnmapBuffer}, {@link GL30C#glMapBufferRange MapBufferRange}, {@link GL15C#glGetBufferSubData GetBufferSubData},
     * {@link GL15C#glGetBufferParameteriv GetBufferParameteriv}, {@link GL32C#glGetBufferParameteri64v GetBufferParameteri64v}, {@link GL15C#glGetBufferPointerv GetBufferPointerv},
     * {@link GL43C#glClearBufferSubData ClearBufferSubData}, and the {@code readtarget} and {@code writetarget} parameters of {@link GL31C#glCopyBufferSubData CopyBufferSubData}.
     */
    int GL_QUERY_BUFFER = 0x9192;

    /**
     * Accepted by the {@code pname} parameter of {@link GL11C#glGetBooleanv GetBooleanv}, {@link GL11C#glGetIntegerv GetIntegerv}, {@link GL11C#glGetFloatv GetFloatv},
     * and {@link GL11C#glGetDoublev GetDoublev}.
     */
    int GL_QUERY_BUFFER_BINDING = 0x9193;

    /** Accepted in the {@code barriers} bitfield in {@link GL42C#glMemoryBarrier MemoryBarrier}. */
    int GL_QUERY_BUFFER_BARRIER_BIT = 0x8000;

    /**
     * Accepted by the {@code param} parameter of TexParameter{if}, SamplerParameter{if} and SamplerParameter{if}v, and by the {@code params} parameter of
     * TexParameter{if}v, TexParameterI{i ui}v and SamplerParameterI{i ui}v when their {@code pname} parameter is {@link GL11#GL_TEXTURE_WRAP_S TEXTURE_WRAP_S}, {@link GL11#GL_TEXTURE_WRAP_T TEXTURE_WRAP_T}, or
     * {@link GL12#GL_TEXTURE_WRAP_R TEXTURE_WRAP_R},
     */
    int GL_MIRROR_CLAMP_TO_EDGE = 0x8743;

    /** Accepted by the {@code depth} parameter of {@link #glClipControl ClipControl}. */
    int
            GL_NEGATIVE_ONE_TO_ONE = 0x935E,
            GL_ZERO_TO_ONE         = 0x935F;

    /** Accepted by the {@code pname} parameter of GetBooleanv, GetIntegerv, GetFloatv, and GetDoublev. */
    int
            GL_CLIP_ORIGIN     = 0x935C,
            GL_CLIP_DEPTH_MODE = 0x935D;

    /** Accepted by the {@code mode} parameter of {@link GL30C#glBeginConditionalRender BeginConditionalRender}. */
    int
            GL_QUERY_WAIT_INVERTED              = 0x8E17,
            GL_QUERY_NO_WAIT_INVERTED           = 0x8E18,
            GL_QUERY_BY_REGION_WAIT_INVERTED    = 0x8E19,
            GL_QUERY_BY_REGION_NO_WAIT_INVERTED = 0x8E1A;

    /** Accepted by the {@code pname} parameter of GetBooleanv, GetDoublev, GetFloatv, GetIntegerv, and GetInteger64v. */
    int
            GL_MAX_CULL_DISTANCES                   = 0x82F9,
            GL_MAX_COMBINED_CLIP_AND_CULL_DISTANCES = 0x82FA;

    /** Accepted by the {@code pname} parameter of GetTextureParameter{if}v and GetTextureParameterI{i ui}v. */
    int GL_TEXTURE_TARGET = 0x1006;

    /** Accepted by the {@code pname} parameter of GetQueryObjectiv. */
    int GL_QUERY_TARGET = 0x82EA;

    /** Accepted by the {@code pname} parameter of GetIntegerv, GetFloatv, GetBooleanv GetDoublev and GetInteger64v. */
    int GL_CONTEXT_RELEASE_BEHAVIOR = 0x82FB;

    /** Returned in {@code data} by GetIntegerv, GetFloatv, GetBooleanv GetDoublev and GetInteger64v when {@code pname} is {@link #GL_CONTEXT_RELEASE_BEHAVIOR CONTEXT_RELEASE_BEHAVIOR}. */
    int GL_CONTEXT_RELEASE_BEHAVIOR_FLUSH = 0x82FC;

    /** Returned by {@link #glGetGraphicsResetStatus GetGraphicsResetStatus}. */
    int
            GL_GUILTY_CONTEXT_RESET   = 0x8253,
            GL_INNOCENT_CONTEXT_RESET = 0x8254,
            GL_UNKNOWN_CONTEXT_RESET  = 0x8255;

    /** Accepted by the {@code value} parameter of GetBooleanv, GetIntegerv, and GetFloatv. */
    int GL_RESET_NOTIFICATION_STRATEGY = 0x8256;

    /** Returned by GetIntegerv and related simple queries when {@code value} is {@link #GL_RESET_NOTIFICATION_STRATEGY RESET_NOTIFICATION_STRATEGY}. */
    int
            GL_LOSE_CONTEXT_ON_RESET = 0x8252,
            GL_NO_RESET_NOTIFICATION = 0x8261;

    /** Returned by GetIntegerv when {@code pname} is CONTEXT_FLAGS. */
    int GL_CONTEXT_FLAG_ROBUST_ACCESS_BIT = 0x4;

    /** Returned by {@link GL11C#glGetError GetError}. */
    int GL_CONTEXT_LOST = 0x507;



    /**
     * Specifies a callback to receive debugging messages from the GL.
     *
     * <p>The function's prototype must follow the type definition of DEBUGPROC including its platform-dependent calling convention. Anything else will result in
     * undefined behavior. Only one debug callback can be specified for the current context, and further calls overwrite the previous callback. Specifying
     * {@code NULL} as the value of {@code callback} clears the current callback and disables message output through callbacks. Applications can provide
     * user-specified data through the pointer {@code userParam}. The context will store this pointer and will include it as one of the parameters in each call
     * to the callback function.</p>
     *
     * <p>If the application has specified a callback function for receiving debug output, the implementation will call that function whenever any enabled message
     * is generated.  The source, type, ID, and severity of the message are specified by the DEBUGPROC parameters {@code source}, {@code type}, {@code id}, and
     * {@code severity}, respectively. The string representation of the message is stored in {@code message} and its length (excluding the null-terminator) is
     * stored in {@code length}. The parameter {@code userParam} is the user-specified parameter that was given when calling DebugMessageCallback.</p>
     *
     * <p>Applications can query the current callback function and the current user-specified parameter by obtaining the values of {@link GL43C#GL_DEBUG_CALLBACK_FUNCTION DEBUG_CALLBACK_FUNCTION} and
     * {@link GL43C#GL_DEBUG_CALLBACK_USER_PARAM DEBUG_CALLBACK_USER_PARAM}, respectively.</p>
     *
     * <p>Applications that specify a callback function must be aware of certain special conditions when executing code inside a callback when it is called by the
     * GL, regardless of the debug source.</p>
     *
     * <p>The memory for {@code message} is owned and managed by the GL, and should only be considered valid for the duration of the function call.</p>
     *
     * <p>The behavior of calling any GL or window system function from within the callback function is undefined and may lead to program termination.</p>
     *
     * <p>Care must also be taken in securing debug callbacks for use with asynchronous debug output by multi-threaded GL implementations.</p>
     *
     * <p>If the {@link GL43C#GL_DEBUG_OUTPUT DEBUG_OUTPUT} state is disabled then the GL will not call the callback function.</p>
     *
     * @param callback  a callback function that will be called when a debug message is generated
     * @param userParam a user supplied pointer that will be passed on each invocation of {@code callback}
     *
     * @see <a target="_blank" href="http://docs.gl/gl4/glDebugMessageCallback">Reference Page</a>
     */
    void glDebugMessageCallback(GLDebugMessageCallbackI callback, long userParam);
}

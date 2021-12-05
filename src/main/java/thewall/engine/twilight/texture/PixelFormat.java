package thewall.engine.twilight.texture;

public enum PixelFormat {
    STENCIL_INDEX    (0x1901, 0), // TODO verify sizes
    DEPTH_COMPONENT  (0x1902, 0),
    RED              (0x1903, 1),
    GREEN            (0x1904, 1),
    BLUE             (0x1905, 1),
    ALPHA            (0x1906, 0),
    RGB              (0x1907, 3),
    RGBA             (0x1908, 4);

    private final int code, size;

    PixelFormat(int code, int size){
        this.size = size;
        this.code = code;
    }

    public int getSize() {
        return size;
    }

    public int getOpenGLCode(){
        return code;
    }
}

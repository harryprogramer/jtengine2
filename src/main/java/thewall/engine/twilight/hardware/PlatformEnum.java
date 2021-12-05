package thewall.engine.twilight.hardware;

public enum PlatformEnum {
    MACOS("macOS"),

    LINUX("Linux"),

    WINDOWS("Windows"),

    SOLARIS("Solaris"),

    FREEBSD("FreeBSD"),

    OPENBSD("OpenBSD"),

    WINDOWSCE("Windows CE"),

    AIX("AIX"),

    ANDROID("Android"),

    GNU("GNU"),

    KFREEBSD("kFreeBSD"),

    NETBSD("NetBSD"),

    UNKNOWN("Unknown");

    private String name;

    PlatformEnum(String name) {
        this.name = name;
    }

    /**
     * Gets the friendly name of the platform
     *
     * @return the friendly name of the platform
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the friendly name of the specified JNA Platform type
     *
     * @param osType
     *            The constant returned from JNA's
     *            {@link com.sun.jna.Platform#getOSType()} method.
     * @return the friendly name of the specified JNA Platform type
     */
    public static String getName(int osType) {
        return getValue(osType).getName();
    }

    /**
     * Gets the value corresponding to the specified JNA Platform type
     *
     * @param osType
     *            The constant returned from JNA's
     *            {@link com.sun.jna.Platform#getOSType()} method.
     * @return the value corresponding to the specified JNA Platform type
     */
    public static PlatformEnum getValue(int osType) {
        if (osType < 0 || osType >= UNKNOWN.ordinal()) {
            return UNKNOWN;
        }
        return values()[osType];
    }
}

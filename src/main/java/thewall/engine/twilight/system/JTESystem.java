package thewall.engine.twilight.system;

import com.oracle.svm.core.os.IsDefined;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import thewall.engine.twilight.hardware.Hardware;
import thewall.engine.twilight.hardware.hna.RealtimeHNAccess;
import thewall.engine.twilight.system.lwjgl.LegacyLwjglContext;
import thewall.engine.twilight.system.lwjgl.linux.LinuxJTEContext;
import thewall.engine.twilight.system.lwjgl.macos.MacOSJTEContext;
import thewall.engine.twilight.system.lwjgl.windows.WindowsJTEContext;

/**
 * Static access class for system for Java Twilight Engine
 * This class can create and find e.g. context or managers
 */
public final class JTESystem {
    public final static String name = "Twilight 1.2";

    private final static Logger logger = LogManager.getLogger(JTESystem.class);
    private static volatile Class<? extends JTEContext> nativeContext = null;

    private JTESystem(){
        throw new IllegalStateException("JTESystem cannot be have instance, it have a static access");
    }

    /**
     * Set the own context that be use in engine kernel
     * @param context own class context
     */
    public static void setNativeContext(Class<? extends JTEContext> context){
        nativeContext = context;
    }

    /**
     * Find the best context for the engine kernel
     * If native context is set, this function is skipped and the native context is returned
     * Internal use only.
     * @param appSettings app settings to find context
     * @param hardware hardware access for find the best context basing on current platform
     * @return best context
     */
    public static @NotNull JTEContext findBestContext(AppSettings appSettings, Hardware hardware){
        if(nativeContext != null){
            try {
                return nativeContext.getDeclaredConstructor().newInstance();
            }catch (Exception e){
                logger.fatal("Failed to create native context", e);
            }
        }


        switch(hardware.getPlatform()){
            case LINUX -> {
                return new LinuxJTEContext(appSettings);
            }
            case WINDOWS, WINDOWSCE -> {
                return new WindowsJTEContext(appSettings);
            }

            case MACOS -> {
                return new MacOSJTEContext(appSettings);
            }

            default -> throw new UnsupportedOperationException("Unsupported platform [" + hardware.getPlatform().name() + "]");
        }

    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull Hardware createBestHardware() {
        return new RealtimeHNAccess(); // TODO find the best hardware
    }
}

package thewall.engine.twilight.system;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface NativeContext {
    /**
     * Implemented class context to set
     * @return selected context
     */
    @NotNull Class<? extends JTEContext> context();
}

package thewall.engine.twilight.input.mouse;

@FunctionalInterface
public interface TMouseCallback {
    void invoke(int button, int action, int mods);
}

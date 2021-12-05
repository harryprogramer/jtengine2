package thewall.engine.twilight.display;

@Deprecated
public abstract class WindowResizeSystem implements DisplayResizeCallback {

    @Override
    public void invoke(int x, int y) {
        resizeWindow(x, y);
    }

    abstract void resizeWindow(int x, int y);
}

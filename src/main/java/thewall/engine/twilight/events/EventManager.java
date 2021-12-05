package thewall.engine.twilight.events;

public interface EventManager {
    void registerEvents(Listener listener);

    void unregisterEvents(Listener listener);

    void callEvent(AbstractEvent abstractEvent);
}

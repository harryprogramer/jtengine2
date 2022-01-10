package jte2.engine.twilight.events;

public interface EventManager {
    void registerEvents(Listener listener);

    void unregisterEvents(Listener listener);

    void callEvent(AbstractEvent abstractEvent);
}

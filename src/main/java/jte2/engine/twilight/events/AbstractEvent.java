package jte2.engine.twilight.events;

import org.jetbrains.annotations.NotNull;

public abstract class AbstractEvent implements Comparable<AbstractEvent> {
    private final boolean async;
    private final EventType eventType;
    private String name;

    public AbstractEvent(boolean isASync, EventType eventType){
        this.async = isASync;
        this.eventType = eventType;
    }

    public AbstractEvent(EventType eventType){
        this(false, eventType);
    }

    public boolean isAsynchronous(){
        return async;
    }

    @Deprecated
    public EventType getEventType(){
        return eventType;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getEventName(){
        return name == null ? this.getClass().getName() : name;

    }

    @Override
    public int compareTo(@NotNull AbstractEvent o) {
        if(isAsynchronous()) {
            return -1;
        }else {
            return 1;
        }
    }
}

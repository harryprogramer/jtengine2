package jte2.engine.twilight.events;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Event {
    EventType type();

    EventPriority priority() default EventPriority.NORMAL;
}

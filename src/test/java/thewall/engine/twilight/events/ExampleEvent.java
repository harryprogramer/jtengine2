package thewall.engine.twilight.events;

import org.jetbrains.annotations.NotNull;

public class ExampleEvent extends AbstractEvent {
    public ExampleEvent() {
        super(EventType.CUSTOM);
    }

    public static class ListenerEvent implements Listener {
        @Event(type = EventType.CUSTOM)
        void onEvent(@NotNull ExampleEvent test){
            System.out.println(test.getEventName());
        }

    }

    public static class FakeExampleEvent extends AbstractEvent {
        public FakeExampleEvent(){
            super(EventType.CUSTOM);
        }
    }

}

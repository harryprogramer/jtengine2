package jte2.engine.twilight.pipeline;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AppState {
    public enum TaskState {
        STOP,
        WAIT,
        RUN
    }

    public enum StateLifecycle {
        CONSTANT,
        SINGLE

    }
    private final String stateUUID;
    private final String stateName;
    private final StateLifecycle stateLifecycle;
    private final AtomicReference<TaskState> taskState = new AtomicReference<>(TaskState.RUN);
    private final static AtomicInteger appStateCounter = new AtomicInteger();

    public AppState(StateLifecycle lifecycle, String name){
        this.stateLifecycle = lifecycle;
        this.stateUUID = generateUUID();
        this.stateName = name;
    }

    public AppState(StateLifecycle lifecycle){
        this.stateLifecycle = lifecycle;
        this.stateUUID = generateUUID();
        this.stateName = "AppState-" + appStateCounter.getAndIncrement();
    }

    public String getID() {
        return stateUUID;
    }

    public boolean isAsync(){
        return false;
    }

    public StateLifecycle getLifecycle(){
        return stateLifecycle;
    }

    public TaskState getTaskState(){
        return taskState.get();
    }

    public void stopState(){
        taskState.set(TaskState.STOP);
    }

    private static String generateUUID(){
        return UUID.randomUUID().toString();
    }

    public String getStateName(){
        return stateName;
    }

    abstract void initialize(AppStateManager stateManager);

    abstract void update(float delta);

    abstract void render(RenderManager renderManager);

    abstract void postRender();

    abstract void onDetached(AppStateManager stateManager);
}

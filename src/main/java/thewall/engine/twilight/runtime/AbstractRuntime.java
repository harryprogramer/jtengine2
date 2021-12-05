package thewall.engine.twilight.runtime;

import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Abstract object to represent abstract runtime.
 * This class is base for all runtimes.
 * All active runtimes is stored in {@link TwilightRuntimeService}
 * {@link AbstractRuntime} is designed to help easily and quickly find the best environment to run a program.
 * The most popular example is {@link thewall.engine.LegacyApp}.
 * It is the basis of an engine application or game.
 * The best environment for the application is found when the engine is started.
 * {@link AbstractRuntime} environment also supports its own runtime programs.
 * It is very easy to create an environment for any program.
 *
 * @see TwilightRuntimeService
 * @param <P> program that will be executed
 */
public abstract class AbstractRuntime<P> {
    @Getter
    private final String name;

    private P program;

    private final static Logger logger = LogManager.getLogger(AbstractRuntime.class);

    public AbstractRuntime(String name){
        this.name = name;
    }

    private final AtomicBoolean isClosed = new AtomicBoolean(true);

    /**
     * Execute program for this runtime
     * @param program object of program
     */
    public void execute(P program){
        logger.info("Starting runtime for [" + name + "]");
        Objects.requireNonNull(program);
        if(this.program != null){
            throw new RuntimeException("Runtime is already used, active program: " + program.getClass().getSimpleName());
        }

        TwilightRuntimeService.registerActiveRuntime(program, this);

        this.program = program;

        start(program);
        isClosed.set(false);
    }

    /**
     * Force stop of the runtime and ran program
     */
    @SneakyThrows
    public void forceStop(){
        if(this.program == null){
            throw new RuntimeException("Runtime is unused");
        }
        logger.info("Stopping runtime for [" + name + "]");
        TwilightRuntimeService.deleteActiveRuntime(this.program);
        stop();
        isClosed.set(true);
    }

    /**
     * Check if calling thread is this same as runtime thread
     * @deprecated
     * @return is caller thread is runtime thread
     */
    @Deprecated
    public boolean checkThread(){
        return Thread.currentThread() == getThread();
    }

    public boolean isRuntimeClosed(){
        return isClosed.get();
    }

    /**
     * Get runtime thread. Deprecated do not use.
     * @return runtime thread
     * @deprecated deprecated due to thread secure issues
     */
    @Deprecated
    protected abstract Thread getThread();

    /**
     * Execute task in the runtime thread
     * @param runnable runnable for runtime thread
     */
    public abstract void executeTask(Runnable runnable);

    /**
     * To implementation for runtime to run program
     * @param program program to run
     */
    protected abstract void start(P program);

    /**
     * To implementation for runtime to listen to shut down
     */
    protected abstract void stop();

    /**
     * Is runtime ready for program tasks
     * @return is ready
     */
    public abstract boolean isReady();
}

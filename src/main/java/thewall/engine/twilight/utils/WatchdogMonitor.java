package thewall.engine.twilight.utils;

import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Stack;

public class WatchdogMonitor implements WatchdogTimeMonitor{
    private final static Logger logger = LogManager.getLogger(WatchdogMonitor.class);
    volatile boolean keepAlive;
    volatile boolean isRun = true;
    volatile int timeout = 25000;
    private WatchdogThread thread;
    private final Thread watchedThread;

    public WatchdogMonitor(@NotNull Thread watchedThread){
        this.watchedThread = watchedThread;
    }

    @Override
    public long getKeepAliveTime() {
        return thread.lastTime;
    }

    class WatchdogThread extends Thread {
        public WatchdogThread(){
            setName("Watchdog Service");
        }



        private void init(){
            long startTime = System.currentTimeMillis();
            while (!keepAlive){
                if(System.currentTimeMillis() - startTime > timeout){
                    logger.error("Watchdog hasn't receive keep-alive command in time, closing");
                    System.exit(408);
                }
            }
            logger.info("Watchdog keep-alive received successfully, watchdog init complete");
        }

        boolean isAlert = false;
        volatile long lastTime = System.currentTimeMillis();
        StackTraceElement[] freezeStackTrace;
        @Override
        public void run() {
            init();
            keepAlive = false;
            while (isRun){
                if(keepAlive){
                    if(isAlert){
                        logger.info("Watchdog detect program freeze, ticks behind: " + (System.currentTimeMillis() - lastTime) / 1000.0);
                        logger.info("Dumped JVM stack trace during freeze: ");
                        for(StackTraceElement stackTraceElement : freezeStackTrace){
                            logger.info("       " + stackTraceElement);
                        }
                    }
                    lastTime = System.currentTimeMillis();
                    keepAlive = false;
                    continue;
                }


                if(System.currentTimeMillis() - lastTime > (timeout * 0.5)) {
                    if (!isAlert) {
                        logger.warn("Watchdog detected a longer pause in the program, I am investigating the situation. Stand by.");
                        isAlert = true;
                        logger.warn("Scanning for blocking callers...");
                        StackTraceElement[] stackTraceElements =  watchedThread.getStackTrace();
                        freezeStackTrace = stackTraceElements;
                        if(stackTraceElements.length != 0) {
                            logger.warn("Possible block scenario: " + stackTraceElements[1] + " -> " + stackTraceElements[0] );
                        }else {
                            logger.error("No stacktrafe found for thread [" + watchedThread + "]");
                        }
                    }

                }

                if(System.currentTimeMillis() - lastTime > timeout){
                    logger.fatal("Watchdog timeout, the watchdog did not receive a keep-alive command. The program has probably crashed");
                    if(freezeStackTrace.length != 0) {
                        logger.fatal("Thread stacktrace: ");
                        for (StackTraceElement stackTraceElement : freezeStackTrace) {
                            logger.fatal("       " + stackTraceElement);
                        }
                    }
                    new Thread(() -> {
                        WatchdogMonitor.this.stop();
                        System.exit(403);
                    }).start();
                    break;
                }
            }
        }
    }

    public void keepAlive(){
        keepAlive = true;
    }

    public void timeout(int millis){
        this.timeout = millis;
    }

    public void start(){
        logger.info("Watchdog started, working in background active");
        thread = new WatchdogThread();
        thread.start();
    }


    public void stop(){
        logger.info("Stopping watchdog, service going down...");
        isRun = false;
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            logger.error("Watchdog interrupted", e);
        }
        if(thread.isAlive()){
            logger.info("Watchdog hasn't switch down in time, killing process");
            thread.interrupt();
        }
    }

}

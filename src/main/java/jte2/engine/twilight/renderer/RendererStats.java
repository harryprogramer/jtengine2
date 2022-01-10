package jte2.engine.twilight.renderer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public abstract class RendererStats {
    private final static Logger logger = LogManager.getLogger(RendererStats.class);
    private Map<String, Long> timer = new HashMap<>();
    private final Map<String, Long> builder = new HashMap<>();
    private long lastTime = 0;

    protected void updateStats(){
        if(builder.isEmpty()){
            logger.warn("No timings was set, updateStats() abort");
        }

        this.lastTime = System.currentTimeMillis();
        this.timer = builder;
        builder.clear();
    }

    protected void putTiming(String type, long time){
        if(builder.get(type) != null){
            builder.replace(type, time);
        }else{
            builder.put(type, time);
        }
    }

    public long getTiming(String type){
        return timer.get(type);
    }

    public long equalsTimings(String timing1, String timing2){
        long time1 = timer.get(timing1);
        long time2 = timer.get(timing2);

        return time1 - time2;
    }

    public long getLastTimingMillis(){
        return lastTime;
    }
}

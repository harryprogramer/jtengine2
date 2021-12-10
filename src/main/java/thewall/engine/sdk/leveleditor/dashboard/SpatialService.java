package thewall.engine.sdk.leveleditor.dashboard;

import thewall.engine.twilight.spatials.Spatial;

import java.util.HashMap;
import java.util.Map;

public class SpatialService {
    private static int INDEXID = 0;
    private final Map<Integer, Spatial> partials = new HashMap<>();

    public int addSpatial(Spatial spatial){
        int id = ++INDEXID;
        partials.put(id, spatial);
        return id;
    }

    public Spatial getSpatial(int id){
        return partials.get(id);
    }

    public void removeSpatial(int id){
        partials.remove(id);
    }
}

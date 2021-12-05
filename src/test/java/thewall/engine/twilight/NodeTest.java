package thewall.engine.twilight;

import org.junit.jupiter.api.Test;
import thewall.engine.twilight.entity.Box;
import thewall.engine.twilight.entity.Spatial;
import org.joml.Vector3f;

class NodeTest {
    void change(Spatial node){
        node.getMesh().setID(96);
    }

    @Test
    void test(){
        Node test = new Node();
        Box box = new Box(new Vector3f(0, 0, 0), 1, 1, 1);
        test.attachChild(box);
        Spatial test2 = test.getChild(0);
        test2.getMesh().setID(69);
        change(test2);
        System.out.println(test.getChild(0).getMesh().getID());
    }
}
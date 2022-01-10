package jte2.engine.twilight;

import org.junit.jupiter.api.Test;
import jte2.engine.twilight.spatials.Box;
import jte2.engine.twilight.spatials.Spatial;
import jte2.engine.twilight.viewport.Node;

class NodeTest {
    void change(Spatial node){
        node.getMesh().setID(96);
    }

    @Test
    void test(){
        Node test = new Node();
        Box box = new Box(1, 1, 1);
        test.attachChild(box);
        Spatial test2 = test.getChild(0);
        test2.getMesh().setID(69);
        change(test2);
        System.out.println(test.getChild(0).getMesh().getID());
    }
}
package thewall.engine.twilight;

import lombok.SneakyThrows;
import org.joml.Vector3f;
import org.junit.jupiter.api.Test;
import thewall.engine.twilight.entity.Box;
import thewall.engine.twilight.entity.Spatial;

import java.util.List;

class  RenderQueueTest {
    @SneakyThrows
    @Test
    void test(){
        RenderQueue renderQueue = new RenderQueue();
        RendererTest rendererTest = new RendererTest();
        Node node = new Node();
        node.attachChild(new Box(new Vector3f(0, 0, 0), 1, 1, 1));
        renderQueue.add(node);
        for (int i = 0; i < 10; i++){
            rendererTest.prepare(renderQueue);
            rendererTest.render();
            System.out.println(node.getChild(0).getMesh().getID());
            Thread.sleep(1000);

        }
    }

    @Test
    void test2(){
        RendererTest rendererTest = new RendererTest();
        RenderQueue renderQueue = new RenderQueue();
        Node node = new Node();
        node.attachChild(new Box(new Vector3f(0, 0, 0), 1, 1, 1));
        node.attachChild(new Box(new Vector3f(1, 0, 0), 1, 1, 1));
        node.attachChild(new Box(new Vector3f(2, 0, 0), 1, 1, 1));
        node.attachChild(new Box(new Vector3f(3, 0, 0), 1, 1, 1));
        node.attachChild(new Box(new Vector3f(0, 0, 0), 1, 1, 1));
        node.attachChild(new Box(new Vector3f(0, 0, 0), 1, 1, 1));
        node.attachChild(new Box(new Vector3f(0, 0, 0), 1, 1, 1));
        node.attachChild(new Box(new Vector3f(0, 0, 0), 1, 1, 1));
        renderQueue.add(node);
        for(int i = 0; i < 5; i++) {
            System.out.println("INDEX: " + i);
            rendererTest.prepare2(renderQueue);
            while (!renderQueue.isEmpty()) {
                Node node1 = renderQueue.poll();
                assert node1 != null;
                for (Spatial spatial : node1.getChildren()) {
                    System.out.println(spatial.getMesh().getID());
                }
            }
        }
    }

    class RendererTest {
        RenderQueue renderQueue;

        void prepare2(RenderQueue renderQueue){
            for(int i = 0; i < renderQueue.size(); i++){
                List<Spatial> spatialList = renderQueue.get(i).getChildren();
                for(Spatial spatial : spatialList) {
                    spatial.getMesh().setID(++i + 20);
                }
            }
        }

            void prepare(RenderQueue renderQueue) {
                while (!renderQueue.isEmpty()){
                    Node node = renderQueue.poll();
                    List<Spatial> spatialList = node.getChildren();
                    for(Spatial spatial : spatialList) {
                        if (spatial.getMesh() == null) {
                            throw new IllegalStateException("mesh is null");
                        }
                        if (spatial.getMesh().getID() == -1) {
                            System.out.println("ustawianie");
                            spatial.getMesh().setID(96);
                        }
                    }
                }
                this.renderQueue = renderQueue;
            }

            void render(){
                while (!renderQueue.isEmpty()){
                    Node node = renderQueue.poll();
                    System.out.println(node.getMesh().getID());
                }
            }
    }
}
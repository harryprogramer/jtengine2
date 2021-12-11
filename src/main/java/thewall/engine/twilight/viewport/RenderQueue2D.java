package thewall.engine.twilight.viewport;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thewall.engine.twilight.errors.NotImplementedException;
import thewall.engine.twilight.utils.SafeArrayList;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public class RenderQueue2D implements Queue<Node2D> {
    private final AtomicInteger pollIndex = new AtomicInteger(-1);
    private final List<Node2D> nodesRenderQueue = new SafeArrayList<>(Node2D.class);

    @Override
    public int size() {
        return nodesRenderQueue.size();
    }

    @Override
    public boolean isEmpty() {
        return nodesRenderQueue.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this == o;
    }

    public Node2D get(int index){
        return nodesRenderQueue.get(index);
    }

    @NotNull
    @Override
    public Iterator<Node2D> iterator() {
        return nodesRenderQueue.iterator();
    }

    @NotNull
    @Override
    public Object @NotNull [] toArray() {
        return nodesRenderQueue.toArray();
    }

    @NotNull
    @Override
    public <T> T @NotNull [] toArray(@NotNull T @NotNull [] a) {
        return (T[]) nodesRenderQueue.toArray(new Node2D[0]); // TODO
    }

    @Override
    public boolean add(Node2D node) {
        return nodesRenderQueue.add(node);
    }

    @Override
    public boolean remove(Object o) {
        return nodesRenderQueue.remove(o);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends Node2D> c) {
        return nodesRenderQueue.addAll(c);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return nodesRenderQueue.removeAll(c);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return nodesRenderQueue.retainAll(c);
    }

    @Override
    public void clear() {
        nodesRenderQueue.clear();
    }

    @Override
    public boolean offer(Node2D node) {
        throw new NotImplementedException();
    }

    @Contract(pure = true)
    @Override
    public @Nullable Node2D remove() {
        return null;
    }

    @Contract(mutates = "this")
    @Override
    public Node2D poll() {
        int index = pollIndex.incrementAndGet();

        if(index == size()){
            return null;
        }

        if(index == size() - 1){
            Node2D node = nodesRenderQueue.get(index);
            nodesRenderQueue.clear();
            pollIndex.set(-1);
            return node;
        }
        return nodesRenderQueue.get(index);
    }

    @Override
    public Node2D element() {
        throw new NotImplementedException();
    }

    @Override
    public Node2D peek() {
        throw new NotImplementedException();
    }
}

package thewall.engine.twilight;

import com.google.common.base.Objects;
import org.apache.commons.lang.ArrayUtils;
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

public final class RenderQueue implements Queue<Node> {
    private final AtomicInteger pollIndex = new AtomicInteger(-1);
    private final List<Node> nodesRenderQueue = new SafeArrayList<>(Node.class);

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

    public Node get(int index){
        return nodesRenderQueue.get(index);
    }

    @NotNull
    @Override
    public Iterator<Node> iterator() {
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
        return nodesRenderQueue.toArray(a);
    }

    @Override
    public boolean add(Node node) {
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
    public boolean addAll(@NotNull Collection<? extends Node> c) {
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
    public boolean offer(Node node) {
        throw new NotImplementedException();
    }

    @Contract(pure = true)
    @Override
    public @Nullable Node remove() {
        return null;
    }

    @Contract(mutates = "this")
    @Override
    public Node poll() {
        int index = pollIndex.incrementAndGet();

        if(index == size()){
            return null;
        }

        if(index == size() - 1){
            Node node = nodesRenderQueue.get(index);
            nodesRenderQueue.clear();
            pollIndex.set(-1);
            return node;
        }
        return nodesRenderQueue.get(index);
    }

    @Override
    public Node element() {
        throw new NotImplementedException();
    }

    @Override
    public Node peek() {
        throw new NotImplementedException();
    }
}

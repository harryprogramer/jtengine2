package thewall.engine.twilight.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Validation {
    private final static List<NullCallback> callbacks = Collections.synchronizedList(new ArrayList<>());
    private Validation(){
        throw new IllegalStateException("cannot have instance");
    }

    @FunctionalInterface
    interface NullCallback {
        void callback(@Nullable Class<?> sourceClass, @Nullable String name);
    }

    public static void addNullCallback(@NotNull NullCallback callback){
        callbacks.add(callback);
    }

    public static int checkNull(int number){
        if(number == 0){
            throw new IllegalStateException("This number cannot be null");
        }

        return number;
    }

    private static void callCallback(@Nullable Class<?> sourceClass, @Nullable String name){
         for(NullCallback callback : callbacks){
            CompletableFuture.runAsync(() -> callback.callback(sourceClass, name));
        }
    }

    private static boolean isNull(Object obj){
        if(obj == null){
            callCallback(null, null);
            return true;
        }

        return false;
    }

    private static boolean isNull(Object obj, Class<?> sourceClass){
        if(obj == null){
            callCallback(sourceClass, null);
            return true;
        }

        return false;
    }

    private static boolean isNull(Object obj, String name){
        if(obj == null){
            callCallback(null, name);
            return true;
        }

        return false;
    }

    public static <T> T checkNull(T obj){
        if(isNull(obj)){
            throw new NullPointerException();
        }

        return obj;
    }

    @SafeVarargs
    public static <T> void checkNull(T @NotNull ... obj){
        for(T var : obj) {
            if (isNull(var)) {
                throw new NullPointerException();
            }
        }

    }

    @SafeVarargs
    public static <T> void checkNull(Class<?> sourceClass, T @NotNull ... obj){
        for(T var : obj) {
            if (isNull(var, sourceClass)) {
                throw new NullPointerException();
            }
        }
    }

    @SafeVarargs
    public static <T> void checkNull(String name, T @NotNull ... obj){
        for(T var : obj) {
            if (isNull(var, name)) {
                throw new NullPointerException();
            }
        }

    }

    public static <T> T checkNull(T obj, Class<?> sourceClass){
        if(isNull(obj, sourceClass)){
            throw new NullPointerException(sourceClass.getSimpleName() + " cannot be null");
        }

        return obj;
    }

    public static <T> T checkNull(T obj, String name){
        if(isNull(obj, name)){
            throw new NullPointerException(name +  " cannot be null");
        }

        return obj;
    }
}

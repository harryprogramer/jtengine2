package thewall;

import java.lang.reflect.Method;

public abstract class TestAbstract {
    public void getFunctions(){
        Method[] methods = getClass().getMethods();
        for(Method method : methods){
            System.out.println(method.getName());
        }
    }
}

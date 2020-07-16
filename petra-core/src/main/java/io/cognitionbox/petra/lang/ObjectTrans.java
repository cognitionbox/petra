package io.cognitionbox.petra.lang;

import io.cognitionbox.petra.core.impl.ReflectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public class ObjectTrans {

    static class A {
        String x = "x";
        String y = "y";

        @Override
        public String toString() {
            return "A{" +
                    "x='" + x + '\'' +
                    ", y='" + y + '\'' +
                    '}';
        }
    }

    private Map<Field,Object> storedValues = new HashMap<>();
    public ObjectTrans() {}

    public void capture(Object object){
        actionAllFieldsAccessibleFromObjectInstance(object,(f,v)->{
            // later add support for petra's parallel / distributable atomic reference
            // we capture/replace the value rather than the reference
            storedValues.put(f,v);
        });
    }

    public static void main(String[] args){
        A a = new A();
        ObjectTrans objectTrans = new ObjectTrans();
        objectTrans.capture(a);
        System.out.println(a);
        a.x = "hello";
        System.out.println(a);
        objectTrans.restore(a);
        System.out.println(a);

        A a2 = new A();
        System.out.println(a2);
        a2.x = "a";
        objectTrans.restore(a2);
        System.out.println(a2);
    }

    public void restore(Object object){
        actionAllFieldsAccessibleFromObjectInstance(object,(f,v)->{
            // later add support for petra's parallel / distributable atomic reference
            // we capture/replace the value rather than the reference
            Object restored = storedValues.get(f);
            boolean access = f.isAccessible();
            try {
                f.set(object,restored);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            f.setAccessible(access);
        });
    }

    static public <T> void actionAllFieldsAccessibleFromObjectInstance(Object value, BiConsumer<Field,Object> fieldConsumer) {
        if (value==null){
            return;
        }
        if (Boolean.class.isAssignableFrom(value.getClass()) || String.class.isAssignableFrom(value.getClass()) || Integer.class.isAssignableFrom(value.getClass())){
            return;
        }
        Set<Field> fields = ReflectUtils.getAllFieldsAccessibleFromObject(value.getClass());
        for (Field f : fields){
            if (!(Boolean.class.isAssignableFrom(value.getClass()) || String.class.isAssignableFrom(value.getClass()) || Integer.class.isAssignableFrom(value.getClass()))){
                if (!Modifier.isStatic(f.getModifiers())){
                    try {
                        boolean isAccessable = f.isAccessible();
                        f.setAccessible(true);
                        Object v = f.get(value);
                        actionAllFieldsAccessibleFromObjectInstance(v,fieldConsumer);
                        f.setAccessible(isAccessable);
                        fieldConsumer.accept(f,v);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

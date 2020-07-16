package io.cognitionbox.petra.lang;

import io.cognitionbox.petra.core.impl.ObjectCopyerViaSerialization;
import io.cognitionbox.petra.core.impl.ReflectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

public class ObjectTrans {

    static class A {
        String x = "x";
        String y = "y";
        List<Integer> list = new ArrayList<>();

        {
            list.add(1);
            list.add(2);
            list.add(3);
        }

        @Override
        public String toString() {
            return "A{" +
                    "x='" + x + '\'' +
                    ", y='" + y + '\'' +
                    ", list=" + list +
                    '}';
        }
    }

    private ObjectCopyerViaSerialization copyer = new ObjectCopyerViaSerialization();
    private Map<Field,Object> storedValues = new ConcurrentHashMap<>();
    public ObjectTrans() {}

    public void capture(Object object){
        Set<Field> fields = ReflectUtils.getAllFieldsAccessibleFromObject(object.getClass());
        fields.parallelStream().forEach(f->{
            try {
                Object value = f.get(object);
                // later add support for petra's parallel / distributable atomic reference
                // we capture/replace the value rather than the reference
                storedValues.put(f,copyer.copy(value));
            } catch (Throwable e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args){
        A a = new A();
        ObjectTrans objectTrans = new ObjectTrans();
        objectTrans.capture(a);
        System.out.println(a);
        a.x = "hello";
        a.list.clear();
        System.out.println(a);
        objectTrans.restore(a);
        System.out.println(a);
//
//        A a2 = new A();
//        System.out.println(a2);
//        a2.x = "a";
//        objectTrans.restore(a2);
//        System.out.println(a2);
    }

    public void restore(Object object){
        Set<Field> fields = ReflectUtils.getAllFieldsAccessibleFromObject(object.getClass());
        fields.parallelStream().forEach(f->{
            // later add support for petra's parallel / distributable atomic reference
            // we capture/replace the value rather than the reference
            try {
                boolean access = f.isAccessible();
                Object restored = storedValues.get(f);
                // later add support for petra's parallel / distributable atomic reference
                // we capture/replace the value rather than the reference
                f.set(object,restored);
                f.setAccessible(access);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }
}

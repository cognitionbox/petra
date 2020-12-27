package io.cognitionbox.petra.lang;

import io.cognitionbox.petra.core.impl.ObjectCopyerViaSerialization;
import io.cognitionbox.petra.core.impl.ReflectUtils;
import io.cognitionbox.petra.factory.PetraParallelComponentsFactory;
import io.cognitionbox.petra.factory.PetraSequentialComponentsFactory;
import io.cognitionbox.petra.util.Petra;
import io.cognitionbox.petra.util.impl.PList;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static io.cognitionbox.petra.util.Petra.rw;

public class ObjectTrans {

    static class A {
        String x = "x";
        String y = "y";
        List<Integer> list = new PList<>();
        RW<Integer> i = Petra.rw(1);

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
                    ", i=" + i +
                    '}';
        }
    }

    private ObjectCopyerViaSerialization copyer = new ObjectCopyerViaSerialization();
    private Map<Field,Object> storedValues = new ConcurrentHashMap<>();
    public ObjectTrans() {}

    public void capture(Object object){
        Set<Field> fields = ReflectUtils.getAllNonStaticFieldsAccessibleFromObject(object.getClass());
        fields.parallelStream().forEach(f->{
            boolean access = f.isAccessible();
            f.setAccessible(true);
            try {
                Object value = f.get(object);
                if (value instanceof Serializable){
                    // later add support for petra's parallel / distributable atomic reference
                    // we capture/replace the value rather than the reference
                    storedValues.put(f,copyer.copy(value));
                }
            } catch (Throwable e) {
                e.printStackTrace();
            } finally {
                f.setAccessible(access);
            }
        });
    }

    public static void main(String[] args){

        PComputer.getConfig()
                .enableStatesLogging()
                .setConstructionGuaranteeChecks(true)
                .setStrictModeExtraConstructionGuarantee(true)
                .setSequentialModeFactory(new PetraSequentialComponentsFactory())
                .setParallelModeFactory(new PetraParallelComponentsFactory());

        A a = new A();
        ObjectTrans objectTrans = new ObjectTrans();
        objectTrans.capture(a);
        System.out.println(a);
        a.x = "hello";
        a.i.set(7);
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
        Set<Field> fields = ReflectUtils.getAllNonStaticFieldsAccessibleFromObject(object.getClass());
        fields.parallelStream().forEach(f->{
            // later add support for petra's parallel / distributable atomic reference
            // we capture/replace the value rather than the reference
            boolean access = f.isAccessible();
            f.setAccessible(true);
            try {
                if (storedValues.containsKey(f)){
                    Object restored = storedValues.get(f);
                    // later add support for petra's parallel / distributable atomic reference
                    // we capture/replace the value rather than the reference
                    f.set(object,restored);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } finally {
                f.setAccessible(access);
            }
        });
    }
}

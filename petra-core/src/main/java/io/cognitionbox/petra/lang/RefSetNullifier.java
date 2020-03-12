package io.cognitionbox.petra.lang;

import io.cognitionbox.petra.core.impl.ReflectUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static io.cognitionbox.petra.util.Petra.ref;

public class RefSetNullifier {
    private Map<Field,Ref> map = new HashMap<>();


    // after operation we need to revert back the reference

    public void nullifyAllSettersOfRefsInObject(Object o, boolean trueForNullifyFalseForRevert){
        Set<Field> fields = ReflectUtils.getAllFieldsAccessibleFromObject(o.getClass())
                .stream()
                .filter(f->Ref.class.isAssignableFrom(f.getType()))
                .collect(Collectors.toSet());

        // need to do this recursively for all other refs
        for (Field f : fields){
            try {
                Ref r = null;
                boolean acc = f.isAccessible();
                f.setAccessible(true);
                if (trueForNullifyFalseForRevert){

                    r = (Ref) f.get(o);
                    map.put(f,r); // need to store original ref values against fields, e.g. in a Map
                    f.set(o,ref(r.get())); // will be set with new ref of same value
                    // get will return same result but set will set on this new ref rather than original
                } else {
                    r = map.get(f);
                    f.set(o,r); // add original ref back in
                }
                f.setAccessible(acc);
                // recursive step to
                if (r.get()!=null){ // refs value might be null hence we don't need to recurse it
                    nullifyAllSettersOfRefsInObject(r.get(),trueForNullifyFalseForRevert);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}

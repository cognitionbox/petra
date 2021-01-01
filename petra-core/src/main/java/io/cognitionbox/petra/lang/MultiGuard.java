package io.cognitionbox.petra.lang;

import java.util.ArrayList;
import java.util.List;

public class MultiGuard<E> extends Guard<E> {
    private List<Guard<E>> guards = new ArrayList<>();
    public MultiGuard(){}
    public MultiGuard(List<Guard<E>> guards){
        this.guards = guards;
    }
    void addGuard(Guard<E> guard){
        this.guards.add(guard);
    }
    @Override
    public boolean test(Object value) {
        for (Guard<E> guard : this.guards){
            if (guard.test(value)){
                return true;
            }
        }
        return false;
    }

    public void clear() {
        this.guards.clear();
    }
}

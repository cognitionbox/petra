/**
 * Copyright (C) 2016-2020 Aran Hakki.
 *
 * This file is part of Petra.
 *
 * Petra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Petra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Petra.  If not, see <https://www.gnu.org/licenses/>.
 */
package io.cognitionbox.petra.lang;

import io.cognitionbox.petra.util.impl.PLock;
import io.cognitionbox.petra.util.impl.PMap;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

public class Exclusives {

    private static Map<Class<?>,Object> exclusives = new PMap<>("Exclusives.exclusives");
    private static Map<Class<?>,Set<Field>> dependencies = new PMap<>("Exclusives.dependencies");
    private static Map<Set<Class<?>>,Lock> classLocks = new PMap<>("Exclusives.classLocks");

    static void clearAll(){
        dependencies.clear();
        exclusives.clear();
        classLocks.clear();
    }

    static <T> T get(Field field){
        return (T)exclusives.get(field);
    }

    private static Set<Class<?>> getClassSetKeyForRootExclusive(Class<?> clazz){
        Set<Class<?>> clazzSet = dependencies
                .getOrDefault(clazz,new HashSet<>())
                .stream()
                .map(f->f.getType())
                .collect(Collectors.toSet());
        return clazzSet;
    }

    static Set<Set<Class<?>>> getKeysWhichIntersectWithKey(Set<Class<?>> k1){
        Set<Set<Class<?>>> keysWhichIntersect = new HashSet<>();
        for (Set<Class<?>> k2 : classLocks.keySet()){
            Set<Class<?>> a = new HashSet<>(k1);
            Set<Class<?>> b = new HashSet<>(k2);
            a.retainAll(b); // set intersection
            if (!a.isEmpty()){
                keysWhichIntersect.add(k2);
            }
        }
        return keysWhichIntersect;
    }
//         Set<Class<?>> clazzSet = getClassSetKeyForRootExclusive(clazz);
    static boolean tryAquireExclusive(Set<Class<?>> key){
        Set<Set<Class<?>>> keysWhichIntersect = getKeysWhichIntersectWithKey(key);
        boolean keysAquired = true;
        Collection<Lock> locks = new ArrayList<>();
        for (Set<Class<?>> k : keysWhichIntersect){
            Lock lock = classLocks.get(k);
            if (lock.tryLock()){
                locks.add(lock);
                keysAquired = keysAquired && true;
            } else {
                keysAquired = false;
                break;
            }
        }
        if (keysAquired){
            return true;
        } else {
            locks.forEach(l->l.unlock());
            return false;
        }
    }

    // Set<Class<?>> clazzSet = getClassSetKeyForRootExclusive(clazz);
    static void returnExclusive(Set<Class<?>> key){
        Set<Set<Class<?>>> keysWhichIntersect = getKeysWhichIntersectWithKey(key);
        for (Set<Class<?>> k : keysWhichIntersect){
            Lock lock = classLocks.get(k);
            lock.unlock();
        }
    }

    public static void register(Class<?> clazz, Set<Field> dependancies) throws InstantiationException, IllegalAccessException {
//        if (!dependencies.isEmpty()){
//            markedExclusive.add(clazz);
//        }
        Exclusives.dependencies.put(clazz,dependancies);
        Set<Class<?>> clazzSet = getClassSetKeyForRootExclusive(clazz);
        classLocks.put(clazzSet,new PLock());
        try {
            // eiger load
            for (Field dependancy : dependancies){
                Object singleton = exclusives.get(dependancy.getType());
                if (singleton==null){
                    singleton = dependancy.getType().newInstance();
                    exclusives.put(dependancy.getType(),singleton);
                }
            }
        } catch (Exception e){
            throw new ExclusivesLoadException(e);
        }
    }

    static class ExclusivesLoadException extends RuntimeException {
        public ExclusivesLoadException(Throwable cause) {
            super(cause);
        }
    }

    static void load(Object instance, Class<?> clazz) {
        try {
            for (Field dependancy : dependencies.get(clazz)){
                Object singleton = exclusives.get(dependancy.getType());
                // lazy load mode, better to verify successful construction upfront
                // as there will only be a few exclusive resources
//                if (singleton==null){
//                    singleton = dependancy.getType().newInstance();
//                    exclusives.put(dependancy.getType(),singleton);
//                }
                dependancy.setAccessible(true);
                dependancy.set(instance,singleton);
                dependancy.setAccessible(false);
            }
        } catch (Exception e){
            throw new ExclusivesLoadException(e);
        }
    }
}

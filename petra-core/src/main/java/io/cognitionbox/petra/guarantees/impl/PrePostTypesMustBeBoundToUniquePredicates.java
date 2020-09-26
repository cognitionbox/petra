/**
 * Copyright 2016-2020 Aran Hakki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.cognitionbox.petra.guarantees.impl;

import io.cognitionbox.petra.guarantees.StepCheck;
import io.cognitionbox.petra.core.IStep;
import io.cognitionbox.petra.lang.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class PrePostTypesMustBeBoundToUniquePredicates implements StepCheck {

        private Map<Class<?>, byte[]> map = new HashMap<>();

        private byte[] objectToBytes(Object o) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = null;
            try {
                out = new ObjectOutputStream(bos);
                out.writeObject(o);
                out.flush();
                return bos.toByteArray();
            } catch (Exception e) {

            } finally {
                try {
                    bos.close();
                } catch (IOException ex) {
                    // ignore close exception
                }
            }
            return null;
        }

        private boolean checkPType(Guard guard) {
            if (guard instanceof GuardXOR) {
                boolean ok = true;
                for (Guard p : ((GuardXOR<?>) guard).getChoices()) {
                    ok = ok && checkTypeImpl(p);
                }
                return ok;
            } else {
                return checkTypeImpl(guard);
            }
        }

        private boolean checkTypeImpl(Guard guard) {
            byte[] current = objectToBytes(guard.getPredicate());
            if (map.containsKey(guard.getTypeClass())) {
                byte[] read = map.get(guard.getTypeClass());
                float matches = 0;
                // Heuristic method, short term hack for checking predicate uniqueness.
                // Need to look more into structure of serialized lambda...
                // Other approach would be to use ast to parse and extract lambda, storing
                // the lamda predicate as a string, this approach will definitely work
                if (read.length > current.length) {
                    for (int i = 0; i < current.length; i++) {
                        if (read[i] == current[i]) {
                            matches++;
                        }
                    }
                    return (matches / current.length) > 0.3;
                } else if (read.length <= current.length) {
                    for (int i = 0; i < read.length; i++) {
                        if (read[i] == current[i]) {
                            matches++;
                        }
                    }
                    return (matches / read.length) > 0.3;
                }
                return false;
//                if (Arrays.equals(read,current)){
//                    return true;
//                } else {
//                    return false;
//                }
            } else {
                map.put(guard.getTypeClass(), current);
                return true;
            }
        }

        @Override
        public boolean test(IStep<?> step) {
            if (step instanceof RGraph) {
                boolean ok = true;
                for (IStep<?> s : ((RGraph<?, ?>) step).getParallizable()) {
                    ok = ok && checkPType(s.p()) && checkPType(s.q());
                }
                return ok;
            } else if (step instanceof PEdge) {
                return checkPType(step.p()) && checkPType(step.q());
            }
            return false;
        }
    }
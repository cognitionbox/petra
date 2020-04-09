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
package io.cognitionbox.petra.core.impl;

import io.cognitionbox.petra.lang.*;
import io.cognitionbox.petra.lang.PJoin2;
import io.cognitionbox.petra.lang.PJoin;
import io.cognitionbox.petra.util.impl.PList;
import io.cognitionbox.petra.core.IJoin;
import io.cognitionbox.petra.core.IRollback;

import java.io.Serializable;

public class JoinRollbackHelper implements Serializable {

    public boolean isJoinSideEffect(IJoin join){
        return false;//joinSome instanceof IEffect;
    }

    public Class<?> getSideEffectType(IJoin join){
        if (join instanceof PJoin){
            return ((PJoin) join).r().getTypeClass();
        } else if (join instanceof PJoin2){
            return ((PJoin2) join).r().getTypeClass();
        } else if (join instanceof PJoin3){
            return ((PJoin3) join).r().getTypeClass();
        }
        return null;
    }

    private void captureListStates(long millisBeforeRetry, IJoin join, PList... lists){
        if (isJoinSideEffect(join)){
            for (PList list : lists){
                if (getSideEffectType(join).isAssignableFrom(list.get(0).getClass())){
                    list.stream().forEach(s->capture(millisBeforeRetry,s,join));
                }
            }
        }
    }

    private void rollbackListStates(long millisBeforeRetry, IJoin join, PList... lists){
        if (isJoinSideEffect(join)){
            Guard[] types = new Guard[lists.length];
            if (join instanceof PJoin){
                Guard typeA = ((PJoin) join).a();
                types[0] = typeA;
            } else if (join instanceof PJoin2){
                Guard typeA = ((PJoin2) join).a();
                Guard typeB = ((PJoin2) join).b();
                types[0] = typeA;
                types[1] = typeB;
            }  else if (join instanceof PJoin3){
                Guard typeA = ((PJoin3) join).a();
                Guard typeB = ((PJoin3) join).b();
                Guard typeC = ((PJoin3) join).c();
                types[0] = typeA;
                types[1] = typeB;
                types[2] = typeC;
            }

            for (int i=0;i<lists.length;i++){
                final int iCopy = i;
                PList list = lists[iCopy];
                list.stream()
                        .filter(s->getSideEffectType(join).isAssignableFrom(s.getClass()))
                        .forEach(s->rollback(millisBeforeRetry,s,join,types[iCopy]));
            }
        }
    }

    private boolean sideEffectsCheck(IJoin join, PList... lists){
        if (isJoinSideEffect(join)){
            // only check non side effected types
            // it should have the effect of isolating lists which do not have the side effects,
            // and hence states in these lists should not have changed.
            boolean ok = true;
            if (lists.length==1){
                ok = ok && lists[0].stream()
                        .filter(s->!getSideEffectType(join).isAssignableFrom(s.getClass()))
                        .allMatch(s->((PJoin) join).a().test(s));
            } else if (lists.length==2){
                ok = ok && lists[0].stream()
                        .filter(s->!getSideEffectType(join).isAssignableFrom(s.getClass()))
                        .allMatch(s->((PJoin2) join).a().test(s));
                ok = ok && lists[1].stream()
                        .filter(s->!getSideEffectType(join).isAssignableFrom(s.getClass()))
                        .allMatch(s->((PJoin2) join).b().test(s));
            } else if (lists.length==3){
                ok = ok && lists[0].stream()
                        .filter(s->!getSideEffectType(join).isAssignableFrom(s.getClass()))
                        .allMatch(s->((PJoin3) join).a().test(s));
                ok = ok && lists[1].stream()
                        .filter(s->!getSideEffectType(join).isAssignableFrom(s.getClass()))
                        .allMatch(s->((PJoin3) join).b().test(s));
                ok = ok && lists[2].stream()
                        .filter(s->!getSideEffectType(join).isAssignableFrom(s.getClass()))
                        .allMatch(s->((PJoin3) join).c().test(s));
            }
            return ok;
        } else {
            boolean ok = true;
            if (lists.length==1){
                ok = ok && lists[0].stream().allMatch(s->((PJoin) join).a().test(s));
            } else if (lists.length==2){
                ok = ok && lists[0].stream().allMatch(s->((PJoin2) join).a().test(s));
                ok = ok && lists[1].stream().allMatch(s->((PJoin2) join).b().test(s));
            } else if (lists.length==3){
                ok = ok && lists[0].stream().allMatch(s->((PJoin3) join).a().test(s));
                ok = ok && lists[1].stream().allMatch(s->((PJoin3) join).b().test(s));
                ok = ok && lists[2].stream().allMatch(s->((PJoin3) join).c().test(s));
            }
            return ok;
        }
    }

    private void waitBeforeRetry(long millisBeforeRetry){
        try {
            Thread.sleep(millisBeforeRetry);
        } catch (Exception e){}
    }

    public void capture(long millisBeforeRetry, Object input,  IJoin join){
        if (join instanceof IRollback){
            while(true){
                try {
                    synchronized (this){
                        ((IRollback) join).capture(input);
                        break;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    waitBeforeRetry(millisBeforeRetry);
                }
            }
        }
    }

    public void rollback(long millisBeforeRetry, Object input, IJoin join, Guard preGuard){
        if (join instanceof IRollback){
            while(true){
                try {
                    synchronized (this){
                        ((IRollback) join).rollback(input);
                    }
                    if (preGuard.test(input)){
                        break;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    waitBeforeRetry(millisBeforeRetry);
                }
            }
        }
    }
}

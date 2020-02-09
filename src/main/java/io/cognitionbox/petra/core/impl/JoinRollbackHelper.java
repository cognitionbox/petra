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
package io.cognitionbox.petra.core.impl;

import io.cognitionbox.petra.lang.Guard;
import io.cognitionbox.petra.lang.PJoin;
import io.cognitionbox.petra.lang.PJoin2;
import io.cognitionbox.petra.lang.PJoin3;
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

    public void captureListStates(long millisBeforeRetry, IJoin join, PList... lists){
        if (isJoinSideEffect(join)){
            for (PList list : lists){
                if (getSideEffectType(join).isAssignableFrom(list.get(0).getClass())){
                    list.stream().forEach(s->capture(millisBeforeRetry,s,join));
                }
            }
        }
    }

    public void rollbackListStates(long millisBeforeRetry, IJoin join, PList... lists){
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

    public boolean sideEffectsCheck(IJoin join, PList... lists){
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

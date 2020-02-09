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
package io.cognitionbox.petra.util;

import io.cognitionbox.petra.lang.Guard;
import io.cognitionbox.petra.core.impl.OperationType;
import io.cognitionbox.petra.core.impl.ReflectUtils;
import io.cognitionbox.petra.google.Optional;

import java.util.List;

public class PetraUtils {

    public static Optional<Class<?>> getEffectTypeForJoinWithInputTypesAndReturnType(List<Guard<?>> inputTypes, Guard<?> r) {
        Class<?> ret = r.getTypeClass();  //(Class<R>) inputTypes.get(inputTypes.size()-1).getTypeClass();
        int count = 0;
        Class<?> commonSubType = null;
        for (int i=0;i<inputTypes.size()-1;i++){
            Class<?> t = inputTypes.get(i).getTypeClass();
            if (inputTypes.get(i).getOperationType()==OperationType.WRITE){
                java.util.Optional<Class<?>> optional = ReflectUtils.getCommonSubType(t,ret);
                if (optional.isPresent()){
                    count++;
                    commonSubType = optional.get();
                }
            }
        }
        if (count==1){
            return Optional.of(commonSubType);
        }
        return Optional.absent();
    }
}

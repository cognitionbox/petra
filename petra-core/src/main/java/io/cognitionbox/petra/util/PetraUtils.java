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

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

import com.hazelcast.core.IList;
import io.cognitionbox.petra.util.impl.JetListWrapper;
import io.cognitionbox.petra.util.impl.PList;

import java.util.List;

public class PetraJetUtils {

    public static IList unwrap(List list){
        if (list instanceof JetListWrapper){
            JetListWrapper wrapper = (JetListWrapper) list;
            return (IList) wrapper.getJetList();
        } else if (list instanceof PList){
            return ((JetListWrapper)((PList) list).getBackingList()).getJetList();
        }
        return null;
    }
}

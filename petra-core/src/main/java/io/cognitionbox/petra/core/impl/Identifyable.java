/**
 * Copyright 2016-2020 Aran Hakki
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.cognitionbox.petra.core.impl;

import io.cognitionbox.petra.core.IIdentifyable;
import io.cognitionbox.petra.core.Id;

import java.io.Serializable;
import java.util.UUID;

public class Identifyable implements IIdentifyable, Serializable {
    // this uniqueId is used for storing list of states in hazelcast
    private String uniqueId = UUID.randomUUID().toString();
    private String partitionKey;

    public Identifyable(Id id) {
        this.uniqueId = id.getUniqueId();
    }

    public Identifyable() {
        this("");
    }

    public Identifyable(String partitionKey) {
        this.partitionKey = (partitionKey == null || partitionKey.equals("")) ? getClass().getSimpleName() : partitionKey;
    }

    // for place hierarchy can do id@rootGraphPartitionKey.subGraph1ParitionKey...
    public String getUniqueId() {
        return uniqueId + "@" + partitionKey;
    }

    @Override
    public String getPartitionKey() {
        return partitionKey;
    }

    @Override
    public String toString() {
        return getPartitionKey();
        //return getUniqueId();
    }
}

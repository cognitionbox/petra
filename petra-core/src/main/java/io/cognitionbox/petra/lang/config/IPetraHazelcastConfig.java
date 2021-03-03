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
package io.cognitionbox.petra.lang.config;

import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import io.cognitionbox.petra.config.IPetraConfig;

public interface IPetraHazelcastConfig extends IPetraConfig {
    HazelcastServerMode getHazelcastServerMode();

    IPetraHazelcastConfig setHazelcastServerMode(HazelcastServerMode mode);

    void initClient();

    public ClientConfig getHazelcastClientConfig();

    public HazelcastInstance getHazelcastClient();
}

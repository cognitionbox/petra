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
package io.cognitionbox.petra.config;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;

public class PetraHazelcastConfig extends PetraConfig implements IPetraHazelcastConfig {
    protected volatile ClientConfig config;
    protected HazelcastServerMode hazelcastServerMode = HazelcastServerMode.EMBEDDED;
    protected volatile HazelcastInstance hazelcastClient;

    public HazelcastServerMode getHazelcastServerMode() {
        return hazelcastServerMode;
    }

    public IPetraHazelcastConfig setHazelcastServerMode(HazelcastServerMode mode) {
        this.hazelcastServerMode = mode;
        if (getHazelcastServerMode().isCLOUD()) {
            config = new HazelcastCloudClientConfig();
        } else {
            config = new ClientConfig();
        }
        return this;
    }

    public void initClient() {
        if (isTestMode()) {
            this.hazelcastClient = HazelcastClient.newHazelcastClient();
        } else {
            this.hazelcastClient = HazelcastClient.newHazelcastClient(config);
        }
    }

    public ClientConfig getHazelcastClientConfig() {
        return config;
    }

    public HazelcastInstance getHazelcastClient() {
        if (hazelcastClient == null) {
            initClient();
        }
        return hazelcastClient;
    }
}

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
package io.cognitionbox.petra.util.impl;

import io.cognitionbox.petra.core.impl.Identifyable;
import io.cognitionbox.petra.lang.RGraphComputer;
import io.cognitionbox.petra.lang.config.IPetraHazelcastConfig;
import io.cognitionbox.petra.util.IPLock;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public final class HazelcastLockWrapper extends Identifyable implements IPLock {

    public HazelcastLockWrapper(String name) {
        super(name);
    }

    private transient Lock lock = ((IPetraHazelcastConfig) RGraphComputer.getConfig())
            .getHazelcastClient()
            .getLock(getUniqueId());

    private void readObject(java.io.ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        lock = ((IPetraHazelcastConfig) RGraphComputer.getConfig())
                .getHazelcastClient()
                .getLock(getUniqueId());
    }

    @Override
    public void lock() {
        lock.lock();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        lock.lockInterruptibly();
    }

    @Override
    public boolean tryLock() {
        return lock.tryLock();
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return lock.tryLock(time, unit);
    }

    @Override
    public void unlock() {
        lock.unlock();
    }

    @Override
    public Condition newCondition() {
        return lock.newCondition();
    }
}

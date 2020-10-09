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
package io.cognitionbox.petra.examples.tradingsystem.objects;


import io.cognitionbox.petra.lang.annotations.Extract;

import java.io.Serializable;
import java.util.stream.Collectors;

@Extract
public class State implements Serializable {
    private DecisionsStore decisionsStore = new DecisionsStore();
    private ExposureStore exposureStore = new ExposureStore();
    private Traders traders = new Traders();
    @Extract
    public Traders traders(){
        return traders;
    }

    public void addTrader(Trader trader) {
        traders.add(trader);
    }

    public Traders getTraders() {
        return traders;
    }

    public void collectDecisions(){
        decisionsStore.getAllDecisions().addAll(traders().stream().flatMap(d->d.getDecisions().stream()).collect(Collectors.toList()));
        traders().forEach(t->t.getDecisions().clear());
    }

    public void updateExposure(){
        exposureStore.addExposure(decisionsStore.getAllDecisions().stream().mapToDouble(d->d.exposure()).sum());
    }

    private boolean exposureStoreNotNull() {
        return exposureStore!=null;
    }

    private boolean exposureNotNull() {
        return exposureStoreNotNull() && exposureStore.getExposure()!=null;
    }

    public boolean exposureGtZero() {
        return exposureNotNull() && exposureStore.getExposure()>0;
    }

    public boolean exposureEqZero() {
        return exposureNotNull() && exposureStore.getExposure()==0;
    }

    public boolean exposureLt200() {
        return exposureNotNull() && exposureStore.getExposure()<200;
    }

    public boolean exposureEq200() {
        return exposureNotNull() && exposureStore.getExposure()==200;
    }

    public DecisionsStore getDecisionStore() {
        return decisionsStore;
    }

    public ExposureStore getExposureStore() {
        return exposureStore;
    }

    public boolean hasDecisions() {
        return getDecisionStore().hasDecisions();
    }
}
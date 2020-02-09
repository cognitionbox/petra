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
  public Identifyable() {this("");}
  public Identifyable(String partitionKey) {
    this.partitionKey = (partitionKey ==null || partitionKey.equals(""))?getClass().getSimpleName(): partitionKey;
  }

  // for place hierarchy can do id@rootGraphPartitionKey.subGraph1ParitionKey...
  public String getUniqueId() {
    return uniqueId +"@"+ partitionKey;
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

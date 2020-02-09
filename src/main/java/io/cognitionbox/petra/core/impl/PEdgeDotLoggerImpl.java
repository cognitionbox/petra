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

import io.cognitionbox.petra.core.IStep;
import io.cognitionbox.petra.core.IEdgeDotLogger;
import io.cognitionbox.petra.lang.PGraph;

public class PEdgeDotLoggerImpl implements IEdgeDotLogger {

  private StringBuilder diagram = new StringBuilder();

  @Override
  public void logCompletedStep(IStep from, IStep too) {
    if ( ((Identifyable)from).getPartitionKey().contains("extractThrowablesStep") ||
            ((Identifyable)too).getPartitionKey().contains("extractThrowablesStep")){
      return;
    }
            diagram.append(from+"->"+too+" [style=dotted, label=owns];\n");
    if (too instanceof PGraph){
      diagram.append(((PGraph) too).getTransitions());
    }
  }

  @Override
  public void logJoin(IStep from, int joinIndex) {
    diagram.append(from+"_join_"+joinIndex+" [shape=rect style=filled, fillcolor=orange fontcolor=black];\n");
    diagram.append(from+"->"+from+"_join_"+joinIndex+" [style=dotted, label=owns];\n");
  }

  @Override
  public String getTransitions(){
    return diagram.toString();
  }
}

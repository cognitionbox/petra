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

import io.cognitionbox.petra.lang.AbstractStep;
import io.cognitionbox.petra.lang.Guard;
import io.cognitionbox.petra.lang.PGraph;
import io.cognitionbox.petra.lang.RGraph;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;

public final class LogicStepsCollector {

    public static List<AbstractStep> getAllSteps(RGraph pgraph){
        List<AbstractStep> steps = new ArrayList<>();
        List<Pair<List<Guard>, Guard>> joinTypes = new ArrayList<>();
        getAllSteps(pgraph,steps,joinTypes);
        return steps;
    }

    public static List<AbstractStep> getAllSteps(RGraph pgraph, List<Pair<List<Guard>, Guard>> joinTypes){
        List<AbstractStep> steps = new ArrayList<>();
        getAllSteps(pgraph,steps,joinTypes);
        return steps;
    }

    private static void getAllSteps(RGraph XGraph, List<AbstractStep> steps, List<Pair<List<Guard>, Guard>> joinTypes){
        steps.add(XGraph);
        for (Object s : XGraph.getParallizable()){
            if (s instanceof PGraph){
                getAllSteps((PGraph) s,steps,joinTypes);
            } else {
                steps.add((AbstractStep) s);
//                for (Object jt : XGraph.getJoinTypes()){
//                    joinTypes.add((Pair<List<Guard>, Guard>) jt);
//                }
            }
        }
    }
}

/**
 * Copyright (C) 2016-2020 Aran Hakki.
 * <p>
 * This file is part of Petra.
 * <p>
 * Petra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * Petra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with Petra.  If not, see <https://www.gnu.org/licenses/>.
 */
package io.cognitionbox.petra.examples.fizzbuzz2;

import java.util.ArrayList;
import java.util.List;

public class X {

    private final List<String> lines = new ArrayList<>();

    public void addLine(String line) {
        System.out.println("Adding line: " + line);
        lines.add(line);
    }

    public String[] getLines() {
        return lines.toArray(new String[0]);
    }

    public int i = 1;
}
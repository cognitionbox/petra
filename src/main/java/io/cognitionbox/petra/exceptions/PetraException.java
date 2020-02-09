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
package io.cognitionbox.petra.exceptions;

import java.util.List;

public class PetraException extends RuntimeException{
    @Override
    public Throwable getCause() {
        return causes[0];
    }

    public Throwable[] getCauses() {
        return causes;
    }

    private Throwable[] causes;
    public PetraException(Throwable... causes){
        this.causes = causes;
    }
    public PetraException(List<? extends Throwable> causes){
        Throwable[] arr = new Throwable[causes.size()];
        for (int i=0;i<arr.length;i++){
            arr[i] = causes.get(0);
        }
        this.causes = arr;
    }
}

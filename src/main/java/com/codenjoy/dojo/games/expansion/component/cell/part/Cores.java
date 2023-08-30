package com.codenjoy.dojo.games.expansion.component.cell.part;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2023 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.codenjoy.dojo.games.expansion.Forces;
import com.codenjoy.dojo.games.expansion.ForcesMoves;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.QDirection;
import java.util.List;
import java.util.Optional;
import lombok.Getter;

@Getter
public class Cores {

  protected Forces actual;
  protected Forces prev;
  protected ForcesMoves move;

  public Cores(Forces actual) {
    this.actual = actual;
  }

  public boolean refreshAndCheckIsAlive(List<Forces> myForces) {
    if(actual == null) {
      return false;
    }

    Point newPoint = actual.getRegion().copy();
    newPoint.move(QDirection.valueOf(move.getDirection()));
    Optional<Forces> forceAtNewPoint = myForces.stream().filter(f -> f.getRegion().equals(newPoint)).findFirst();
    if(forceAtNewPoint.isPresent()) {
      prev = actual;
      actual = forceAtNewPoint.get();
      return true;
    }

    Optional<Forces> forceAtActualPoint = myForces.stream().filter(f -> f.getRegion().equals(actual.getRegion())).findFirst();
    if(forceAtActualPoint.isPresent()) {
      prev = actual;
      actual = forceAtActualPoint.get();
      return true;
    }

    return false;
  }
}

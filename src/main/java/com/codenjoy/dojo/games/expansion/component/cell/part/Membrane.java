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

import com.codenjoy.dojo.games.expansion.Board;
import com.codenjoy.dojo.games.expansion.Forces;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.QDirection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Membrane {

  protected Set<Forces> membraneForces = new HashSet<>(100);


  //TODO do i really need it? only if i wanna have state
  public void rebuild(Board board, Set<Forces> cellForces, List<Cores> cores, List<Forces> myForces) {
    Set<Forces> myForcesSet = new HashSet<>(myForces);
    membraneForces = new HashSet<>(100);

    for (Forces cellForce : cellForces) {
      boolean isLiveEdge = false;

      if(cores.stream().anyMatch(c -> c.getActual().getRegion().equals(cellForce.getRegion()))){
        //its core
        continue;
      }
      //check adjusted cells

      for (QDirection dir : QDirection.getValues()) {
        Point point = cellForce.getRegion().copy();
        point.move(dir);

        if(board.isOutOf(point.getX(), point.getY())) {
          continue;
        }

        if(cores.stream().anyMatch(c -> c.getActual().getRegion().equals(point))){
          continue;
        }

        if(myForcesSet.contains(new Forces(point, 0))) {
          isLiveEdge = false;
        } else if (board.isBarrierAt(point.getX(), point.getY())) {
          isLiveEdge = false;
        } else {
          isLiveEdge = true;
          break;
        }
      }

      if(isLiveEdge) {
        membraneForces.add(cellForce);
      }
    }
  }
}

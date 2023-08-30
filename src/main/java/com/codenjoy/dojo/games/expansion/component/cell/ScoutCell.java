package com.codenjoy.dojo.games.expansion.component.cell;

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
import com.codenjoy.dojo.games.expansion.Element;
import com.codenjoy.dojo.games.expansion.Forces;
import com.codenjoy.dojo.games.expansion.Forces.ForceType;
import com.codenjoy.dojo.services.QDirection;
import java.util.Optional;

/**
 * Behaviour model:
 * - all forces to membrane
 * - find new gold
 * - avoid combat
 */
public class ScoutCell extends CellWithMembrane {

  public ScoutCell(Forces force) {
    super(force);
  }


  @Override
  public void moveForces(Board board){
    collectMembraneCells();

    moveAimlessToMembrane(board);
    moveMembraneToGold(board);
  }

  protected void moveAimlessToMembrane(Board board){
    for (Forces cellForce : this.getCellForces()) {
      if(cellForce.getType() != ForceType.AIMLESS
          || cellForce.getCount() < 2) {
        continue;
      }
      Optional<QDirection> qDirection = forceToForceDirection(
          cellForce,
          board,
          p -> true,
          membraneForces);
      qDirection.ifPresent(direction -> cellForce.move(direction, cellForce.getCount()));
    }
  }


  protected void moveMembraneToGold(Board board){
    for (Forces cellForce : this.getCellForces()) {
      if(cellForce.getType() != ForceType.MEMBRANE
          || cellForce.getCount() < 2) {
        continue;
      }
      Optional<QDirection> qDirection = forceToElementsDirection(
          cellForce,
          board,
          p -> !cellForces.contains(new Forces(p ,1)),
          Element.GOLD);
      qDirection.ifPresent(direction -> cellForce.move(direction, cellForce.getCount()));
    }
  }
}

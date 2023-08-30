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

import static java.util.Arrays.asList;

import com.codenjoy.dojo.games.expansion.Board;
import com.codenjoy.dojo.games.expansion.Element;
import com.codenjoy.dojo.games.expansion.Forces;
import com.codenjoy.dojo.games.expansion.Forces.ForceType;
import com.codenjoy.dojo.games.expansion.component.path.PlainDistance;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.QDirection;
import java.util.HashSet;
import java.util.Optional;

/**
 * Behaviour model:
 * - maintain buffer
 * - keep core
 * - avoid combat
 * - predict threats
 */
public class GuardCell extends CellWithMembraneAndCore {
  public static final int PATH_TO_CORE_MAX_DISTANCE = 3;

  public GuardCell(Forces force) {
    super(
        force,
        asList(
            Element.GOLD,
            Element.BASE1,
            Element.BASE2,
            Element.BASE3,
            Element.BASE4
        )
      );
  }

  @Override
  public void moveForces(Board board){
    updateCore(board);
    if(!containsCore()) {
      core = null;
      cellForces = new HashSet<Forces>();
      System.out.println("destroy cell");
    } else {
      collectMembraneCells();

      moveAllToBuffer(board);
      moveMembraneToGold(board);
    }
  }


  protected void moveAllToBuffer(Board board){
    for (Forces cellForce : this.getCellForces()) {
      if(cellForce.getType() != ForceType.CORE
          || cellForce.getCount() < 2) {
        continue;
      }
      int distance = PlainDistance.distance(cellForce.getRegion(), core.getRegion());
      if(distance > PATH_TO_CORE_MAX_DISTANCE) {
        //force shrink
        QDirection direction = cellForce.getRegion().direction(core.getRegion());


        Point lookPoint = cellForce.getRegion().copy().move(direction);
        if(!board.isBarrierAt(lookPoint)){
          cellForce.move(direction, cellForce.getCount());
        }

      } else if (distance < PATH_TO_CORE_MAX_DISTANCE) {
        //expand
        QDirection direction = cellForce.getRegion().direction(core.getRegion()).inverted();
        Point lookPoint = cellForce.getRegion().copy().move(direction);
        if(!board.isBarrierAt(lookPoint)){
          cellForce.move(direction, cellForce.getCount());
        }
      }

      //fill the gaps with membrane


      Optional<QDirection> qDirection = forceToForceDirection(
          cellForce,
          board,
          p -> true,
          membraneForces);
      qDirection.ifPresent(direction -> cellForce.move(direction, cellForce.getCount() - 1));
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
      qDirection.ifPresent(direction -> cellForce.move(direction, cellForce.getCount() - 1));
    }
  }
}

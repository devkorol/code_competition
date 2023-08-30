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

import static com.codenjoy.dojo.games.expansion.component.GreyGoo.PATH_TO_MAX_TOLERANCE;
import static com.codenjoy.dojo.games.expansion.component.GreyGoo.PATH_TO_MAX_VARIATIONS;

import com.codenjoy.dojo.games.expansion.Board;
import com.codenjoy.dojo.games.expansion.Element;
import com.codenjoy.dojo.games.expansion.Forces;
import com.codenjoy.dojo.games.expansion.component.path.ASearch;
import com.codenjoy.dojo.games.expansion.component.path.ASearch.Node;
import com.codenjoy.dojo.games.expansion.component.path.PlainDistance;
import com.codenjoy.dojo.games.expansion.component.path.PlainDistance.PlainDistanceToElementsFilter;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.QDirection;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

public abstract class CellMovable extends Cell {

  public CellMovable(Forces core) {
    super(core);
  }

  protected Optional<QDirection> forceToForceDirection(
      Forces from,
      Board board,
      PlainDistanceToElementsFilter filter,
      List<Forces> targets) {
    TreeSet<Point> pointsByDistance = PlainDistance.getElementsByDistance(from.getRegion(), filter, targets);

    return forceToPointsDirection(from, board, pointsByDistance);
  }

  protected Optional<QDirection> forceToElementsDirection(
      Forces from,
      Board board,
      PlainDistanceToElementsFilter filter,
      Element... elements) {
    TreeSet<Point> pointsByDistance = PlainDistance.getElementsByDistance(from.getRegion(), board, filter, elements);


    return forceToPointsDirection(from, board, pointsByDistance);
  }

  private Optional<QDirection> forceToPointsDirection(Forces from, Board board,
      TreeSet<Point> pointsByDistance) {
    int minSteps = Integer.MAX_VALUE;
    Node minPath = null;

    int i = 0;
    for (Point point : pointsByDistance) {
      if (++i > PATH_TO_MAX_VARIATIONS) {
        break;
      }

      Node path = ASearch.path(from.getRegion(), point, board);
      if (path.getSteps() <= PATH_TO_MAX_TOLERANCE) {
        minSteps = path.getSteps();
        minPath = path;
        break;
      }

      if (minPath == null
          || path.getSteps() < minSteps) {
        minSteps = path.getSteps();
        minPath = path;
      }
    }

    if (minPath != null) {
      return Optional.of(minPath.getNext().getDirection());
    }
    return Optional.empty();
  }

}

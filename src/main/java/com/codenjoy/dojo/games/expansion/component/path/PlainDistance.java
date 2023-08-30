package com.codenjoy.dojo.games.expansion.component.path;

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
import com.codenjoy.dojo.services.Point;
import java.util.List;
import java.util.TreeSet;

public class PlainDistance {

  public static TreeSet<Point> getElementsByDistance(Point source, PlainDistanceToElementsFilter filter, List<Forces> forces) {
    TreeSet<Point> boardElements = initTreeSet(source);
    if(forces != null && !forces.isEmpty()) {
      forces.forEach(f -> boardElements.add(f.getRegion()));
    }
    return boardElements;
  }

  public static TreeSet<Point> getElementsByDistance(Point source, Board board, PlainDistanceToElementsFilter filter, Element... elements) {
    TreeSet<Point> boardElements = initTreeSet(source);

    List<Point> points = board.get(elements);
    points.stream()
        .filter(p -> filter.apply(p))
        .forEach(p -> boardElements.add(p));
    return boardElements;
  }

  protected static TreeSet<Point> initTreeSet(Point source) {
    return new TreeSet<>((o1, o2) -> {
      int result = Integer.compare(
          distance(source, o1),
          distance(source, o2)
      );
      if(result == 0) {
        result = 1;
      }
      return result;
    });
  }

  public static int distance(Point p1, Point p2) {
    return Math.max(
        Math.abs(p1.getX() - p2.getX()),
        Math.abs(p1.getY() - p2.getY())
      );
  }


  @FunctionalInterface
  public interface PlainDistanceToElementsFilter{
    boolean apply(Point p);
  }
}

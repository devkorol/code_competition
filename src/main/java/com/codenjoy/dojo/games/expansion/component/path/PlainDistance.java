package com.codenjoy.dojo.games.expansion.component.path;

import com.codenjoy.dojo.games.expansion.Board;
import com.codenjoy.dojo.games.expansion.Element;
import com.codenjoy.dojo.games.expansion.Forces;
import com.codenjoy.dojo.services.Point;
import java.util.List;
import java.util.TreeSet;

public class PlainDistance {

  public static TreeSet<Point> getElementsByDistance(Point source, List<Forces> forces) {
    TreeSet<Point> boardElements = initTreeSet(source);
    if(forces != null && !forces.isEmpty()) {
      forces.forEach(f -> boardElements.add(f.getRegion()));
    }
    return boardElements;
  }

  public static TreeSet<Point> getElementsByDistance(Point source, Board board, Element... elements) {
    TreeSet<Point> boardElements = initTreeSet(source);

    List<Point> points = board.get(elements);
    boardElements.addAll(board.get(elements));

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

  protected static int distance(Point p1, Point p2) {
    return Math.max(
        Math.abs(p1.getX() - p2.getX()),
        Math.abs(p1.getY() - p2.getY())
      );
  }
}

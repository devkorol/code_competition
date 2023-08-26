package com.codenjoy.dojo.games.expansion.component.path;

import com.codenjoy.dojo.games.expansion.Board;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.QDirection;
import java.util.TreeSet;
import lombok.Getter;
import lombok.ToString;

public class ASearch {
  private static final int WEIGHT_STRAIGHT_PASS = 10;
  private static final int WEIGHT_DIAGONAL_PASS = 14;

  public static Node path(Point source, Point target, Board board) {
    TreeSet<Node> path = new TreeSet<>();
    Node start = new Node(null, source,0, distance(source, target), null);
    Node node = buildPath(target, start, path, board);

    do {
      Node prev = node.getPrev();
      prev.next = node;
      node = prev;
    } while (node.getPrev() != null);

    return node;
  }

  private static Node buildPath(Point target, Node prev, TreeSet<Node> path, Board board) {
    for (QDirection direction : QDirection.values()) {
      Point newPoint = prev.getPoint().copy().move(direction);

      if(board.isOutOf(newPoint)
        || board.isBarrierAt(newPoint)) {
        continue;
      }

      Node current = new Node(
          prev,
          newPoint,
          prev.getCost() + (direction.value() < 4 ? WEIGHT_STRAIGHT_PASS : WEIGHT_DIAGONAL_PASS),
          distance(prev.getPoint(), target),
          direction);

      if(target.equals(current.getPoint())) {
        return current;
      }
      path.add(current);
    }

    Node first = path.first();
    path.remove(first);
    return buildPath(target, first, path, board);
  }

  protected static double distance(Point p1, Point p2) {
    return Math.sqrt(
        Math.pow(p1.getX() - p2.getX(), 2)
        + Math.pow(p1.getY() - p2.getY(), 2)
        );
  }


  @Getter
  @ToString
  public static class Node implements Comparable<Node>{

    private Point point;
    private int cost;
    private double distance;
    private double weight; //the lower the better
    private QDirection direction;
    @ToString.Exclude
    private Node prev;
    private Node next;


    public Node(Node prev, Point point, int cost, double distance, QDirection direction) {
      this.prev = prev;
      this.point = point;
      this.cost = cost;
      this.distance = distance;
      this.weight = cost + distance;
      this.direction = direction;
    }

    @Override
    public int compareTo(Node other) {
      int compare = Double.compare(this.weight, other.getWeight());
      if(compare == 0) {
        compare = Integer.compare(this.direction.value(), other.direction.value());
      }

      return compare;
    }
  }
}

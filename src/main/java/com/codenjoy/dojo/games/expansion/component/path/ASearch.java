package com.codenjoy.dojo.games.expansion.component.path;

import com.codenjoy.dojo.games.expansion.Board;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.QDirection;
import java.util.HashSet;
import java.util.TreeSet;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

public class ASearch {
  private static final int WEIGHT_STRAIGHT_PASS = 10;
  private static final int WEIGHT_DIAGONAL_PASS = 14;

  public static Node path(Point source, Point target, Board board) {
    TreeSet<Node> path = new TreeSet<>();
    Node start = new Node(null, source,0, distance(source, target), null);
    Node node = buildPath(target, start, path, new HashSet<>(1000), board);

    int step = 0;
    do {
      Node prev = node.getPrev();
      prev.next = node;
      node.steps = ++step;
      node = prev;
    } while (node.getPrev() != null);

    node.steps = ++step;
    return node;
  }

  private static Node buildPath(Point target, Node prev, TreeSet<Node> path, HashSet<Node> processed, Board board) {
    processed.add(prev);

    for (QDirection direction : QDirection.getValues()) {
      Point newPoint = prev.getPoint().copy().move(direction);

      if(board.isOutOf(newPoint)
        || board.isBarrierAt(newPoint)) {
        continue;
      }

      if(iWasHere(prev, newPoint)){
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

      if(!processed.contains(current)) {
        path.add(current);
      }
    }

    Node first = path.first();
    path.remove(first);
    return buildPath(target, first, path, processed, board);
  }

  private static boolean iWasHere(Node node, Point newPoint) {
    do {
      if(node.getPoint().equals(newPoint)) {
        return true;
      }
      node = node.getPrev();
    } while (node != null && node.getPrev() != null);

    return false;
  }

  protected static double distance(Point p1, Point p2) {
    return Math.sqrt(
        Math.pow(p1.getX() - p2.getX(), 2)
        + Math.pow(p1.getY() - p2.getY(), 2)
        );
  }


  @Getter
  @ToString
  @EqualsAndHashCode(onlyExplicitlyIncluded = true)
  public static class Node implements Comparable<Node>{

    @EqualsAndHashCode.Include
    private Point point;
    private int cost;
    private double distance;
    private double weight; //the lower the better
    private QDirection direction;
    private int steps;
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

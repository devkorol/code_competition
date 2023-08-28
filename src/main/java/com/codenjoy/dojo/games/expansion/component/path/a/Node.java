package com.codenjoy.dojo.games.expansion.component.path.a;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.QDirection;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Node implements Comparable<Node> {

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
    if (compare == 0) {
      compare = Integer.compare(this.direction.value(), other.direction.value());
    }

    return compare;
  }
}

package com.codenjoy.dojo.games.expansion.component.path.a;

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

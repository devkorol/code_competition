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


import static com.codenjoy.dojo.games.expansion.Forces.ForceType.AIMLESS;
import static com.codenjoy.dojo.games.expansion.Forces.ForceType.MEMBRANE;
import static com.codenjoy.dojo.games.expansion.component.GreyGoo.HISTORY_CELL_SIZE;

import com.codenjoy.dojo.games.expansion.Board;
import com.codenjoy.dojo.games.expansion.Forces;
import com.codenjoy.dojo.games.expansion.component.cell.part.Cores;
import com.codenjoy.dojo.games.expansion.component.cell.part.Membrane;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.QDirection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import org.apache.commons.collections4.queue.CircularFifoQueue;


@Getter
public abstract class Cell {

  protected Set<Forces> cellForces = new HashSet<>(500);

  protected final CircularFifoQueue<HashSet<Forces>> lostForcesHistory = new CircularFifoQueue(
      HISTORY_CELL_SIZE);
  protected final CircularFifoQueue<HashSet<Forces>> newForcesHistory = new CircularFifoQueue(
      HISTORY_CELL_SIZE);

  @Deprecated
  private final Membrane membrane;
  @Deprecated
  private final List<Cores> cores = new ArrayList<>();

  @Deprecated
  private Map<Cell, Integer> cellSameForcesCount = new HashMap<>();

  public Cell(Forces core) {
    membrane = new Membrane();
    cellForces.add(core);
  }

  public boolean isKnown(Forces force) {
    return cellForces.contains(force);
  }

  public Cell deduplicatePointsAfterMoves() {
    Map<Forces, Integer> collect = cellForces.stream()
        .collect(Collectors.toMap(
            c -> c,
            c -> 1,
            (f1, f2) -> {
              //TODO pass types, history etc
              return f1;
            }
        ));
    cellForces = new HashSet<>(collect.keySet());
    return this;
  }

  public Cell refreshPoints(List<Forces> myForces) {
    HashSet<Forces> lostForces = new HashSet<>();
    lostForcesHistory.add(lostForces);

    Iterator<Forces> iterator = cellForces.iterator();
    while (iterator.hasNext()) {
      Forces cellForce = iterator.next();

      Optional<Forces> boardForce =
          myForces.stream()
              .filter(f -> f.equals(cellForce))
              .findFirst();
      if (boardForce.isPresent()) {
        cellForce
            //TODO im sure to lost history??? except sones
            .setType(AIMLESS)
            .setCount(boardForce.get().getCount());
      } else if(cellForce.getMoveHistory() != null
          && !cellForce.getMoveHistory().isEmpty()) {
        //sooooooooo maybe i didnt move?
        boardForce =
            myForces.stream()
                .filter(f -> f.getRegion().equals(cellForce.getMoveHistory().get(0)))
                .findFirst();
        if(boardForce.isPresent()) {
          cellForce
              //TODO im sure to lost history??? except sones
              .setType(AIMLESS)
              .setRegion(boardForce.get().getRegion())
              .setCount(boardForce.get().getCount());
        }

        lostForces.add(cellForce);
        iterator.remove();
      }
    }
    System.out.println(String.format("REFRESH: %s", cellForces));
    return this;
  }

  public Cell roundRobinCollectCellPoints(Board board, List<Forces> myForces, List<Cell> other) {
    HashSet<Forces> newForces = new HashSet<>();
    newForcesHistory.add(newForces);

    Queue<Forces> queue = new LinkedList<>(cellForces);
    cellSameForcesCount = new HashMap<>();

    while (!queue.isEmpty()) {
      Forces cellForce = queue.remove();

      //search for new
      for (QDirection direction : QDirection.getValues()) {
        Point lookPoint = cellForce.getRegion().copy().move(direction);

        if (board.isOutOf(lookPoint)) {
          continue;
        }


        Optional<Forces> anyMyForce = myForces.stream().filter(f -> f.getRegion().equals(lookPoint))
            .findAny();
        if (!anyMyForce.isPresent()) {
          if (!board.isBarrierAt(lookPoint)) {
            cellForce
                .setType(MEMBRANE)
                .getNearPoints().put(direction, lookPoint);
          }

          continue;
        }

        Forces lookForce = anyMyForce.get();
        if (cellForces.contains(lookForce)) {
          continue;
        }

        Optional<Cell> intersectedCell = other.stream().filter(c -> c.isKnown(lookForce))
            .findFirst();
        if (intersectedCell.isPresent()) {
          Integer cellIntersections = cellSameForcesCount.getOrDefault(intersectedCell.get(), 0);
          cellSameForcesCount.put(intersectedCell.get(), ++cellIntersections);

          continue;
        }
//        System.out.println(String.format("match %s - %s", lookForce, anyMyForce.get()));

        newForces.add(lookForce);
        cellForces.add(lookForce);
        queue.add(lookForce);
      }
    }

    System.out.println(String.format("EXPAND: %s", cellForces));
    return this;
  }


  public abstract void moveForces(Board board);
}

package com.codenjoy.dojo.games.expansion.component.cell;


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
import lombok.Getter;
import org.apache.commons.collections4.queue.CircularFifoQueue;


@Getter
public abstract class Cell {

  protected final Set<Forces> cellForces = new HashSet<>(500);

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

  public Cell(Forces force) {
    membrane = new Membrane();
    cellForces.add(force);
  }

  public boolean isKnown(Forces force) {
    return cellForces.contains(force);
  }

  public Cell refreshPoints(List<Forces> myForces){
    HashSet<Forces> lostForces = new HashSet<>();
    lostForcesHistory.add(lostForces);

    Iterator<Forces> iterator = cellForces.iterator();
    while (iterator.hasNext()) {
      Forces cellForce = iterator.next();
      Optional<Forces> boardForce =
          myForces.stream()
              .filter(f -> f.equals(cellForce))
              .findFirst();

      if(boardForce.isPresent()) {
        cellForce
            //TODO im sure to lost history???
            .setType(AIMLESS)
            .setCount(boardForce.get().getCount());
      } else {
        lostForces.add(cellForce);
        iterator.remove();
      }
    }
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

        if(board.isOutOf(lookPoint)) {
          continue;
        }

        Forces lookForce = new Forces(lookPoint, 1);
        if (cellForces.contains(lookForce)) {
          continue;
        }

        //not mine forces so add?
        if(!board.isBarrierAt(lookPoint)) {
          cellForce
              .setType(MEMBRANE)
              .getNearPoints().put(direction, lookPoint);
        }

        //TODO maybe delete it
        Optional<Cell> intersectedCell = other.stream().filter(c -> c.isKnown(lookForce)).findFirst();
        if(intersectedCell.isPresent()) {
          Integer cellIntersections = cellSameForcesCount.getOrDefault(intersectedCell.get(), 0);
          cellSameForcesCount.put(intersectedCell.get(), ++cellIntersections);

          continue;
        }

        Optional<Forces> anyMyForce = myForces.stream().filter(f -> f.getRegion().equals(lookPoint)).findAny();
        if (anyMyForce.isPresent()) {
          lookForce.setCount(anyMyForce.get().getCount());

          newForces.add(lookForce);
          cellForces.add(lookForce);
          queue.add(lookForce);
        }
      }
    }

    return this;
  }


}

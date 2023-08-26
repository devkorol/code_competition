package com.codenjoy.dojo.games.expansion.component.cell;


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
public class Cell {
  private final Membrane membrane;
  private final List<Cores> cores = new ArrayList<>();
  private final Set<Forces> cellForces = new HashSet<>(500);

  private final CircularFifoQueue<HashSet<Forces>> lostForcesHistory = new CircularFifoQueue(
      HISTORY_CELL_SIZE);
  private final CircularFifoQueue<HashSet<Forces>> newForcesHistory = new CircularFifoQueue(
      HISTORY_CELL_SIZE);

  private Map<Cell, Integer> cellSameForcesCount = new HashMap<>();

  public Cell(Forces force) {
    membrane = new Membrane();
    cellForces.add(force);
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
        cellForce.setCount(boardForce.get().getCount());
      } else {
        lostForces.add(cellForce);
        iterator.remove();
      }
    }
    return this;
  }

  public Cell expandToAdjacent(Board board, List<Forces> myForces, List<Cell> other) {
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

  public Cell rebuildParts(Board board, List<Forces> myForces){
//    cores.rebuild(cellForces, myForces);
    //todo merge cores
//    membrane.rebuild(board, cellForces, cores, myForces);
    return this;
  }

  public boolean isKnown(Forces force) {
    return cellForces.contains(force);
  }

  private void mergeCells(Cell c1, Cell c2) {
    //TODO add logic
    //TODO merge cells??? pass cells, membrane, readjust etc...
  }

}

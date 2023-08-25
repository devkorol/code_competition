package com.codenjoy.dojo.games.expansion.component.cell;


import com.codenjoy.dojo.games.expansion.Board;
import com.codenjoy.dojo.games.expansion.Forces;
import com.codenjoy.dojo.games.expansion.component.cell.part.Core;
import com.codenjoy.dojo.games.expansion.component.cell.part.Membrane;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.QDirection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import lombok.Getter;


@Getter
public class Cell {
  private final Membrane membrane;
  private final List<Core> cores = new ArrayList<>();
  private final Set<Forces> cellForces = new HashSet<>(100);

  public Cell(Forces force) {
    membrane = new Membrane();
    cellForces.add(force);
  }

  public Cell refreshPoints(List<Forces> myForces){
    Iterator<Forces> iterator = cellForces.iterator();
    while (iterator.hasNext()) {
      Forces cellForce = iterator.next();
      Optional<Forces> boardForce = myForces.stream().filter(f -> f.getRegion().equals(cellForce.getRegion())).findFirst();
      if(boardForce.isPresent()) {
        cellForce.setCount(boardForce.get().getCount());
      } else {
        iterator.remove();
      }
    }
    return this;
  }

  public Cell expandToAdjacent(Board board, List<Forces> myForces, List<Cell> other) {
    Queue<Forces> queue = new LinkedList<>(cellForces);

    while (!queue.isEmpty()) {
      Forces force = queue.remove();

      //search for new
      for (QDirection direction : QDirection.getValues()) {
        Point newPoint = force.getRegion().copy();
        newPoint.move(direction);
        if(board.isOutOf(newPoint.getX(), newPoint.getY())) {
          continue;
        }

        Forces newForce = new Forces(newPoint, 1);

        if (cellForces.contains(newForce)) {
          continue;
        }
        if(other.stream().anyMatch(c -> c.isKnown(newForce))) {
//          todo mergeCells();
          continue;
        }

        Optional<Forces> anyMyForce = myForces.stream().filter(f -> f.getRegion().equals(newPoint)).findAny();
        if (anyMyForce.isPresent()) {
          newForce.setCount(anyMyForce.get().getCount());
          cellForces.add(newForce);
          queue.add(anyMyForce.get());
        }
      }
    }

    return this;
  }

  public Cell rebuildParts(Board board, List<Forces> myForces){
    Iterator<Core> iterator = cores.iterator();
    while (iterator.hasNext()) {
      Core core = iterator.next();
      if(!core.refreshAndCheckIsAlive(myForces)) {
        iterator.remove();
      }
    }
    //todo merge cores


    membrane.rebuild(board, cellForces, cores, myForces);
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

package com.codenjoy.dojo.games.expansion.component.cell.part;

import com.codenjoy.dojo.games.expansion.Board;
import com.codenjoy.dojo.games.expansion.Forces;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.QDirection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Membrane {

  protected Set<Forces> membraneForces = new HashSet<>(100);


  //TODO do i really need it? only if i wanna have state
  public void rebuild(Board board, Set<Forces> cellForces, List<Cores> cores, List<Forces> myForces) {
    Set<Forces> myForcesSet = new HashSet<>(myForces);
    membraneForces = new HashSet<>(100);

    for (Forces cellForce : cellForces) {
      boolean isLiveEdge = false;

      if(cores.stream().anyMatch(c -> c.getActual().getRegion().equals(cellForce.getRegion()))){
        //its core
        continue;
      }
      //check adjusted cells

      for (QDirection dir : QDirection.getValues()) {
        Point point = cellForce.getRegion().copy();
        point.move(dir);

        if(board.isOutOf(point.getX(), point.getY())) {
          continue;
        }

        if(cores.stream().anyMatch(c -> c.getActual().getRegion().equals(point))){
          continue;
        }

        if(myForcesSet.contains(new Forces(point, 0))) {
          isLiveEdge = false;
        } else if (board.isBarrierAt(point.getX(), point.getY())) {
          isLiveEdge = false;
        } else {
          isLiveEdge = true;
          break;
        }
      }

      if(isLiveEdge) {
        membraneForces.add(cellForce);
      }
    }
  }
}

package com.codenjoy.dojo.games.expansion.component;


import static com.codenjoy.dojo.games.expansion.Command.doNothing;

import com.codenjoy.dojo.games.expansion.Board;
import com.codenjoy.dojo.games.expansion.Command;
import com.codenjoy.dojo.games.expansion.Forces;
import com.codenjoy.dojo.games.expansion.ForcesMoves;
import com.codenjoy.dojo.games.expansion.component.cell.Cell;
import com.codenjoy.dojo.services.QDirection;
import java.util.ArrayList;
import java.util.List;

public class GreyGoo {

  protected List<Cell> cells = new ArrayList<>();

  public void reset() {
    cells = new ArrayList<>();
  }

  public Command process(Board board) {
    List<Forces> myForces = board.getMyForces();
    if (myForces.isEmpty()) return doNothing();

    //refresh start
    cells.forEach(c -> c
        .refreshPoints(myForces)
        .expandToAdjacent(board, myForces, cells)
        .rebuildParts(board, myForces));

    //init new cells
    for (Forces myForce : myForces) {
      if(cells.isEmpty()
          || cells.stream().noneMatch(c -> c.isKnown(myForce))) {
        cells.add(new Cell(myForce)
            .refreshPoints(myForces)
            .expandToAdjacent(board, myForces, cells)
            .rebuildParts(board, myForces));
      }
    }

    //refresh end




    Forces last = myForces.get(myForces.size() - 1);
    return Command
        .move(new ForcesMoves(last.getRegion(), last.getCount() -1, QDirection.LEFT))
        .increase(new Forces(last.getRegion(),  (10)))
        .build();
  }
}

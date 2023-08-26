package com.codenjoy.dojo.games.expansion.component;


import static com.codenjoy.dojo.games.expansion.Command.doNothing;

import com.codenjoy.dojo.games.expansion.Board;
import com.codenjoy.dojo.games.expansion.Command;
import com.codenjoy.dojo.games.expansion.Forces;
import com.codenjoy.dojo.games.expansion.ForcesMoves;
import com.codenjoy.dojo.games.expansion.component.cell.Cell;
import com.codenjoy.dojo.services.QDirection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class GreyGoo {
  public static final int HISTORY_CELL_SIZE = 3;
  public static final int HISTORY_FORCE_MOVE_SIZE = 3;

  protected List<Cell> cells = new ArrayList<>();

  public void reset() {
    cells = new ArrayList<>();
  }

  public Command process(Board board) {
    List<Forces> myForces = board.getMyForces();
    if (myForces.isEmpty())
      return doNothing();

    beforeDecisionRefresh(board, myForces);

    //todo delete
    Forces last = myForces.get(myForces.size() - 1);
    return Command
        .move(new ForcesMoves(last.getRegion(), last.getCount() -1, QDirection.LEFT))
        .increase(new Forces(last.getRegion(),  (10)))
        .build();
  }

  protected void beforeDecisionRefresh(Board board, List<Forces> myForces) {
    cells.forEach(c -> c
        .refreshPoints(myForces)
        .expandToAdjacent(board, myForces, cells));

    mergeAdjactentCells();

    cells.forEach(c -> c
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
  }

  protected void mergeAdjactentCells() {
    Iterator<Cell> iterator = cells.iterator();
    while (iterator.hasNext()) {
      Cell cell = iterator.next();
      if(!cell.getCellSameForcesCount().isEmpty()) {
        for (Entry<Cell, Integer> sameCellEntry : cell.getCellSameForcesCount().entrySet()) {
          //TODO merge
//          sameCellEntry.merge(cell);
          iterator.remove();
        }
      }
    }
  }
}

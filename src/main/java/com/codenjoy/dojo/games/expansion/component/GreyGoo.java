package com.codenjoy.dojo.games.expansion.component;

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


import static com.codenjoy.dojo.games.expansion.Command.doNothing;

import com.codenjoy.dojo.games.expansion.Board;
import com.codenjoy.dojo.games.expansion.Command;
import com.codenjoy.dojo.games.expansion.Forces;
import com.codenjoy.dojo.games.expansion.ForcesMoves;
import com.codenjoy.dojo.games.expansion.component.cell.Cell;
import com.codenjoy.dojo.games.expansion.component.cell.ScoutCell;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

public class GreyGoo {
  public static final boolean IS_LOG_PRINT_BOARD = false;
  public static final boolean IS_LOG_PRINT_ANSWER = true;

  //------------------------------------------------------------------------------------------------

  public static final int HISTORY_CELL_SIZE = 3;
  public static final int HISTORY_FORCE_MOVE_SIZE = 3;

  public static final int PATH_TO_MAX_VARIATIONS = 50;
  public static final int PATH_TO_MAX_TOLERANCE = 10;

  protected List<Cell> cells = new ArrayList<>();

  public void reset() {
    cells = new ArrayList<>();
  }

  public Command process(Board board) {
    List<Forces> myForces = board.getMyForces();
    if (myForces.isEmpty())
      return doNothing();

    initNewEmptyCell(board, myForces);
    refreshCells(board, myForces);
    killEmptyCells(board, myForces);
    initCellsFromWanderingParticles(board, myForces);

    triggerMoveCells(board, myForces);

    //todo delete
    Forces last = myForces.get(myForces.size() - 1);
    Forces increase = new Forces(last.getRegion(), (board.getIncrease()));

    return Command
        .move(collectCellForcesMoves())
        .increase(increase)
        .build();
  }

  private void killEmptyCells(Board board, List<Forces> myForces) {
    Iterator<Cell> iterator = cells.iterator();
    while (iterator.hasNext()) {
      Cell cell = iterator.next();
      if(cell.getCellForces() == null
          || cell.getCellForces().isEmpty()){
        iterator.remove();
      }
    }
  }

  private void initNewEmptyCell(Board board, List<Forces> myForces) {
    //only at round start
    //TODO add logic for cell type
    if(cells.isEmpty()) {
      cells.add(new ScoutCell(myForces.get(0)));
    }
  }

  private void initCellsFromWanderingParticles(Board board, List<Forces> myForces) {
    for (Forces myForce : myForces) {
      if(cells.stream().noneMatch(c -> c.isKnown(myForce))) {
        //TODO add logic for cell type
        Cell cell = new ScoutCell(myForce);
        refreshCell(board, myForces, cell);
        cells.add(cell);
      }
    }
  }

  protected void refreshCells(Board board, List<Forces> myForces) {
    cells.forEach(c -> refreshCell(board, myForces, c));
  }

  private Cell refreshCell(Board board, List<Forces> myForces, Cell c) {
    return c
        .deduplicatePointsAfterMoves()
        .refreshPoints(myForces)
        .deduplicatePointsAfterMoves()
        .roundRobinCollectCellPoints(board, myForces, cells);
  }

  private void triggerMoveCells(Board board, List<Forces> myForces) {
    cells.forEach(c -> c.moveForces(board));
  }

  private List<ForcesMoves> collectCellForcesMoves() {
    return cells.stream()
        .flatMap(c -> c.getCellForces().stream())
        .map(f -> f.getAndDumpMove())
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  @Deprecated
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

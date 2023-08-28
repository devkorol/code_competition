package com.codenjoy.dojo.games.expansion.component.cell;

import static org.junit.Assert.assertEquals;

import com.codenjoy.dojo.games.expansion.Board;
import com.codenjoy.dojo.games.expansion.Forces;
import com.codenjoy.dojo.services.PointImpl;
import org.junit.Before;
import org.junit.Test;

public class CellDeduplicateTest {

  private Forces base = new Forces(new PointImpl(1,5), 1);

  @Test
  public void refreshCountTest() {
    Forces f = new Forces(new PointImpl(1, 6), 1);
    a.getCellForces().add(
        f
    );
    f.getRegion().move(base.getRegion());
    a.deduplicatePointsAfterMoves();

    System.out.println(a.getCellForces());
    assertEquals(1, a.getCellForces().size());
  }

  private Cell a;

  @Before
  public void setup() {
    a = new Cell(base) {
      @Override
      public void moveForces(Board board) {

      }
    };
  }
}
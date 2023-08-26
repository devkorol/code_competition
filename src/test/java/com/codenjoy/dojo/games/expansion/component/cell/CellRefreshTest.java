package com.codenjoy.dojo.games.expansion.component.cell;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import com.codenjoy.dojo.games.expansion.Forces;
import com.codenjoy.dojo.services.PointImpl;
import org.junit.Before;
import org.junit.Test;

public class CellRefreshTest {

  private Forces base = new Forces(new PointImpl(1,5), 1);

  @Test
  public void refreshCountTest() {
    a.refreshPoints(asList(
        new Forces(new PointImpl(1,5), 10)
    ));
    assertEquals(10, a.getCellForces().stream().findFirst().get().getCount());
  }

  @Test
  public void lostForceTest() {
    a.getCellForces().add(new Forces(new PointImpl(2,5), 10));
    a.getCellForces().add(new Forces(new PointImpl(3,5), 10));

    a.refreshPoints(asList(
        base
    ));
    assertEquals(1, a.getCellForces().size());
    assertEquals(2, a.getLostForcesHistory().get(0).size());
  }

  @Test
  public void lostForceOneTestAndField() {
    Forces lost = new Forces(new PointImpl(3, 5), 10);
    a.getCellForces().add(lost);
    a.getCellForces().add(base);

    a.refreshPoints(asList(
        base
    ));
    assertEquals(1, a.getCellForces().size());
    assertEquals(lost, a.getLostForcesHistory().get(0).stream().findFirst().get());
  }

  private Cell a;

  @Before
  public void setup() {
    a = new Cell(base) {};
  }
}
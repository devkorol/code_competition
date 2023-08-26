package com.codenjoy.dojo.games.expansion.component.cell.part;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.codenjoy.dojo.games.expansion.Forces;
import com.codenjoy.dojo.games.expansion.ForcesMoves;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.QDirection;
import org.junit.Test;

public class CoreTest {

  @Test
  public void isAliveTest_rightTrue() {
    Cores a = new Cores(new Forces(new PointImpl(1, 1), 1));
    a.move = new ForcesMoves(new PointImpl(1, 1), 1, QDirection.RIGHT);

    boolean b = a.refreshAndCheckIsAlive(
        asList(
            new Forces(new PointImpl(2, 1), 1)
        ));
    assertTrue(b);
  }

  @Test
  public void isAliveTest_rightLostAndPrev() {
    Cores a = new Cores(new Forces(new PointImpl(1, 1), 1));
    a.move = new ForcesMoves(new PointImpl(1, 1), 1, QDirection.RIGHT);

    boolean b = a.refreshAndCheckIsAlive(
        asList(
            new Forces(new PointImpl(1, 1), 1)
        ));
    assertTrue(b);
  }

  @Test
  public void isAliveTest_notFound() {
    Cores a = new Cores(new Forces(new PointImpl(1, 1), 1));
    a.move = new ForcesMoves(new PointImpl(1, 1), 1, QDirection.RIGHT);

    boolean b = a.refreshAndCheckIsAlive(
        asList(
            new Forces(new PointImpl(10, 10), 1)
        ));
    assertFalse(b);
  }


  @Test
  public void isAliveTest_updateActualForce() {
    Cores a = new Cores(new Forces(new PointImpl(1, 1), 1));
    a.move = new ForcesMoves(new PointImpl(1, 1), 1, QDirection.RIGHT);

    boolean b = a.refreshAndCheckIsAlive(
        asList(
            new Forces(new PointImpl(2, 1), 10)
        ));
    assertTrue(b);
    assertEquals(10, a.actual.getCount());
  }
}
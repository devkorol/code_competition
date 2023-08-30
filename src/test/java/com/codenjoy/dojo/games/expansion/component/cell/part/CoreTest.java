package com.codenjoy.dojo.games.expansion.component.cell.part;

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

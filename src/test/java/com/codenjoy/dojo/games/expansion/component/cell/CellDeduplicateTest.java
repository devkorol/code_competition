package com.codenjoy.dojo.games.expansion.component.cell;

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

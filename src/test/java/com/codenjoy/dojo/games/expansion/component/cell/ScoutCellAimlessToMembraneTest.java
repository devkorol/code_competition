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


import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.codenjoy.dojo.games.expansion.Board;
import com.codenjoy.dojo.games.expansion.Forces;
import com.codenjoy.dojo.games.expansion.Forces.ForceType;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.QDirection;
import org.json.JSONObject;
import org.junit.Test;

public class ScoutCellAimlessToMembraneTest {

  @Test
  public void aimlessToMembraneTest_noMembranes() {
    ScoutCell cell = new ScoutCell(
        new Forces(new PointImpl(1, 1), 10).setType(ForceType.AIMLESS)
    );
    cell.collectMembraneCells();
    cell.moveAimlessToMembrane(getBoard());

    cell.getCellForces().forEach(c ->
        System.out.println(String.format("%s - %s", c, c.getForcesMoves())));
    assertNull(cell.getCellForces().stream().findFirst().get().getForcesMoves());
  }

  @Test
  public void aimlessToMembraneTest_noAimless() {
    ScoutCell cell = new ScoutCell(
        new Forces(new PointImpl(1, 1), 10).setType(ForceType.MEMBRANE)
    );
    cell.getCellForces().addAll(asList(
        new Forces(new PointImpl(1, 2), 1).setType(ForceType.MEMBRANE),
        new Forces(new PointImpl(2, 1), 1).setType(ForceType.MEMBRANE),
        new Forces(new PointImpl(2, 2), 1).setType(ForceType.MEMBRANE)
    ));
    cell.collectMembraneCells();
    cell.moveAimlessToMembrane(getBoard());


    cell.getCellForces().forEach(c -> {
        System.out.println(String.format("%s - %s", c, c.getForcesMoves()));
        assertNull(c.getForcesMoves());
    });
    assertNull(cell.getCellForces().stream().findFirst().get().getForcesMoves());
  }


  @Test
  public void aimlessToMembraneTest_aimlessOneForce() {
    ScoutCell cell = new ScoutCell(
        new Forces(new PointImpl(1, 1), 1).setType(ForceType.AIMLESS)
    );
    cell.getCellForces().addAll(asList(
        new Forces(new PointImpl(1, 2), 1).setType(ForceType.AIMLESS),
        new Forces(new PointImpl(2, 1), 1).setType(ForceType.MEMBRANE),
        new Forces(new PointImpl(2, 2), 1).setType(ForceType.MEMBRANE),
        new Forces(new PointImpl(3, 1), 1).setType(ForceType.MEMBRANE),
        new Forces(new PointImpl(3, 2), 1).setType(ForceType.MEMBRANE),
        new Forces(new PointImpl(3, 3), 1).setType(ForceType.MEMBRANE),
        new Forces(new PointImpl(2, 3), 1).setType(ForceType.MEMBRANE),
        new Forces(new PointImpl(1, 3), 1).setType(ForceType.MEMBRANE)
    ));
    cell.collectMembraneCells();
    cell.moveAimlessToMembrane(getBoard());


    cell.getCellForces().forEach(c -> {
      System.out.println(String.format("%s - %s", c, c.getForcesMoves()));
      assertNull(c.getForcesMoves());
    });
    assertNull(cell.getCellForces().stream().findFirst().get().getForcesMoves());
  }

  @Test
  public void aimlessToMembraneTest_aimless() {
    ScoutCell cell = new ScoutCell(
        new Forces(new PointImpl(1, 1), 10).setType(ForceType.AIMLESS)
    );
    cell.getCellForces().addAll(asList(
        new Forces(new PointImpl(2, 1), 1).setType(ForceType.MEMBRANE)
    ));
    cell.collectMembraneCells();
    cell.moveAimlessToMembrane(getBoard());


    cell.getCellForces().forEach(c -> {
      System.out.println(String.format("%s - %s", c, c.getForcesMoves()));
    });
    assertEquals(QDirection.RIGHT, cell.getCellForces().stream().filter(f -> f.getType() == ForceType.AIMLESS)
        .findFirst().get().getForcesMoves().getDirectionEnum());
  }

  private Board getBoard() {
    return board(
        "{'myBase':{'x':1,'y':5}," +
            "'myColor':0, 'available':10, 'inLobby':false, 'round':1, 'rounds':10, 'tick':10," +
            "'forces':'" +
            "-=#-=#-=#-=#-=#-=#-=#" +
            "-=#-=#-=#-=#-=#-=#-=#" +
            "-=#-=#-=#-=#-=#-=#-=#" +
            "-=#-=#-=#-=#-=#-=#-=#" +
            "-=#-=#-=#-=#-=#-=#-=#" +
            "-=#-=#-=#-=#-=#-=#-=#" +
            "-=#-=#-=#-=#-=#-=#-=#'" +
            "}",
        "╔═════┐" +
            "║1...$│" +
            "║.....│" +
            "║.....│" +
            "║.....│" +
            "║....F│" +
            "└─────┘",
        "-------" +
            "-------" +
            "-------" +
            "-------" +
            "-------" +
            "-------" +
            "-------");
  }

  private Board board(String json, String layer1, String layer2) {
    Board board = (Board) new Board().forString(layer1, layer2);

    board.setSource(new JSONObject(json));
    return board;
  }
}

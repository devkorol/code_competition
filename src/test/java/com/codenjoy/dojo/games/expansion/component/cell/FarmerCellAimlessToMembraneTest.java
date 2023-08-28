package com.codenjoy.dojo.games.expansion.component.cell;


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

public class FarmerCellAimlessToMembraneTest {

  @Test
  public void aimlessToMembraneTest_noMembranes() {
    FarmerCell cell = new FarmerCell(
        new Forces(new PointImpl(1, 1), 10).setType(ForceType.AIMLESS)
    );
    cell.updateMembraneCells();
    cell.aimlessToMembrane(getBoard());

    cell.getCellForces().forEach(c ->
        System.out.println(String.format("%s - %s", c, c.getForcesMoves())));
    assertNull(cell.getCellForces().stream().findFirst().get().getForcesMoves());
  }

  @Test
  public void aimlessToMembraneTest_noAimless() {
    FarmerCell cell = new FarmerCell(
        new Forces(new PointImpl(1, 1), 10).setType(ForceType.MEMBRANE)
    );
    cell.getCellForces().addAll(asList(
        new Forces(new PointImpl(1, 2), 1).setType(ForceType.MEMBRANE),
        new Forces(new PointImpl(2, 1), 1).setType(ForceType.MEMBRANE),
        new Forces(new PointImpl(2, 2), 1).setType(ForceType.MEMBRANE)
    ));
    cell.updateMembraneCells();
    cell.aimlessToMembrane(getBoard());


    cell.getCellForces().forEach(c -> {
        System.out.println(String.format("%s - %s", c, c.getForcesMoves()));
        assertNull(c.getForcesMoves());
    });
    assertNull(cell.getCellForces().stream().findFirst().get().getForcesMoves());
  }


  @Test
  public void aimlessToMembraneTest_aimlessOneForce() {
    FarmerCell cell = new FarmerCell(
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
    cell.updateMembraneCells();
    cell.aimlessToMembrane(getBoard());


    cell.getCellForces().forEach(c -> {
      System.out.println(String.format("%s - %s", c, c.getForcesMoves()));
      assertNull(c.getForcesMoves());
    });
    assertNull(cell.getCellForces().stream().findFirst().get().getForcesMoves());
  }

  @Test
  public void aimlessToMembraneTest_aimless() {
    FarmerCell cell = new FarmerCell(
        new Forces(new PointImpl(1, 1), 10).setType(ForceType.AIMLESS)
    );
    cell.getCellForces().addAll(asList(
        new Forces(new PointImpl(2, 1), 1).setType(ForceType.MEMBRANE)
    ));
    cell.updateMembraneCells();
    cell.aimlessToMembrane(getBoard());


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
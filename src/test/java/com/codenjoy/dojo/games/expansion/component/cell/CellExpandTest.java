package com.codenjoy.dojo.games.expansion.component.cell;


import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import com.codenjoy.dojo.games.expansion.Board;
import com.codenjoy.dojo.games.expansion.Forces;
import com.codenjoy.dojo.games.expansion.Forces.ForceType;
import com.codenjoy.dojo.services.PointImpl;
import java.util.Collections;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

public class CellExpandTest {

  private Forces base = new Forces(new PointImpl(1,5), 1);

  @Test
  public void expandToAdjacentTest_SamePoint() {
    a.roundRobinCollectCellPoints(getBoard(), asList(
        new Forces(new PointImpl(1,5), 1)
    ), Collections.emptyList());
    assertEquals(1, a.getCellForces().size());
  }

  @Test
  public void expandToAdjacentTest_NextPoint() {
    a.roundRobinCollectCellPoints(getBoard(), asList(
        new Forces(new PointImpl(1,5), 1),
        new Forces(new PointImpl(2,5), 1)
    ), Collections.emptyList());
    System.out.println(a.getCellForces());
    assertEquals(2, a.getCellForces().size());
  }

  @Test
  public void expandToAdjacentTest_Gap() {
    a.roundRobinCollectCellPoints(getBoard(), asList(
        new Forces(new PointImpl(1,5), 1),
        new Forces(new PointImpl(3,5), 1)
    ), Collections.emptyList());
    System.out.println(a.getCellForces());
    assertEquals(1, a.getCellForces().size());
  }

  @Test
  public void expandToAdjacentTest_Tree() {
    a.roundRobinCollectCellPoints(getBoard(), asList(
        new Forces(new PointImpl(1,5), 1),
        new Forces(new PointImpl(2,5), 1),
        new Forces(new PointImpl(3,5), 1),
        new Forces(new PointImpl(3,4), 1),
        new Forces(new PointImpl(3,3), 1),
        new Forces(new PointImpl(3,2), 1),
        new Forces(new PointImpl(3,1), 1),
        new Forces(new PointImpl(4,5), 1),
        new Forces(new PointImpl(5,5), 1)
    ), Collections.emptyList());
    System.out.println(a.getCellForces());
    assertEquals(9, a.getCellForces().size());
  }


  @Test
  public void expandToAdjacentTest_TwoCells() {
    a.roundRobinCollectCellPoints(getBoard(), asList(
        new Forces(new PointImpl(1,5), 1),
        new Forces(new PointImpl(2,5), 1),
        new Forces(new PointImpl(3,5), 1)
    ), asList(
        getCell(new Forces(new PointImpl(2, 5), 1))
    ));
    System.out.println(a.getCellForces());
    assertEquals(1, a.getCellForces().size());
  }

  private static Cell getCell(Forces forces) {
    return new Cell(
        forces
    ){};
  }

  @Test
  public void expandToAdjacentTest_TwoCellsMerge() {
    Cell sameCell = getCell(new Forces(new PointImpl(2, 5), 1));
    a.roundRobinCollectCellPoints(getBoard(), asList(
        new Forces(new PointImpl(1,5), 1),
        new Forces(new PointImpl(2,5), 1),
        new Forces(new PointImpl(3,5), 1)
    ), asList(
        sameCell
    ));
    System.out.println(a.getCellSameForcesCount());
    assertEquals(1, a.getCellForces().size());
    assertEquals(1, a.getCellSameForcesCount().size());
    assertEquals(new Integer(1), a.getCellSameForcesCount().get(sameCell));
  }


  @Test
  public void expandToAdjacentTest_NextPointHistory() {
    a.roundRobinCollectCellPoints(getBoard(), asList(
        new Forces(new PointImpl(1,5), 1),
        new Forces(new PointImpl(2,5), 1)
    ), Collections.emptyList());

    System.out.println(a.getCellForces());
    assertEquals(2, a.getCellForces().size());
    assertEquals(1, a.getNewForcesHistory().get(0).size());
  }

  @Test
  public void expandToAdjacentTest_SingleCheckTypeInTheCorner() {
    a.roundRobinCollectCellPoints(getBoard(), asList(
        new Forces(new PointImpl(1,5), 1)
    ), Collections.emptyList());

    System.out.println(base.getNearPoints());
    assertEquals(ForceType.MEMBRANE, base.getType());
    assertEquals(3, base.getNearPoints().size());
  }

  @Test
  public void expandToAdjacentTest_SingleCheckTypeInTheCornerMyAdjacent() {
    a.getCellForces().add(
        new Forces(new PointImpl(2,5), 1)
    );
    a.roundRobinCollectCellPoints(getBoard(), asList(
        new Forces(new PointImpl(1,5), 1),
        new Forces(new PointImpl(2,5), 1)
    ), Collections.emptyList());


    System.out.println(a.getCellForces());
    System.out.println(base.getNearPoints());
    assertEquals(ForceType.MEMBRANE, base.getType());
    assertEquals(2, base.getNearPoints().size());
  }



  private Cell a;

  @Before
  public void setup() {
    a = getCell(base);
  }

  private Board getBoard() {
    return board(
        "{'myBase':{'x':1,'y':5}," +
            "'myColor':0, 'available':10, 'inLobby':false, 'round':1, 'rounds':10, 'tick':10," +
            "'forces':'" +
            "-=#-=#-=#-=#-=#-=#-=#" +
            "-=#00A-=#-=#-=#-=#-=#" +
            "-=#-=#-=#-=#-=#-=#-=#" +
            "-=#00A-=#-=#-=#-=#-=#" +
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
            "-♥-----" +
            "-------" +
            "-♥-----" +
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
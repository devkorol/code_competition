package com.codenjoy.dojo.games.expansion.component.cell.part;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import com.codenjoy.dojo.games.expansion.Board;
import com.codenjoy.dojo.games.expansion.Forces;
import com.codenjoy.dojo.services.PointImpl;
import java.util.Collections;
import java.util.HashSet;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

public class MembraneTest {

  @Test
  public void rebuildTest_SingleSame() {
    a.rebuild(
        getBoard(),
        new HashSet<>(asList(
            new Forces(new PointImpl(1, 5), 1)
        )), Collections.emptyList(), asList(
            new Forces(new PointImpl(1, 5), 1)
        ));
    assertEquals(1, a.membraneForces.size());
  }


  @Test
  public void rebuildTest_SingleAroundOur() {
    a.rebuild(
        getBoard(),
        new HashSet<>(asList(
            new Forces(new PointImpl(3, 3), 1)
        )), Collections.emptyList(), asList(
            new Forces(new PointImpl(2, 2), 1),
            new Forces(new PointImpl(2, 3), 1),
            new Forces(new PointImpl(2, 4), 1),
            new Forces(new PointImpl(3, 2), 1),
            new Forces(new PointImpl(3, 4), 1),
            new Forces(new PointImpl(4, 2), 1),
            new Forces(new PointImpl(4, 3), 1),
            new Forces(new PointImpl(4, 4), 1)
        ));
    assertEquals(0, a.membraneForces.size());
  }


  @Test
  public void rebuildTest_SingleNew() {
    a.rebuild(
        getBoard(),
        new HashSet<>(asList(
            new Forces(new PointImpl(3, 3), 1)
        )), Collections.emptyList(), asList(
            new Forces(new PointImpl(3, 3), 1),
            new Forces(new PointImpl(2, 3), 1)
        ));
    assertEquals(1, a.membraneForces.size());
  }

  @Test
  public void rebuildTest_BoardBarrier() {
    a.rebuild(
        getBoard(),
        new HashSet<>(asList(
            new Forces(new PointImpl(2, 4), 1)
        )), Collections.emptyList(), asList(
            new Forces(new PointImpl(2, 4), 1)
        ));
    assertEquals(1, a.membraneForces.size());
  }

  @Test
  public void rebuildTest_SingleCore() {
    a.rebuild(
        getBoard(),
        new HashSet<>(asList(
            new Forces(new PointImpl(1, 5), 1)
        )), asList(
            new Cores(new Forces(new PointImpl(1, 5), 1))
        ), asList(
            new Forces(new PointImpl(1, 5), 1)
        ));
    assertEquals(0, a.membraneForces.size());
  }


  private Membrane a;

  @Before
  public void setup() {
    a = new Membrane();
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
            "║O ...│" +
            "║.-..F│" +
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
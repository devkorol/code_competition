package com.codenjoy.dojo.games.expansion.component.path;

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
import static org.junit.Assert.assertTrue;

import com.codenjoy.dojo.games.expansion.Board;
import com.codenjoy.dojo.games.expansion.component.path.ASearch.Node;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.QDirection;
import org.json.JSONObject;
import org.junit.Test;

public class ASearchTest {



  @Test
  public void distanceTest() {
    double distance = ASearch.distance(
        new PointImpl(1, 10),
        new PointImpl(1, 5)
    );
    System.out.println(distance);
    assertTrue(new Double(5.0d).equals(distance));
  }


  @Test
  public void nodeCompareTest() {
    Node n1 = new Node(null, null, 1, 2, QDirection.RIGHT);
    Node n2 = new Node(null, null, 1, 10, QDirection.LEFT);
    int compare = n1.compareTo(n2);

    System.out.println(compare);
    assertEquals(-1, compare);
  }


  @Test
  public void pathTest_Straight() {
    PointImpl start = new PointImpl(1, 1);
    PointImpl target = new PointImpl(2, 1);
    Node path = ASearch.path(
        start,
        target,
        getBoard()
    );
    System.out.println(path);
    assertEquals(start, path.getPoint());
    assertEquals(target, path.getNext().getPoint());
  }

  @Test
  public void pathTest_Diagonal() {
    PointImpl start = new PointImpl(2, 2);
    PointImpl target = new PointImpl(3, 4);
    Node path = ASearch.path(
        start,
        target,
        getBoard()
    );
    System.out.println(path);
    assertEquals(new PointImpl(3, 3), path.getNext().getPoint());
  }

  @Test
  public void pathTest_Complex() {
    Node path = ASearch.path(
        new PointImpl(1, 1),
        new PointImpl(5, 1),
        getBoard()
    );
    System.out.println(path);
    assertEquals(5, path.getSteps());
  }


  @Test
  public void pathTest_StackOverflow() {
    Node path = ASearch.path(
        new PointImpl(2, 4),
        new PointImpl(1, 1),
        board(
            "{\"myColor\":3,\"round\":43,\"offset\":{\"x\":0,\"y\":0},\"myBase\":{\"x\":2,\"y\":4},\"forces\":\"-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#00A-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#00A-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#00A-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#00A-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\",\"layers\":[\"╔══════════════════┐║$......$..$......$│║BBBBB........BBBBB│║....BB......BB....│║.1..B.B....B.B..2.│║....BB.B..B.BB....│║....B.BBBBBB.B....│║....B.B....B.B....│║..................│║..................│║..................│║..................│║....B.B....B.B....│║....B.BBBBBB.B....│║....BB.B..B.BB....│║.4..B.B....B.B..3.│║....BB......BB....│║BBBBB........BBBBB│║$......$..$......$│└──────────────────┘\",\"----------------------------------------------------------------------------------♥--------------♦------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------♠--------------♣----------------------------------------------------------------------------------\"],\"available\":10,\"tick\":43,\"rounds\":600}",
            "╔══════════════════┐║$......$..$......$│║BBBBB........BBBBB│║....BB......BB....│║.1..B.B....B.B..2.│║....BB.B..B.BB....│║....B.BBBBBB.B....│║....B.B....B.B....│║..................│║..................│║..................│║..................│║....B.B....B.B....│║....B.BBBBBB.B....│║....BB.B..B.BB....│║.4..B.B....B.B..3.│║....BB......BB....│║BBBBB........BBBBB│║$......$..$......$│└──────────────────┘",
            "----------------------------------------------------------------------------------♥--------------♦------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------♠--------------♣----------------------------------------------------------------------------------"
        )
    );
    System.out.println(path);
    assertEquals(16, path.getSteps());
  }

  private Board getBoard(){
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
            "║....$│" +
            "║.....│" +
            "║.B.B.│" +
            "║O.O..│" +
            "║..O..│" +
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

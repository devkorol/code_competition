package com.codenjoy.dojo.games.expansion.component;

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
import com.codenjoy.dojo.games.expansion.Command;
import com.codenjoy.dojo.games.expansion.Forces;
import com.codenjoy.dojo.games.expansion.component.cell.Cell;
import com.codenjoy.dojo.services.PointImpl;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

public class GreyGooCellTest {

  @Test
  public void newCellTest() {
    Command process = ai.process(board(
        "{'myBase':{'x':1,'y':5}," +
            "'myColor':0," +
            "'available':10," +
            "'inLobby':true," +
            "'round':1," +
            "'rounds':10," +
            "'tick':10," +
            "'forces':'" +
            "-=#-=#-=#-=#-=#-=#-=#" +
            "-=#00A-=#-=#-=#-=#-=#" +
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
            "-♥-----" +
            "-------" +
            "-------" +
            "-------" +
            "-------" +
            "-------"));
    assertEquals(1, ai.cells.size());

  }

  @Test
  public void twoCellTest() {
    Command process = ai.process(board(
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
            "-------"));
    System.out.println(ai.cells);
    assertEquals(2, ai.cells.size());

  }

  @Test
  public void existCellTest() {
    ai.cells.add(
        new Cell(
            new Forces(new PointImpl(1, 5), 1)){
          @Override
          public void moveForces(Board board) {

          }
        });
    Command process = ai.process(board(
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
            "-------"));
    System.out.println(ai.cells);
    assertEquals(2, ai.cells.size());

  }

  private GreyGoo ai;

  @Before
  public void setup() {
    ai = new GreyGoo();
  }

  private Board board(String json, String layer1, String layer2) {
    Board board = (Board) new Board().forString(layer1, layer2);

    board.setSource(new JSONObject(json));
    return board;
  }

}

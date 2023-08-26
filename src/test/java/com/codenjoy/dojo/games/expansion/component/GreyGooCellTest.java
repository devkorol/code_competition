package com.codenjoy.dojo.games.expansion.component;


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
            new Forces(new PointImpl(1, 5), 1)){});
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
package com.codenjoy.dojo.games.expansion.component.path;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.codenjoy.dojo.games.expansion.Board;
import com.codenjoy.dojo.games.expansion.Element;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import java.util.TreeSet;
import org.apache.commons.collections4.CollectionUtils;
import org.json.JSONObject;
import org.junit.Test;

public class PlainDistanceTest  {



  @Test
  public void distanceTest() {
    TreeSet<Point> elementsByDistance = PlainDistance.getElementsByDistance(
        new PointImpl(1, 5),
        getBoard(),
        Element.GOLD
    );
    System.out.println(elementsByDistance);
    assertEquals(new PointImpl(5,5), elementsByDistance.first());
  }

  @Test
  public void distanceTest_Holes() {
    TreeSet<Point> elementsByDistance = PlainDistance.getElementsByDistance(
        new PointImpl(1, 5),
        getBoard(),
        Element.HOLE
    );

    System.out.println(elementsByDistance);
    assertTrue(CollectionUtils.isEqualCollection(
        asList(
            new PointImpl(1,2),
            new PointImpl(3,2),
            new PointImpl(3,1)
        ), elementsByDistance
    ));
  }

  @Test
  public void distanceTest_HolesAndCold() {
    TreeSet<Point> elementsByDistance = PlainDistance.getElementsByDistance(
        new PointImpl(1, 5),
        getBoard(),
        Element.HOLE,
        Element.GOLD
    );

    System.out.println(elementsByDistance);
    assertTrue(CollectionUtils.isEqualCollection(
        asList(
            new PointImpl(1,2),
            new PointImpl(3,2),
            new PointImpl(3,1),
            new PointImpl(5,5)
        ), elementsByDistance
    ));
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
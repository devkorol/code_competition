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
        p -> true,
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
        p -> true,
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
        p -> true,
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

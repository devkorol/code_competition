package com.codenjoy.dojo.games.expansion;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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
import static org.mockito.Mockito.mock;

import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.services.Dice;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class DoNothingTest {

    private Solver ai;

    @Before
    public void setup() {
        ai = new YourSolver(mock(Dice.class));
    }

    private Board board(String json, String layer1, String layer2) {
        Board board = (Board) new Board().forString(layer1, layer2);
        board.setSource(new JSONObject(json));
        return board;
    }

    @Test
    @Ignore
    public void inLobbyTest() {
        assertL(
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
                "-------",
            Command.doNothing());

    }

    @Test
    public void gameOver() {
        assertL(
            "{'myBase':{'x':1,'y':5}," +
                "'myColor':0," +
                "'available':10," +
                "'inLobby':false," +
                "'round':1," +
                "'rounds':10," +
                "'tick':10," +
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
                "-♥-----" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------",
            Command.doNothing());

    }

    private void assertL(String json, String layer1, String layer2, Command expected) {
        String actual = ai.get(board(json, layer1, layer2));
        assertEquals(String.format("message('%s')", expected.toString()), actual);
    }
}
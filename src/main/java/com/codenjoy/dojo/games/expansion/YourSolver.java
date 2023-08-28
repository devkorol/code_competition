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


import static com.codenjoy.dojo.games.expansion.Command.doNothing;

import com.codenjoy.dojo.games.expansion.component.GreyGoo;
import com.codenjoy.dojo.services.Dice;

/**
 * Author: your name
 *
 * This is your AI algorithm for the game.
 * Implement it at your own discretion.
 * Pay attention to {@link YourSolverTest} - there is
 * a test framework for you.
 */

public class YourSolver extends AbstractSolver {

    private Dice dice;

    public YourSolver(Dice dice) {
        this.dice = dice;
        greyGoo = new GreyGoo();
    }

    public YourSolver() {
        greyGoo = new GreyGoo();
    }

    private GreyGoo greyGoo;
    private int lastTick = 0;

    /**
     * @param board use it for find elements on board
     * @return what hero should do in this tick (for this board)
     */
    @Override
    public Command whatToDo(Board board) {
        try {
            if (board.isGameOver()
//                || board.isInLobby()
            ) return doNothing();

            if(lastTick > board.getTick()) {
                greyGoo.reset();
            }
            lastTick = board.getTick();

            return greyGoo.process(board);
        } catch (Exception e) {
            System.out.println("MAIN ERROR");
            e.printStackTrace();
            System.out.println(board.getSource());
            return Command.doNothing();
        }
    }

    @Override
    protected String buildAnswer(Board board) {
        return String.format("message('%s')", super.buildAnswer(board));
    }

}
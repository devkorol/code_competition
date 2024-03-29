package com.codenjoy.dojo.services;

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


import static com.codenjoy.dojo.services.PointImpl.pt;

import com.codenjoy.dojo.services.dice.RandomDice;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * Реализует возможные направления движения чего либо
 */
public enum Direction {

    LEFT(0, -1, 0),
    RIGHT(1, 1, 0),
    UP(2, 0, 1),
    DOWN(3, 0, -1),
    ACT(4, 0, 0), STOP(5, 0, 0);

    private static final List<Direction> values = Arrays.asList(LEFT, RIGHT, UP, DOWN);

    private final int value;
    private final int dx;
    private final int dy;

    Direction(int value, int dx, int dy) {
        this.value = value;
        this.dx = dx;
        this.dy = dy;
    }

    public static List<Direction> getValues() {
        return values;
    }

    public static Direction valueOf(int index) {
        for (Direction d : Direction.values()) {
            if (d.value == index) {
                return d;
            }
        }
        throw new IllegalArgumentException("No such Direction for: " + index);
    }

    /**
     * @param string any string
     * @return true if this is valid direction -
     * one of: LEFT, RIGHT, UP, DOWN, ACT, STOP; in any case
     */
    public static boolean isValid(String string) {
        try {
            Direction.valueOf(string.toUpperCase());
            return true;
        } catch (NullPointerException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * @param x Given point.x.
     * @return New point.x that will be after move from current point.x in given direction.
     */
    public int changeX(int x) {
        return x + dx;
    }

    /**
     * @param y Given point.y
     * @return New point.y that will be after move from current point.y in given direction.
     */
    public int changeY(int y) {
        return y + dy;
    }

    /**
     * @param point Current point.
     * @return New point that will be after move from current point in given direction.
     */
    public Point change(Point point) {
        return pt(changeX(point.getX()),
                changeY(point.getY()));
    }

    /**
     * @return Value of this direction.
     */
    public int value() {
        return value;
    }

    public String toString() {
        return this.name();
    }

    /**
     * @return Inverted direction. Inverts UP to DOWN, RIGHT to LEFT, etc.
     */
    public Direction inverted() {
        switch (this) {
            case UP : return DOWN;
            case DOWN : return UP;
            case LEFT : return RIGHT;
            case RIGHT : return LEFT;
        }
        throw new IllegalArgumentException("Cant invert for: " + this);
    }

    /**
     * @return Random direction.
     */
    public static Direction random() {
        return random(new RandomDice());
    }

    /**
     * @param dice Given dice.
     * @return Random direction for given dice.
     */
    public static Direction random(Dice dice) {
        return Direction.valueOf(dice.next(4));
    }

    /**
     * @return Next clockwise direction. LEFT -> UP -> RIGHT -> DOWN -> LEFT.
     */
    public Direction clockwise() {
        switch (this) {
            case LEFT: return UP;
            case UP: return RIGHT;
            case RIGHT: return DOWN;
            case DOWN: return LEFT;
        }
        throw new IllegalArgumentException("Cant clockwise for: " + this);
    }

    /**
     * @return Next counter clockwise direction. LEFT -> DOWN -> RIGHT -> UP -> LEFT.
     */
    public Direction counterClockwise() {
        switch (this) {
            case LEFT: return DOWN;
            case DOWN: return RIGHT;
            case RIGHT: return UP;
            case UP: return LEFT;
        }
        throw new IllegalArgumentException("Cant counter clockwise for: " + this);
    }

    /**
     * @param params Given act parameters.
     * @return ACT with parameters
     */
    public static String ACT(int... params) {
        String string = Arrays.toString(params).replaceAll("[\\[\\] ]", "");
        return ACT +
                ((StringUtils.isEmpty(string)) ? "" : "(" + string + ")");
    }

    /**
     * @param before true if direction should be before.
     * @param params Given act parameters.
     * @return ACT with current Direction.
     */
    public String ACT(boolean before, int... params) {
        String act = Direction.ACT(params);
        return before
                ? act + "," + this
                : this + "," + act;
    }
}

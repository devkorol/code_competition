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


import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Encapsulates coordinate of Element at the Board. All Board elements must extends from PointImpl,
 * realized from of this interface.
 */
public interface Point extends Comparable<Point> {

    /**
     * @return Current X coordinate.
     */
    int getX();

    /**
     * @return Current Y coordinate.
     */
    int getY();

    /**
     * Change X and Y coordinates with new values.
     */
    void move(int x, int y);

    /**
     * Change X and Y coordinates with new values.
     */
    void move(Point pt);

    /**
     * @return cloned object.
     */
    Point copy();

    void setX(int x);

    void setY(int y);

    void beforeChange(Consumer<Point> beforeChange);

    /**
     * @return true if points has equals X and Y coordinates.
     */
    boolean itsMe(Point point);

    /**
     * @return true if points has equals X and Y coordinates.
     */
    boolean itsMe(int x, int y);

    /**
     * @param size Board size.
     * @return true if point is out of board with given size.
     */
    boolean isOutOf(int size);

    /**
     * @param dw Border inside board.
     * @param dh Border inside board.
     * @param size Board size.
     * @return true if point is out of rectangle inside board.
     */
    boolean isOutOfExclude(int dw, int dh, int size);

    static boolean isOutOfExclude(int x, int y, int dw, int dh, int size) {
        return x < dw
                || y < dh
                || y > size - 1 - dh
                || x > size - 1 - dw;
    }

    static boolean isOutOf(int x, int y, int size) {
        return isOutOfExclude(x, y, 0, 0, size);
    }

    /**
     * @param point2 Another point.
     * @return Distance between two points.
     */
    double distance(Point point2);

    /**
     * Changes current point with given delta.
     * @param delta Increment.
     */
    void moveDelta(Point delta);

    /**
     * Changes current point in given direction
     * @param direction one of 8 directions
     */
    Point move(QDirection direction);

    /**
     * Changes current point in given direction
     * @param direction one of 4 directions
     */
    void move(Direction direction);

    /**
     * @param offset
     * @return this.x - offset.x, this.y - offset.y
     */
    Point relative(Point offset);

    /**
     * @param to destination point
     * @return Direction from current position to given point.
     * Returns Null if points are not neighbours.
     */
    QDirection direction(Point to);

    void onChange(BiConsumer<Point, Point> onChange);

    default Stream<Integer> stream() {
        return Arrays.stream(new Integer[]{getX(), getY()});
    }

    static boolean equals(Object o1, Object o2) {
        if (o1 == o2) {
            return true;
        }

        try {
            Point pt1 = (Point) o1;
            Point pt2 = (Point) o2;

            return pt1.itsMe(pt2);
        } catch (Exception exception) {
            return false;
        }
    }
}

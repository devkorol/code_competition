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

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.json.JSONObject;

/**
 * Каждый объект на поле имеет свои координаты. Этот класс обычно используется дял указания координат или как родитель.
 * Может использоваться в коллекциях.
 */
public class PointImpl implements Point, Comparable<Point> {

    private Consumer<Point> beforeChange;
    private BiConsumer<Point, Point> onChange;

    protected int x;
    protected int y;

    public PointImpl(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public PointImpl() {
        this(-1, -1);
    }

    public PointImpl(Point pt) {
        this(pt.getX(), pt.getY());
    }

    public PointImpl(JSONObject json) {
        this(json.getInt("x"), json.getInt("y"));
    }

    @Override
    public void onChange(BiConsumer<Point, Point> onChange) {
        this.onChange = onChange;
    }

    @Override
    public void beforeChange(Consumer<Point> beforeChange) {
        this.beforeChange = beforeChange;
    }

    private void fireBeforeChange() {
        if (beforeChange != null) {
            beforeChange.accept(this);
        }
    }

    private void fireOnChange(Point from) {
        if (onChange != null) {
            onChange.accept(from, this);
        }
    }

    @Override
    public boolean itsMe(Point pt) {
        return itsMe(pt.getX(), pt.getY());
    }

    public boolean itsMe(int x, int y) {
        return this.x == x && this.y == y;
    }

    @Override
    public boolean isOutOf(int size) {
        return Point.isOutOf(x, y, size);
    }

    @Override
    public boolean isOutOfExclude(int dw, int dh, int size) {
        return Point.isOutOfExclude(x, y, dw, dh, size);
    }

    @Override
    public double distance(Point other) {
        int dx = x - other.getX();
        int dy = y - other.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public int hashCode() {
        return x*1000 + y;
    }

    public int parentHashCode() {
        return super.hashCode();
    }

    public boolean parentEquals(Object o) {
        return super.equals(o);
    }

    @Override
    public String toString() {
        return String.format("[%s,%s]", x, y);
    }

    @Override
    public boolean equals(Object o) {
        return Point.equals(this, o);
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setX(int x) {
        Point old = this.copy();

        fireBeforeChange();

        this.x = x;

        fireOnChange(old);
    }

    @Override
    public void setY(int y) {
        Point old = this.copy();

        fireBeforeChange();

        this.y = y;

        fireOnChange(old);
    }

    @Override
    public void move(int x, int y) {
        Point from = this.copy();

        fireBeforeChange();

        this.x = x;
        this.y = y;

        fireOnChange(from);
    }

    @Override
    public void move(Point pt) {
        move(pt.getX(), pt.getY());
    }

    @Override
    public Point copy() {
        return new PointImpl(this);
    }

    @Override
    public void moveDelta(Point delta) {
        move(x + delta.getX(),
            y + delta.getY());
    }

    @Override
    public Point move(QDirection direction) {
        this.move(direction.change(this));
        return this;
    }

    @Override
    public void move(Direction direction) {
        this.move(direction.change(this));
    }

    public static Point pt(int x, int y) {
        return new PointImpl(x, y);
    }

    @Override
    public int compareTo(Point o) {
        if (o == null) {
            return -1;
        }
        return Integer.compare(this.hashCode(), o.hashCode());
    }

    @Override
    public Point relative(Point offset) {
        return pt(x - offset.getX(), y - offset.getY());
    }

    @Override
    public Direction direction(Point to) {
        return Direction.getValues().stream()
                .filter(direction -> direction.change(this).itsMe(to))
                .findFirst()
                .orElse(null);
    }

    public static Point random(Dice dice, int size) {
        return pt(dice.next(size), dice.next(size));
    }
}

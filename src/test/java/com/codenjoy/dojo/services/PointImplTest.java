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


import static com.codenjoy.dojo.services.Direction.DOWN;
import static com.codenjoy.dojo.services.Direction.LEFT;
import static com.codenjoy.dojo.services.Direction.RIGHT;
import static com.codenjoy.dojo.services.Direction.UP;
import static com.codenjoy.dojo.services.PointImpl.pt;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.codenjoy.dojo.services.dice.MockDice;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class PointImplTest {

    private List<String> status;

    @Before
    public void setup() {
        status = new LinkedList<>();
    }

    @Test
    public void shouldSaveXY() {
        // given
        Point pt = pt(10, 12);

        // when then
        assertEquals(10, pt.getX());
        assertEquals(12, pt.getY());
    }

    @Test
    public void shouldSaveXY_staticMethod() {
        // given
        Point pt = pt(10, 12);

        // when then
        assertEquals(10, pt.getX());
        assertEquals(12, pt.getY());
    }

    @Test
    public void shouldPrintToString() {
        // given
        Point pt = pt(10, 12);

        // when then
        assertEquals("[10,12]", pt.toString());
    }

    @Test
    public void shouldCopyConstructor() {
        // given
        Point source = pt(10, 12);

        // when
        Point pt = new PointImpl(source);

        // then
        assertEquals("[10,12]", pt.toString());
    }

    @Test
    public void shouldItsMe() {
        // given
        Point pt = pt(10, 12);

        // when then
        assertTrue(pt.itsMe(10, 12));
        assertFalse(pt.itsMe(10 + 1, 12));
        assertFalse(pt.itsMe(10, 12 + 1));

        assertTrue(pt.itsMe(pt(10, 12)));
        assertFalse(pt.itsMe(pt(10 + 1, 12)));
        assertFalse(pt.itsMe(pt(10, 12 + 1)));
    }

    @Test
    public void shouldIsOutOf_byY() {
        // given
        Point pt = pt(10, 12);

        // when then
        assertTrue(pt.isOutOf(9));
        assertTrue(pt.isOutOf(10));
        assertTrue(pt.isOutOf(11));
        assertTrue(pt.isOutOf(12));
        assertFalse(pt.isOutOf(13));
        assertFalse(pt.isOutOf(14));
    }

    @Test
    public void shouldIsOutOf_byX() {
        // given
        Point pt = pt(12, 10);

        // when then
        assertTrue(pt.isOutOf(9));
        assertTrue(pt.isOutOf(10));
        assertTrue(pt.isOutOf(11));
        assertTrue(pt.isOutOf(12));
        assertFalse(pt.isOutOf(13));
        assertFalse(pt.isOutOf(14));
    }

    @Test
    public void shouldIsOutOf_byYNegative() {
        // given
        Point pt = pt(10, -12);

        // when then
        assertTrue(pt.isOutOf(14));
    }

    @Test
    public void shouldIsOutOf_byXNegative() {
        // given
        Point pt = pt(-12, 10);

        // when then
        assertTrue(pt.isOutOf(14));
    }

    @Test
    public void shouldIsOutOfDelta_from0() {
        // given
        Point pt = pt(1, 5);

        // when then
        assertFalse(pt.isOutOfExclude(0, 0, 20));
        assertFalse(pt.isOutOfExclude(1, 0, 20));
        assertTrue(pt.isOutOfExclude(2, 0, 20));
        assertTrue(pt.isOutOfExclude(3, 0, 20));

        assertFalse(pt.isOutOfExclude(0, 0, 20));
        assertFalse(pt.isOutOfExclude(0, 1, 20));
        assertFalse(pt.isOutOfExclude(0, 2, 20));
        assertFalse(pt.isOutOfExclude(0, 3, 20));
        assertFalse(pt.isOutOfExclude(0, 4, 20));
        assertFalse(pt.isOutOfExclude(0, 5, 20));
        assertTrue(pt.isOutOfExclude(0, 6, 20));
        assertTrue(pt.isOutOfExclude(0, 7, 20));
    }

    @Test
    public void shouldIsOutOfDelta_fromSize() {
        // given
        Point pt = pt(10, 15);

        // when then
        assertFalse(pt.isOutOfExclude(0, 0, 20));
        assertFalse(pt.isOutOfExclude(0, 1, 20));
        assertFalse(pt.isOutOfExclude(0, 2, 20));
        assertFalse(pt.isOutOfExclude(0, 3, 20));
        assertFalse(pt.isOutOfExclude(0, 4, 20));
        assertTrue(pt.isOutOfExclude(0, 5, 20));
        assertTrue(pt.isOutOfExclude(0, 6, 20));
        assertTrue(pt.isOutOfExclude(0, 7, 20));

        assertFalse(pt.isOutOfExclude(8, 0, 20));
        assertFalse(pt.isOutOfExclude(9, 0, 20));
        assertTrue(pt.isOutOfExclude(10, 0, 20));
        assertTrue(pt.isOutOfExclude(11, 0, 20));
    }

    @Test
    public void shouldIsOutOfDelta_from0_staticMethod() {
        // given
        int x = 1;
        int y = 5;

        // when then
        assertFalse(Point.isOutOfExclude(x, y, 0, 0, 20));
        assertFalse(Point.isOutOfExclude(x, y, 1, 0, 20));
        assertTrue(Point.isOutOfExclude(x, y, 2, 0, 20));
        assertTrue(Point.isOutOfExclude(x, y, 3, 0, 20));

        assertFalse(Point.isOutOfExclude(x, y, 0, 0, 20));
        assertFalse(Point.isOutOfExclude(x, y, 0, 1, 20));
        assertFalse(Point.isOutOfExclude(x, y, 0, 2, 20));
        assertFalse(Point.isOutOfExclude(x, y, 0, 3, 20));
        assertFalse(Point.isOutOfExclude(x, y, 0, 4, 20));
        assertFalse(Point.isOutOfExclude(x, y, 0, 5, 20));
        assertTrue(Point.isOutOfExclude(x, y, 0, 6, 20));
        assertTrue(Point.isOutOfExclude(x, y, 0, 7, 20));
    }

    @Test
    public void shouldIsOutOfDelta_fromSize_staticMethod() {
        // given
        int x = 10;
        int y = 15;

        // when then
        assertFalse(Point.isOutOfExclude(x, y, 0, 0, 20));
        assertFalse(Point.isOutOfExclude(x, y, 0, 1, 20));
        assertFalse(Point.isOutOfExclude(x, y, 0, 2, 20));
        assertFalse(Point.isOutOfExclude(x, y, 0, 3, 20));
        assertFalse(Point.isOutOfExclude(x, y, 0, 4, 20));
        assertTrue(Point.isOutOfExclude(x, y, 0, 5, 20));
        assertTrue(Point.isOutOfExclude(x, y, 0, 6, 20));
        assertTrue(Point.isOutOfExclude(x, y, 0, 7, 20));

        assertFalse(Point.isOutOfExclude(x, y, 8, 0, 20));
        assertFalse(Point.isOutOfExclude(x, y, 9, 0, 20));
        assertTrue(Point.isOutOfExclude(x, y, 10, 0, 20));
        assertTrue(Point.isOutOfExclude(x, y, 11, 0, 20));
    }

    @Test
    public void shouldDistance() {
        // given
        Point pt = pt(10, 15);

        // when then
        assertEquals(0.0, pt.distance(pt(10, 15)), 0.001);
        assertEquals(11.180339887498949, pt.distance(pt(20, 20)), 0.001);
    }

    @Test
    public void shouldEqualsAndHashCode() {
        // given
        Point pt = pt(10, 15);

        // when then
        assertTrue(pt.equals(pt));
        assertTrue(pt.equals(pt(10, 15)));
        assertFalse(pt.equals(pt(10 + 1, 15)));
        assertFalse(pt.equals(pt(10, 15 + 1)));
        assertFalse(pt.equals(null));
        assertFalse(pt.equals(new Object()));

        assertEquals(pt.hashCode(), pt(10, 15).hashCode());
        assertNotSame(pt.hashCode(), pt(10 + 1, 15).hashCode());
        assertNotSame(pt.hashCode(), pt(10, 15 + 1).hashCode());
    }

    @Test
    public void shouldMove() {
        // given
        Point pt = pt(10, 15);

        // when
        pt.move(pt(20, 23));

        // then
        assertEquals("[20,23]", pt.toString());

        // when
        pt.move(40, 43);

        // then
        assertEquals("[40,43]", pt.toString());
    }

    @Test
    public void shouldMove_onChange() {
        // given
        Point pt = pt(10, 15);

        setupOnChange(pt, 1);

        // when
        pt.move(pt(20, 23));

        // then
        assertStatus(
                "before(1): [10,15]\n" +
                "after(1):  [10,15] -> [20,23]");
    }

    private void assertStatus(String expected) {
        assertEquals(expected, status.stream().collect(joining("\n")));
    }

    private void setupOnChange(Point pt, int id) {
        pt.onChange((from, to) ->
                status.add(String.format("after(%s):  %s -> %s", id, from, to)));

        pt.beforeChange(from ->
                status.add(String.format("before(%s): %s", id, from)));
    }

    @Test
    public void shouldOnlyLastOnChangeWillProcessed() {
        // given
        Point pt = pt(10, 15);

        setupOnChange(pt, 1); // ignored
        setupOnChange(pt, 2); // ignored
        setupOnChange(pt, 3); // active

        // when
        pt.move(pt(20, 23));

        // then
        assertStatus(
                "before(3): [10,15]\n" +
                "after(3):  [10,15] -> [20,23]");
    }

    @Test
    public void shouldMoveDirection() {
        // given
        Point pt = pt(10, 15);

        // when then
        pt.move(UP);
        assertEquals("[10,16]", pt.toString());

        // when then
        pt.move(DOWN);
        assertEquals("[10,15]", pt.toString());

        // when then
        pt.move(Direction.LEFT);
        assertEquals("[9,15]", pt.toString());

        // when then
        pt.move(RIGHT);
        assertEquals("[10,15]", pt.toString());
    }

    @Test
    public void shouldMoveDirection_onChange() {
        // given
        Point pt = pt(10, 15);

        setupOnChange(pt, 1);

        // when
        pt.move(UP);

        // then
        assertStatus(
                "before(1): [10,15]\n" +
                "after(1):  [10,15] -> [10,16]");
    }

    @Test
    public void shouldMoveQDirection() {
        // given
        Point pt = pt(10, 15);

        // when then
        pt.move(QDirection.UP);
        assertEquals("[10,16]", pt.toString());

        // when then
        pt.move(QDirection.DOWN);
        assertEquals("[10,15]", pt.toString());

        // when then
        pt.move(QDirection.LEFT);
        assertEquals("[9,15]", pt.toString());

        // when then
        pt.move(QDirection.RIGHT);
        assertEquals("[10,15]", pt.toString());

        // when then
        pt.move(QDirection.RIGHT_UP);
        assertEquals("[11,16]", pt.toString());

        // when then
        pt.move(QDirection.LEFT_DOWN);
        assertEquals("[10,15]", pt.toString());

        // when then
        pt.move(QDirection.RIGHT_DOWN);
        assertEquals("[11,14]", pt.toString());

        // when then
        pt.move(QDirection.LEFT_UP);
        assertEquals("[10,15]", pt.toString());
    }

    @Test
    public void shouldMoveQDirection_onChange() {
        // given
        Point pt = pt(10, 15);

        setupOnChange(pt, 1);

        // when
        pt.move(QDirection.LEFT_DOWN);

        // then
        assertStatus(
                "before(1): [10,15]\n" +
                "after(1):  [10,15] -> [9,14]");
    }

    @Test
    public void shouldDefaultConstructor() {
        // given when
        Point pt = new PointImpl();

        // then
        assertEquals("[-1,-1]", pt.toString());
    }

    @Test
    public void shouldSet() {
        // given
        Point pt = pt(10, 15);

        // when
        pt.setX(20);
        pt.setY(23);

        // then
        assertEquals("[20,23]", pt.toString());

        // when
        pt.setX(40);
        pt.setY(43);

        // then
        assertEquals("[40,43]", pt.toString());
    }

    @Test
    public void shouldSetX_onChange() {
        // given
        Point pt = pt(10, 15);

        setupOnChange(pt, 1);

        // when
        pt.setX(20);

        // then
        assertStatus(
                "before(1): [10,15]\n" +
                "after(1):  [10,15] -> [20,15]");
    }

    @Test
    public void shouldSetY_onChange() {
        // given
        Point pt = pt(10, 15);

        setupOnChange(pt, 1);

        // when
        pt.setY(20);

        // then
        assertStatus(
                "before(1): [10,15]\n" +
                "after(1):  [10,15] -> [10,20]");
    }

    @Test
    public void shouldCopy() {
        // given
        Point pt = pt(10, 15);

        // when then
        Point pt2 = pt.copy();
        pt.move(1, 2);

        // then
        assertEquals("[1,2]", pt.toString());
        assertEquals("[10,15]", pt2.toString());
    }

    @Test
    public void shouldMoveDelta() {
        // given
        Point pt = pt(10, 15);

        // when
        pt.moveDelta(pt(12, -23));

        // then
        assertEquals("[22,-8]", pt.toString());
    }

    @Test
    public void shouldMoveDelta_onChange() {
        // given
        Point pt = pt(10, 15);

        setupOnChange(pt, 1);

        // when
        pt.moveDelta(pt(12, -23));

        // then
        assertStatus(
                "before(1): [10,15]\n" +
                "after(1):  [10,15] -> [22,-8]");
    }

    @Test
    public void shouldCompareTo() {
        // given
        Point pt = pt(10, 15);

        // when then
        assertEquals(1, pt.compareTo(pt(10, 14)));
        assertEquals(0, pt.compareTo(pt(10, 15)));
        assertEquals(-1, pt.compareTo(pt(10, 16)));

        assertEquals(1, pt.compareTo(pt(9, 15)));
        assertEquals(0, pt.compareTo(pt(10, 15)));
        assertEquals(-1, pt.compareTo(pt(11, 15)));

        assertEquals(1, pt.compareTo(pt(9, 100)));
        assertEquals(0, pt.compareTo(pt(10, 15)));
        assertEquals(-1, pt.compareTo(pt(11, 1)));

        assertEquals(-1, pt.compareTo(null));
    }

    @Test
    public void shouldRelative() {
        // given
        Point pt = pt(10, 15);

        // when then
        assertEquals("[9,14]", pt.relative(pt(1, 1)).toString());
        assertEquals("[8,14]", pt.relative(pt(2, 1)).toString());
        assertEquals("[9,13]", pt.relative(pt(1, 2)).toString());
        assertEquals("[11,16]", pt.relative(pt(-1, -1)).toString());
        assertEquals("[10,15]", pt.relative(pt(0, 0)).toString());
        assertEquals("[-30,15]", pt.relative(pt(40, 0)).toString());
    }

    @Test
    public void shouldGenerateRandom() {
        // given
        MockDice dice = spy(new MockDice());
        dice.then(100, 101);

        // when
        int size = 24;
        Point pt = PointImpl.random(dice, size);

        // then
        verify(dice, times(2)).next(size);
        assertEquals("[100,101]", pt.toString());
    }

    @Test
    public void equalsPerformanceTest() {
        // given
        int size = 1000;
        int count = 10000;
        final int[] index = {0};
        List<Point> points = Stream.generate(() -> pt(index[0]++, index[0]++))
                .limit(count).collect(toList());

        // when then
        for (int step = 0; step < count; step++) {
            points.contains(pt(size / 2, size / 2));
        }
    }

    @Test
    @Ignore
    public void shouldDirectionTo() {
        // given when then
        assertEquals(RIGHT, pt(1, 1).direction(pt(2, 1)));
        assertEquals(LEFT, pt(1, 1).direction(pt(0, 1)));
        assertEquals(UP, pt(1, 1).direction(pt(1, 2)));
        assertEquals(DOWN, pt(1, 1).direction(pt(1, 0)));
        assertEquals(null, pt(1, 1).direction(pt(0, 0)));
    }

    @Test
    public void shouldGetStream() {
        assertEquals("[12, 34]",
                pt(12, 34).stream()
                        .map(Object::toString)
                        .collect(toList())
                        .toString());
    }

}

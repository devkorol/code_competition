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


import static com.codenjoy.dojo.client.AbstractLayeredBoard.Layers.LAYER1;
import static com.codenjoy.dojo.client.AbstractLayeredBoard.Layers.LAYER2;
import static com.codenjoy.dojo.games.expansion.Element.ANGLE_IN_LEFT;
import static com.codenjoy.dojo.games.expansion.Element.ANGLE_IN_RIGHT;
import static com.codenjoy.dojo.games.expansion.Element.BASE3;
import static com.codenjoy.dojo.games.expansion.Element.BASE4;
import static com.codenjoy.dojo.games.expansion.Element.EMPTY;
import static com.codenjoy.dojo.games.expansion.Element.FLOOR;
import static com.codenjoy.dojo.games.expansion.Element.GOLD;
import static com.codenjoy.dojo.games.expansion.Element.HOLE;
import static com.codenjoy.dojo.games.expansion.Element.SPACE;
import static com.codenjoy.dojo.games.expansion.ElementUtils.barriers;
import static com.codenjoy.dojo.services.PointImpl.pt;

import com.codenjoy.dojo.client.AbstractBoard;
import com.codenjoy.dojo.client.Utils;
import com.codenjoy.dojo.services.Point;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

/**
 * The class is a wrapper over the board string
 * coming from the server. Contains a number of
 * inherited methods {@link AbstractBoard},
 * but you can add any methods based on them here.
 */
public class Board extends AbstractBoard<Element> {

    private final int COUNT_NUMBERS = 3;

    @Override
    public Element[] elements() {
        return Element.values();
    }

    @Override
    protected int inversionY(int y) {
        return size - 1 - y;
    }

    /**
     * @param x X coordinate.
     * @param y Y coordinate.
     * @return Is it possible to go through the cell with {x,y} coordinates.
     */
    public boolean isBarrierAt(int x, int y) {
        return isAt(x, y, barriers);
    }

    /**
     * @return All barriers on the map.
     */
    public List<Point> barriers() {
        return layer(LAYER1).get(barriers);
    }

    /**
     * @return All barriers on the map.
     */
    public List<Point> holes() {
        return layer(LAYER1).get(HOLE);
    }

    /**
     * @param x X coordinate.
     * @param y Y coordinate.
     * @return Is Hole on the way?
     */
    public boolean isHoleAt(int x, int y) {
        return layer(LAYER1).isAt(x, y, HOLE);
    }

    /**
     * @return My forces color
     */
    public Element getMyForcesColor() {
        int color = source.getInt("myColor");
        return ElementUtils.force(color);
    }

    /**
     * @return My forces available
     */
    public int getForcesAvailable() {
        return source.getInt("available");
    }

    /**
     * @return number of tick on server - for debug
     */
    public int getTick() {
        return source.getInt("tick");
    }

    /*
     * @return true if you are in lobby wait for another users
     */
    public boolean isInLobby() {
        return source.getBoolean("inLobby");
    }

    public int getIncrease() {
        return source.getInt("available");
    }

    /*
     * @return pt.x - current round tick, pt.y - total ticks per round
     */
    public Point getRound() {
        return pt(source.getInt("round"), source.getInt("rounds"));
    }

    /**
     * @return My base base position
     */
    public Point getMyBasePosition() {
        JSONObject pt = source.getJSONObject("myBase");
        return pt(pt.getInt("x"), pt.getInt("y"));
    }

    /**
     * @return Returns position of your forces.
     */
    public List<Forces> getMyForces() {
        List<Forces> forces = getAllForces();
        List<Forces> result = new LinkedList<>();
        Element myColor = getMyForcesColor();
        for (Forces force : forces) {
            Point pt = force.getRegion();
            if (isAt(pt.getX(), pt.getY(), myColor)) {
                result.add(force);
            }
        }
        return result;
    }

    public List<Forces> getAllForces() {
        String map = getForcesString();
        int[][] array = getForcesArray(map);
        List<Forces> result = getForces(array);
        return result;
    }

    private String getForcesString() {
        return source.getString("forces");
    }

    private List<Forces> getForces(int[][] array) {
        List<Forces> result = new LinkedList<>();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                int count = array[x][y];
                if (count > 0) {
                    Forces force = new Forces(pt(x, y), count);
                    result.add(force);
                }
            }
        }
        return result;
    }

    private int[][] getForcesArray(String map) {
        int[][] result = new int[size][size];
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                int l = y * size + x;
                String sub = map.substring(l*COUNT_NUMBERS, (l + 1)*COUNT_NUMBERS);
                int count = parseCount(sub);

                int xx = inversionX(x);
                int yy = inversionY(y);

                result[xx][yy] = count;
            }
        }
        return result;
    }

    private int parseCount(String sub) {
        if (sub.equals("-=#")) {
            return 0;
        } else {
            return Integer.parseInt(sub, Character.MAX_RADIX);
        }
    }

    /**
     * @return Returns list of coordinates for all visible enemy forces.
     */
    public List<Forces> getEnemyForces() {
        List<Forces> forces = getAllForces();
        List<Forces> result = new LinkedList<>();
        Element myColor = getMyForcesColor();
        for (Forces force : forces) {
            Point pt = force.getRegion();
            if (!isAt(pt.getX(), pt.getY(), myColor)) {
                result.add(force);
            }
        }
        return result;
    }

    /**
     * @return Returns list of coordinates for all visible Walls.
     */
    public List<Point> getWalls() {
        return layer(LAYER1).get(
                ANGLE_IN_LEFT,
                Element.WALL_FRONT,
                ANGLE_IN_RIGHT,
                Element.WALL_RIGHT,
                Element.ANGLE_BACK_RIGHT,
                Element.WALL_BACK,
                Element.ANGLE_BACK_LEFT,
                Element.WALL_LEFT,
                Element.WALL_BACK_ANGLE_LEFT,
                Element.WALL_BACK_ANGLE_RIGHT,
                Element.ANGLE_OUT_RIGHT,
                Element.ANGLE_OUT_LEFT,
                SPACE);
    }

    /**
     * @return Returns list of coordinates for all visible Breaks.
     */
    public List<Point> getBreaks() {
        return layer(LAYER1).get(Element.BREAK);
    }

    /**
     * @return Returns list of coordinates for all visible Holes.
     */
    public List<Point> getHoles() {
        return layer(LAYER1).get(Element.HOLE);
    }

    /**
     * @return Returns list of coordinates for all visible Exit points.
     */
    public List<Point> getExits() {
        return layer(LAYER1).get(Element.EXIT);
    }

    /**
     * @return Returns list of coordinates for all visible Start points.
     */
    public List<Point> getBases() {
        return layer(LAYER1).get(Element.BASE1, Element.BASE2, BASE3, BASE4);
    }

    /**
     * @return Returns list of coordinates for all visible Gold.
     */
    public List<Point> getGold() {
        return layer(LAYER1).get(GOLD);
    }

    /**
     * @return Checks if you have no forces.
     */
    public boolean isGameOver() {
        return getFirst(getMyForcesColor()) == null;
    }

    public List<Point> getFreeSpaces() {
        List<Point> empty = layer(LAYER2).get(EMPTY);
        List<Point> floor = layer(LAYER1).get(FLOOR);
        List<Point> result = new LinkedList<>();
        for (Point pt : floor) {
            for (Point pt2 : empty) {
                if (pt.equals(pt2)) {
                    result.add(pt);
                }
            }
        }
        return result;
    }

    public String maskOverlay(String source, String mask) {
        StringBuilder result = new StringBuilder(source);
        for (int i = 0; i < result.length(); ++i) {
            Element el = valueOf(mask.charAt(i));
            if (ElementUtils.isWall(el)) {
                result.setCharAt(i, el.ch());
            }
        }

        return result.toString();
    }

    @Override
    public String toString() {
        String temp = "0123456789012345678901234567890";
        String temp2 = "  0    1    2    3    4    5    6    7    8    9" +
                "    0    1    2    3    4    5    6    7    8    9" +
                "    0    1    2    3    4    5    6    7    8    9    0";

        String[] layer1 = layer(LAYER1).boardAsString().split("\n");
        String[] layer2 = layer(LAYER2).boardAsString().split("\n");
        String[] layer3 = Utils.injectNN(getForcesString()).split("\n");

        int[][] array = getForcesArray(getForcesString());
        for (int y = 0; y < size; y++) {
            String line = "";
            for (int x = 0; x < size; x++) {
                String num = StringUtils.leftPad(Integer.toString(array[x][y]), COUNT_NUMBERS + 1, ' ');
                if (num.equals("   0")) num = "    ";
                line += num + '|';
            }
            layer3[size - 1 - y] = line;
        }

        int size = layer1.length;
        String numbers = temp.substring(0, size);
        String numbers2 = temp2.substring(0, size*(COUNT_NUMBERS + 2));
        String space = StringUtils.leftPad("", size - 5);
        String numbersLine = "  " + numbers + "   " + numbers +  "   " + numbers2;
        String firstPart = " Layer1 " + space + " Layer1 " + space + " Layer3\n" + numbersLine;

        StringBuilder builder = new StringBuilder();
        builder.append(firstPart)
                .append('\n');
        for (int i = 0; i < size; ++i) {
            int ii = size - 1 - i;
            String index = (ii < 10 ? " " : "") + ii;
            builder.append(index + layer1[i] + " " + index + maskOverlay(layer2[i], layer1[i]) + " " + index + layer3[i]);

            switch (i) {
                case 0:
                    builder.append(" My Forces: " + listToString(getMyForces()));
                    break;
                case 1:
                    builder.append(" Enemy Forces: " + listToString(getEnemyForces()));
                    break;
                case 2:
                    builder.append(" Gold: " + listToString(getGold()));
                    break;
                case 3:
                    builder.append(" Bases: " + listToString(getBases()));
                    break;
                case 4:
                    builder.append(" Exits: " + listToString(getExits()));
                    break;
                case 5:
                    builder.append(" Breaks: " + listToString(getBreaks()));
                    break;
                case 6:
                    builder.append(" Holes: " + listToString(getHoles()));
                    break;
            }

            if (i != size - 1) {
                builder.append("\n");
            }
        }

        builder.append('\n')
                .append(numbersLine)
                .append(" Tick#")
                .append(getTick());

        return builder.toString();
    }

    private String listToString(List<? extends Object> list) {
        String result = list.toString();
        result = result.replace('"', '\'');
        return result.substring(1, result.length() - 1);
    }

    public Forces forceAt(Point point) {
        for (Forces force : getAllForces()) {
            if (force.getRegion().itsMe(point)) {
                return force;
            }
        }
        return null;
    }

    public boolean isBarrierAt(Point p) {
        return isBarrierAt(p.getX(), p.getY());
    }

    public JSONObject getSource() {
        return source;
    }
}
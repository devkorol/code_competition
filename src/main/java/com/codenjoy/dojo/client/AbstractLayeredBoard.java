package com.codenjoy.dojo.client;

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

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.annotations.PerformanceOptimized;
import com.codenjoy.dojo.services.printer.CharElement;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import org.json.JSONArray;
import org.json.JSONObject;

public abstract class AbstractLayeredBoard<E extends CharElement> implements ClientBoard {

    public static final String LAYERS = "layers";

    protected int size;
    private char[][][] field;
    private GetLayer[] layers;
    protected JSONObject source;
    protected List<String> layersString = new LinkedList<>();
    private ElementsMap<E> elements;

    public ClientBoard forString(String boardString) {
        if (boardString.contains(LAYERS)) {
            source = new JSONObject(boardString);
            JSONArray layers = source.getJSONArray(LAYERS);
            return forString(layers.toList().toArray(new String[0]));
        }

        return forString(new String[]{boardString});
    }

    public ClientBoard forString(String... layers) {
        elements = (elements != null) ? elements : new ElementsMap<>(elements());
        layersString.clear();
        layersString.addAll(Arrays.asList(layers));

        String board = layers[0].replaceAll("\n", "");
        size = (int) Math.sqrt(board.length());
        field = new char[layers.length][size][size];
        this.layers = new AbstractLayeredBoard.GetLayer[layers.length];

        for (int layer = 0; layer < layers.length; ++layer) {
            board = layers[layer].replaceAll("\n", "");
            this.layers[layer] = new GetLayer(layer);

            char[] temp = board.toCharArray();
            for (int y = 0; y < size; y++) {
                int dy = y * size;
                for (int x = 0; x < size; x++) {
                    field[layer][inversionX(x)][inversionY(y)] = temp[dy + x];
                }
            }
        }

        return this;
    }

    protected int inversionX(int x) {
        return x;
    }

    protected int inversionY(int y) {
        return y;
    }

    /**
     * Метод уточняет, что являетсяся оперделением "вокруг" героя.
     * @return true - если стоит не учитывать диагональные углы.
     */
    protected boolean withoutCorners() {
        return false;
    }

    protected boolean isEquals(E e1, E e2) {
        return e1 == e2;
    }

    @PerformanceOptimized
    public E valueOf(char ch) {
        return elements.get(ch);
    }

    public abstract E[] elements();

    public int size() {
        return size;
    }

    public static class Layers {
        public final static int LAYER1 = 0;
        public final static int LAYER2 = 1;
        public final static int LAYER3 = 2;
    }

    @PerformanceOptimized
    protected GetLayer layer(int layer) {
        if (layer >= layers.length) {
            throw new IllegalArgumentException("There are only " + layers.length + " layers");
        }
        return layers[layer];
    }

    public class GetLayer {

        private char[][] layerField;

        /**
         * @param layer Layer number (from 0).
         */
        @PerformanceOptimized
        public GetLayer(int layer) {
            layerField = field[layer];
        }

        public E fieldElement(int x, int y) {
            return valueOf(layerField[x][y]);
        }

        private void getAnd(Function<Point, Boolean> function, E... elements) {
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    E value = fieldElement(x, y);
                    for (E element : elements) {
                        if (isEquals(value, element)) {
                            if (!function.apply(pt(x, y))) return;
                        }
                    }
                }
            }
        }

        /**
         * @param elements List of elements that we try to find.
         * @return All positions of element specified.
         */
        public List<Point> get(E... elements) {
            List<Point> result = new LinkedList<>();
            getAnd(pt -> { result.add(pt); return true; }, elements);
            return result;
        }

        /**
         * @param elements List of elements that we try to find.
         * @return First found position of element specified.
         */
        public Point getFirst(E... elements) {
            AtomicReference<Point> result = new AtomicReference<>();
            getAnd(pt -> { result.set(pt); return false; }, elements);
            return result.get();
        }

        /**
         * Says if at given position (X, Y) at given layer has given element.
         *
         * @param x        X coordinate.
         * @param y        Y coordinate.
         * @param element  Element that we try to detect on this point.
         * @return true is element was found.
         */
        public boolean isAt(int x, int y, E element) {
            if (isOutOf(x, y)) {
                return false;
            }
            return isEquals(getAt(x, y), element);
        }

        /**
         * @param x        X coordinate.
         * @param y        Y coordinate.
         * @return Returns element at position specified.
         */
        public E getAt(int x, int y) {
            return fieldElement(x, y);
        }

        public char field(int x, int y) {
            return layerField[x][y];
        }

        public String boardAsString() {
            StringBuilder result = new StringBuilder();
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    result.append(field(inversionX(x), inversionY(y)));
                }
                result.append("\n");
            }
            return result.toString();
        }

        /**
         * Says if at given position (X, Y) at given layer has given elements.
         *
         * @param x        X coordinate.
         * @param y        Y coordinate.
         * @param elements List of elements that we try to detect on this point.
         * @return true is any of this elements was found.
         */
        public boolean isAt(int x, int y, E... elements) {
            for (E element : elements) {
                if (isAt(x, y, element)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Says if near (at left, right, down, up,
         * left-down, left-up, right-down, right-up)
         * given position (X, Y) at given layer exists given element.
         *
         * @param x        X coordinate.
         * @param y        Y coordinate.
         * @param element  Element that we try to detect on near point.
         * @return true is element was found.
         */
        public boolean isNear(int x, int y, E element) {
            if (isOutOf(x, y)) {
                return false;
            }
            return countNear(x, y, element) > 0;
        }

        /**
         * @param x        X coordinate.
         * @param y        Y coordinate.
         * @param element  Element that we try to detect on near point.
         * @return Returns count of elements with type specified near
         * (at left, right, down, up,
         * left-down, left-up, right-down, right-up) {x,y} point.
         */
        public int countNear(int x, int y, E element) {
            if (isOutOf(x, y)) {
                return 0;
            }
            return (int) getNear(x, y).stream()
                    .filter( it -> isEquals(it, element))
                    .count();
        }

        /**
         * @param x        X coordinate.
         * @param y        Y coordinate.
         * @return All elements around
         * (at left, right, down, up,
         * left-down, left-up, right-down, right-up) position.
         */
        public List<E> getNear(int x, int y) {
            List<E> result = new LinkedList<>();

            int radius = 1;
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dy = -radius; dy <= radius; dy++) {
                    if (isOutOf(x + dx, y + dy)) {
                        continue;
                    }
                    if (dx == 0 && dy == 0) {
                        continue;
                    }
                    if (withoutCorners() && (dx != 0 && dy != 0)) {
                        continue;
                    }
                    result.add(getAt(x + dx, y + dy));
                }
            }

            return result;
        }

        public void set(int x, int y, char ch) {
            layerField[x][y] = ch;
        }

        public char[][] field() {
            return layerField;
        }
    }

    public int countLayers() {
        return field.length;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Board:");
        for (int layer = 0; layer < countLayers(); layer++) {
            result.append("\n").append(layer(layer).boardAsString());
        }
        return result.toString();
    }

    public boolean isOutOf(Point point) {
        return isOutOf(point.getX(), point.getY());
    }

    public boolean isOutOf(int x, int y) {
        return Point.isOutOf(x, y, size);
    }

    public List<String> getLayersString() {
        return layersString;
    }

    public void setSource(JSONObject source) {
        this.source = source;
    }
}

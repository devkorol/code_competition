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
import static com.codenjoy.dojo.games.expansion.Element.ANGLE_BACK_LEFT;
import static com.codenjoy.dojo.games.expansion.Element.ANGLE_BACK_RIGHT;
import static com.codenjoy.dojo.games.expansion.Element.ANGLE_IN_LEFT;
import static com.codenjoy.dojo.games.expansion.Element.ANGLE_IN_RIGHT;
import static com.codenjoy.dojo.games.expansion.Element.ANGLE_OUT_LEFT;
import static com.codenjoy.dojo.games.expansion.Element.ANGLE_OUT_RIGHT;
import static com.codenjoy.dojo.games.expansion.Element.BACKGROUND;
import static com.codenjoy.dojo.games.expansion.Element.BASE1;
import static com.codenjoy.dojo.games.expansion.Element.BASE2;
import static com.codenjoy.dojo.games.expansion.Element.BASE3;
import static com.codenjoy.dojo.games.expansion.Element.BASE4;
import static com.codenjoy.dojo.games.expansion.Element.BREAK;
import static com.codenjoy.dojo.games.expansion.Element.EMPTY;
import static com.codenjoy.dojo.games.expansion.Element.EXIT;
import static com.codenjoy.dojo.games.expansion.Element.FLOOR;
import static com.codenjoy.dojo.games.expansion.Element.FOG;
import static com.codenjoy.dojo.games.expansion.Element.FORCE1;
import static com.codenjoy.dojo.games.expansion.Element.FORCE2;
import static com.codenjoy.dojo.games.expansion.Element.FORCE3;
import static com.codenjoy.dojo.games.expansion.Element.FORCE4;
import static com.codenjoy.dojo.games.expansion.Element.GOLD;
import static com.codenjoy.dojo.games.expansion.Element.HOLE;
import static com.codenjoy.dojo.games.expansion.Element.SPACE;
import static com.codenjoy.dojo.games.expansion.Element.WALL_BACK;
import static com.codenjoy.dojo.games.expansion.Element.WALL_BACK_ANGLE_LEFT;
import static com.codenjoy.dojo.games.expansion.Element.WALL_BACK_ANGLE_RIGHT;
import static com.codenjoy.dojo.games.expansion.Element.WALL_FRONT;
import static com.codenjoy.dojo.games.expansion.Element.WALL_LEFT;
import static com.codenjoy.dojo.games.expansion.Element.WALL_RIGHT;

import java.util.Arrays;
import java.util.LinkedList;

public class ElementUtils {

    public static final Element[] walls = new Element[] {
            ANGLE_IN_LEFT,
            WALL_FRONT,
            ANGLE_IN_RIGHT,
            WALL_RIGHT,
            ANGLE_BACK_RIGHT,
            WALL_BACK,
            ANGLE_BACK_LEFT,
            WALL_LEFT,
            WALL_BACK_ANGLE_LEFT,
            WALL_BACK_ANGLE_RIGHT,
            ANGLE_OUT_RIGHT,
            ANGLE_OUT_LEFT,
            SPACE,
    };

    public static final Element[] holes = new Element[] {
            HOLE,
    };

    public static final Element[] layer1 = new Element[] {
            FLOOR,
            ANGLE_IN_LEFT,
            WALL_FRONT,
            ANGLE_IN_RIGHT,
            WALL_RIGHT,
            ANGLE_BACK_RIGHT,
            WALL_BACK,
            ANGLE_BACK_LEFT,
            WALL_LEFT,
            WALL_BACK_ANGLE_LEFT,
            WALL_BACK_ANGLE_RIGHT,
            ANGLE_OUT_RIGHT,
            ANGLE_OUT_LEFT,
            SPACE,
            EXIT,
            HOLE,
            BREAK,
            GOLD,
            BASE1,
            BASE2,
            BASE3,
            BASE4,
            FOG,
    };

    public static final Element[] layer2 = new Element[] {
            EMPTY,
            FORCE1,
            FORCE2,
            FORCE3,
            FORCE4,
            BACKGROUND,
    };

    public static final Element[] barriers = new LinkedList<Element>() {{
        addAll(Arrays.asList(walls));
        addAll(Arrays.asList(SPACE, BREAK, SPACE));
        addAll(Arrays.asList(HOLE));
    }}.toArray(Element[]::new);

    public static int index(Element baseColor) {
        switch (baseColor) {
            case BASE1:
            case FORCE1:
                return 0;
            case BASE2:
            case FORCE2:
                return 1;
            case BASE3:
            case FORCE3:
                return 2;
            case BASE4:
            case FORCE4:
                return 3;
            default:
                throw new IllegalArgumentException(
                    "Force element not found for element: " + baseColor);
        }
    }

    public static boolean isWall(Element element) {
        return element.is(walls);
    }

    public static Element force(int index) {
        switch (index) {
            case 0: return FORCE1;
            case 1: return FORCE2;
            case 2: return FORCE3;
            case 3: return FORCE4;
            default: throw new IllegalArgumentException(
                    "Force element not found for index: " + index);
        }
    }

    public static int layer(Element element) {
        for (Element el : layer2) {
            if (el == element) {
                return LAYER2;
            }
        }
        return LAYER1;
    }
}

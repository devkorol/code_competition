package com.codenjoy.dojo.games.expansion;

/*-
 * #%L
 * expansion - it's a dojo-like platform from developers to developers.
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


import static com.codenjoy.dojo.games.expansion.component.GreyGoo.HISTORY_FORCE_MOVE_SIZE;
import static com.codenjoy.dojo.services.PointImpl.pt;

import com.codenjoy.dojo.client.Utils;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.QDirection;
import java.util.HashMap;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.json.JSONObject;
import org.json.JSONPropertyIgnore;

@Setter
@Accessors(chain = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Forces {
    private int count;
    @EqualsAndHashCode.Include
    private Point region;

    private ForceType type;

    private final CircularFifoQueue<Point> moveHistory = new CircularFifoQueue(HISTORY_FORCE_MOVE_SIZE);

    private Map<QDirection, Point> nearPoints = new HashMap<>(10);

    private ForcesMoves forcesMoves;

    public Forces(Point region, int count) {
        this.region = new PointImpl(region);
        this.count = count;
    }

    public Forces(JSONObject json) {
        count = json.getInt("count");
        JSONObject pt = json.getJSONObject("region");
        region = pt(pt.getInt("x"), pt.getInt("y"));
    }

    public String json() {
        return Utils.prettyPrintObject(this);
    }

    @Override
    public String toString() {
        return String.format("[%s,%s]=%s %s", region.getX(), region.getY(), count, type);
    }

    public Point getRegion() {
        return region;
    }

    public int getCount() {
        return count;
    }

    @JSONPropertyIgnore
    public ForceType getType() {
        return type;
    }

    @JSONPropertyIgnore
    public CircularFifoQueue<Point> getMoveHistory() {
        return moveHistory;
    }

    @JSONPropertyIgnore
    public Map<QDirection, Point> getNearPoints() {
        return nearPoints;
    }

    @JSONPropertyIgnore
    public ForcesMoves getForcesMoves() {
        return forcesMoves;
    }

    public void move(QDirection direction, int i) {
        forcesMoves = new ForcesMoves(region.copy(), i, direction);
    }

    public ForcesMoves getAndDumpMove() {
        if(forcesMoves == null) {
            return null;
        }
        moveHistory.add(forcesMoves.getRegion().copy().move(forcesMoves.getDirectionEnum()));
        region = region.move(forcesMoves.getDirectionEnum());

        ForcesMoves tmp = forcesMoves;
        forcesMoves = null;
        return tmp;
    }

    public static enum ForceType {
        CORE,
        MEMBRANE,
        AIMLESS;
    }
}

package com.codenjoy.dojo.games.expansion.component.cell;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2023 Codenjoy
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

import com.codenjoy.dojo.games.expansion.Board;
import com.codenjoy.dojo.games.expansion.Element;
import com.codenjoy.dojo.games.expansion.Forces;
import java.util.List;
import java.util.Optional;

public abstract class CellWithMembraneAndCore extends CellWithMembrane {

  protected Forces core;
  protected List<Element> coreTypes;

  public CellWithMembraneAndCore(Forces core, List<Element> coreTypes) {
    super(core);
    this.core = core;
    this.coreTypes = coreTypes;
  }

  protected void updateCore(Board board) {
    if(!containsCore()) {
      reinitCore(board);
    }
  }

  protected boolean containsCore() {
    return cellForces.contains(core);
  }

  private void reinitCore(Board board) {
    Optional<Forces> force = cellForces.stream()
        .filter(f -> coreTypes.contains(board.getAt(f.getRegion())))
        .findFirst();
    if(force.isPresent()) {
      core = force.get();
    }
  }

}

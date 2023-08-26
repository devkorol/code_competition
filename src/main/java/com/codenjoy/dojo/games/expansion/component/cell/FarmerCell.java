package com.codenjoy.dojo.games.expansion.component.cell;

import com.codenjoy.dojo.games.expansion.Forces;
import com.codenjoy.dojo.games.expansion.Forces.ForceType;

/**
 * Behaviour model:
 * - all forces to membrane
 * - keep gold
 * - find new gold
 * - avoid combat
 */
public class FarmerCell extends Cell {

  public FarmerCell(Forces force) {
    super(force);
  }

  protected void allToMembrane(){
    for (Forces cellForce : this.getCellForces()) {
      if(cellForce.getType() != ForceType.AIMLESS
          || cellForce.getCount() < 2) {
        continue;
      }
      //move to membrane

    }



  }

  public void increase(){}

  public void move(){}



}

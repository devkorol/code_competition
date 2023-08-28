package com.codenjoy.dojo.games.expansion.component.cell;

import static com.codenjoy.dojo.games.expansion.component.GreyGoo.PATH_TO_GOLD_MAX_TOLERANCE;
import static com.codenjoy.dojo.games.expansion.component.GreyGoo.PATH_TO_GOLD_MAX_VARIATIONS;
import static com.codenjoy.dojo.games.expansion.component.GreyGoo.PATH_TO_MEMBRANE_MAX_TOLERANCE;
import static com.codenjoy.dojo.games.expansion.component.GreyGoo.PATH_TO_MEMBRANE_MAX_VARIATIONS;

import com.codenjoy.dojo.games.expansion.Board;
import com.codenjoy.dojo.games.expansion.Element;
import com.codenjoy.dojo.games.expansion.Forces;
import com.codenjoy.dojo.games.expansion.Forces.ForceType;
import com.codenjoy.dojo.games.expansion.component.path.ASearch;
import com.codenjoy.dojo.games.expansion.component.path.ASearch.Node;
import com.codenjoy.dojo.games.expansion.component.path.PlainDistance;
import com.codenjoy.dojo.services.Point;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Behaviour model:
 * - all forces to membrane
 * - find new gold
 * - avoid combat
 * - keep gold
 */
public class FarmerCell extends Cell {


  private List<Forces> membraneForces;

  public FarmerCell(Forces force) {
    super(force);
  }

  protected void updateMembraneCells(){
    membraneForces = cellForces.stream()
        .filter(c -> c.getType() == ForceType.MEMBRANE)
        .collect(Collectors.toList());
  }

  @Override
  public void moveForces(Board board){
    updateMembraneCells();
    aimlessToMembrane(board);
    membraneToGold(board);
  }

  protected void aimlessToMembrane(Board board){
    for (Forces cellForce : this.getCellForces()) {
      if(cellForce.getType() != ForceType.AIMLESS
          || cellForce.getCount() < 2) {
        continue;
      }

      int minSteps = Integer.MAX_VALUE;
      Node minPath = null;

      //move to membrane
      TreeSet<Point> membraneByDistance = PlainDistance.getElementsByDistance(cellForce.getRegion(),
          membraneForces);
      int i = 0;
      for (Point point : membraneByDistance) {
        if(++i > PATH_TO_MEMBRANE_MAX_VARIATIONS) {
          break;
        }

        Node path = ASearch.path(cellForce.getRegion(), point, board);
        if(path.getSteps() <= PATH_TO_MEMBRANE_MAX_TOLERANCE){
          minSteps = path.getSteps();
          minPath = path;
          break;
        }

        if(minPath == null || path.getSteps() < minSteps){
          minSteps = path.getSteps();
          minPath = path;
        }
      }

      if(minPath != null) {
        cellForce.move(minPath.getNext().getDirection(), cellForce.getCount() - 1);
      }
    }
  }

  protected void membraneToGold(Board board){
    for (Forces cellForce : this.getCellForces()) {
      if(cellForce.getType() != ForceType.MEMBRANE
          || cellForce.getCount() < 2) {
        continue;
      }

      int minSteps = Integer.MAX_VALUE;
      Node minPath = null;
      //move to membrane
      TreeSet<Point> goldByDistance = PlainDistance.getElementsByDistance(cellForce.getRegion(), board, Element.GOLD);
      int i = 0;
      for (Point point : goldByDistance) {
        if(cellForces.contains(new Forces(point, 1))){
          //known gold
          continue;
        }
        if(++i > PATH_TO_GOLD_MAX_VARIATIONS) {
          break;
        }

        Node path = ASearch.path(cellForce.getRegion(), point, board);
        if(path.getSteps() <= PATH_TO_GOLD_MAX_TOLERANCE){
          minSteps = path.getSteps();
          minPath = path;
          break;
        }

        if(minPath == null || path.getSteps() < minSteps){
          minSteps = path.getSteps();
          minPath = path;
        }
      }

      if(minPath != null) {
        cellForce.move(minPath.getNext().getDirection(), cellForce.getCount() - 1);
      }
    }
  }

  public void increase(){}

  public void move(){}



}

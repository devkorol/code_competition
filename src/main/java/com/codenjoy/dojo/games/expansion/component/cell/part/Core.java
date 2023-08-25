package com.codenjoy.dojo.games.expansion.component.cell.part;

import com.codenjoy.dojo.games.expansion.Forces;
import com.codenjoy.dojo.games.expansion.ForcesMoves;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.QDirection;
import java.util.List;
import java.util.Optional;
import lombok.Getter;

@Getter
public class Core {

  protected Forces actual;
  protected Forces prev;
  protected ForcesMoves move;

  public Core(Forces actual) {
    this.actual = actual;
  }

  public boolean refreshAndCheckIsAlive(List<Forces> myForces) {
    if(actual == null) {
      return false;
    }

    Point newPoint = actual.getRegion().copy();
    newPoint.move(QDirection.valueOf(move.getDirection()));
    Optional<Forces> forceAtNewPoint = myForces.stream().filter(f -> f.getRegion().equals(newPoint)).findFirst();
    if(forceAtNewPoint.isPresent()) {
      prev = actual;
      actual = forceAtNewPoint.get();
      return true;
    }

    Optional<Forces> forceAtActualPoint = myForces.stream().filter(f -> f.getRegion().equals(actual.getRegion())).findFirst();
    if(forceAtActualPoint.isPresent()) {
      prev = actual;
      actual = forceAtActualPoint.get();
      return true;
    }

    return false;
  }



}
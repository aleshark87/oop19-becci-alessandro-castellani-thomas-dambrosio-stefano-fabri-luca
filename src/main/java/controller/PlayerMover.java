package controller;

import model.*;
import model.roundenvironment.coordinate.Coordinate;

/**
 * this class allow to move the current player on the board
 * 
 * @author Thomas
 */

public interface PlayerMover {
	
	public void movePlayer(Coordinate position);
	
}

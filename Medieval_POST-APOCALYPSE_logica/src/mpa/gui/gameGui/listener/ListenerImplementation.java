package mpa.gui.gameGui.listener;

import java.util.ArrayList;
import java.util.HashMap;

import com.jme3.math.Vector2f;

public abstract class ListenerImplementation
{
	abstract void setPause();

	abstract HashMap<String, HashMap<String, Integer>> getPlayersResourceAmount();

	abstract String getPickedObject( Vector2f click );

	abstract void changeItem( String item );

	abstract ArrayList<String> playerAction( Vector2f direction );

	abstract boolean occupyProperty( String property );

	abstract String createTower( Vector2f point );

	abstract ArrayList<String> createMinions( String boss, String target, int quantity );

	abstract void updateInformation();

	abstract void createStateInformation();

}

package mpa.gui.gameGui.listener;

import java.util.ArrayList;
import java.util.HashMap;

import com.jme3.math.Vector2f;

public abstract class ListenerImplementation
{
	public abstract void setPause();

	public abstract HashMap<String, HashMap<String, Integer>> getPlayersResourceAmount();

	public abstract String getPickedObject( Vector2f click );

	public abstract void changeItem( String item );

	public abstract ArrayList<String> playerAction( Vector2f direction );

	public abstract boolean occupyProperty( String property );

	public abstract String createTower( Vector2f point );

	public abstract ArrayList<String> createMinions( String boss, String target, int quantity );

	public abstract void updateInformation();

	public abstract void createStateInformation();

}

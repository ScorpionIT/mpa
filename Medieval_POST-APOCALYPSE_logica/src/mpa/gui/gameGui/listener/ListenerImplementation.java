package mpa.gui.gameGui.listener;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import com.jme3.math.Vector2f;

public abstract class ListenerImplementation
{
	public abstract void setPause();

	public abstract Map<String, Map<String, Integer>> getPlayersResourceAmount();

	public abstract String getPickedObject( Vector2f click );

	public abstract void changeItem( String item );

	public abstract ArrayList<String> playerAction( Vector2f direction );

	public abstract boolean occupyProperty( String property );

	public abstract String createTower( String property );

	public abstract ArrayList<String> createMinions( String boss, String target, int quantity );

	public abstract String createTowerCrusher( String boss, String target );

	public abstract void updateInformation();

	public abstract void createStateInformation();

	public abstract void computePath( Vector2f click );

	public abstract void computePath( Vector2f click, String playerName );

	public abstract String getPickedObjectOwner( String objectType, String objectID );

	public abstract int getPickedObjectProductivity( String objectType, String objectID );

	public abstract Map<String, Integer> getPlayerResourcesAmount( String playerName );

	public abstract int getNumberOfPlayer();

	public abstract String getPlayerLevel( String player );

	public abstract int getPlayerHP( String player );

	public abstract int getPlayerMP( String player );

	public abstract Set<String> getPlayersName();

}

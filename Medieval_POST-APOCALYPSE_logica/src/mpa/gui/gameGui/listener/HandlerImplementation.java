package mpa.gui.gameGui.listener;

import java.util.Map;
import java.util.Set;

import com.jme3.math.Vector2f;

public abstract class HandlerImplementation
{
	public abstract void setPause();

	public abstract Map<String, Map<String, Integer>> getPlayersResourceAmount();

	public abstract String getPickedObject( Vector2f click );

	public abstract void changeItem( String item );

	public abstract void playerAction( Vector2f direction );

	public abstract boolean occupyProperty( String property );

	public abstract boolean createTower( String property );

	public abstract boolean createMinions( String boss, String target, int quantity );

	public abstract boolean createTowerCrusher( String boss, String target );

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

	public abstract boolean buyHPPotion( String playerName );

	public abstract boolean buyMPPotion( String playerName );

	public abstract boolean buyGranade( String playerName );

	public abstract int getPlayerHPPotion( String playerName );

	public abstract int getPlayerMPPotion( String playerName );

	public abstract int getPlayerGranade( String playerName );

	public abstract String getMinionBoss( String ID );

	public abstract void endGame();

}

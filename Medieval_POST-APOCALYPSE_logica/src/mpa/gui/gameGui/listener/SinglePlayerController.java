package mpa.gui.gameGui.listener;

import java.util.ArrayList;
import java.util.HashMap;

import mpa.core.logic.AbstractObject;
import mpa.core.logic.GameManager;
import mpa.core.logic.GameManagerProxy;
import mpa.gui.gameGui.playingGUI.GuiObjectManager;

import com.jme3.math.Vector2f;

public class SinglePlayerController extends ListenerImplementation
{
	private GuiObjectManager gObjManager = GuiObjectManager.getInstance();
	private GameManagerProxy gManagerProxy = GameManagerProxy.getInstance();

	@Override
	public void setPause()
	{
		gManagerProxy.setPause();
	}

	@Override
	public HashMap<String, HashMap<String, Integer>> getPlayersResourceAmount()
	{
		HashMap<String, HashMap<String, Integer>> playersResourceAmount = new HashMap<>();

		// for( )
		// {
		// playersResourceAmount.put( p.getName(), p.getResources() );
		// }

		return playersResourceAmount;
	}

	@Override
	public String getPickedObject( Vector2f click )
	{
		AbstractObject pickedObject = GameManager.getInstance().getWorld()
				.pickedObject( click.x, click.y );

		String obj = pickedObject.getClass().getSimpleName();
		obj += ":" + pickedObject.getID();

		return obj;

	}

	@Override
	public void changeItem( String item )
	{
		gManagerProxy.changeSelectedItem( gObjManager.getPlayingPlayer(), item );
	}

	@Override
	public ArrayList<String> playerAction( Vector2f direction )
	{
		return gManagerProxy.playerAction( gObjManager.getPlayingPlayer(),
				new javax.vecmath.Vector2f( direction.x, direction.y ) );
	}

	@Override
	public boolean occupyProperty( String property )
	{
		return gManagerProxy.occupyProperty( gObjManager.getPlayingPlayer(), property );
	}

	@Override
	public String createTower( Vector2f point )
	{
		String tower = gManagerProxy.createTower( gObjManager.getPlayingPlayer(),
				new javax.vecmath.Vector2f( point.x, point.y ) );

		gObjManager.addTower( new javax.vecmath.Vector2f( point.x, point.y ), tower );
		return tower;

	}

	@Override
	public ArrayList<String> createMinions( String boss, String target, int quantity )
	{
		ArrayList<String> minions = gManagerProxy.createMinions( boss, quantity, target );
		for( String m : minions )
			gObjManager.addMinion( m );

		return minions;
	}

	@Override
	public void updateInformation()
	{
		ArrayList<String> deadMinions = gManagerProxy.takeDeadMinions();
		ArrayList<String> deadPlayers = gManagerProxy.takeDeadPlayers();
		HashMap<String, javax.vecmath.Vector2f[]> playersPositions = gManagerProxy
				.getPlayersPositions();

		HashMap<String, javax.vecmath.Vector2f[]> minionsPositions = gManagerProxy
				.getMinionsPositions();

		for( String m : deadMinions )
			gObjManager.removeMinion( m );
		for( String p : deadPlayers )
			gObjManager.removePlayer( p );
		for( String p : playersPositions.keySet() )
		{
			javax.vecmath.Vector2f[] positions = playersPositions.get( p );
			gObjManager.updatePlayerPosition( p, positions[0], positions[1] );
		}

		for( String m : minionsPositions.keySet() )
		{
			javax.vecmath.Vector2f[] positions = minionsPositions.get( m );
			gObjManager.updateMinionPosition( m, positions[0], positions[1] );
		}
	}

	@Override
	public void createStateInformation()
	{
		HashMap<String, javax.vecmath.Vector2f[]> initPs = gManagerProxy.getPlayers();
		for( String p : initPs.keySet() )
		{
			javax.vecmath.Vector2f[] positions = initPs.get( p );
			gObjManager.addPlayer( p, positions[3], positions[0], positions[1] );
		}
		HashMap<String, javax.vecmath.Vector2f> fields = gManagerProxy.getFields();
		HashMap<String, javax.vecmath.Vector2f> caves = gManagerProxy.getCaves();
		HashMap<String, javax.vecmath.Vector2f> woods = gManagerProxy.getWoods();
		HashMap<String, javax.vecmath.Vector2f> mines = gManagerProxy.getMines();

		for( String s : fields.keySet() )
			gObjManager.addResource( "FIELD", Integer.parseInt( s ), fields.get( s ) );
		for( String s : caves.keySet() )
			gObjManager.addResource( "CAVE", Integer.parseInt( s ), caves.get( s ) );
		for( String s : woods.keySet() )
			gObjManager.addResource( "WOOD", Integer.parseInt( s ), woods.get( s ) );
		for( String s : mines.keySet() )
			gObjManager.addResource( "MINE", Integer.parseInt( s ), mines.get( s ) );

		gObjManager.setWorldDimension( gManagerProxy.worldDimension() );
	}
}

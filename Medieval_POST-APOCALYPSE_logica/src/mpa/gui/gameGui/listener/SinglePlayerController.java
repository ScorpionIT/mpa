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
	void setPause()
	{
		gManagerProxy.setPause();
	}

	@Override
	HashMap<String, HashMap<String, Integer>> getPlayersResourceAmount()
	{
		HashMap<String, HashMap<String, Integer>> playersResourceAmount = new HashMap<>();

		// for( )
		// {
		// playersResourceAmount.put( p.getName(), p.getResources() );
		// }

		return playersResourceAmount;
	}

	@Override
	String getPickedObject( Vector2f click )
	{
		AbstractObject pickedObject = GameManager.getInstance().getWorld()
				.pickedObject( click.x, click.y );

		String obj = pickedObject.getClass().getSimpleName();
		obj += ":" + pickedObject.getID();

		return obj;

	}

	@Override
	void changeItem( String item )
	{
		gManagerProxy.changeSelectedItem( gObjManager.getPlayingPlayer(), item );
	}

	@Override
	ArrayList<String> playerAction( Vector2f direction )
	{
		return gManagerProxy.playerAction( gObjManager.getPlayingPlayer(),
				new javax.vecmath.Vector2f( direction.x, direction.y ) );
	}

	@Override
	boolean occupyProperty( String property )
	{
		return gManagerProxy.occupyProperty( gObjManager.getPlayingPlayer(), property );
	}

	@Override
	String createTower( Vector2f point )
	{
		String tower = gManagerProxy.createTower( gObjManager.getPlayingPlayer(),
				new javax.vecmath.Vector2f( point.x, point.y ) );

		gObjManager.addTower( new javax.vecmath.Vector2f( point.x, point.y ), tower );
		return tower;

	}

	@Override
	ArrayList<String> createMinions( String boss, String target, int quantity )
	{
		ArrayList<String> minions = gManagerProxy.createMinions( boss, quantity, target );
		for( String m : minions )
			gObjManager.addMinion( m );

		return minions;
	}

	@Override
	void updateInformation()
	{
		ArrayList<String> deadMinions = gManagerProxy.takeDeadMinions();
		ArrayList<String> deadPlayers = gManagerProxy.takeDeadPlayers();
		HashMap<String, javax.vecmath.Vector2f[]> playersPositions = gManagerProxy
				.getPlayersPositions();

		for( String m : deadMinions )
			gObjManager.removeMinion( m );
		for( String p : deadPlayers )
			gObjManager.removePlayer( p );
		for( String p : playersPositions.keySet() )
		{
			javax.vecmath.Vector2f[] positions = playersPositions.get( p );
			gObjManager.updatePlayerPosition( p, positions[0], positions[1] );
		}
	}

	@Override
	void createStateInformation()
	{
		// TODO Auto-generated method stub

	}

}

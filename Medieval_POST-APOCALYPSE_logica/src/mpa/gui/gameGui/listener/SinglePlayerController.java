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

		String obj;

		if( pickedObject == null )
			obj = new String( "GROUND" );
		else
		{
			obj = pickedObject.getClass().getSimpleName();
			obj += ":" + pickedObject.getID();
		}
		return obj;

	}

	@Override
	public void changeItem( String item )
	{
		gManagerProxy.changeSelectedItem( GuiObjectManager.getInstance().getPlayingPlayer(), item );
	}

	@Override
	public ArrayList<String> playerAction( Vector2f direction )
	{
		return gManagerProxy.playerAction( GuiObjectManager.getInstance().getPlayingPlayer(),
				new javax.vecmath.Vector2f( direction.x, direction.y ) );
	}

	@Override
	public boolean occupyProperty( String property )
	{
		return gManagerProxy.occupyProperty( GuiObjectManager.getInstance().getPlayingPlayer(),
				property );
	}

	@Override
	public String createTower( Vector2f point )
	{
		String tower = gManagerProxy.createTower(
				GuiObjectManager.getInstance().getPlayingPlayer(), new javax.vecmath.Vector2f(
						point.x, point.y ) );

		GuiObjectManager.getInstance().addTower( new javax.vecmath.Vector2f( point.x, point.y ),
				tower );
		return tower;

	}

	@Override
	public ArrayList<String> createMinions( String boss, String target, int quantity )
	{
		ArrayList<String> minions = gManagerProxy.createMinions( boss, quantity, target );
		for( String m : minions )
			GuiObjectManager.getInstance().addMinion( m );

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
			GuiObjectManager.getInstance().removeMinion( m );
		for( String p : deadPlayers )
			GuiObjectManager.getInstance().removePlayer( p );
		for( String p : playersPositions.keySet() )
		{
			javax.vecmath.Vector2f[] positions = playersPositions.get( p );
			GuiObjectManager.getInstance().updatePlayerPosition( p, positions[0], positions[1] );
		}

		for( String m : minionsPositions.keySet() )
		{
			javax.vecmath.Vector2f[] positions = minionsPositions.get( m );
			GuiObjectManager.getInstance().updateMinionPosition( m, positions[0], positions[1] );
		}
	}

	@Override
	public void createStateInformation()
	{
		HashMap<String, javax.vecmath.Vector2f[]> initPs = gManagerProxy.getPlayers();
		for( String p : initPs.keySet() )
		{
			javax.vecmath.Vector2f[] positions = initPs.get( p );
			System.out.println( "positions= " + positions );
			System.out.println( GuiObjectManager.getInstance() );
			GuiObjectManager.getInstance().addPlayer( p, positions[2], positions[0], positions[1] );
		}
		HashMap<String, javax.vecmath.Vector2f> fields = gManagerProxy.getFields();
		HashMap<String, javax.vecmath.Vector2f> caves = gManagerProxy.getCaves();
		HashMap<String, javax.vecmath.Vector2f> woods = gManagerProxy.getWoods();
		HashMap<String, javax.vecmath.Vector2f> mines = gManagerProxy.getMines();

		for( String s : fields.keySet() )
			GuiObjectManager.getInstance().addResource( "FIELD", Integer.parseInt( s ),
					fields.get( s ) );
		for( String s : caves.keySet() )
			GuiObjectManager.getInstance().addResource( "CAVE", Integer.parseInt( s ),
					caves.get( s ) );
		for( String s : woods.keySet() )
			GuiObjectManager.getInstance().addResource( "WOOD", Integer.parseInt( s ),
					woods.get( s ) );
		for( String s : mines.keySet() )
			GuiObjectManager.getInstance().addResource( "MINE", Integer.parseInt( s ),
					mines.get( s ) );

		GuiObjectManager.getInstance().setWorldDimension( gManagerProxy.worldDimension() );
	}

	@Override
	public void computePath( Vector2f click )
	{
		gManagerProxy.computePath( GuiObjectManager.getInstance().getPlayingPlayer(), click.x,
				click.y );
	}
}
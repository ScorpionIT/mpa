package mpa.gui.gameGui.listener;

import java.util.ArrayList;
import java.util.HashMap;

import mpa.core.logic.GameManager;
import mpa.core.logic.character.Player;
import mpa.core.logic.character.Player.Item;

import com.jme3.math.Vector2f;

public class SinglePlayerController extends ListenerImplementation
{

	@Override
	void setPause()
	{
		GameManager.getInstance().setPause();
	}

	@Override
	HashMap<String, HashMap<String, Integer>> getPlayersResourceAmount()
	{
		HashMap<String, HashMap<String, Integer>> playersResourceAmount = new HashMap<>();

		for( Player p : GameManager.getInstance().getPlayers() )
		{
			playersResourceAmount.put( p.getName(), p.getResources() );
		}

		return playersResourceAmount;
	}

	@Override
	String getPickedObject( Vector2f click )
	{
		String pickedObject = GameManager.getInstance().getWorld().pickedObject( click.x, click.y )
				.toString();
		return null;

	}

	@Override
	void changeItem( Item item )
	{
		// TODO Auto-generated method stub

	}

	@Override
	ArrayList<String> attack( Vector2f direction )
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	boolean occupyProperty( String property )
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	boolean createTower( Vector2f point )
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	boolean createMinions( String boss, String target, int quantity )
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	void updateInformation()
	{
		// TODO Auto-generated method stub

	}

	@Override
	void createStateInformation()
	{
		// TODO Auto-generated method stub

	}

}

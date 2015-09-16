package mpa.gui.gameGui.listener;

import java.util.ArrayList;
import java.util.HashMap;

import com.jme3.math.Vector2f;

public class MultiPlayerController extends ListenerImplementation
{

	public MultiPlayerController()
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setPause()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public HashMap<String, HashMap<String, Integer>> getPlayersResourceAmount()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPickedObject(Vector2f click)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void changeItem(String item)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public ArrayList<String> playerAction(Vector2f direction)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean occupyProperty(String property)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String createTower(Vector2f point)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> createMinions(String boss, String target, int quantity)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateInformation()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void createStateInformation()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void computePath(Vector2f click)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public String getPickedObjectOwner(String objectType, String objectID)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPickedObjectProductivity(String objectType, String objectID)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public HashMap<String, Integer> getPlayerResourcesAmount(String playerName)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNumberOfPlayer()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void computePath(Vector2f click, String playerName)
	{
		// TODO Stub di metodo generato automaticamente

	}

	@Override
	public String getPlayerLevel(String player)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPlayerHP(String player)
	{
		// TODO Stub di metodo generato automaticamente
		return 0;
	}

	@Override
	public int getPlayerMP(String player)
	{
		// TODO Stub di metodo generato automaticamente
		return 0;
	}

}

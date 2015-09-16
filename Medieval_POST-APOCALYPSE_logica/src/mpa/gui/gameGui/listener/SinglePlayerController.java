package mpa.gui.gameGui.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

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
		return gManagerProxy.getPlayersResourceAmount();
	}

	@Override
	public String getPickedObject(Vector2f click)
	{
		return GameManagerProxy.getInstance().getPickedObject(click);
	}

	@Override
	public String getPickedObjectOwner(String objectType, String objectID)
	{
		return gManagerProxy.getObjectOwner(objectType, objectID);
	}

	@Override
	public int getPickedObjectProductivity(String objectType, String objectID)
	{
		return gManagerProxy.getObjectProductivity(objectType, objectID);
	}

	@Override
	public void changeItem(String item)
	{
		gManagerProxy.changeSelectedItem(GuiObjectManager.getInstance().getPlayingPlayer(), item);
	}

	@Override
	public ArrayList<String> playerAction(Vector2f direction)
	{
		return gManagerProxy.playerAction(GuiObjectManager.getInstance().getPlayingPlayer(), new javax.vecmath.Vector2f(direction.x, direction.y));
	}

	@Override
	public boolean occupyProperty(String property)
	{
		return gManagerProxy.occupyProperty(GuiObjectManager.getInstance().getPlayingPlayer(), property);
	}

	@Override
	public String createTower(Vector2f point)
	{
		String tower = gManagerProxy.createTower(GuiObjectManager.getInstance().getPlayingPlayer(), new javax.vecmath.Vector2f(point.x, point.y));

		GuiObjectManager.getInstance().addTower(new javax.vecmath.Vector2f(point.x, point.y), tower);
		return tower;

	}

	@Override
	public ArrayList<String> createMinions(String boss, String target, int quantity)
	{
		ArrayList<String> minions = gManagerProxy.createMinions(boss, quantity, target);
		for (String m : minions)
			GuiObjectManager.getInstance().addMinion(m);

		return minions;
	}

	@Override
	public void updateInformation()
	{
		ArrayList<String> deadMinions = gManagerProxy.takeDeadMinions();
		ArrayList<String> deadPlayers = gManagerProxy.takeDeadPlayers();
		ArrayList<String> attackingPlayers = gManagerProxy.takePlayerAttacks();
		ArrayList<String> attackingMinions = gManagerProxy.takeMinionAttacks();
		HashMap<String, javax.vecmath.Vector2f[]> playersPositions = gManagerProxy.getPlayersPositions();

		HashMap<String, javax.vecmath.Vector2f[]> minionsPositions = gManagerProxy.getMinionsPositions();

		for (String m : deadMinions)
			GuiObjectManager.getInstance().removeMinion(m);
		for (String p : deadPlayers)
			GuiObjectManager.getInstance().killPlayer(p);
		for (String p : playersPositions.keySet())
		{
			javax.vecmath.Vector2f[] positions = playersPositions.get(p);
			GuiObjectManager.getInstance().updatePlayerPosition(p, positions[0], positions[1], gManagerProxy.isMovingPlayer(p));
		}

		for (String m : minionsPositions.keySet())
		{
			javax.vecmath.Vector2f[] positions = minionsPositions.get(m);
			GuiObjectManager.getInstance().updateMinionPosition(m, positions[0], positions[1]);
		}
		for (String playerName : attackingPlayers)
		{
			GuiObjectManager.getInstance().startPlayerAttackAnimation(playerName);

		}
	}

	@Override
	public void createStateInformation()
	{
		HashMap<String, javax.vecmath.Vector2f[]> initPs = gManagerProxy.getPlayers();
		for (String p : initPs.keySet())
		{
			javax.vecmath.Vector2f[] positions = initPs.get(p);
			System.out.println("positions= " + positions);
			System.out.println(GuiObjectManager.getInstance());
			GuiObjectManager.getInstance().addPlayer(p, positions[2], positions[0], positions[1]);
		}
		HashMap<String, javax.vecmath.Vector2f> fields = gManagerProxy.getFields();
		HashMap<String, javax.vecmath.Vector2f> caves = gManagerProxy.getCaves();
		HashMap<String, javax.vecmath.Vector2f> woods = gManagerProxy.getWoods();
		HashMap<String, javax.vecmath.Vector2f> mines = gManagerProxy.getMines();

		for (String s : fields.keySet())
			GuiObjectManager.getInstance().addResource("FIELD", Integer.parseInt(s), fields.get(s));
		for (String s : caves.keySet())
			GuiObjectManager.getInstance().addResource("CAVE", Integer.parseInt(s), caves.get(s));
		for (String s : woods.keySet())
			GuiObjectManager.getInstance().addResource("WOOD", Integer.parseInt(s), woods.get(s));
		for (String s : mines.keySet())
			GuiObjectManager.getInstance().addResource("MINE", Integer.parseInt(s), mines.get(s));

		GuiObjectManager.getInstance().setWorldDimension(gManagerProxy.worldDimension());
	}

	@Override
	public void computePath(Vector2f click)
	{
		gManagerProxy.computePath(GuiObjectManager.getInstance().getPlayingPlayer(), click.x, click.y);
	}

	@Override
	public HashMap<String, Integer> getPlayerResourcesAmount(String playerName)
	{
		return gManagerProxy.getPlayerResourcesAmout(playerName);
	}

	@Override
	public int getNumberOfPlayer()
	{
		return gManagerProxy.getNumberOfPlayer();
	}

	@Override
	public void computePath(Vector2f click, String playerName)
	{
		gManagerProxy.computePath(playerName, click.x, click.y);
	}

	@Override
	public String getPlayerLevel(String player)
	{
		return gManagerProxy.getPlayerLevel(player);
	}

	@Override
	public int getPlayerHP(String playerName)
	{
		return gManagerProxy.getPLayerHP(playerName);
	}

	@Override
	public int getPlayerMP(String playerName)
	{
		return gManagerProxy.getPlayerMP(playerName);
	}

	@Override
	public Set<String> getPlayersName()
	{
		return gManagerProxy.getPlayersName();
	}
}

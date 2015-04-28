package mpa.core.logic;

import java.util.ArrayList;
import java.util.List;

import mpa.core.logic.building.AbstractPrivateProperty;
import mpa.core.logic.character.Player;
import mpa.core.logic.tool.Potions;

public class GameManager
{
	private World world;
	private List<Player> players;

	private static GameManager gameManager = null;

	public static GameManager getInstance()
	{
		return gameManager;
	}

	public static void init( World world, List<Player> players )
	{
		if( gameManager == null )
		{
			gameManager = new GameManager( world, players );
			new PositionUpdater().start();
			new ResourceUpdater().start();
		}
	}

	private GameManager( World world, List<Player> players )
	{
		this.world = world;
		this.players = players;
	}

	public ArrayList<Pair<Integer, Integer>> computePath( Player player, float xGoal, float yGoal )
	{
		PathCalculatorThread pathCalculatorThread = new PathCalculatorThread( player, xGoal, yGoal );
		pathCalculatorThread.start();
		return pathCalculatorThread.getPath();
	}

	public void updateCharacterPositions()
	{
		for( Player player : players )
			player.movePlayer();
	}

	public World getWorld()
	{
		return world;
	}

	public boolean conquer( AbstractPrivateProperty abstractPrivateProperty, Player player )
	{
		return player.employSubaltern( abstractPrivateProperty ) != null;

	}

	public List<Player> getPlayers()
	{
		return players;
	}

	public ArrayList<Player> attackPhysically( Player attacker )
	{
		return CombatManager.getInstance().attackPhysically( attacker );
	}

	public ArrayList<Player> distanceAttack( Player attacker, Potions potion )
	{
		return CombatManager.getInstance().distanceAttack( attacker, potion );
	}

	// public ArrayList<Player> getPlayersIntTheRange( float xMin, float xMax, float yMin, float
	// yMax )
	// {
	// try
	// {
	// for( Player p : players )
	// p.getReadLock();
	//
	// ArrayList<Player> _players = new ArrayList<>();
	//
	// if( xMin > xMax )
	// {
	// float tmp = xMin;
	// xMin = xMax;
	// xMax = tmp;
	// }
	//
	// if( yMin > yMax )
	// {
	// float tmp = yMin;
	// xMin = yMax;
	// xMax = tmp;
	// }
	//
	// for( Player p : players )
	// {
	//
	// }
	//
	// return _players;
	// } finally
	// {
	// for( Player p : players )
	// p.leaveReadLock();
	// }
	// }

	@Override
	public String toString()
	{
		String s = new String();
		s += world.toString();

		s += "\n" + players.size();
		return s;
	}

}

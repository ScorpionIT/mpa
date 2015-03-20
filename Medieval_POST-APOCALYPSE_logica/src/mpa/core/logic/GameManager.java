package mpa.core.logic;

import java.util.ArrayList;
import java.util.List;

import mpa.core.logic.building.AbstractPrivateProperty;
import mpa.core.logic.character.Player;

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
		}
	}

	private GameManager( World world, List<Player> players )
	{
		this.world = world;
		this.players = players;
	}

	// public ArrayList<Pair<Integer, Integer>> computePath(Player player, float xGoal, float yGoal)
	// {
	// ProvaIntegerPathCalculator pathCalculator = new ProvaIntegerPathCalculator();
	// return pathCalculator.computePath(world, xGoal, yGoal, player.getX(), player.getY());
	//
	// // una volta era Float
	//
	// // TODO
	// }
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

	@Override
	public String toString()
	{
		String s = new String();
		s += world.toString();

		s += "\n" + players.size();
		return s;
	}

}

package mpa.core.logic;

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

	public static void init(World world, List<Player> players)
	{
		if (gameManager == null)
		{
			gameManager = new GameManager(world, players);
		}
	}

	private GameManager(World world, List<Player> players)
	{
		this.world = world;
		this.players = players;
	}

	public void computePath(Player player, float xGoal, float yGoal)
	{
		PathCalculator pathCalculator = new LongRangePathCalculator();
		pathCalculator.computePath(world, player, xGoal, yGoal);

		// TODO
	}

	public World getWorld()
	{
		return world;
	}

	public boolean conquer(AbstractPrivateProperty abstractPrivateProperty, Player player)
	{
		return player.employSubaltern(abstractPrivateProperty) != null;

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

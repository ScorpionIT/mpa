package mpa.core.logic;

import java.util.List;

import mpa.core.logic.building.AbstractPrivateProperty;
import mpa.core.logic.character.DependentCharacter;
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
		// TODO
	}

	public boolean conquer(AbstractPrivateProperty abstractPrivateProperty, Player player)
	{
		DependentCharacter employSubaltern = player.employSubaltern(abstractPrivateProperty);

		return true;
	}

}

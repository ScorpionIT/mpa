package mpa.core.logic;

import java.util.List;

import mpa.core.logic.building.Headquarter;
import mpa.core.logic.character.Player;

public class GameManager
{
	private World world;
	private List<Player> players;

	public GameManager(MapManager mapInfo, int numPlayer, String playersName[], int position[]) throws Exception
	{
		world = mapInfo.decode();
		for (int i = 0; i <= numPlayer; i++)
		{
			Headquarter headquarter = world.addHeadquarter(position[i]);		
			players.add(new Player(playersName[i], 0, 0, 100, Level.NEWBIE, headquarter, 10)); // TODO
		}
	}
}

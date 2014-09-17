package mpa.core.logic;

import java.util.List;

import mpa.core.logic.building.Headquarter;
import mpa.core.logic.character.Player;

@SuppressWarnings("unused")
public class GameManager
{
	private World world;
	private List<Player> players;

	public GameManager(String mapName, int numPlayer, String playersName[], int position[])
	{
		// Instazio il WM e gli dico di caricare la mappa mapname
		WorldManager WorldManager = new WorldManager(mapName);

		for (int i = 0; i < numPlayer; i++)
		{
			Headquarter playerHeadquarter = WorldManager.addHeadquarter(i);
			players.add(new Player(playersName[i], playerHeadquarter.getX(), playerHeadquarter
					.getY(), 100, Level.NEWBIE, playerHeadquarter, 0)); // TODO
		}

	}

}

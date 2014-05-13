package mpa.core.logic;

import java.util.List;

@SuppressWarnings("unused")
public class GameManager
{
	private World world;
	private List<Player> players;

	public GameManager(String mapname, int numplayer, String PlayersName[], int position[])
	{
		// Instazio il WM e gli dico di caricare la mappa mapname
		WorldManager WorldManager = new WorldManager(mapname);

		for (int i = 0; i < numplayer; i++)
		{
			Headquarter playerHeadquarter = WorldManager.addHeadquarter(i);
			players.add(new Player(PlayersName[i], playerHeadquarter.getX(), playerHeadquarter
					.getY(), 100, Level.NEWBIE, playerHeadquarter, 0)); // TODO
		}

	}

}

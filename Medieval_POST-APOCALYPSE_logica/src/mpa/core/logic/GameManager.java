package mpa.core.logic;

import java.util.List;

@SuppressWarnings("unused")
public class GameManager
{
	private World world;
	private List<Player> players;

	public GameManager(String mapname, int numplayer, int position[])
	{
		// Instazio il WM e gli dico di caricare la mappa mapname
		WorldManager WorldManager = new WorldManager(mapname);

		for (int pos : position)
		{
			Headquarter playerHeadquarter = WorldManager.addHeadquarter(pos);
			players.add(new Player("Pippo", 0, 0, 100, Level.NEWBIE, playerHeadquarter, 0)); // TODO
		}

	}

}

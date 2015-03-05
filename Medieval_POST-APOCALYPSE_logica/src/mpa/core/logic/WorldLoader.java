package mpa.core.logic;

import java.util.ArrayList;

import mpa.core.logic.building.Headquarter;
import mpa.core.logic.character.Player;

public class WorldLoader
{
	MapInfo mapInfo;

	public void loadMapInfo(MapInfoCreator mapInfoCreator, String path)
	{
		mapInfo = mapInfoCreator.createMapInfo(path);
	}

	public MapInfo getMapInfo()
	{
		return mapInfo;
	}

	public World loadWorld(WorldCreator worldCreator)
	{
		World world = worldCreator.createWorld(mapInfo);
		return world;

	}

	public ArrayList<Player> createSinglePlayer(String playerName, World world, Pair<Float, Float> selectedHQ)
	{
		ArrayList<Player> players = new ArrayList<>();
		ArrayList<Pair<Float, Float>> headQuarters = mapInfo.getHeadQuarters();
		for (int i = 0; i < headQuarters.size(); i++)
		{
			if (selectedHQ.equals(headQuarters.get(i)))
			{
				Player player = new Player(playerName, selectedHQ.getFirst(), selectedHQ.getSecond() + 10, 100, Level.NEWBIE, null, 100);
				Headquarter headquarter = new Headquarter(headQuarters.get(i).getFirst(), headQuarters.get(i).getSecond(), player);
				world.addObject(headquarter);
				player.setHeadquarter(headquarter);
			}
			else
			{
				Player player = new Player("Giocatore " + (i + 1), headQuarters.get(i).getFirst(), headQuarters.get(i).getSecond() + 10, 100,
						Level.NEWBIE, null, 100);
				Headquarter headquarter = new Headquarter(headQuarters.get(i).getFirst(), headQuarters.get(i).getSecond(), player);
				world.addObject(headquarter);
				player.setHeadquarter(headquarter);

			}
		}
		return players;
	}
}

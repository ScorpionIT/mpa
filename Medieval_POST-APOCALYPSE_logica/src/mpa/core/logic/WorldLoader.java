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

	public ArrayList<Player> makePlayers(String playerName, World world, Pair<Float, Float> selectedHQ)
	{
		ArrayList<Player> players = new ArrayList<>();
		ArrayList<Pair<Float, Float>> headQuarters = mapInfo.getHeadQuarters();
		for (int i = 0; i < headQuarters.size(); i++)
		{
			if (selectedHQ.equals(headQuarters.get(i)))
			{
				Headquarter headquarter = new Headquarter(headQuarters.get(i).getFirst(), headQuarters.get(i).getSecond(), null);
				Player player = new Player(playerName, headquarter.getX() + headquarter.getWidth() / 2 + 120, headquarter.getY()
						+ headquarter.getHeight() / 2 + 120, 100, Level.NEWBIE, headquarter, 100);
				headquarter.setOwner(player);
				world.addObject(headquarter);
				players.add(player);
			}
			else
			{
				Headquarter headquarter = new Headquarter(headQuarters.get(i).getFirst(), headQuarters.get(i).getSecond(), null);
				Player player = new Player("Giocatore " + (i + 1), headquarter.getX() + headquarter.getWidth() / 2 + 120, headquarter.getY()
						+ headquarter.getHeight() / 2 + 120, 100, Level.NEWBIE, headquarter, 100);
				headquarter.setOwner(player);
				world.addObject(headquarter);
				players.add(player);

			}
		}
		return players;
	}
}

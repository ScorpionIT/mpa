package mpa.core.logic;

import java.util.ArrayList;

import javax.vecmath.Vector2f;

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

	public int makePlayers(String playerName, World world, Pair<Float, Float> selectedHQ)
	{
		int headquarterID = 0;
		ArrayList<Pair<Float, Float>> headQuarters = mapInfo.getHeadQuarters();
		int selectedHQIndex = 0;
		for (int i = 0; i < headQuarters.size(); i++)
		{
			Headquarter headquarter = new Headquarter(headQuarters.get(i).getFirst(), headQuarters.get(i).getSecond(), null);
			Vector2f gatheringPlace = headquarter.getGatheringPlace();

			Player player = new Player(playerName, gatheringPlace.x, gatheringPlace.y, 100, Level.NEWBIE, headquarter);
			headquarter.setOwner(player);
			headquarter.setID(Integer.toString(headquarterID));
			world.addObject(headquarter);
			if (selectedHQ.equals(headQuarters.get(i)))
			{
				GameManager.getInstance().addPlayer(player);
				selectedHQIndex = i;
			}
			else
			{
				player.setName("Paola Maledetta " + i);
				GameManager.getInstance().addAIPlayer(player);
			}
		}

		return selectedHQIndex;
	}
}

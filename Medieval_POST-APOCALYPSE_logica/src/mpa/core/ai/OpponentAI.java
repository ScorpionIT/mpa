package mpa.core.ai;

import java.util.ArrayList;

import mpa.core.logic.AbstractObject;
import mpa.core.logic.character.Player;

public class OpponentAI extends Thread
{
	Player player;
	AIState aiState = new ExplorationState();
	AIWorldManager worldManager;
	ArrayList<Player> knownPlayers = new ArrayList<>();
	ArrayList<AbstractObject> knownBuildings = new ArrayList<>();
	boolean knownAllTheWorld;

	public OpponentAI( Player player, DifficultyLevel level )
	{
		this.player = player;
		worldManager = new AIWorldManager( level );
	}

	@Override
	public void run()
	{

	}

	void addBuilding( AbstractObject building )
	{
		if( !knownBuildings.contains( building ) )
			knownBuildings.add( building );
	}

	boolean areThereWeakerPlayers()
	{
		for( Player p : knownPlayers )
			if( p.getPlayerLevel().ordinal() < player.getPlayerLevel().ordinal() )
				return true;

		return false;
	}
}

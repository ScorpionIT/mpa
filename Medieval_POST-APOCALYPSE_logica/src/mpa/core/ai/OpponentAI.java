package mpa.core.ai;

import java.util.ArrayList;

import mpa.core.logic.AbstractObject;
import mpa.core.logic.building.AbstractPrivateProperty;
import mpa.core.logic.character.Player;
import mpa.core.logic.tool.Potions;

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
		if( level.equals( DifficultyLevel.values()[DifficultyLevel.values().length - 1] ) )
			knownAllTheWorld = true;
		else
			worldManager = new AIWorldManager( level );
	}

	@Override
	public void run()
	{
		while( true )
		{
			try
			{
				sleep( 1500 );
			} catch( InterruptedException e )
			{
				e.printStackTrace();
			}

			// System.out.println( "sto ciclando " );
			aiState.action( this );
			aiState = aiState.changeState( this );
		}
	}

	void addBuilding( AbstractObject building )
	{
		if( !knownBuildings.contains( building ) )
			knownBuildings.add( building );
	}

	private boolean isOpponentPlayerWeaker( Player p )
	{
		if( player.getPlayerLevel().equals(
				DifficultyLevel.values()[DifficultyLevel.values().length - 1] ) )
		{
			int opponentPotions = 0;
			int playerPotions = 0;

			for( Potions potion : Potions.values() )
			{
				opponentPotions += p.getPotionAmount( potion );
				playerPotions += p.getPotionAmount( potion );
			}

			if( opponentPotions < playerPotions )
				return true;
			else
				return false;
		}

		else if( p.getPlayerLevel().ordinal() < player.getPlayerLevel().ordinal() )
			return true;

		return false;
	}

	boolean areThereWeakerPlayers()
	{
		for( Player p : knownPlayers )
			if( isOpponentPlayerWeaker( p ) )
				return true;

		return false;
	}

	boolean areThereConquerableBuildings()
	{

		for( AbstractObject b : knownBuildings )
		{
			if( b instanceof AbstractPrivateProperty
					&& ( ( !( ( AbstractPrivateProperty ) b ).isFree() && isOpponentPlayerWeaker( ( ( AbstractPrivateProperty ) b )
							.getOwner() ) ) || ( ( AbstractPrivateProperty ) b ).isFree() ) )
				return true;

		}
		return false;
	}
}

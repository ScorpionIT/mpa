package mpa.core.ai;

import java.util.ArrayList;

import javax.vecmath.Vector2f;

import mpa.core.logic.AbstractObject;
import mpa.core.logic.building.House;
import mpa.core.logic.building.Market;
import mpa.core.logic.character.Player;
import mpa.core.logic.resource.AbstractResourceProducer;
import mpa.core.logic.tool.Potions;

public class OpponentAI extends Thread
{
	Player player;
	AIState aiState = new ExplorationState();
	AIWorldManager worldManager;
	ArrayList<Player> knownPlayers = new ArrayList<>();
	ArrayList<AbstractObject> knownBuildings = new ArrayList<>();

	private ArrayList<House> knownHeadQuarters = new ArrayList<>();
	private ArrayList<AbstractResourceProducer> knownResourceProducers = new ArrayList<>();
	private Vector2f market = null;

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
		int c = 0;
		while( true )
		{
			try
			{
				sleep( 1500 );
			} catch( InterruptedException e )
			{
				e.printStackTrace();
			}

			// aiState.action( this );
			// aiState = aiState.changeState( this );
		}
	}

	private void addBuilding( AbstractObject building )
	{
		if( !knownBuildings.contains( building ) )
			knownBuildings.add( building );

		if( building instanceof AbstractResourceProducer
				&& !knownResourceProducers.contains( building ) )
			knownResourceProducers.add( ( AbstractResourceProducer ) building );
		if( building instanceof House && !knownHeadQuarters.contains( building ) )
			knownHeadQuarters.add( ( House ) building );
		if( building instanceof Market )
			market = Market.getInstance().getGatheringPlace();

		// System.out.println();
		// System.out.println( "ho aggiunto un building !!!" );
		// System.out.println();
	}

	void addBuildings( Vector2f position )
	{
		ArrayList<AbstractObject> newBuildings = worldManager.getBuildings( position );

		for( AbstractObject obj : newBuildings )
			addBuilding( obj );
	}

	public ArrayList<House> getKnownHeadQuarters()
	{
		return knownHeadQuarters;
	}

	public ArrayList<AbstractResourceProducer> getKnownResourceProducers()
	{
		return knownResourceProducers;
	}

	public Vector2f getMarket()
	{
		return market;
	}

	public boolean knowsTheMarket()
	{
		return market != null;
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

		for( AbstractResourceProducer b : knownResourceProducers )
		{
			if( b.isFree() || ( isOpponentPlayerWeaker( b.getOwner() ) ) )
				return true;
		}
		return false;
	}
}

package mpa.core.ai;

import java.util.ArrayList;

import javax.vecmath.Vector2f;

import mpa.core.logic.AbstractObject;
import mpa.core.logic.GameManager;
import mpa.core.logic.MyThread;
import mpa.core.logic.building.Headquarter;
import mpa.core.logic.building.House;
import mpa.core.logic.character.Player;
import mpa.core.logic.character.Player.Item;
import mpa.core.logic.resource.AbstractResourceProducer;
import mpa.core.logic.tool.Potions;

public class OpponentAI extends MyThread
{
	Player player;
	AIState aiState = new ExplorationState();
	AIWorldManager worldManager;
	ArrayList<Player> knownPlayers = new ArrayList<>();
	ArrayList<AbstractObject> knownBuildings = new ArrayList<>();

	private ArrayList<House> knownHeadQuarters = new ArrayList<>();
	private ArrayList<AbstractResourceProducer> knownResourceProducers = new ArrayList<>();

	boolean knownAllTheWorld = false;

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
		while( player.getHP() > 0 )
		{
			super.run();
			try
			{
				sleep( 500 );
			} catch( InterruptedException e )
			{
				e.printStackTrace();
			}

			preroutine();
			aiState.action( this );
			aiState = aiState.changeState( this );
		}

		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println( " SONO " + player.getName().toUpperCase() + " E ME LA SONO QUAZATA" );
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
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
	}

	void addBuildings( Vector2f position )
	{
		ArrayList<AbstractObject> newBuildings = worldManager.getBuildings( position );

		for( AbstractObject obj : newBuildings )
		{
			if( obj instanceof Headquarter )
			{
				if( !knownPlayers.contains( ( ( Headquarter ) obj ).getOwner() )
						&& ( ( Headquarter ) obj ).getOwner() != player )
				{
					knownPlayers.add( ( ( Headquarter ) obj ).getOwner() );
				}
			}
			addBuilding( obj );
		}
	}

	public ArrayList<House> getKnownHeadQuarters()
	{
		return knownHeadQuarters;
	}

	public ArrayList<AbstractResourceProducer> getKnownResourceProducers()
	{
		return knownResourceProducers;
	}

	private boolean isOpponentPlayerWeaker( Player p )
	{
		if( p == player )
			return false;
		if( player.getPlayerLevel().ordinal() == p.getPlayerLevel().ordinal() )
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
		ArrayList<Player> deads = new ArrayList<>();
		try
		{
			for( Player p : knownPlayers )
				if( GameManager.getInstance().isPlayerDead( p ) )
					deads.add( p );
				else if( isOpponentPlayerWeaker( p ) )
					return true;

			return false;
		} finally
		{
			for( Player p : deads )
				knownPlayers.remove( p );
		}
	}

	boolean areThereConquerableBuildings()
	{

		for( AbstractResourceProducer b : knownResourceProducers )
		{
			if( b.isFree() /* || ( isOpponentPlayerWeaker( b.getOwner() ) ) */)
				return true;
		}
		return false;
	}

	private void preroutine()
	{
		if( player.getHP() < 100 )
		{
			GameManager.getInstance().changeSelectedItem( player, Item.HP_POTION );
			GameManager.getInstance().playerAction( player, null );
		}
		if( player.getMP() < 100 )
		{
			GameManager.getInstance().changeSelectedItem( player, Item.MP_POTION );
			GameManager.getInstance().playerAction( player, null );
		}
	}

	boolean canIFightWithHim( Player bully )
	{
		if( player.getHP() < 5 )
			return false;
		if( player.getMP() < 5 )
			return false;
		if( player.getPotionAmount( Potions.GRANADE ) == 0 )
			return false;
		System.out.println( "ma sto bully ? " + bully );
		if( player.getPlayerLevel().ordinal() < bully.getPlayerLevel().ordinal() )
			return false;

		return true;
	}

	boolean shouldBuyPotions()
	{
		for( Potions p : Potions.values() )
			if( player.getPotionAmount( p ) == 0 )
				return true;

		return false;
	}

	public Player getPlayer()
	{
		return player;
	}

	public void gotAttackedBy( Player bully )
	{
		aiState.heIsAttackingYou( bully );
	}
}

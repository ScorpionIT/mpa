package mpa.core.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import javax.vecmath.Vector2f;

import mpa.core.ai.DifficultyLevel;
import mpa.core.ai.OpponentAI;
import mpa.core.logic.building.AbstractPrivateProperty;
import mpa.core.logic.character.DependentCharacter;
import mpa.core.logic.character.Player;
import mpa.core.logic.character.Player.Item;
import mpa.core.logic.fights.CombatManager;
import mpa.core.logic.tool.Potions;

public class GameManager
{
	private World world;
	private List<Player> players;
	private List<OpponentAI> AI_players;
	private DifficultyLevel difficultyLevel;
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	private ReadLock readLock = lock.readLock();
	private WriteLock writeLock = lock.writeLock();

	private static GameManager gameManager = null;

	public static GameManager getInstance()
	{
		return gameManager;
	}

	public void takeLock()
	{
		readLock.lock();
	}

	public void leaveLock()
	{
		readLock.unlock();
	}

	public static void init( World world, DifficultyLevel level )
	{
		if( gameManager == null )
		{
			gameManager = new GameManager( world, level );
			ThreadManager.getInstance().addGameThread( new PositionUpdater() );
			ThreadManager.getInstance().addGameThread( new ResourceUpdater() );
			ThreadManager.getInstance().startGameThreads();
		}
	}

	private GameManager( World world, DifficultyLevel level )
	{
		this.world = world;
		this.players = new ArrayList<Player>();
		this.AI_players = new ArrayList<>();
		this.difficultyLevel = level;
	}

	public void computePath( Player player, float xGoal, float yGoal )
	{
		PathCalculatorThread pathCalculatorThread = new PathCalculatorThread( player, xGoal, yGoal );
		pathCalculatorThread.start();
	}

	public void addPlayer( Player player )
	{
		players.add( player );
	}

	public void addAIPlayer( Player player )
	{
		OpponentAI newAI = new OpponentAI( player, difficultyLevel );
		AI_players.add( newAI );
		addPlayer( player );
		// newAI.start();
	}

	public void updateCharacterPositions()
	{
		readLock.lock();
		for( Player player : players )
		{
			player.movePlayer();
			for( DependentCharacter dC : player.getSubalterns() )
				dC.movePlayer();
		}
		readLock.unlock();
	}

	public World getWorld()
	{
		return world;
	}

	public boolean conquer( AbstractPrivateProperty abstractPrivateProperty, Player player )
	{
		Vector2f gatheringPlace = abstractPrivateProperty.getGatheringPlace();
		if( ( ( int ) gatheringPlace.x ) != ( ( int ) player.x )
				|| ( ( int ) gatheringPlace.y ) != ( ( int ) player.y ) )
		{
			computePath( player, gatheringPlace.x, gatheringPlace.y );
			return false;
		}

		DependentCharacter employSubaltern = player.employSubaltern( abstractPrivateProperty );
		if( employSubaltern != null )
		{
			abstractPrivateProperty.setOwner( player );
			return true;
		}
		return false;

	}

	public void killPlayer( Player p )
	{
		writeLock.lock();
		p.die();
		if( players.contains( p ) )
			players.remove( p );
		if( AI_players.contains( p ) )
			AI_players.remove( p );
		writeLock.unlock();
	}

	public List<Player> getPlayers()
	{
		return players;
	}

	private ArrayList<Player> attackPhysically( Player attacker )
	{
		System.out.println( "sto per attaccare" );
		return CombatManager.getInstance().attackPhysically( attacker );
	}

	private ArrayList<Player> distanceAttack( Player attacker, Potions potion, Vector2f target )
	{
		return CombatManager.getInstance().distanceAttack( attacker, potion, target );
	}

	public ArrayList<Player> playerAction( Player p, Vector2f target )
	{
		Item selectedItem = p.getSelectedItem();
		ArrayList<Player> hitPlayers = null;

		switch( selectedItem )
		{
			case WEAPON:
				hitPlayers = attackPhysically( p );
				break;
			case GRANADE:
				hitPlayers = distanceAttack( p, p.takePotion( Potions.GRANADE ), target );
				break;
			case FLASH_BANG:
				hitPlayers = distanceAttack( p, p.takePotion( Potions.FLASH_BANG ), target );
				break;
			default:
				hitPlayers = new ArrayList<>();
				hitPlayers.add( p );

		}
		return hitPlayers;
	}

	public boolean occupyProperty( Player player, AbstractPrivateProperty property )
	{
		if( !property.isFree() || !player.isThereAnyFreeSulbaltern() )
			return false;

		property.setOwner( player );
		player.employSubaltern( property );
		return true;
	}

	public void setPause()
	{
		ThreadManager.getInstance().pause( !ThreadManager.getInstance().getPauseState() );
	}

	public void startFlashTimer( Player p )
	{
		ThreadManager.getInstance().startFlashBangThread( p );
	}

	public void changeSelectedItem( Player p, Item selected )
	{
		p.setSelectedItem( selected );
	}

	public boolean getPauseState()
	{
		return ThreadManager.getInstance().getPauseState();
	}

	@Override
	public String toString()
	{
		String s = new String();
		s += world.toString();

		s += "\n" + players.size();
		return s;
	}

}

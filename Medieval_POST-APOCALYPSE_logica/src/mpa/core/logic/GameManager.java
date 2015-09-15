package mpa.core.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import javax.vecmath.Vector2f;

import mpa.core.ai.DifficultyLevel;
import mpa.core.ai.OpponentAI;
import mpa.core.logic.building.AbstractPrivateProperty;
import mpa.core.logic.building.ControllerAlreadyPresentException;
import mpa.core.logic.building.DifferentOwnerException;
import mpa.core.logic.building.Tower;
import mpa.core.logic.character.AbstractCharacter;
import mpa.core.logic.character.DependentCharacter;
import mpa.core.logic.character.Minion;
import mpa.core.logic.character.Player;
import mpa.core.logic.character.Player.Item;
import mpa.core.logic.character.TowerCrusher;
import mpa.core.logic.fights.CombatManager;
import mpa.core.logic.tool.Potions;
import mpa.core.maths.MyMath;
import mpa.core.util.GameProperties;

public class GameManager
{
	private World world;
	private List<Player> players;
	private HashMap<Player, OpponentAI> AI_players;
	private List<Tower> towers = new ArrayList<Tower>();
	private DifficultyLevel difficultyLevel;
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	private ReadLock readLock = lock.readLock();
	private WriteLock writeLock = lock.writeLock();
	private ArrayList<Minion> minionsAlive = new ArrayList<>();

	private IDPool minionIDs;
	private IDPool towerCrusherIDs;

	private ArrayList<Player> deadPlayers = new ArrayList<>();
	private ArrayList<Minion> deadMinions = new ArrayList<>();

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

	public static void init( World world, DifficultyLevel level, boolean multiplayer )
	{
		if( gameManager == null )
		{
			gameManager = new GameManager( world, level );
			ThreadManager.getInstance().addGameThread( new PositionUpdater() );
			ThreadManager.getInstance().addGameThread( new ResourceUpdater() );
			ThreadManager.getInstance().startGameThreads();

			if( multiplayer )
				;
			else
				;
		}
	}

	private GameManager( World world, DifficultyLevel level )
	{
		this.world = world;
		this.players = new ArrayList<Player>();
		AI_players = new HashMap<>();
		this.difficultyLevel = level;
		minionIDs = new IDPool( 100 );
		towerCrusherIDs = new IDPool( 100 );
	}

	public void computePath( AbstractCharacter player, float xGoal, float yGoal )
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
		AI_players.put( player, newAI );
		addPlayer( player );
		newAI.start();
	}

	public ArrayList<Minion> createMinions( Player boss, int quantity, Player target )
	{
		ArrayList<Minion> minions = new ArrayList<>();

		for( int i = 0; i < quantity; i++ )
			minions.add( boss.createMinion( target, minionIDs.getID() ) );

		return minions;
	}

	public ArrayList<TowerCrusher> createTowerCrushers( Player boss, int quantity, Tower target )
	{
		ArrayList<TowerCrusher> towerCrushers = new ArrayList<>();
		for( int i = 0; i < quantity; i++ )
			towerCrushers.add( boss.createTowerCrusher( target, towerCrusherIDs.getID() ) );

		return towerCrushers;
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

	public boolean addWorker( Player player, AbstractPrivateProperty abstractPrivateProperty )
	{
		if( abstractPrivateProperty.getOwner() != player )
			return false;

		DependentCharacter employee = player.employSubaltern( abstractPrivateProperty );
		if( employee == null )
			return false;

		try
		{
			abstractPrivateProperty.setController( employee );
		} catch( ControllerAlreadyPresentException | DifferentOwnerException e )
		{
			e.printStackTrace();
		}
		return true;
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
			try
			{
				abstractPrivateProperty.setController( employSubaltern );
				computePath( employSubaltern, abstractPrivateProperty.getGatheringPlace().x,
						abstractPrivateProperty.getGatheringPlace().y );
			} catch( ControllerAlreadyPresentException | DifferentOwnerException e )
			{
				e.printStackTrace();
			}
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
		if( AI_players.keySet().contains( p ) )
			AI_players.remove( p );

		deadPlayers.add( p );
		writeLock.unlock();
	}

	public void killMinion( Minion m )
	{
		deadMinions.add( m );
		minionsAlive.remove( m );
	}

	// TODO killTowerCrusher

	public List<Player> getPlayers()
	{
		return players;
	}

	ArrayList<Player> takeDeadPlayers()
	{
		try
		{
			return deadPlayers;
		} finally
		{
			deadPlayers.clear();
		}
	}

	ArrayList<Minion> takeDeadMinions()
	{
		try
		{
			return deadMinions;
		} finally
		{
			deadMinions.clear();
		}
	}

	// TODO takeDeadTowerCrushers

	ArrayList<Player> attackPhysically( Player attacker )
	{
		System.out.println( "sto per attaccare" );
		return CombatManager.getInstance().attackPhysically( attacker );
	}

	public ArrayList<Player> attackPhysically( Minion attacker )
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
		if( target != null )
			p.setDirection( MyMath.computeDirection( p.getPosition(), target ) );

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

		for( Player hitPlayer : hitPlayers )
		{
			if( hitPlayer != p && AI_players.keySet().contains( hitPlayer ) )
				AI_players.get( hitPlayer ).gotAttackedBy( p );

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

	public Tower createTower( Player p, Vector2f position )
	{
		try
		{
			p.getWriteLock();
			if( !p.hasEnoughResources( GameProperties.getInstance().getPrices( "tower" ) ) )
				return null;

			Tower tower = new Tower( position.x, position.y, GameProperties.getInstance()
					.getObjectWidth( "tower" ), GameProperties.getInstance().getObjectHeight(
					"tower" ), p );
			if( !world.addTower( tower ) )
				return null;

			p.takeResources( GameProperties.getInstance().getPrices( "tower" ) );
			p.addTower( tower );
			towers.add( tower );

			return tower;
		} finally
		{
			p.leaveWriteLock();
		}
	}

	void checkForTowerDamages()
	{
		for( Tower t : towers )
		{
			ArrayList<Player> hitPlayers = t.attack();

			for( Player p : hitPlayers )
				if( p.getHP() < 0 )
					killPlayer( p );
		}
	}

	public Level getPlayerLevel( Player player )
	{
		return player.getPlayerLevel();
	}

	public void destroyTower( Tower t )
	{
		world.destroyObject( t );
		t.getOwner().removeTower( t );
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

	public void attackPhysically( TowerCrusher towerCrusher )
	{
		CombatManager.getInstance().attackOnTower( towerCrusher );
	}

	public boolean isPlayerDead( Player target )
	{
		return !players.contains( target );
	}

	public boolean isTowerDestroyed( Tower t )
	{
		return !towers.contains( t );
	}
}

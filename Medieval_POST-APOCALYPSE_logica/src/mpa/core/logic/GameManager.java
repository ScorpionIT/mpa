package mpa.core.logic;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector2f;

import mpa.core.ai.DifficultyLevel;
import mpa.core.ai.OpponentAI;
import mpa.core.logic.building.AbstractPrivateProperty;
import mpa.core.logic.character.Player;
import mpa.core.logic.fights.CombatManager;
import mpa.core.logic.tool.Potions;

public class GameManager
{
	private World world;
	private List<Player> players;
	private List<OpponentAI> AI_players;
	private DifficultyLevel difficultyLevel;

	private static GameManager gameManager = null;

	public static GameManager getInstance()
	{
		return gameManager;
	}

	public static void init( World world, DifficultyLevel level )
	{
		if( gameManager == null )
		{
			gameManager = new GameManager( world, level );
			new PositionUpdater().start();
			new ResourceUpdater().start();
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
		AI_players.add( new OpponentAI( player, difficultyLevel ) );
		players.add( player );
		AI_players.get( AI_players.size() - 1 ).start();
	}

	public void updateCharacterPositions()
	{
		for( Player player : players )
			player.movePlayer();
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
			return false;

		return player.employSubaltern( abstractPrivateProperty ) != null;

	}

	public List<Player> getPlayers()
	{
		return players;
	}

	public ArrayList<Player> attackPhysically( Player attacker )
	{
		return CombatManager.getInstance().attackPhysically( attacker );
	}

	public ArrayList<Player> distanceAttack( Player attacker, Potions potion )
	{
		return CombatManager.getInstance().distanceAttack( attacker, potion );
	}

	public boolean occupyProperty( Player player, AbstractPrivateProperty property )
	{
		if( !property.isFree() || !player.isThereAnyFreeSulbaltern() )
			return false;

		property.setOwner( player );
		player.employSubaltern( property );
		return true;
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

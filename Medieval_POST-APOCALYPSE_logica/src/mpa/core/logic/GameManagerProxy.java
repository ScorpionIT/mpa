package mpa.core.logic;

import java.util.ArrayList;
import java.util.HashMap;

import javax.vecmath.Vector2f;

import mpa.core.logic.building.AbstractPrivateProperty;
import mpa.core.logic.building.Headquarter;
import mpa.core.logic.building.Tower;
import mpa.core.logic.character.Minion;
import mpa.core.logic.character.Player;
import mpa.core.logic.character.Player.Item;
import mpa.core.logic.resource.Cave;
import mpa.core.logic.resource.Field;
import mpa.core.logic.resource.Mine;
import mpa.core.logic.resource.Wood;

public class GameManagerProxy
{
	private static GameManagerProxy gameManagerProxy = null;
	private GameManager gm;
	private HashMap<String, Player> players = new HashMap<>();
	private HashMap<String, Headquarter> player_headquarter = new HashMap<>();
	private HashMap<String, Headquarter> headquarters = new HashMap<>();
	private HashMap<String, Field> fields = new HashMap<>();
	private HashMap<String, Cave> caves = new HashMap<>();
	private HashMap<String, Mine> mines = new HashMap<>();
	private HashMap<String, Wood> woods = new HashMap<>();
	private HashMap<String, Minion> minions = new HashMap<>();
	private HashMap<String, Tower> towers = new HashMap<>();

	protected GameManagerProxy( GameManager gm )
	{
		this.gm = gm;
		for( Player p : gm.getPlayers() )
		{
			players.put( p.getName(), p );
			player_headquarter.put( p.getName(), p.getHeadquarter() );
			headquarters.put( p.getHeadquarter().getID(), p.getHeadquarter() );
		}

		for( AbstractObject obj : gm.getWorld().getAllObjects() )
		{
			if( obj instanceof Wood )
				woods.put( obj.getID(), ( Wood ) obj );
			else if( obj instanceof Cave )
				caves.put( obj.getID(), ( Cave ) obj );
			else if( obj instanceof Mine )
				mines.put( obj.getID(), ( Mine ) obj );
			else if( obj instanceof Field )
				fields.put( obj.getID(), ( Field ) obj );
		}
	}

	public static void init( GameManager gm )
	{
		if( gameManagerProxy == null )
			gameManagerProxy = new GameManagerProxy( gm );
	}

	public static GameManagerProxy getInstance()
	{
		return gameManagerProxy;
	}

	public void computePath( String player, float xGoal, float yGoal )
	{
		gm.computePath( players.get( player ), xGoal, yGoal );
	}

	public ArrayList<String> createMinions( String boss, int quantity, String target )
	{
		ArrayList<Minion> createdMinions = gm.createMinions( players.get( boss ), quantity,
				players.get( target ) );
		ArrayList<String> minionNames = new ArrayList<>();
		for( Minion minion : createdMinions )
		{
			minions.put( minion.getID(), minion );
			minionNames.add( minion.getID() );
		}

		return minionNames;
	}

	public boolean conquer( String abstractPrivateProperty, String player )
	{

		AbstractPrivateProperty obj = null;

		String[] field = abstractPrivateProperty.split( ":" );

		if( field[0].equals( "CAVE" ) )
			obj = caves.get( field[1] );
		else if( field[0].equals( "MINE" ) )
			obj = mines.get( field[1] );

		else if( field[0].equals( "FIELD" ) )
			obj = fields.get( field[1] );

		else if( field[0].equals( "WOOD" ) )
			obj = woods.get( field[1] );

		return gm.conquer( obj, players.get( player ) );
	}

	public ArrayList<String> attackPhysically( String attacker )
	{
		ArrayList<Player> hitPlayers = gm.attackPhysically( minions.get( attacker ) );

		ArrayList<String> hitPlayersNames = new ArrayList<>();

		for( Player p : hitPlayers )
			hitPlayersNames.add( p.getName() );

		return hitPlayersNames;
	}

	public ArrayList<String> playerAction( String p, Vector2f target )
	{
		ArrayList<Player> hitPlayers = gm.playerAction( players.get( p ), target );

		ArrayList<String> hitPlayersNames = new ArrayList<>();

		for( Player player : hitPlayers )
			hitPlayersNames.add( player.getName() );

		return hitPlayersNames;

	}

	public boolean occupyProperty( String player, String property )
	{
		AbstractPrivateProperty obj = null;

		String[] field = property.split( ":" );

		if( field[0].equals( "CAVE" ) )
			obj = caves.get( field[1] );
		else if( field[0].equals( "MINE" ) )
			obj = mines.get( field[1] );

		else if( field[0].equals( "FIELD" ) )
			obj = fields.get( field[1] );

		else if( field[0].equals( "WOOD" ) )
			obj = woods.get( field[1] );
		return gm.occupyProperty( players.get( player ), obj );
	}

	public String createTower( String p, Vector2f position )
	{

		Tower tower = gm.createTower( players.get( p ), position );

		if( tower != null )
		{
			towers.put( tower.getID(), tower );
			return tower.getID();
		}

		return "";
	}

	public void setPause()
	{
		gm.setPause();
	}

	public void changeSelectedItem( String p, String selected )
	{
		Item item = null;

		switch( selected )
		{
			case "WEAPON":
				item = Item.WEAPON;
				break;
			case "FLASH_BANG":
				item = Item.FLASH_BANG;
				break;
			case "GRANADE":
				item = Item.GRANADE;
				break;
			case "HP_POTION":
				item = Item.HP_POTION;
				break;
			case "MP_POTION":
				item = Item.MP_POTION;
				break;
			default:
				return;
		}

		gm.changeSelectedItem( players.get( p ), item );
	}

	public ArrayList<String> takeDeadPlayers()
	{
		ArrayList<Player> deadPlayers = gm.takeDeadPlayers();
		ArrayList<String> names = new ArrayList<>();

		for( Player p : deadPlayers )
			names.add( p.getName() );

		return names;
	}

	public ArrayList<String> takeDeadMinions()
	{
		ArrayList<Minion> deadMinions = gm.takeDeadMinions();
		ArrayList<String> names = new ArrayList<>();

		for( Minion m : deadMinions )
			names.add( m.getName() );

		return names;
	}

	// TODO takeDeadTowerCrushers

	public HashMap<String, Vector2f[]> getPlayersPositions()
	{
		HashMap<String, Vector2f[]> newPositions = new HashMap<>();
		for( String p : players.keySet() )
		{
			Vector2f[] position = new Vector2f[2];
			position[0] = players.get( p ).getPosition();
			position[1] = players.get( p ).getPlayerDirection();
			newPositions.put( p, position );
		}

		return newPositions;

	}
}

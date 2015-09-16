package mpa.core.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.vecmath.Vector2f;

import mpa.core.logic.building.AbstractPrivateProperty;
import mpa.core.logic.building.Headquarter;
import mpa.core.logic.building.Tower;
import mpa.core.logic.character.Minion;
import mpa.core.logic.character.Player;
import mpa.core.logic.character.Player.Item;
import mpa.core.logic.character.TowerCrusher;
import mpa.core.logic.resource.AbstractResourceProducer;

public class GameManagerProxy
{

	private static GameManagerProxy gameManagerProxy = null;
	private GameManager gm;
	private HashMap<String, Player> players = new HashMap<>();
	private HashMap<String, Headquarter> player_headquarter = new HashMap<>();

	private HashMap<String, Minion> minions = new HashMap<>();
	private HashMap<String, TowerCrusher> towerCrushers = new HashMap<>();
	private HashMap<String, HashMap<String, AbstractPrivateProperty>> objects = new HashMap<>();

	protected GameManagerProxy(GameManager gm)
	{
		this.gm = gm;

		objects.put("headquarter", new HashMap<String, AbstractPrivateProperty>());
		objects.put("field", new HashMap<String, AbstractPrivateProperty>());
		objects.put("wood", new HashMap<String, AbstractPrivateProperty>());
		objects.put("mine", new HashMap<String, AbstractPrivateProperty>());
		objects.put("cave", new HashMap<String, AbstractPrivateProperty>());
		objects.put("tower", new HashMap<String, AbstractPrivateProperty>());

		for (Player p : gm.getPlayers())
		{
			players.put(p.getName(), p);
			player_headquarter.put(p.getName(), p.getHeadquarter());
			objects.get("headquarter").put(p.getHeadquarter().getID(), p.getHeadquarter());
			System.out.println("ID " + p.getHeadquarter().getID());
		}

		for (AbstractPrivateProperty obj : gm.getWorld().getAllObjects())
		{

			// nome della classe per la chiave dell'hashmap
			String className = obj.getClass().toString();
			String[] split = className.split("\\.");
			String objectKey = split[split.length - 1].toLowerCase();

			objects.get(objectKey).put(obj.getID(), obj);
		}
	}

	public static void init(GameManager gm)
	{
		if (gameManagerProxy == null)
			gameManagerProxy = new GameManagerProxy(gm);
	}

	public static GameManagerProxy getInstance()
	{
		if (gameManagerProxy == null)
			gameManagerProxy = new GameManagerProxy(GameManager.getInstance());
		return gameManagerProxy;
	}

	public String getPickedObject(com.jme3.math.Vector2f contactPoint)
	{
		AbstractObject pickedObject = GameManager.getInstance().getWorld().pickedObject(contactPoint.x, contactPoint.y);

		String obj;

		if (pickedObject == null)
			obj = new String("GROUND");
		else
		{
			obj = pickedObject.getClass().getSimpleName();
			obj += ":" + pickedObject.getID();
		}
		return obj;
	}

	public void computePath(String player, float xGoal, float yGoal)
	{
		gm.computePath(players.get(player), xGoal, yGoal);
	}

	public HashMap<String, Vector2f> getFields()
	{
		HashMap<String, Vector2f> toReturn = new HashMap<>();

		HashMap<String, AbstractPrivateProperty> fields = objects.get("field");
		for (String s : fields.keySet())
			toReturn.put(s, fields.get(s).getPosition());

		return toReturn;
	}

	public String getPlayerLevel(String player)
	{
		return GameManager.getInstance().getPlayerLevel(players.get(player)).toString();
	}

	public HashMap<String, Vector2f> getWoods()
	{
		HashMap<String, Vector2f> toReturn = new HashMap<>();

		HashMap<String, AbstractPrivateProperty> woods = objects.get("wood");
		for (String s : woods.keySet())
			toReturn.put(s, woods.get(s).getPosition());

		return toReturn;
	}

	public HashMap<String, Vector2f> getMines()
	{
		HashMap<String, Vector2f> toReturn = new HashMap<>();

		HashMap<String, AbstractPrivateProperty> mines = objects.get("mine");
		for (String s : mines.keySet())
			toReturn.put(s, mines.get(s).getPosition());

		return toReturn;
	}

	public HashMap<String, Vector2f> getCaves()
	{
		HashMap<String, Vector2f> toReturn = new HashMap<>();

		HashMap<String, AbstractPrivateProperty> caves = objects.get("cave");
		for (String s : caves.keySet())
			toReturn.put(s, caves.get(s).getPosition());

		return toReturn;
	}

	public ArrayList<String> createMinions(String boss, int quantity, String target)
	{
		ArrayList<Minion> createdMinions = gm.createMinions(players.get(boss), quantity, players.get(target));
		ArrayList<String> minionNames = new ArrayList<>();
		for (Minion minion : createdMinions)
		{
			minions.put(minion.getID(), minion);
			minionNames.add(minion.getID());
		}

		return minionNames;
	}

	public ArrayList<String> createTowerCrushers(String boss, int quantity, String target)
	{
		ArrayList<TowerCrusher> createdTowerCrushers = GameManager.getInstance().createTowerCrushers(players.get(boss), quantity,
				(Tower) objects.get("tower").get(target));

		ArrayList<String> towerCrusherNames = new ArrayList<>();

		for (TowerCrusher tC : createdTowerCrushers)
		{
			towerCrushers.put(tC.getID(), tC);
			towerCrusherNames.add(tC.getID());
		}

		return towerCrusherNames;
	}

	public boolean conquer(String abstractPrivateProperty, String player)
	{
		String[] field = abstractPrivateProperty.split(":");

		AbstractPrivateProperty conqueredObject = objects.get(field[0].toLowerCase()).get(field[1]);
		return gm.conquer(conqueredObject, players.get(player));
	}

	public ArrayList<String> attackPhysically(String attacker)
	{
		ArrayList<Player> hitPlayers = gm.attackPhysically(minions.get(attacker));

		ArrayList<String> hitPlayersNames = new ArrayList<>();

		for (Player p : hitPlayers)
			hitPlayersNames.add(p.getName());

		return hitPlayersNames;
	}

	public ArrayList<String> playerAction(String p, Vector2f target)
	{
		ArrayList<Player> hitPlayers = gm.playerAction(players.get(p), target);

		ArrayList<String> hitPlayersNames = new ArrayList<>();

		for (Player player : hitPlayers)
			hitPlayersNames.add(player.getName());

		return hitPlayersNames;

	}

	public boolean occupyProperty(String player, String property)
	{

		String[] field = property.split(":");
		AbstractPrivateProperty conqueredObject = objects.get(field[0].toLowerCase()).get(field[1]);

		return gm.conquer(conqueredObject, players.get(player));
	}

	public String createTower(String p, Vector2f position)
	{

		Tower tower = gm.createTower(players.get(p), position);

		if (tower != null)
		{
			objects.get("tower").put(tower.getID(), tower);
			return tower.getID();
		}

		return "";
	}

	public void setPause()
	{
		gm.setPause();
	}

	public void changeSelectedItem(String p, String selected)
	{
		Item item = null;

		switch (selected)
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

		gm.changeSelectedItem(players.get(p), item);
	}

	public ArrayList<String> takePlayerAttacks()
	{
		ArrayList<String> attackers = new ArrayList<>();

		for (Player p : GameManager.getInstance().takePlayerAttacks())
			attackers.add(p.getName());

		return attackers;
	}

	public ArrayList<String> takeMinionAttacks()
	{
		ArrayList<String> attackers = new ArrayList<>();

		for (Minion m : GameManager.getInstance().takeMinionAttacks())
			attackers.add(m.getName());

		return attackers;
	}

	public ArrayList<String> takeTowerCrusherAttacks()
	{
		ArrayList<String> attackers = new ArrayList<>();

		for (TowerCrusher t : GameManager.getInstance().takeTowerCrusherAttacks())
			attackers.add(t.getName());

		return attackers;

	}

	public ArrayList<String> takeDeadPlayers()
	{
		ArrayList<Player> deadPlayers = gm.takeDeadPlayers();
		ArrayList<String> names = new ArrayList<>();

		for (Player p : deadPlayers)
			names.add(p.getName());

		return names;
	}

	public ArrayList<String> takeDeadMinions()
	{
		ArrayList<Minion> deadMinions = gm.takeDeadMinions();
		ArrayList<String> names = new ArrayList<>();

		for (Minion m : deadMinions)
			names.add(m.getName());

		return names;
	}

	// TODO takeDeadTowerCrushers

	public HashMap<String, Vector2f[]> getPlayersPositions()
	{
		HashMap<String, Vector2f[]> newPositions = new HashMap<>();
		for (String p : players.keySet())
		{
			Vector2f[] position = new Vector2f[2];
			position[0] = players.get(p).getPosition();
			position[1] = players.get(p).getPlayerDirection();
			newPositions.put(p, position);
		}

		return newPositions;

	}

	public HashMap<String, Vector2f[]> getMinionsPositions()
	{
		HashMap<String, Vector2f[]> newPositions = new HashMap<>();

		for (String m : minions.keySet())
		{
			Vector2f[] position = new Vector2f[2];
			position[0] = minions.get(m).getPosition();
			position[1] = minions.get(m).getPlayerDirection();
			newPositions.put(m, position);
		}

		return newPositions;

	}

	// vector[0] position, vector[1] direction, vector[2] hq
	public HashMap<String, Vector2f[]> getPlayers()
	{
		HashMap<String, Vector2f[]> players = new HashMap<>();
		for (Player p : gm.getPlayers())
		{
			Vector2f[] positions = new Vector2f[3];
			positions[0] = p.getPosition();
			positions[1] = p.getPlayerDirection();
			positions[2] = p.getHeadquarter().getPosition();
			players.put(p.getName(), positions);
		}

		return players;
	}

	public String getObjectOwner(String objectType, String objectID)
	{
		Player owner = objects.get(objectType.toLowerCase()).get(objectID).getOwner();
		if (owner == null)
		{
			return null;
		}
		else
			return owner.getName();

	}

	public int getObjectProductivity(String objectType, String objectID)
	{

		AbstractPrivateProperty object = objects.get(objectType.toLowerCase()).get(objectID);
		if (object instanceof AbstractResourceProducer)
			return ((AbstractResourceProducer) object).getProviding();
		else
			return -1;

	}

	public HashMap<String, HashMap<String, Integer>> getPlayersResourceAmount()
	{
		HashMap<String, HashMap<String, Integer>> playersResourceAmount = new HashMap<>();
		Collection<Player> listPlayers = players.values();

		for (Player player : listPlayers)
			playersResourceAmount.put(player.getName(), player.getResources());
		return playersResourceAmount;
	}

	public HashMap<String, Integer> getPlayerResourcesAmout(String playerName)
	{
		return players.get(playerName).getResources();

	}

	public float worldDimension()
	{
		return gm.getWorld().getWidth();
	}

	public int getNumberOfPlayer()
	{
		return players.size();
	}

	public boolean isMovingPlayer(String playerName)
	{
		ArrayList<Vector2f> playerPath = players.get(playerName).getPath();
		if (playerPath == null || playerPath.isEmpty())
			return false;
		else
			return true;
	}

	public int getPLayerHP(String playerName)
	{
		return players.get(playerName).getHP();
	}

	public int getPLayerMP(String playerName)
	{
		return players.get(playerName).getMP();
	}
	// private static GameManagerProxy gameManagerProxy = null;
	// private GameManager gm;
	// private HashMap<String, Player> players = new HashMap<>();
	// private HashMap<String, Headquarter> player_headquarter = new HashMap<>();
	// private HashMap<String, Headquarter> headquarters = new HashMap<>();
	// private HashMap<String, Field> fields = new HashMap<>();
	// private HashMap<String, Cave> caves = new HashMap<>();
	// private HashMap<String, Mine> mines = new HashMap<>();
	// private HashMap<String, Wood> woods = new HashMap<>();
	// private HashMap<String, Minion> minions = new HashMap<>();
	// private HashMap<String, Tower> towers = new HashMap<>();
	//
	// protected GameManagerProxy(GameManager gm)
	// {
	// this.gm = gm;
	// for (Player p : gm.getPlayers())
	// {
	// players.put(p.getName(), p);
	// player_headquarter.put(p.getName(), p.getHeadquarter());
	// headquarters.put(p.getHeadquarter().getID(), p.getHeadquarter());
	// }
	//
	// for (AbstractObject obj : gm.getWorld().getAllObjects())
	// {
	// if (obj instanceof Wood)
	// woods.put(obj.getID(), (Wood) obj);
	// else if (obj instanceof Cave)
	// caves.put(obj.getID(), (Cave) obj);
	// else if (obj instanceof Mine)
	// mines.put(obj.getID(), (Mine) obj);
	// else if (obj instanceof Field)
	// fields.put(obj.getID(), (Field) obj);
	// }
	// }
	//
	// public static void init(GameManager gm)
	// {
	// if (gameManagerProxy == null)
	// gameManagerProxy = new GameManagerProxy(gm);
	// }
	//
	// public static GameManagerProxy getInstance()
	// {
	// if (gameManagerProxy == null)
	// gameManagerProxy = new GameManagerProxy(GameManager.getInstance());
	// return gameManagerProxy;
	// }
	//
	// public void computePath(String player, float xGoal, float yGoal)
	// {
	// gm.computePath(players.get(player), xGoal, yGoal);
	// }
	//
	// public HashMap<String, Vector2f> getFields()
	// {
	// HashMap<String, Vector2f> toReturn = new HashMap<>();
	//
	// for (String s : fields.keySet())
	// toReturn.put(s, fields.get(s).getPosition());
	//
	// return toReturn;
	// }
	//
	// public HashMap<String, Vector2f> getWoods()
	// {
	// HashMap<String, Vector2f> toReturn = new HashMap<>();
	//
	// for (String s : woods.keySet())
	// toReturn.put(s, woods.get(s).getPosition());
	//
	// return toReturn;
	// }
	//
	// public HashMap<String, Vector2f> getMines()
	// {
	// HashMap<String, Vector2f> toReturn = new HashMap<>();
	//
	// for (String s : mines.keySet())
	// toReturn.put(s, mines.get(s).getPosition());
	//
	// return toReturn;
	// }
	//
	// public HashMap<String, Vector2f> getCaves()
	// {
	// HashMap<String, Vector2f> toReturn = new HashMap<>();
	//
	// for (String s : caves.keySet())
	// toReturn.put(s, caves.get(s).getPosition());
	//
	// return toReturn;
	// }
	//
	// public ArrayList<String> createMinions(String boss, int quantity, String target)
	// {
	// ArrayList<Minion> createdMinions = gm.createMinions(players.get(boss), quantity,
	// players.get(target));
	// ArrayList<String> minionNames = new ArrayList<>();
	// for (Minion minion : createdMinions)
	// {
	// minions.put(minion.getID(), minion);
	// minionNames.add(minion.getID());
	// }
	//
	// return minionNames;
	// }
	//
	// public boolean conquer(String abstractPrivateProperty, String player)
	// {
	//
	// AbstractPrivateProperty obj = null;
	//
	// String[] field = abstractPrivateProperty.split(":");
	//
	// if (field[0].equals("CAVE"))
	// obj = caves.get(field[1]);
	// else if (field[0].equals("MINE"))
	// obj = mines.get(field[1]);
	//
	// else if (field[0].equals("FIELD"))
	// obj = fields.get(field[1]);
	//
	// else if (field[0].equals("WOOD"))
	// obj = woods.get(field[1]);
	//
	// return gm.conquer(obj, players.get(player));
	// }
	//
	// public ArrayList<String> attackPhysically(String attacker)
	// {
	// ArrayList<Player> hitPlayers = gm.attackPhysically(minions.get(attacker));
	//
	// ArrayList<String> hitPlayersNames = new ArrayList<>();
	//
	// for (Player p : hitPlayers)
	// hitPlayersNames.add(p.getName());
	//
	// return hitPlayersNames;
	// }
	//
	// public ArrayList<String> playerAction(String p, Vector2f target)
	// {
	// ArrayList<Player> hitPlayers = gm.playerAction(players.get(p), target);
	//
	// ArrayList<String> hitPlayersNames = new ArrayList<>();
	//
	// for (Player player : hitPlayers)
	// hitPlayersNames.add(player.getName());
	//
	// return hitPlayersNames;
	//
	// }
	//
	// public boolean occupyProperty(String player, String property)
	// {
	// AbstractPrivateProperty obj = null;
	//
	// String[] field = property.split(":");
	//
	// if (field[0].equals("CAVE"))
	// obj = caves.get(field[1]);
	// else if (field[0].equals("MINE"))
	// obj = mines.get(field[1]);
	//
	// else if (field[0].equals("FIELD"))
	// obj = fields.get(field[1]);
	//
	// else if (field[0].equals("WOOD"))
	// obj = woods.get(field[1]);
	// return gm.occupyProperty(players.get(player), obj);
	// }
	//
	// public String createTower(String p, Vector2f position)
	// {
	//
	// Tower tower = gm.createTower(players.get(p), position);
	//
	// if (tower != null)
	// {
	// towers.put(tower.getID(), tower);
	// return tower.getID();
	// }
	//
	// return "";
	// }
	//
	// public void setPause()
	// {
	// gm.setPause();
	// }
	//
	// public void changeSelectedItem(String p, String selected)
	// {
	// Item item = null;
	//
	// switch (selected)
	// {
	// case "WEAPON":
	// item = Item.WEAPON;
	// break;
	// case "FLASH_BANG":
	// item = Item.FLASH_BANG;
	// break;
	// case "GRANADE":
	// item = Item.GRANADE;
	// break;
	// case "HP_POTION":
	// item = Item.HP_POTION;
	// break;
	// case "MP_POTION":
	// item = Item.MP_POTION;
	// break;
	// default:
	// return;
	// }
	//
	// gm.changeSelectedItem(players.get(p), item);
	// }
	//
	// public ArrayList<String> takeDeadPlayers()
	// {
	// ArrayList<Player> deadPlayers = gm.takeDeadPlayers();
	// ArrayList<String> names = new ArrayList<>();
	//
	// for (Player p : deadPlayers)
	// names.add(p.getName());
	//
	// return names;
	// }
	//
	// public ArrayList<String> takeDeadMinions()
	// {
	// ArrayList<Minion> deadMinions = gm.takeDeadMinions();
	// ArrayList<String> names = new ArrayList<>();
	//
	// for (Minion m : deadMinions)
	// names.add(m.getName());
	//
	// return names;
	// }
	//
	// // TODO takeDeadTowerCrushers
	//
	// public HashMap<String, Vector2f[]> getPlayersPositions()
	// {
	// HashMap<String, Vector2f[]> newPositions = new HashMap<>();
	// for (String p : players.keySet())
	// {
	// Vector2f[] position = new Vector2f[2];
	// position[0] = players.get(p).getPosition();
	// position[1] = players.get(p).getPlayerDirection();
	// newPositions.put(p, position);
	// }
	//
	// return newPositions;
	//
	// }
	//
	// public HashMap<String, Vector2f[]> getMinionsPositions()
	// {
	// HashMap<String, Vector2f[]> newPositions = new HashMap<>();
	//
	// for (String m : minions.keySet())
	// {
	// Vector2f[] position = new Vector2f[2];
	// position[0] = minions.get(m).getPosition();
	// position[1] = minions.get(m).getPlayerDirection();
	// newPositions.put(m, position);
	// }
	//
	// return newPositions;
	//
	// }
	//
	// // vector[0] position, vector[1] direction, vector[2] hq
	// public HashMap<String, Vector2f[]> getPlayers()
	// {
	// HashMap<String, Vector2f[]> players = new HashMap<>();
	// for (Player p : gm.getPlayers())
	// {
	// Vector2f[] positions = new Vector2f[3];
	// positions[0] = p.getPosition();
	// positions[1] = p.getPlayerDirection();
	// positions[2] = p.getHeadquarter().getPosition();
	// players.put(p.getName(), positions);
	// }
	//
	// return players;
	// }
	//
	// // public String getObjectOwner(String objectType, String objectID)
	// // {
	// // switch (objectType.toLowerCase())
	// // {
	// // case "headquarter":
	// // Headquarter headquarter = headquarters.get(objectID);
	// // return headquarter.getOwner().getName();
	// //
	// // case "field":
	// // Field field = fields.get(objectID);
	// // return field.getOwner().getName();
	// //
	// // case "cave":
	// // Cave cave = caves.get(objectID);
	// // return cave.getOwner().getName();
	// // case "mine":
	// // Mine mine = mines.get(objectID);
	// // return mine.getOwner().getName();
	// // case "wood":
	// // Wood wood = woods.get(objectID);
	// // return wood.getOwner().getName();
	// // default:
	// // break;
	// // }
	// // }
	//
	// public float worldDimension()
	// {
	// return gm.getWorld().getWidth();
	// }

}

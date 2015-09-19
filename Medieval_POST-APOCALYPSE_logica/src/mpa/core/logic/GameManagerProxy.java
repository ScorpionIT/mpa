package mpa.core.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.vecmath.Vector2f;

import mpa.core.logic.building.AbstractPrivateProperty;
import mpa.core.logic.building.Headquarter;
import mpa.core.logic.building.Tower;
import mpa.core.logic.character.Minion;
import mpa.core.logic.character.Player;
import mpa.core.logic.character.Player.Item;
import mpa.core.logic.character.TowerCrusher;
import mpa.core.logic.resource.AbstractResourceProducer;
import mpa.core.logic.tool.Potions;

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

	public Map<String, Vector2f> getTowers()
	{
		List<Tower> towersObject = gm.getTowers();
		for (Tower tower : towersObject)
		{
			if (!objects.get("tower").containsKey(tower.getID()))
				objects.get("tower").put(tower.getID(), tower);
		}

		HashMap<String, Vector2f> toReturn = new HashMap<>();

		HashMap<String, AbstractPrivateProperty> towers = objects.get("tower");
		for (String s : towers.keySet())
			toReturn.put(s, towers.get(s).getPosition());

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

	public ArrayList<String> createTowerCrushers(String boss, String target)
	{
		ArrayList<TowerCrusher> createdTowerCrushers = GameManager.getInstance().createTowerCrushers(players.get(boss),
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

	public List<String> playerAction(String p, Vector2f target)
	{
		List<Player> hitPlayers = gm.playerAction(players.get(p), target);

		ArrayList<String> hitPlayersNames = new ArrayList<>();

		if (hitPlayers != null)
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

	public Vector2f getTowerAvaiblePosition(String property)
	{
		String[] field = property.split(":");

		List<Vector2f> gatheringPlaces = objects.get(field[0].toLowerCase()).get(field[1]).getAvaibleGatheringPlaces();
		if (gatheringPlaces != null && !gatheringPlaces.isEmpty())
		{
			System.out.println("NON CI ARRIVO");
			return gatheringPlaces.get(0);
		}
		else
			return null;
	}

	public String createTower(String playerName, Vector2f position, String property)
	{

		String[] field = property.split(":");
		AbstractPrivateProperty propertyObject = objects.get(field[0].toLowerCase()).get(field[1]);
		Tower tower = gm.createTower(players.get(playerName), position, propertyObject);

		if (tower != null)
		{
			propertyObject.addTower(tower, position);
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
		List<Player> deadPlayers = gm.takeDeadPlayers();
		ArrayList<String> names = new ArrayList<>();

		for (Player p : deadPlayers)
		{
			players.remove(p.getName());
			names.add(p.getName());
		}

		return names;
	}

	public List<String> takeDeadMinions()
	{
		List<Minion> deadMinions = gm.takeDeadMinions();
		List<String> names = new ArrayList<>();

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

	public int getLifeTower(String objectID)
	{

		AbstractPrivateProperty object = objects.get("tower").get(objectID);
		if (object != null)
		{
			return ((Tower) object).getLife();
		}
		return 0;

	}

	public Map<String, Map<String, Integer>> getPlayersResourceAmount()
	{
		Map<String, Map<String, Integer>> playersResourceAmount = new HashMap<>();
		Collection<Player> listPlayers = players.values();

		for (Player player : listPlayers)
		{
			playersResourceAmount.put(player.getName(), player.getResources());
		}
		return playersResourceAmount;
	}

	public Map<String, Integer> getPlayerResourcesAmout(String playerName)
	{
		Player player = players.get(playerName);
		if (player != null)
			return players.get(playerName).getResources();
		else
			return null;

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

	public boolean isMovingMinion(String ID)
	{
		ArrayList<Vector2f> minionPath = minions.get(ID).getPath();
		if (minionPath == null || minionPath.isEmpty())
			return false;
		else
			return true;
	}

	public int getPLayerHP(String playerName)
	{
		return players.get(playerName).getHP();
	}

	public int getPlayerMP(String playerName)
	{
		return players.get(playerName).getMP();
	}

	public Set<String> getPlayersName()
	{
		return players.keySet();
	}

	public boolean buyPotion(String playerName, String potionType)
	{
		Player player = players.get(playerName);
		if (!player.canBuyPotions())
			return false;

		if (potionType.toLowerCase().equals("mp"))
			player.buyPotion(Potions.MP);
		else if (potionType.toLowerCase().equals("hp"))
			player.buyPotion(Potions.HP);
		else if (potionType.toLowerCase().equals("granade"))
			player.buyPotion(Potions.GRANADE);
		return true;
	}

	public int getPotionAmount(String playerName, String potionType)
	{
		Player player = players.get(playerName);

		if (potionType.toLowerCase().equals("mp"))
			return player.getPotionAmount(Potions.MP);
		else if (potionType.toLowerCase().equals("hp"))
			return player.getPotionAmount(Potions.HP);
		else if (potionType.toLowerCase().equals("granade"))
			return player.getPotionAmount(Potions.GRANADE);

		return -100;
	}

	public String getMinionBoss(String ID)
	{
		Minion minion = minions.get(ID);
		if (minion != null)
		{
			return minion.getBoss().getName();
		}
		else
			return null;
	}
}

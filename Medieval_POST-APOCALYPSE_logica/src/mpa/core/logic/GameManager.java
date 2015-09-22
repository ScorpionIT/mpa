package mpa.core.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import javax.vecmath.Vector2f;

import mpa.core.ai.DifficultyLevel;
import mpa.core.ai.OpponentAI;
import mpa.core.logic.building.AbstractPrivateProperty;
import mpa.core.logic.building.AbstractProperty;
import mpa.core.logic.building.Tower;
import mpa.core.logic.characters.AbstractCharacter;
import mpa.core.logic.characters.DependentCharacter;
import mpa.core.logic.characters.Minion;
import mpa.core.logic.characters.Player;
import mpa.core.logic.characters.Player.Item;
import mpa.core.logic.characters.TowerCrusher;
import mpa.core.logic.fights.AttackRequests;
import mpa.core.logic.fights.CombatManager;
import mpa.core.logic.potions.Potions;
import mpa.core.maths.MyMath;
import mpa.core.util.GameProperties;

public class GameManager
{
	private World world;
	private List<Player> players;
	private Map<Player, OpponentAI> AI_players;
	private List<Tower> towers = new ArrayList<Tower>();
	private DifficultyLevel difficultyLevel;
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	private ReadLock readLock = lock.readLock();
	private WriteLock writeLock = lock.writeLock();
	private List<Minion> minionsAlive = new CopyOnWriteArrayList<>();
	private List<TowerCrusher> towerCrushers = new CopyOnWriteArrayList<>();

	private IDPool minionIDs;
	private IDPool towerCrusherIDs;
	private IDPool towerIDs;

	private List<Player> deadPlayers = new CopyOnWriteArrayList<Player>();
	private List<Minion> deadMinions = new CopyOnWriteArrayList<>();
	private List<TowerCrusher> deadTowerCrushers = new CopyOnWriteArrayList<>();

	private Map<Player, Integer> numberOfMinions = new ConcurrentHashMap<>();
	private Map<Player, Integer> numberOfTowerCrushers = new ConcurrentHashMap<>();
	private final int MAX_NUMBER_MINIONS_FOR_PLAYER = 5;
	private final int MAX_NUMBER_TOWER_CRUSHERS_FOR_PLAYER = 1;

	private static GameManager gameManager = null;

	private AttackRequests attackRequests;

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

	public static void init(World world, DifficultyLevel level, boolean multiplayer)
	{
		if (gameManager == null)
		{
			gameManager = new GameManager(world, level);
			ThreadManager.getInstance().addGameThread(new PositionUpdater());
			ThreadManager.getInstance().addGameThread(new ResourceUpdater());
			ThreadManager.getInstance().addGameThread(new TowerThread());
			ThreadManager.getInstance().startGameThreads();

			if (multiplayer)
				;
			else
				;
		}
	}

	public List<Tower> getTowers()
	{
		try
		{
			readLock.lock();
			return towers;
		} finally
		{
			readLock.unlock();
		}
	}

	private GameManager(World world, DifficultyLevel level)
	{
		this.world = world;
		this.players = new CopyOnWriteArrayList<>();
		AI_players = new ConcurrentHashMap<Player, OpponentAI>();
		this.difficultyLevel = level;
		minionIDs = new IDPool(100);
		towerCrusherIDs = new IDPool(100);
		towerIDs = new IDPool(100);
		attackRequests = new AttackRequests(AI_players);
		ThreadManager.getInstance().addGameThread(attackRequests);

	}

	public void computePath(AbstractCharacter player, float xGoal, float yGoal)
	{
		Thread pathCalculatorThread = new PathCalculatorThread(player, xGoal, yGoal);
		pathCalculatorThread.start();
	}

	public void addPlayer(Player player)
	{
		players.add(player);
		numberOfMinions.put(player, 0);
		numberOfTowerCrushers.put(player, 0);
	}

	public void addAIPlayer(Player player)
	{
		OpponentAI newAI = new OpponentAI(player, difficultyLevel);
		AI_players.put(player, newAI);
		addPlayer(player);
		newAI.start();
	}

	public List<Minion> createMinions(Player boss, int quantity, Player target)
	{
		List<Minion> minions = new ArrayList<>();

		if (boss == null)
		{
			return minions;
		}
		if (numberOfMinions.get(boss) == MAX_NUMBER_MINIONS_FOR_PLAYER)
		{
			return minions;
		}
		if (numberOfMinions.get(boss) + quantity > MAX_NUMBER_MINIONS_FOR_PLAYER)
		{
			quantity = MAX_NUMBER_MINIONS_FOR_PLAYER - numberOfMinions.get(boss);
		}
		Map<String, Integer> required = new HashMap<String, Integer>();
		Map<String, Integer> prices = GameProperties.getInstance().getPrices("minion");
		for (String element : prices.keySet())
			required.put(element, prices.get(element) * quantity);

		if (!boss.hasEnoughResources(required))
			return minions;

		List<Vector2f> computeRandomPointsCircumference = MyMath.computeRandomPointsCircumference(boss.getHeadquarter().getPosition(),
				MyMath.distanceFloat(boss.getHeadquarter().getPosition(), boss.getHeadquarter().getGatheringPlace()), quantity);
		// for( Vector2f vector2f : computeRandomPointsCircumference )
		// {
		// System.out.println( vector2f );
		// }

		for (int i = 0; i < quantity; i++)
		{
			Minion createdMinion = boss.createMinion(target, minionIDs.getID(), computeRandomPointsCircumference.get(i));
			minions.add(createdMinion);
			minionsAlive.add(createdMinion);
		}
		numberOfMinions.put(boss, numberOfMinions.get(boss) + quantity);

		return minions;
	}

	public List<TowerCrusher> createTowerCrushers(Player boss, Tower target)
	{

		List<TowerCrusher> towerCrushers = new ArrayList<>();
		if (numberOfTowerCrushers.get(boss) >= MAX_NUMBER_TOWER_CRUSHERS_FOR_PLAYER)
		{
			return towerCrushers;
		}
		if (!boss.hasEnoughResources(GameProperties.getInstance().getPrices("towercrusher")))
			return towerCrushers;
		TowerCrusher createTowerCrusher = boss.createTowerCrusher(target, towerCrusherIDs.getID());
		this.towerCrushers.add(createTowerCrusher);
		towerCrushers.add(createTowerCrusher);
		numberOfTowerCrushers.put(boss, numberOfTowerCrushers.get(boss) + 1);

		return towerCrushers;
	}

	// public TowerCrusher createTowerCrusher( Player boss, Tower target )
	// {
	// if( !boss.hasEnoughResources( GameProperties.getInstance().getPrices( "towercrusher" ) ) )
	// return null;
	// TowerCrusher createTowerCrusher = boss.createTowerCrusher( target, towerCrusherIDs.getID() );
	// towerCrushers.add( createTowerCrusher );
	//
	// return createTowerCrusher;
	// }

	public void updateCharacterPositions()
	{
		readLock.lock();
		for (Player player : players)
		{
			player.movePlayer();
			for (DependentCharacter dC : player.getSubalterns())
				dC.movePlayer();
		}
		for (Minion minion : minionsAlive)
		{
			minion.movePlayer();
		}

		for (TowerCrusher towerCrusher : towerCrushers)
		{
			towerCrusher.movePlayer();
		}
		readLock.unlock();
	}

	public World getWorld()
	{
		return world;
	}

	public boolean addWorker(Player player, AbstractPrivateProperty abstractPrivateProperty)
	{
		if (abstractPrivateProperty.getOwner() != player)
			return false;

		DependentCharacter employee = player.employSubaltern(abstractPrivateProperty);
		if (employee == null)
			return false;

		abstractPrivateProperty.setController(employee);
		return true;
	}

	public boolean conquer(AbstractPrivateProperty abstractPrivateProperty, Player player)
	{
		Vector2f gatheringPlace = abstractPrivateProperty.getGatheringPlace();
		if (((int) gatheringPlace.x) != ((int) player.x) || ((int) gatheringPlace.y) != ((int) player.y))
		{
			computePath(player, gatheringPlace.x, gatheringPlace.y);
			return false;
		}

		DependentCharacter employSubaltern = player.employSubaltern(abstractPrivateProperty);
		if (employSubaltern != null)
		{
			abstractPrivateProperty.setOwner(player);
			abstractPrivateProperty.setController(employSubaltern);
			computePath(employSubaltern, abstractPrivateProperty.getGatheringPlace().x, abstractPrivateProperty.getGatheringPlace().y);
			return true;
		}
		return false;

	}

	public void killPlayer(Player p)
	{
		writeLock.lock();

		p.die();
		if (players.contains(p))
			players.remove(p);
		if (AI_players.keySet().contains(p))
		{
			AI_players.remove(p);
		}

		deadPlayers.add(p);

		for (Minion minion : minionsAlive)
		{
			if (minion.getBoss() == p)
			{
				deadMinions.add(minion);
				minionsAlive.remove(minion);
			}
		}

		for (TowerCrusher towerCrusher : towerCrushers)
		{
			if (towerCrusher.getBoss() == p)
			{
				deadTowerCrushers.add(towerCrusher);
				towerCrushers.remove(towerCrusher);
			}

		}
		writeLock.unlock();
	}

	public void killMinion(Minion minion)
	{
		writeLock.lock();
		minion.stopMoving();
		numberOfMinions.put(minion.getBoss(), numberOfMinions.get(minion.getBoss()) - 1);
		deadMinions.add(minion);
		minionsAlive.remove(minion);
		minionIDs.freeID(minion.getID());
		writeLock.unlock();
	}

	private void killTowerCrusher(TowerCrusher towerCrusher)
	{
		writeLock.lock();
		towerCrusher.stopMoving();
		numberOfTowerCrushers.put(towerCrusher.getBoss(), numberOfTowerCrushers.get(towerCrusher.getBoss()) - 1);
		deadTowerCrushers.add(towerCrusher);
		towerCrushers.remove(towerCrusher);
		writeLock.unlock();
	}

	public List<Player> getPlayers()
	{
		return players;
	}

	public List<Minion> getMinionsAlive()
	{
		return minionsAlive;
	}

	public List<TowerCrusher> getTowerCrushers()
	{
		return towerCrushers;
	}

	List<Player> takeDeadPlayers()
	{

		try
		{
			writeLock.lock();
			List<Player> deads = new CopyOnWriteArrayList<>();

			for (Player p : deadPlayers)
			{
				System.out.println("STO KILLANDO " + p.getName());
				System.out.println("STO KILLANDO " + p.getName());
				System.out.println("STO KILLANDO " + p.getName());
				System.out.println("STO KILLANDO " + p.getName());
				System.out.println("STO KILLANDO " + p.getName());
				System.out.println("STO KILLANDO " + p.getName());
				deads.add(p);

			}

			deadPlayers.clear();

			return deads;
		} finally
		{

			writeLock.unlock();
		}

	}

	List<Minion> takeDeadMinions()
	{
		try
		{
			writeLock.lock();
			List<Minion> deads = new ArrayList<>();

			for (Minion m : deadMinions)
				deads.add(m);

			deadMinions.clear();

			return deads;
		} finally
		{
			writeLock.unlock();
		}
	}

	List<TowerCrusher> takeDeadTowerCrushers()
	{
		try
		{
			writeLock.lock();
			List<TowerCrusher> deads = new ArrayList<>();

			for (TowerCrusher towerCrusherID : deadTowerCrushers)
				deads.add(towerCrusherID);

			deadTowerCrushers.clear();

			return deads;
		} finally
		{
			writeLock.unlock();
		}
	}

	public void endGame(Object applicant)
	{
		writeLock.lock();
		if (!(applicant instanceof GameManagerProxy))
			return;
		ThreadManager.getInstance().destroyAllThreads(gameManager);
		players = null;
		world = null;
		AI_players = null;
		attackRequests = null;
		deadMinions = null;
		deadPlayers = null;
		towerCrushers = null;
		minionIDs = null;
		minionsAlive = null;
		towerCrusherIDs = null;
		towerIDs = null;
		towers = null;
		gameManager = null;
		writeLock.unlock();
	}

	public List<Player> getPlayersAround(Player player, float ray)
	{
		try
		{
			readLock.lock();
			Vector2f playerPosition = player.getPosition();
			ArrayList<Player> playersAround = new ArrayList<>();
			for (Player p : players)
			{
				if (p == player)
					continue;

				Vector2f p_position = p.getPosition();

				if (MyMath.distanceFloat(playerPosition.x, playerPosition.y, p_position.x, p_position.y) <= ray)
					playersAround.add(p);
			}

			return playersAround;
		} finally
		{
			readLock.unlock();
		}
	}

	List<AbstractCharacter> attackPhysically(Player attacker)
	{
		System.out.println("sto per attaccare");
		return CombatManager.getInstance().attackPhysically(attacker);
	}

	List<Player> takePlayerAttacks()
	{
		return CombatManager.getInstance().takePlayerAttacks();
	}

	List<Minion> takeMinionAttacks()
	{
		return CombatManager.getInstance().takeMinionAttacks();
	}

	List<TowerCrusher> takeTowerCrusherAttacks()
	{
		return CombatManager.getInstance().takeTowerCrusherAttacks();
	}

	public void attackPhysically(Minion attacker)
	{
		System.out.println("sto per attaccare");
		attackRequests.addRequest(attacker, attacker.getCurrentVector());
	}

	public List<Player> playerAction(final Player p, Vector2f target)
	{

		List<Player> hitPlayers = null;
		if (target != null && p != null)
			p.setDirection(MyMath.computeDirection(p.getPosition(), target));

		if (p != null && p.getSelectedItem().equals(Item.WEAPON) || p.getSelectedItem().equals(Item.GRANADE)
				|| p.getSelectedItem().equals(Item.FLASH_BANG))
		{
			attackRequests.addRequest(p, target);
		}
		else if (p.getSelectedItem().equals(Item.HP_POTION))
		{
			p.getWriteLock();
			if (p.getPotionAmount(Potions.HP) > 0)
			{
				p.restoreHealth(Potions.HP);
				p.takePotion(Potions.HP);
			}
			p.leaveWriteLock();
		}
		else
		{
			p.getWriteLock();
			if (p.getPotionAmount(Potions.MP) > 0)
			{
				p.restoreHealth(Potions.MP);
				p.takePotion(Potions.MP);
			}
			p.leaveWriteLock();
		}

		return hitPlayers;
	}

	public boolean occupyProperty(Player player, AbstractPrivateProperty property)
	{
		if (!property.isFree() || !player.isThereAnyFreeSulbaltern())
			return false;

		property.setOwner(player);
		player.employSubaltern(property);
		return true;
	}

	public Tower createTower(Player p, Vector2f position, AbstractPrivateProperty abstractPrivateProperty)
	{
		try
		{
			p.getWriteLock();
			if (!p.hasEnoughResources(GameProperties.getInstance().getPrices("tower")))
				return null;

			Tower tower = new Tower(position.x, position.y, GameProperties.getInstance().getObjectWidth("tower"), GameProperties.getInstance()
					.getObjectHeight("tower"), p, abstractPrivateProperty);
			tower.setID(towerIDs.getID());
			if (!world.addTower(tower))
				return null;

			p.takeResources(GameProperties.getInstance().getPrices("tower"));
			p.addTower(tower);
			towers.add(tower);

			return tower;
		} finally
		{
			p.leaveWriteLock();
		}
	}

	void checkForTowerDamages()
	{
		writeLock.lock();
		System.out.println("sono dopo il lock");
		for (Tower t : towers)
		{
			List<AbstractCharacter> hitCharacters = t.attack();

			for (AbstractCharacter hitCharacter : hitCharacters)
			{
				if (hitCharacter != t.getOwner())
					hitCharacter.inflictDamage(t.getDamage());
			}

			for (AbstractCharacter character : hitCharacters)
			{
				if (!character.amIAlive())
				{
					if (character != t.getOwner())
					{
						if (character instanceof Player)
							killPlayer(((Player) character));
						else if (character instanceof TowerCrusher)
							killTowerCrusher(((TowerCrusher) character));
						else if (character instanceof Minion)
							killMinion(((Minion) character));
					}
				}
			}
		}
		writeLock.unlock();
		System.out.println("CI ARRIVO QUA?");
	}

	public Level getPlayerLevel(Player player)
	{
		return player.getPlayerLevel();
	}

	public void destroyTower(Tower tower)
	{
		world.destroyObject(tower);
		tower.getOwner().removeTower(tower);
		AbstractProperty property = tower.getProperty();
		property.removeTower(tower);
	}

	public void setPause()
	{
		ThreadManager.getInstance().pause(!ThreadManager.getInstance().getPauseState());
	}

	public void startFlashTimer(Player p)
	{
		ThreadManager.getInstance().startFlashBangThread(p);
	}

	public void changeSelectedItem(Player p, Item selected)
	{
		p.setSelectedItem(selected);
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

	public void attackPhysically(TowerCrusher towerCrusher)
	{
		CombatManager.getInstance().attackOnTower(towerCrusher);
	}

	public boolean isPlayerDead(Player target)
	{
		return !players.contains(target);
	}

	public boolean isTowerDestroyed(Tower t)
	{
		return !towers.contains(t);
	}

	public boolean upgradeLevel(Player player)
	{
		if (player.canUpgrade())
		{
			player.upgradeLevel();
			return true;
		}
		else
			return false;
	}
}

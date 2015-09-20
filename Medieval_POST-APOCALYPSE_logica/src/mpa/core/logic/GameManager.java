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
import mpa.core.logic.building.AbstractProperty;
import mpa.core.logic.building.ControllerAlreadyPresentException;
import mpa.core.logic.building.DifferentOwnerException;
import mpa.core.logic.building.Tower;
import mpa.core.logic.character.AbstractCharacter;
import mpa.core.logic.character.DependentCharacter;
import mpa.core.logic.character.Minion;
import mpa.core.logic.character.Player;
import mpa.core.logic.character.Player.Item;
import mpa.core.logic.character.TowerCrusher;
import mpa.core.logic.fights.AttackRequests;
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
	private List<Minion> minionsAlive = new ArrayList<>();
	private List<TowerCrusher> towerCrushers = new ArrayList<>();

	private IDPool minionIDs;
	private IDPool towerCrusherIDs;
	private IDPool towerIDs;

	private List<Player> deadPlayers = new ArrayList<>();
	private List<Minion> deadMinions = new ArrayList<>();
	private List<TowerCrusher> deadTowerCrushers = new ArrayList<>();

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
		this.players = new ArrayList<Player>();
		AI_players = new HashMap<>();
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
	}

	public void addAIPlayer(Player player)
	{
		OpponentAI newAI = new OpponentAI(player, difficultyLevel);
		AI_players.put(player, newAI);
		addPlayer(player);
		newAI.start();
	}

	public ArrayList<Minion> createMinions(Player boss, int quantity, Player target)
	{
		ArrayList<Minion> minions = new ArrayList<>();

		ArrayList<Vector2f> computeRandomPointsCircumference = MyMath.computeRandomPointsCircumference(boss.getHeadquarter().getPosition(),
				MyMath.distanceFloat(boss.getHeadquarter().getPosition(), boss.getHeadquarter().getGatheringPlace()), quantity);
		for (Vector2f vector2f : computeRandomPointsCircumference)
		{
			System.out.println(vector2f);
		}

		for (int i = 0; i < quantity; i++)
		{
			Minion createdMinion = boss.createMinion(target, minionIDs.getID(), computeRandomPointsCircumference.get(i));
			minions.add(createdMinion);
			minionsAlive.add(createdMinion);
		}

		return minions;
	}

	public ArrayList<TowerCrusher> createTowerCrushers(Player boss, Tower target)
	{
		ArrayList<TowerCrusher> towerCrushers = new ArrayList<>();
		towerCrushers.add(boss.createTowerCrusher(target, towerCrusherIDs.getID()));

		return towerCrushers;
	}

	public TowerCrusher createTowerCrusher(Player boss, Tower target)
	{
		TowerCrusher createTowerCrusher = boss.createTowerCrusher(target, towerCrusherIDs.getID());
		towerCrushers.add(createTowerCrusher);

		return createTowerCrusher;
	}

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

		try
		{
			abstractPrivateProperty.setController(employee);
		} catch (ControllerAlreadyPresentException | DifferentOwnerException e)
		{
			e.printStackTrace();
		}
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
			try
			{
				abstractPrivateProperty.setController(employSubaltern);
				computePath(employSubaltern, abstractPrivateProperty.getGatheringPlace().x, abstractPrivateProperty.getGatheringPlace().y);
			} catch (ControllerAlreadyPresentException | DifferentOwnerException e)
			{
				e.printStackTrace();
			}
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
		writeLock.unlock();
	}

	public void killMinion(Minion m)
	{
		m.stopMoving();
		deadMinions.add(m);
		minionsAlive.remove(m);
	}

	// TODO killTowerCrusher

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

		List<Player> deads = new ArrayList<>();

		for (Player p : deadPlayers)
			deads.add(p);

		deadPlayers.clear();

		return deads;

	}

	List<Minion> takeDeadMinions()
	{
		List<Minion> deads = new ArrayList<>();

		for (Minion m : deadMinions)
			deads.add(m);

		deadMinions.clear();

		return deads;
	}

	List<TowerCrusher> takeDeadTowerCrushers()
	{
		List<TowerCrusher> deads = new ArrayList<>();

		for (TowerCrusher towerCrusherID : deadTowerCrushers)
			deads.add(towerCrusherID);

		deadTowerCrushers.clear();

		return deads;
	}

	public void endGame(Object applicant)
	{
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

	public List<Player> playerAction(Player p, Vector2f target)
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
			if (p.getPotionAmount(Potions.HP) > 0)
			{
				p.restoreHealth(Potions.HP);
				p.takePotion(Potions.HP);
			}
		}
		else
		{
			if (p.getPotionAmount(Potions.MP) > 0)
			{
				p.restoreHealth(Potions.MP);
				p.takePotion(Potions.MP);
			}
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
		for (Tower t : towers)
		{
			List<Player> hitPlayers = t.attack();

			for (Player p : hitPlayers)
				if (p.getHP() < 0)
					killPlayer(p);
		}
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

}

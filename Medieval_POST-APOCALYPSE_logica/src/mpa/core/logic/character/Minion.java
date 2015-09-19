package mpa.core.logic.character;

import javax.vecmath.Vector2f;

import mpa.core.logic.GameManager;
import mpa.core.logic.building.Headquarter;
import mpa.core.maths.MyMath;

public class Minion extends AbstractCharacter
{
	private Player boss;
	private Player target;
	private int attackStrength = 5;
	private float rangeOfPhysicallAttack = 7;
	private Vector2f targetPosition;
	private int health = 20;

	public Minion(String name, float x, float y, int health, Headquarter headquarter, Player boss, Player target)
	{
		super(name, x, y, health, headquarter);
		this.boss = boss;
		this.target = target;
		this.targetPosition = new Vector2f(target.getX(), target.getY());
		setID(name);

		GameManager.getInstance().computePath(this, target.getX(), target.getY());
	}

	@Override
	public boolean movePlayer()
	{
		// writeLock.lock();
		if (GameManager.getInstance().isPlayerDead(target))
		{
			for (Player p : GameManager.getInstance().getPlayers())
				if (p != boss)
				{
					target = p;
					targetPosition = p.getPosition();
					GameManager.getInstance().computePath(this, targetPosition.x, targetPosition.y);
					break;
				}
		}

		if (MyMath.distanceFloat(x, y, target.getX(), target.getY()) <= rangeOfPhysicallAttack)
		{
			System.out.println("sono un minion e sto attaccando");
			GameManager.getInstance().attackPhysically(this);
		}

		if (MyMath.distanceFloat(targetPosition.x, targetPosition.y, target.getX(), target.getY()) > 15f)
		{
			targetPosition.set(target.getX(), target.getY());
			GameManager.getInstance().computePath(this, targetPosition.x, targetPosition.y);
		}

		return super.movePlayer();
	}

	@Override
	public boolean amIAlive()
	{

		return health > 0;
	}

	public float getRangeOfPhysicallAttack()
	{
		try
		{
			readLock.lock();
			return rangeOfPhysicallAttack;

		} finally
		{
			readLock.unlock();
		}
	}

	public int getDamage()
	{
		return attackStrength + boss.getPlayerLevel().ordinal();
	}

	@Override
	public boolean inflictDamage(int damage)
	{
		try
		{
			writeLock.lock();
			health -= damage;
			return health <= 0;

		} finally
		{

			writeLock.unlock();
		}
	}

	public Player getBoss()
	{
		return boss;
	}
}

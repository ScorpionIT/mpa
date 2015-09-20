package mpa.core.logic.characters;

import java.util.List;

import mpa.core.logic.GameManager;
import mpa.core.logic.building.Headquarter;
import mpa.core.logic.building.Tower;
import mpa.core.maths.MyMath;

public class TowerCrusher extends AbstractCharacter
{
	private int attackStrength = 5;
	private float rangeOfPhysicallAttack = 3;
	private Player boss;
	private Player enemy;
	private Tower target;
	private int health = 10;

	public TowerCrusher(String name, float x, float y, int health, Headquarter headquarter, Player boss, Player enemy, Tower target)
	{
		super(name, x, y, health, headquarter);
		setID(name);
		this.boss = boss;
		this.target = target;
		this.enemy = enemy;
		GameManager.getInstance().computePath(this, target.getGatheringPlace().x, target.getGatheringPlace().y);

	}

	@Override
	public boolean movePlayer()
	{
		if (GameManager.getInstance().isTowerDestroyed(target))
		{
			if (GameManager.getInstance().isPlayerDead(enemy))
				for (Player p : GameManager.getInstance().getPlayers())
					if (p != boss)
					{
						enemy = p;
						break;
					}
			List<Tower> towers = enemy.getTowers();

			float minDistance = Float.MAX_VALUE;

			for (Tower t : towers)
			{
				float currentDistance = MyMath.distanceFloat(x, y, t.getX(), t.getY());
				if (currentDistance < minDistance)
				{
					target = t;
					minDistance = currentDistance;
				}
			}
			enemy = target.getOwner();
			GameManager.getInstance().computePath(this, target.getX(), target.getY());
		}
		if (MyMath.distanceFloat(x, y, target.getX(), target.getY()) < rangeOfPhysicallAttack)
			GameManager.getInstance().attackPhysically(this);

		return super.movePlayer();
	}

	@Override
	public boolean amIAlive()
	{
		return health > 0;
	}

	public Tower getTarget()
	{
		return target;
	}

	public int getAttackStrength()
	{
		return attackStrength;
	}

	public float getRangeOfAttack()
	{
		return rangeOfPhysicallAttack;
	}

	public Player getBoss()
	{
		return boss;
	}

	public int getHealth()
	{
		return health;
	}

}

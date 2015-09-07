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

	public Minion( String name, float x, float y, int health, Headquarter headquarter, Player boss,
			Player target )
	{
		super( name, x, y, health, headquarter );
		this.boss = boss;
		this.target = target;
		this.targetPosition = new Vector2f( target.getX(), target.getY() );

		GameManager.getInstance().computePath( this, target.getX(), target.getY() );
	}

	@Override
	public boolean movePlayer()
	{
		if( MyMath.distanceFloat( x, y, target.getX(), target.getY() ) < rangeOfPhysicallAttack )
			GameManager.getInstance().attackPhysically( this );

		if( MyMath.distanceFloat( targetPosition.x, targetPosition.y, target.getX(), target.getY() ) > 15f )
		{
			targetPosition.set( target.getX(), target.getY() );
			GameManager.getInstance().computePath( this, targetPosition.x, targetPosition.y );
		}

		return super.movePlayer();
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
}

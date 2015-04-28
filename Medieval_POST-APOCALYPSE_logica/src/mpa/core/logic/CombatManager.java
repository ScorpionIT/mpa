package mpa.core.logic;

import java.util.ArrayList;

import javax.vecmath.Vector2f;

import mpa.core.logic.character.Player;
import mpa.core.logic.tool.Potions;
import mpa.core.maths.MyMath;

public class CombatManager
{
	private static CombatManager combatManager = null;
	private final int MP_REQUIRED_FOR_PHYSICALL_ATTACK = 10;
	private final int MP_REQUIRED_FOR_DISTANCE_ATTACK = 15;

	private CombatManager()
	{
	}

	public static CombatManager getInstance()
	{
		if( combatManager == null )
			combatManager = new CombatManager();

		return combatManager;
	}

	public ArrayList<Player> attackPhysically( Player attacker )
	{

		try
		{
			attacker.getReadLock();

			ArrayList<Player> hitPlayers = new ArrayList<>();
			if( attacker.getMP() < MP_REQUIRED_FOR_PHYSICALL_ATTACK )
				return hitPlayers;
			else
				attacker.setMP( attacker.getMP() - MP_REQUIRED_FOR_PHYSICALL_ATTACK );

			Vector2f direction = attacker.getPlayerDirection();

			for( Player p : GameManager.getInstance().getPlayers() )
			{
				if( p == attacker )
					continue;

				if( attacker.getName().compareTo( p.getName() ) <= 0 )
				{
					attacker.getWriteLock();
					p.getWriteLock();
				}
				else
				{
					p.getWriteLock();
					attacker.getWriteLock();
				}

				if( MyMath.distanceFloat(
						attacker.getX() + direction.x * attacker.getRangeOfPhysicallAttack(),
						attacker.getY() + direction.y * attacker.getRangeOfPhysicallAttack(),
						p.getX(), p.getY() ) <= 1
						|| MyMath.distanceFloat( attacker.getX(), attacker.getY(), p.getX(),
								p.getY() ) <= attacker.getRangeOfPhysicallAttack() )
				{
					p.inflictDamage( attacker.getPhysicallAttackDamage() );
					hitPlayers.add( p );
				}

				attacker.leaveWriteLock();
				p.leaveWriteLock();
			}

			return hitPlayers;

		} finally
		{
			attacker.leaveReadLock();
		}
	}

	private ArrayList<Player> flashBangAttack( Player attacker )
	{
		ArrayList<Player> hitPlayers = new ArrayList<>();

		// TODO

		return hitPlayers;
	}

	private ArrayList<Player> granadeAttack( Player attacker )
	{
		ArrayList<Player> hitPlayers = new ArrayList<>();

		try
		{
			attacker.getReadLock();

			if( attacker.getMP() < MP_REQUIRED_FOR_DISTANCE_ATTACK
					|| attacker.getPotionAmount( Potions.GRANADE ) == 0 )
			{
				// throw exception

				return hitPlayers;
			}
			else
				attacker.setMP( attacker.getMP() - MP_REQUIRED_FOR_DISTANCE_ATTACK );

			Vector2f direction = attacker.getPlayerDirection();

			for( Player p : GameManager.getInstance().getPlayers() )
			{
				if( p == attacker )
					continue;

				if( attacker.getName().compareTo( p.getName() ) <= 0 )
				{
					attacker.getWriteLock();
					p.getWriteLock();
				}
				else
				{
					p.getWriteLock();
					attacker.getWriteLock();
				}

				float distance = MyMath.distanceFloat(
						attacker.getX() + direction.x * attacker.getRangeOfDistanceAttack(),
						attacker.getX() + direction.y * attacker.getRangeOfDistanceAttack(),
						p.getX(), p.getY() );

				float collisionRay = attacker.getDistanceAttackRayOfCollision();

				if( distance <= collisionRay )
				{
					p.inflictDamage( Potions.granadeDamage()
							- ( int ) ( distance * 100 / collisionRay ) );
					hitPlayers.add( p );
				}

				attacker.leaveWriteLock();
				p.leaveWriteLock();

			}

			return hitPlayers;
		} finally
		{
			attacker.leaveReadLock();
		}

	}

	public ArrayList<Player> distanceAttack( Player attacker, Potions potion )
	{

		if( potion.equals( Potions.FLASH_BANG ) )
			; // TODO

		return granadeAttack( attacker );

	}
}

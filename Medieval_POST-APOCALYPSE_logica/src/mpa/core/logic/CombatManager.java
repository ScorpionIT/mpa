package mpa.core.logic;

import java.util.ArrayList;

import javax.vecmath.Vector2f;

import mpa.core.logic.character.Player;
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
						p.getX(), p.getY() ) <= 1 )
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
}

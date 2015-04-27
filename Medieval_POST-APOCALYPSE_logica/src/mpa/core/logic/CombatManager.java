package mpa.core.logic;

import mpa.core.logic.character.Player;

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

	public boolean attackPhysically( Player attacker )
	{
		// try
		// {
		//
		// if( attacker.getName().compareTo( target.getName() ) <= 0 )
		// {
		// attacker.getWriteLock();
		// target.getWriteLock();
		// }
		// else
		// {
		// target.getWriteLock();
		// attacker.getWriteLock();
		// }
		//
		// if( attacker.getMP() < MP_REQUIRED_FOR_PHYSICALL_ATTACK )
		// return false;
		//
		// if( MyMath.distanceFloat( attacker.getX() + attacker.getRangeOfPhysicallAttack(), y1,
		// x2, y2 ) )
		//
		return true;
		//
		// } finally
		// {
		// attacker.leaveWriteLock();
		// target.leaveWriteLock();
		// }
	}
}

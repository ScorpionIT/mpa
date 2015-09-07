package mpa.core.logic.fights;

import java.util.ArrayList;

import javax.vecmath.Vector2f;

import mpa.core.logic.GameManager;
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

		ArrayList<Player> deadPlayers = new ArrayList<>();
		try
		{
			GameManager.getInstance().takeLock();

			attacker.getWriteLock();
			attacker.stopMoving();

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
					// attacker.getWriteLock();
					p.getWriteLock();
				}
				else
				{
					p.getWriteLock();
					// attacker.getWriteLock();
				}
				float distanceFloat = MyMath.distanceFloat( attacker.getX() + direction.x
						* attacker.getRangeOfPhysicallAttack(), attacker.getY() + direction.y
						* attacker.getRangeOfPhysicallAttack(), p.getX(), p.getY() );

				System.out.println( "la distanza è " + distanceFloat );

				float distanceFloat2 = MyMath.distanceFloat( attacker.getX(), attacker.getY(),
						p.getX(), p.getY() );

				System.out.println( "distano " + distanceFloat2 );
				if( MyMath.distanceFloat(
						attacker.getX() + direction.x * attacker.getRangeOfPhysicallAttack(),
						attacker.getY() + direction.y * attacker.getRangeOfPhysicallAttack(),
						p.getX(), p.getY() ) <= 1
						|| distanceFloat2 <= attacker.getRangeOfPhysicallAttack() )
				{
					if( p.inflictDamage( attacker.getPhysicallAttackDamage() ) )
					{
						System.out.println( "è morto?" );
						deadPlayers.add( p );
					}
					else
						hitPlayers.add( p );
				}

				// attacker.leaveWriteLock();
				p.leaveWriteLock();
			}

			return hitPlayers;

		} finally
		{
			GameManager.getInstance().leaveLock();
			for( Player dead : deadPlayers )
				GameManager.getInstance().killPlayer( dead );
			attacker.leaveWriteLock();
		}
	}

	private ArrayList<Player> granadeAttack( Player attacker, Vector2f target, boolean isFlashBang )
	{
		ArrayList<Player> hitPlayers = new ArrayList<>();
		ArrayList<Player> deadPlayers = new ArrayList<>();

		try
		{
			GameManager.getInstance().takeLock();

			float distanceToTarget = MyMath.distanceFloat( attacker.getX(), attacker.getY(),
					target.x, target.y );

			if( distanceToTarget > attacker.getRangeOfDistanceAttack() )
			{
				target = new Vector2f( attacker.getX() + attacker.getPlayerDirection().x
						* attacker.getRangeOfDistanceAttack(), attacker.getY()
						+ attacker.getPlayerDirection().y * attacker.getRangeOfDistanceAttack() );
			}

			attacker.getWriteLock();
			attacker.stopMoving();

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
					// attacker.getWriteLock();
					p.getWriteLock();
				}
				else
				{
					p.getWriteLock();
					// attacker.getWriteLock();
				}

				float distance = MyMath.distanceFloat( target.x, target.y, p.getX(), p.getY() );

				float collisionRay = attacker.getDistanceAttackRayOfCollision();

				if( distance <= collisionRay )
				{

					if( isFlashBang )
					{
						p.setFlashed( true );
						GameManager.getInstance().startFlashTimer( p );
						hitPlayers.add( p );
					}
					else
					{
						int damage = ( int ) ( ( Potions.granadeDamage() * distance ) / collisionRay );
						if( p.inflictDamage( Potions.granadeDamage() - damage ) )
						{
							System.out.println( "è morto?" );
							deadPlayers.add( p );
						}
						else
							hitPlayers.add( p );
					}
				}

				// attacker.leaveWriteLock();
				p.leaveWriteLock();

			}

			return hitPlayers;
		} finally
		{
			GameManager.getInstance().leaveLock();
			for( Player dead : deadPlayers )
				GameManager.getInstance().killPlayer( dead );
			attacker.leaveWriteLock();
		}

	}

	public ArrayList<Player> distanceAttack( Player attacker, Potions potion, Vector2f target )
	{

		switch( potion )
		{
			case GRANADE:
				return granadeAttack( attacker, target, false );
			case FLASH_BANG:
				return granadeAttack( attacker, target, true );

			default:
				ArrayList<Player> players = new ArrayList<Player>();
				players.add( attacker );
				return players;
		}

	}
}

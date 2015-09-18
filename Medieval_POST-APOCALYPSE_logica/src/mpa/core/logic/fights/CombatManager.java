package mpa.core.logic.fights;

import java.util.ArrayList;

import javax.vecmath.Vector2f;

import mpa.core.logic.GameManager;
import mpa.core.logic.building.Tower;
import mpa.core.logic.character.AbstractCharacter;
import mpa.core.logic.character.Minion;
import mpa.core.logic.character.Player;
import mpa.core.logic.character.TowerCrusher;
import mpa.core.logic.tool.Potions;
import mpa.core.maths.MyMath;

public class CombatManager
{
	private static CombatManager combatManager = null;
	private final int MP_REQUIRED_FOR_PHYSICALL_ATTACK = 10;
	private final int MP_REQUIRED_FOR_DISTANCE_ATTACK = 15;

	private ArrayList<Player> playerAttacks = new ArrayList<>();
	private ArrayList<Minion> minionAttacks = new ArrayList<>();
	private ArrayList<TowerCrusher> towerCrusherAttacks = new ArrayList<>();

	private CombatManager()
	{
	}

	public static CombatManager getInstance()
	{
		if( combatManager == null )
			combatManager = new CombatManager();

		return combatManager;
	}

	public synchronized ArrayList<Player> takePlayerAttacks()
	{
		ArrayList<Player> attackers = new ArrayList<>();

		for( Player p : playerAttacks )
			attackers.add( p );

		playerAttacks.clear();
		return attackers;

	}

	public synchronized ArrayList<Minion> takeMinionAttacks()
	{
		ArrayList<Minion> attackers = new ArrayList<>();

		for( Minion m : minionAttacks )
			attackers.add( m );

		minionAttacks.clear();
		return attackers;

	}

	public synchronized ArrayList<TowerCrusher> takeTowerCrusherAttacks()
	{
		ArrayList<TowerCrusher> attackers = new ArrayList<>();

		for( TowerCrusher t : towerCrusherAttacks )
			attackers.add( t );

		towerCrusherAttacks.clear();
		return attackers;

	}

	public synchronized ArrayList<AbstractCharacter> attackPhysically( Minion attacker )
	{
		ArrayList<Player> deadPlayers = new ArrayList<>();
		try
		{
			GameManager.getInstance().takeLock();

			attacker.getWriteLock();
			minionAttacks.add( attacker );
			attacker.stopMoving();

			ArrayList<AbstractCharacter> hitPlayers = new ArrayList<>();

			Vector2f direction = attacker.getPlayerDirection();

			for( Player p : GameManager.getInstance().getPlayers() )
			{
				float distanceFloat = MyMath.distanceFloat( attacker.getX() + direction.x
						* attacker.getRangeOfPhysicallAttack(), attacker.getY() + direction.y
						* attacker.getRangeOfPhysicallAttack(), p.getX(), p.getY() );

				System.out.println( "la distanza è " + distanceFloat );
				p.getWriteLock();

				float distanceFloat2 = MyMath.distanceFloat( attacker.getX(), attacker.getY(),
						p.getX(), p.getY() );

				System.out.println( "distano " + distanceFloat2 );
				if( !( MyMath.distanceFloat(
						attacker.getX() + direction.x * attacker.getRangeOfPhysicallAttack(),
						attacker.getY() + direction.y * attacker.getRangeOfPhysicallAttack(),
						p.getX(), p.getY() ) <= 1 || distanceFloat2 <= attacker
						.getRangeOfPhysicallAttack() ) )
				{
					p.leaveWriteLock();
				}
				else
				{
					if( p.inflictDamage( attacker.getDamage() ) && p instanceof Player )
					{
						System.out.println( "è morto?" );
						deadPlayers.add( p );
					}
					else
					{
						p.stopMoving();
						moveHitPlayer( p, attacker.getCurrentVector(), attacker.getDamage() );
						hitPlayers.add( p );
					}
					p.leaveWriteLock();
				}

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

	public synchronized ArrayList<AbstractCharacter> attackPhysically( Player attacker )
	{

		ArrayList<Player> deadPlayers = new ArrayList<>();
		ArrayList<Minion> deadMinions = new ArrayList<>();
		ArrayList<TowerCrusher> deadTowerCrushers = new ArrayList<>();
		try
		{
			GameManager.getInstance().takeLock();

			attacker.getWriteLock();

			ArrayList<AbstractCharacter> hitPlayers = new ArrayList<>();
			if( attacker.getMP() < MP_REQUIRED_FOR_PHYSICALL_ATTACK )
				return hitPlayers;
			else
				attacker.setMP( attacker.getMP() - MP_REQUIRED_FOR_PHYSICALL_ATTACK );

			attacker.stopMoving();
			playerAttacks.add( attacker );

			Vector2f direction = attacker.getPlayerDirection();

			ArrayList<AbstractCharacter> allCharacters = new ArrayList<>();
			for( Player p : GameManager.getInstance().getPlayers() )
				allCharacters.add( p );
			for( Minion m : GameManager.getInstance().getMinionsAlive() )
				allCharacters.add( m );
			for( TowerCrusher tC : GameManager.getInstance().getTowerCrushers() )
				allCharacters.add( tC );

			for( AbstractCharacter p : allCharacters )
			{
				if( p == attacker )
					continue;

				p.getWriteLock();

				float distanceFloat = MyMath.distanceFloat( attacker.getX() + direction.x
						* attacker.getRangeOfPhysicallAttack(), attacker.getY() + direction.y
						* attacker.getRangeOfPhysicallAttack(), p.getX(), p.getY() );

				System.out.println( "la distanza è " + distanceFloat );

				float distanceFloat2 = MyMath.distanceFloat( attacker.getX(), attacker.getY(),
						p.getX(), p.getY() );

				System.out.println( "distano " + distanceFloat2 );
				if( !( MyMath.distanceFloat(
						attacker.getX() + direction.x * attacker.getRangeOfPhysicallAttack(),
						attacker.getY() + direction.y * attacker.getRangeOfPhysicallAttack(),
						p.getX(), p.getY() ) <= 1 || distanceFloat2 <= attacker
						.getRangeOfPhysicallAttack() ) )
				{

					p.leaveWriteLock();
				}
				else
				{
					p.stopMoving();
					p.setDirection( new Vector2f( -direction.x, -direction.y ) );
					if( p.inflictDamage( attacker.getPhysicallAttackDamage() ) )
					{
						System.out.println( "è morto?" );
						if( p instanceof Player )
							deadPlayers.add( ( ( Player ) p ) );
						else if( p instanceof Minion )
							deadMinions.add( ( ( Minion ) p ) );
						else if( p instanceof TowerCrusher )
							deadTowerCrushers.add( ( ( TowerCrusher ) p ) );
					}
					else
					{
						moveHitPlayer( p, attacker.getCurrentVector(),
								attacker.getPhysicallAttackDamage() );
						hitPlayers.add( p );
					}
					p.leaveWriteLock();
				}

			}

			return hitPlayers;

		} finally
		{
			GameManager.getInstance().leaveLock();
			for( Player dead : deadPlayers )
				GameManager.getInstance().killPlayer( dead );
			for( Minion m : deadMinions )
				GameManager.getInstance().killMinion( m );
			attacker.leaveWriteLock();
		}
	}

	private void moveHitPlayer( AbstractCharacter hit, Vector2f direction, int strength )
	{
		Vector2f newPosition = new Vector2f( hit.getX() + direction.x * strength / 5, hit.getY()
				+ direction.y * strength / 5 );
		hit.setPosition( newPosition );
	}

	public ArrayList<Tower> attackOnTower( TowerCrusher tC )
	{
		ArrayList<Tower> hitTowers = new ArrayList<>();

		Tower target = tC.getTarget();

		if( MyMath.distanceFloat( tC.getX(), tC.getY(), target.getX(), target.getY() ) <= tC
				.getRangeOfAttack() )
		{

			if( target.inflictDamage( tC.getAttackStrength() ) )
				GameManager.getInstance().destroyTower( target );

			hitTowers.add( target );

		}

		return hitTowers;

	}

	private synchronized ArrayList<AbstractCharacter> granadeAttack( Player attacker,
			Vector2f target, boolean isFlashBang )
	{
		ArrayList<AbstractCharacter> hitPlayers = new ArrayList<>();
		ArrayList<Player> deadPlayers = new ArrayList<>();
		ArrayList<Minion> deadMinions = new ArrayList<>();
		ArrayList<TowerCrusher> deadTowerCrushers = new ArrayList<>();

		try
		{
			GameManager.getInstance().takeLock();

			attacker.getWriteLock();

			if( attacker.getMP() < MP_REQUIRED_FOR_DISTANCE_ATTACK
					|| attacker.getPotionAmount( Potions.GRANADE ) == 0 )
				return hitPlayers;
			else
				attacker.setMP( attacker.getMP() - MP_REQUIRED_FOR_DISTANCE_ATTACK );

			attacker.stopMoving();
			float distanceToTarget = MyMath.distanceFloat( attacker.getX(), attacker.getY(),
					target.x, target.y );

			if( distanceToTarget > attacker.getRangeOfDistanceAttack() )
			{
				target = new Vector2f( attacker.getX() + attacker.getPlayerDirection().x
						* attacker.getRangeOfDistanceAttack(), attacker.getY()
						+ attacker.getPlayerDirection().y * attacker.getRangeOfDistanceAttack() );
			}

			ArrayList<AbstractCharacter> allCharacters = new ArrayList<>();
			for( Player p : GameManager.getInstance().getPlayers() )
				allCharacters.add( p );
			for( Minion m : GameManager.getInstance().getMinionsAlive() )
				allCharacters.add( m );
			for( TowerCrusher tC : GameManager.getInstance().getTowerCrushers() )
				allCharacters.add( tC );

			for( AbstractCharacter p : allCharacters )
			{
				if( p == attacker )
					continue;

				p.getWriteLock();
				float distance = MyMath.distanceFloat( target.x, target.y, p.getX(), p.getY() );

				float collisionRay = attacker.getDistanceAttackRayOfCollision();

				if( distance > collisionRay )
				{
					p.leaveWriteLock();
				}
				else
				{

					if( isFlashBang && p instanceof Player )
					{
						( ( Player ) p ).setFlashed( true );
						GameManager.getInstance().startFlashTimer( ( ( Player ) p ) );
						hitPlayers.add( p );
					}
					else
					{
						int damage = ( int ) ( ( Potions.granadeDamage() * distance ) / collisionRay );
						if( p.inflictDamage( Potions.granadeDamage() - damage ) )
						{
							System.out.println( "è morto?" );
							if( p instanceof Player )
								deadPlayers.add( ( ( Player ) p ) );
							else if( p instanceof Minion )
								deadMinions.add( ( ( Minion ) p ) );
							else if( p instanceof TowerCrusher )
								deadTowerCrushers.add( ( ( TowerCrusher ) p ) );
						}
						else
							hitPlayers.add( p );
					}
					p.leaveWriteLock();
				}

			}

			return hitPlayers;
		} finally
		{
			GameManager.getInstance().leaveLock();
			for( Player dead : deadPlayers )
				GameManager.getInstance().killPlayer( dead );
			for( Minion m : deadMinions )
				GameManager.getInstance().killMinion( m );
			attacker.leaveWriteLock();
		}

	}

	public ArrayList<AbstractCharacter> distanceAttack( Player attacker, Potions potion,
			Vector2f target )
	{

		switch( potion )
		{
			case GRANADE:
				return granadeAttack( attacker, target, false );
			case FLASH_BANG:
				return granadeAttack( attacker, target, true );

			default:
				ArrayList<AbstractCharacter> players = new ArrayList<>();
				players.add( attacker );
				return players;
		}

	}

	public int getMP_REQUIRED_FOR_DISTANCE_ATTACK()
	{
		return MP_REQUIRED_FOR_DISTANCE_ATTACK;
	}

	public int getMP_REQUIRED_FOR_PHYSICALL_ATTACK()
	{
		return MP_REQUIRED_FOR_PHYSICALL_ATTACK;
	}
}

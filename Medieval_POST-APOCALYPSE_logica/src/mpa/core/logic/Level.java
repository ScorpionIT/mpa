package mpa.core.logic;

import mpa.core.logic.characters.Player;
import mpa.core.logic.potions.Potions;

public enum Level
{
	NEWBIE, SER, LORD, WAR_LORD, KING;

	private int stoneRequired = 150;
	private int wheatRequired = 200;
	private int woodRequired = 150;
	private int herbsRequired = 45;
	private final int RANGE_PHYSICALL_ATTACK_INCREMENT = 2;
	private final int COLLISION_RAY_INCREMENT = 2;
	private final int PHYSICALL_ATTACK_DAMAGE_INCREMENT = 10;

	private final int MAXIMUM_HP_POTION_ALLOWED = 2;
	private final int MAXIMUM_MP_POTION_ALLOWED = 2;
	private final int MAXIMUM_GRANADE_ALLOWED = 3;
	private final int MAXIMUM_FLASH_BANG_ALLOWED = 1;

	public boolean hasNext()
	{
		if( this.ordinal() < Level.values().length - 1 )
			return true;
		return false;
	}

	public boolean hasPrevious()
	{
		if( this.ordinal() > 0 )
			return true;
		return false;
	}

	private boolean areResourcesEnough( Player player )
	{
		if( player.getResourceAmount( "wheat" ) < ( player.getPlayerLevel().ordinal() + 1 )
				* wheatRequired )
			return false;
		else if( player.getResourceAmount( "stone" ) < ( player.getPlayerLevel().ordinal() + 1 )
				* stoneRequired )
			return false;
		else if( player.getResourceAmount( "herbs" ) < ( player.getPlayerLevel().ordinal() + 1 )
				* herbsRequired )
			return false;
		else if( player.getResourceAmount( "wood" ) < ( player.getPlayerLevel().ordinal() + 1 )
				* woodRequired )
			return false;
		else
			return true;
	}

	public boolean canUpgrade( Player player )
	{
		if( player.getPlayerLevel().hasNext() && areResourcesEnough( player ) )
			return true;

		return false;
	}

	public boolean upgradeLevel( Player player )
	{
		if( canUpgrade( player ) )
		{
			// player.putResources( "IRON", -( player.getPlayerLevel().ordinal() + 1 * ironRequired
			// ) );
			player.putResources( "stone",
					-( ( player.getPlayerLevel().ordinal() ) + 1 * stoneRequired ) );
			player.putResources( "wheat",
					-( ( player.getPlayerLevel().ordinal() + 1 * wheatRequired ) ) );
			player.putResources( "herbs",
					-( ( player.getPlayerLevel().ordinal() + 1 * herbsRequired ) ) );
			player.putResources( "wood",
					-( ( player.getPlayerLevel().ordinal() + 1 * woodRequired ) ) );
			player.setRangeOfPhysicallAttack( player.getRangeOfPhysicallAttack()
					+ RANGE_PHYSICALL_ATTACK_INCREMENT );
			player.setDistanceAttackRayOfCollision( player.getDistanceAttackRayOfCollision()
					+ COLLISION_RAY_INCREMENT );
			player.setPhysicallAttackDamage( player.getPhysicallAttackDamage()
					+ PHYSICALL_ATTACK_DAMAGE_INCREMENT );
			player.setLevel( Level.values()[player.getPlayerLevel().ordinal() + 1] );

			System.out.println();
			System.out.println();
			System.out.println( "HO UPPATO DI LIVELLO!" );
			System.out.println( "HO UPPATO DI LIVELLO!" );
			System.out.println( "HO UPPATO DI LIVELLO!" );
			System.out.println( "HO UPPATO DI LIVELLO!" );
			System.out.println();
			System.out.println();
			return true;
		}
		return false;
	}

	public Level getPrevious( Level current )
	{
		if( current.hasPrevious() )
			return Level.values()[current.ordinal() - 1];
		return current;
	}

	public int getNumberOfSubalterns( Level level )
	{
		switch( level )
		{
			case NEWBIE:
				return 4;
			case SER:
				return 8;
			case LORD:
				return 12;
			case WAR_LORD:
				return 16;
			case KING:
				return 20;

		}
		return 0;
	}

	public boolean canBuy( Potions potion, int amount )
	{
		int maximumAllowed = 0;

		switch( potion )
		{
			case HP:
				maximumAllowed = MAXIMUM_HP_POTION_ALLOWED;
				break;
			case MP:
				maximumAllowed = MAXIMUM_MP_POTION_ALLOWED;
				break;
			case GRANADE:
				maximumAllowed = MAXIMUM_GRANADE_ALLOWED;
				break;
			case FLASH_BANG:
				maximumAllowed = MAXIMUM_FLASH_BANG_ALLOWED;
				break;
		}

		if( amount >= maximumAllowed + ordinal() )
			return false;
		return true;
	}
}

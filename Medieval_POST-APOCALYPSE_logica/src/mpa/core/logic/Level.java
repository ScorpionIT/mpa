package mpa.core.logic;

import mpa.core.logic.character.Player;
import mpa.core.logic.resource.Resources;

public enum Level
{
	NEWBIE, SER, LORD, WAR_LORD, KING;

	private int goldRequired = 100;
	private int ironRequired = 150;
	private int stoneRequired = 150;
	private final int RANGE_PHYSICALL_ATTACK_INCREMENT = 2;
	private final int COLLISION_RAY_INCREMENT = 2;

	public boolean hasNext()
	{
		if( this.ordinal() < Level.values().length )
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
		if( player.getResourceAmount( "Iron" ) < ( player.getPlayerLevel().ordinal() + 1 )
				* ironRequired )
			return false;
		else if( player.getResourceAmount( "Stone" ) < ( player.getPlayerLevel().ordinal() + 1 )
				* stoneRequired )
			return false;
		else if( player.getResourceAmount( "Gold" ) < ( player.getPlayerLevel().ordinal() + 1 )
				* stoneRequired )
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
			player.putResources( Resources.IRON,
					-( player.getPlayerLevel().ordinal() + 1 * ironRequired ) );
			player.putResources( Resources.STONE,
					-( player.getPlayerLevel().ordinal() + 1 * stoneRequired ) );
			// player.putResources( Resources.GO, -( player.getPlayerLevel().ordinal() + 1 *
			// goldRequired ) );
			player.setRangeOfPhysicallAttack( player.getRangeOfPhysicallAttack()
					+ RANGE_PHYSICALL_ATTACK_INCREMENT );
			player.setDistanceAttackRayOfCollision( player.getDistanceAttackRayOfCollision()
					+ COLLISION_RAY_INCREMENT );
			player.setLevel( Level.values()[player.getPlayerLevel().ordinal() + 1] );

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

}

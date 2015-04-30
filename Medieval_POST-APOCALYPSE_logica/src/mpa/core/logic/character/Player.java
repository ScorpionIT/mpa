package mpa.core.logic.character;

import java.util.ArrayList;
import java.util.HashMap;

import mpa.core.logic.Level;
import mpa.core.logic.building.AbstractPrivateProperty;
import mpa.core.logic.building.Headquarter;
import mpa.core.logic.resource.Resources;
import mpa.core.logic.tool.AbstractTool;
import mpa.core.logic.tool.PotionManager;
import mpa.core.logic.tool.Potions;

public class Player extends AbstractCharacter
{
	private Headquarter headquarter;
	private ArrayList<DependentCharacter> subalterns;

	private int employedSubalterns = 0;

	private Level level;

	// private Weapon weapon = null;

	private int MP;
	private float rangeOfPhysicallAttack = 5;
	private int physicallAttackDamage = 0;
	private float rangeOfDistanceAttack = 12;
	private float distanceAttackRayOfCollision = 4;

	private HashMap<Resources, Integer> resources = new HashMap<>();

	private HashMap<Potions, Integer> potions = new HashMap<>();

	public Player( String name, float x, float y, int health, Level level, Headquarter headquarter,
			int bagDimension )
	{
		super( name, x, y, health, bagDimension );
		this.headquarter = headquarter;
		subalterns = new ArrayList<DependentCharacter>();
		this.level = level;
		for( int i = 0; i < level.getNumberOfSubalterns( level ); i++ )
		{
			subalterns.add( new DependentCharacter( "ERia", headquarter.getX(), headquarter.getY(),
					100, 100, null, level, this ) );

		}

		MP = 100;

		resources.put( Resources.WHEAT, 0 );
		resources.put( Resources.IRON, 0 );
		resources.put( Resources.WOOD, 0 );
		resources.put( Resources.STONE, 0 );
		resources.put( Resources.HERBS, 0 );

		potions.put( Potions.HP, 0 );
		potions.put( Potions.MP, 0 );
		potions.put( Potions.GRANADE, 0 );
		potions.put( Potions.FLASH_BANG, 0 );

	}

	public Headquarter getHeadquarter()
	{
		return headquarter;
	}

	public boolean pickUpTool( AbstractTool tool )
	{
		return this.bag.addTool( tool );
	}

	public boolean throwTool( AbstractTool tool )
	{
		return this.bag.removeTool( tool );
	}

	public DependentCharacter employSubaltern( AbstractPrivateProperty abstractPrivateProperty )
	{
		for( DependentCharacter subaltern : subalterns )
		{
			if( subaltern.getAbstractPrivateProperty() == null )
			{
				subaltern.setAbstractPrivateProperty( abstractPrivateProperty );
				subaltern.setAbstractPrivateProperty( abstractPrivateProperty );
				employedSubalterns++;
				return subaltern;
			}
		}

		return null;
	}

	public boolean employSubaltern( DependentCharacter dependentCharacter,
			AbstractPrivateProperty abstractPrivateProperty )
	{
		if( subalterns.contains( dependentCharacter ) )
		{
			dependentCharacter.setAbstractPrivateProperty( abstractPrivateProperty );
			return true;
		}
		else
			return false;

	}

	public ArrayList<DependentCharacter> getFreeSubalterns()
	{
		ArrayList<DependentCharacter> freeSubalterns = new ArrayList<>();
		for( DependentCharacter subaltern : subalterns )
		{
			if( subaltern.getAbstractPrivateProperty() == null )
			{
				freeSubalterns.add( subaltern );
			}

		}
		return freeSubalterns;
	}

	public DependentCharacter getSubaltern( AbstractPrivateProperty abstractPrivateProperty )
	{
		for( DependentCharacter subaltern : subalterns )
		{
			if( abstractPrivateProperty == subaltern.getAbstractPrivateProperty() )
				return subaltern;

		}
		return null;

	}

	public void inflictDamage( int damage )
	{
		writeLock.lock();

		if( damage >= health )
		{

		}
		else
			health -= damage;

		writeLock.unlock();

	}

	public boolean freeSubaltern( DependentCharacter dependentCharacter )
	{
		if( subalterns.contains( dependentCharacter ) )
		{
			dependentCharacter.setAbstractPrivateProperty( null );
			employedSubalterns--;
			return true;

		}
		return false;
	}

	public Level getPlayerLevel()
	{
		return this.level;
	}

	public void setLevel( Level level )
	{
		this.level = level;
	}

	public ArrayList<DependentCharacter> getSubalterns()
	{
		return subalterns;
	}

	public void kickPlayer()
	{
		writeLock.lock();

		headquarter.setOwner( null );

		for( DependentCharacter subaltern : subalterns )
		{
			subaltern.leaveProperty();
		}

		writeLock.unlock();
	}

	public void putResources( Resources type, int providing )
	{
		writeLock.lock();
		switch( type )
		{
			case IRON:
				resources.put( Resources.IRON, resources.get( Resources.IRON ) + providing );
				break;
			case WHEAT:
				resources.put( Resources.WHEAT, resources.get( Resources.WHEAT ) + providing );
				break;
			case WOOD:
				resources.put( Resources.WOOD, resources.get( Resources.WOOD ) + providing );
				break;
			case HERBS:
				resources.put( Resources.HERBS, resources.get( Resources.HERBS ) + providing );
				break;
			default:
		}

		writeLock.unlock();
	}

	public int getResourceAmount( Resources type )
	{
		try
		{
			readLock.lock();
			return resources.get( type );
		} finally
		{
			readLock.unlock();
		}
	}

	public boolean upgradeLevel()
	{
		try
		{
			writeLock.lock();

			if( level.canUpgrade( this ) )
			{
				level.upgradeLevel( this );
				return true;
			}
			else
			{
				return false;
			}
		} finally
		{
			writeLock.unlock();
		}
	}

	public boolean canUpgrade()
	{
		try
		{
			readLock.lock();
			return level.canUpgrade( this );
		} finally
		{
			readLock.unlock();
		}
	}

	public boolean isThereAnyFreeSulbaltern()
	{
		try
		{
			readLock.lock();
			return subalterns.size() - employedSubalterns > 0;
		} finally
		{
			readLock.unlock();
		}
	}

	public boolean canBuyPotions()
	{
		try
		{
			readLock.lock();

			for( Potions p : Potions.values() )
			{
				HashMap<Resources, Integer> price = PotionManager.getInstance().getPrice( p );

				for( Resources r : price.keySet() )
				{
					if( resources.get( r ) < price.get( r ) )
						return false;
				}
			}

			return true;

		} finally
		{
			readLock.unlock();
		}
	}

	public boolean canBuyPotion( Potions potion )
	{
		try
		{
			readLock.lock();

			HashMap<Resources, Integer> price = PotionManager.getInstance().getPrice( potion );

			for( Resources r : price.keySet() )
			{
				if( resources.get( r ) < price.get( r ) )
					return false;
			}

			return true;

		} finally
		{
			readLock.unlock();
		}
	}

	public int getPotionAmount( Potions p )
	{
		try
		{
			readLock.lock();

			return potions.get( p );
		} finally
		{
			readLock.unlock();
		}
	}

	public boolean buyPotion( Potions potion )
	{
		try
		{
			writeLock.lock();
			if( x != headquarter.getCollectionPoint().x && y != headquarter.getCollectionPoint().y )
				return false;

			HashMap<Resources, Integer> price = PotionManager.getInstance().getPrice( potion );

			for( Resources r : price.keySet() )
			{
				if( price.get( r ) > resources.get( r ) )
					return false;
			}
			for( Resources r : price.keySet() )
			{
				resources.put( r, resources.get( r ) - price.get( r ) );
			}

			return true;

		} finally
		{
			writeLock.unlock();
		}

	}

	public int getMP()
	{
		try
		{
			readLock.lock();
			return MP;
		} finally
		{
			readLock.unlock();
		}
	}

	public void setMP( int mp )
	{
		MP = mp;
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

	public void setRangeOfPhysicallAttack( float rangeOfPhysicallAttack )
	{
		writeLock.lock();
		this.rangeOfPhysicallAttack = rangeOfPhysicallAttack;
		writeLock.unlock();
	}

	public float getRangeOfDistanceAttack()
	{
		try
		{
			readLock.lock();

			return rangeOfDistanceAttack;
		} finally
		{
			readLock.unlock();
		}
	}

	public void setRangeOfDistanceAttack( int rangeOfDistanceAttack )
	{
		writeLock.lock();
		this.rangeOfDistanceAttack = rangeOfDistanceAttack;
		writeLock.unlock();
	}

	public float getDistanceAttackRayOfCollision()
	{
		try
		{
			readLock.lock();
			return distanceAttackRayOfCollision;

		} finally
		{
			readLock.unlock();
		}
	}

	public void setDistanceAttackRayOfCollision( float distanceAttackRayOfCollision )
	{
		writeLock.lock();
		this.distanceAttackRayOfCollision = distanceAttackRayOfCollision;
		writeLock.unlock();
	}

	public int getPhysicallAttackDamage()
	{
		try
		{
			readLock.lock();
			return physicallAttackDamage;
		} finally
		{
			readLock.unlock();
		}
	}

	public void setPhysicallAttackDamage( int physicallAttackDamage )
	{
		writeLock.lock();
		this.physicallAttackDamage = physicallAttackDamage;
		writeLock.unlock();
	}

	public HashMap<Resources, Integer> getResources()
	{
		return resources;
	}

}

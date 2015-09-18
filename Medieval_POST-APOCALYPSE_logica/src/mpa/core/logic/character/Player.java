package mpa.core.logic.character;

import java.util.ArrayList;
import java.util.HashMap;

import javax.vecmath.Vector2f;

import mpa.core.logic.Level;
import mpa.core.logic.building.AbstractPrivateProperty;
import mpa.core.logic.building.Headquarter;
import mpa.core.logic.building.Tower;
import mpa.core.logic.tool.PotionManager;
import mpa.core.logic.tool.Potions;

public class Player extends AbstractCharacter
{
	public enum Item
	{
		WEAPON, HP_POTION, MP_POTION, GRANADE, FLASH_BANG
	}

	private ArrayList<DependentCharacter> subalterns;

	private int employedSubalterns = 0;

	private Level level;

	private Item selectedItem = Item.WEAPON;
	private int MP;
	private float rangeOfPhysicallAttack = 20;
	private int physicallAttackDamage = 90;
	private float rangeOfDistanceAttack = 30;
	private float distanceAttackRayOfCollision = 12;
	private boolean flashed = false;
	private ArrayList<Tower> towers = new ArrayList<>();
	private ArrayList<AbstractPrivateProperty> properties = new ArrayList<>();

	private HashMap<String, Integer> resources = new HashMap<>();

	private HashMap<Potions, Integer> potions = new HashMap<>();

	public Player( String name, float x, float y, int health, Level level, Headquarter headquarter )
	{
		super( name, x, y, 100, headquarter );
		subalterns = new ArrayList<DependentCharacter>();
		this.level = level;
		Vector2f gatheringPlace = headquarter.getGatheringPlace();
		for( int i = 0; i < level.getNumberOfSubalterns( level ); i++ )
		{
			subalterns.add( new DependentCharacter( "ERia", gatheringPlace.x, gatheringPlace.y,
					100, level, this, headquarter ) );

		}

		MP = 100;

		resources.put( "WHEAT", 0 );
		resources.put( "IRON", 0 );
		resources.put( "WOOD", 0 );
		resources.put( "STONE", 0 );
		resources.put( "HERBS", 0 );

		potions.put( Potions.HP, 0 );
		potions.put( Potions.MP, 0 );
		potions.put( Potions.GRANADE, 0 );
		potions.put( Potions.FLASH_BANG, 0 );

	}

	public Headquarter getHeadquarter()
	{
		return headquarter;
	}

	public DependentCharacter employSubaltern( AbstractPrivateProperty abstractPrivateProperty )
	{
		try
		{
			writeLock.lock();
			for( DependentCharacter subaltern : subalterns )
			{
				if( subaltern.getAbstractPrivateProperty() == null )
				{
					subaltern.setAbstractPrivateProperty( abstractPrivateProperty );
					properties.add( abstractPrivateProperty );
					employedSubalterns++;
					return subaltern;
				}
			}
			return null;
		} finally
		{
			writeLock.unlock();
		}
	}

	public ArrayList<AbstractPrivateProperty> getProperties()
	{
		try
		{
			readLock.lock();
			return properties;
		} finally
		{
			readLock.unlock();
		}
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

	public void die()
	{
		writeLock.lock();
		for( DependentCharacter subaltern : subalterns )
			subaltern.leaveProperty();

		towers.clear();
		writeLock.unlock();
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

	@Override
	public boolean amIAlive()
	{
		return health > 0;
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

	public boolean inflictDamage( int damage )
	{
		try
		{
			writeLock.lock();
			health -= damage;
			// System.out.println( "la sua vita dopo l'attacco è " + health );
			return health <= 0;

		} finally
		{

			writeLock.unlock();
		}

	}

	public boolean hasEnoughResources( HashMap<String, Integer> required )
	{
		for( String request : required.keySet() )
		{
			if( resources.get( request ) < required.get( request ) )
				return false;
		}

		return true;
	}

	public void addTower( Tower t )
	{
		writeLock.lock();
		towers.add( t );
		writeLock.unlock();
	}

	public void removeTower( Tower t )
	{
		writeLock.lock();
		towers.remove( t );
		writeLock.unlock();
	}

	public void takeResources( HashMap<String, Integer> toTake )
	{
		for( String s : toTake.keySet() )
			resources.put( s, resources.get( s ) - toTake.get( s ) );

	}

	public boolean freeSubaltern( DependentCharacter dependentCharacter )
	{
		if( subalterns.contains( dependentCharacter ) )
		{
			dependentCharacter.leaveProperty();
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

	public void putResources( String type, int providing )
	{
		writeLock.lock();
		type = type.toUpperCase();
		switch( type )
		{
			case "STONE":
				resources.put( "STONE", resources.get( "STONE" ) + providing );
				break;
			case "IRON":
				resources.put( "IRON", resources.get( "STONE" ) + providing );
				break;
			case "WHEAT":
				resources.put( "WHEAT", resources.get( "STONE" ) + providing );
				break;
			case "WOOD":
				resources.put( "WOOD", resources.get( "STONE" ) + providing );
				break;
			case "HERBS":
				resources.put( "HERBS", resources.get( "STONE" ) + providing );
				break;
			default:
		}

		writeLock.unlock();
	}

	public int getResourceAmount( String type )
	{
		try
		{
			readLock.lock();
			type = type.toUpperCase();
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

			int counter = 0;
			for( Potions p : Potions.values() )
			{
				if( !level.canBuy( p, potions.get( p ) ) )
					counter++;
				HashMap<String, Integer> price = PotionManager.getInstance().getPrice( p );

				for( String r : price.keySet() )
				{
					if( resources.get( r ) < price.get( r ) )
						return false;
				}
			}

			if( counter == Potions.values().length )
				return false;

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

			if( level.canBuy( potion, potions.get( potion ) ) )
				return false;

			HashMap<String, Integer> price = PotionManager.getInstance().getPrice( potion );

			for( String r : price.keySet() )
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
			if( x != headquarter.getGatheringPlace().x && y != headquarter.getGatheringPlace().y )
				return false;

			if( !level.canBuy( potion, potions.get( potion ) ) )
				return false;

			HashMap<String, Integer> price = PotionManager.getInstance().getPrice( potion );

			for( String r : price.keySet() )
			{
				if( price.get( r ) > resources.get( r ) )
					return false;
			}
			for( String r : price.keySet() )
			{
				resources.put( r, resources.get( r ) - price.get( r ) );
			}

			potions.put( potion, potions.get( potion ) + 1 );
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
		System.out.println( "c'avevo " + MP + " me ne rimangono " + mp );
		MP = mp;
	}

	public int getHP()
	{
		try
		{
			readLock.lock();
			return health;
		} finally
		{
			readLock.unlock();
		}
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

	public void setSelectedItem( Item it )
	{
		writeLock.lock();
		selectedItem = it;
		writeLock.unlock();
	}

	public Item getSelectedItem()
	{
		try
		{
			readLock.lock();
			return selectedItem;
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

	public HashMap<String, Integer> getResources()
	{
		try
		{
			readLock.lock();
			return resources;
		} finally
		{
			readLock.unlock();
		}
	}

	public Potions takePotion( Potions potion )
	{
		try
		{
			writeLock.lock();
			potions.put( potion, potions.get( potion ) - 1 );
			return potion;
		} finally
		{
			writeLock.unlock();
		}
	}

	public boolean isFlashed()
	{
		try
		{
			readLock.lock();
			return flashed;
		} finally
		{
			readLock.unlock();
		}
	}

	public void setFlashed( boolean flash )
	{
		// writeLock.lock();

		flashed = flash;
		// writeLock.unlock();
	}

	public boolean restoreHealth( Potions potion )
	{
		if( !( potion.equals( Potions.HP ) || potion.equals( Potions.MP ) ) )
			return false;

		if( ( potion.equals( Potions.HP ) && health == 100 )
				|| ( potion.equals( Potions.MP ) && MP == 100 ) )
		{
			potions.put( potion, potions.get( potion ) + 1 );
			return false;
		}

		if( potion.equals( Potions.HP ) )
			health += Potions.getHPRestoring( level );
		else
			MP += Potions.getMPRestoring( level );

		return true;

	}

	public ArrayList<Tower> getTowers()
	{
		try
		{
			readLock.lock();
			return towers;
		} finally
		{
			readLock.unlock();
		}
	}

	public Minion createMinion( Player target, String ID, Vector2f position )
	{
		return new Minion( ID, position.x, position.y, 5 + 2 * level.ordinal(), headquarter, this,
				target );
	}

	public TowerCrusher createTowerCrusher( Tower target, String ID )
	{
		return new TowerCrusher( ID, headquarter.getGatheringPlace().x,
				headquarter.getGatheringPlace().y, 5 + 2 * level.ordinal(), headquarter, this,
				target.getOwner(), target );
	}
}

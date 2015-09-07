package mpa.core.logic.character;

import mpa.core.logic.Level;
import mpa.core.logic.building.AbstractPrivateProperty;
import mpa.core.logic.building.Headquarter;
import mpa.core.logic.resource.AbstractResourceProducer;

public class DependentCharacter extends AbstractCharacter
{
	private AbstractPrivateProperty abstractPrivateProperty;
	private Level level;
	private Player boss;

	public DependentCharacter( String name, float x, float y, int health, Level level, Player boss,
			Headquarter headquarter )
	{
		super( name, x, y, health, headquarter );
		this.level = level;
		this.abstractPrivateProperty = null;
		this.boss = boss;
	}

	public AbstractPrivateProperty getAbstractPrivateProperty()
	{
		return abstractPrivateProperty;
	}

	public void setAbstractPrivateProperty( AbstractPrivateProperty abstractPrivateProperty )
	{
		this.abstractPrivateProperty = abstractPrivateProperty;
		abstractPrivateProperty.setOwner( boss );

	}

	public Level getLevel()
	{
		return level;
	}

	public boolean setLevel( Object object, Level level )
	{
		if( object == boss )
		{
			this.level = level;
			return true;
		}
		else
			return false;
	}

	public boolean leaveProperty()
	{
		try
		{
			writeLock.lock();

			if( abstractPrivateProperty == null )
				return false;

			abstractPrivateProperty.setOwner( null );
			abstractPrivateProperty = null;
			return true;
		} finally
		{
			writeLock.unlock();
		}
	}

	public Player getBoss()
	{
		return boss;
	}

	@Override
	public boolean movePlayer()
	{
		boolean b = super.movePlayer();
		if( ( path == null || path.isEmpty() ) && abstractPrivateProperty != null )
			if( abstractPrivateProperty instanceof AbstractResourceProducer )
			{
				( ( AbstractResourceProducer ) abstractPrivateProperty ).setWorking( true );

				System.out.println( "stone " + boss.getResourceAmount( "STONE" ) );
			}

		return b;
	}
}

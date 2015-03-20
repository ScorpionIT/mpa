package mpa.core.logic.character;

import java.util.ArrayList;

import mpa.core.logic.Level;
import mpa.core.logic.building.AbstractPrivateProperty;
import mpa.core.logic.building.Headquarter;
import mpa.core.logic.tool.AbstractTool;

@SuppressWarnings("unused")
public class Player extends AbstractCharacter
{
	private Headquarter headquarter;
	private ArrayList<DependentCharacter> subalterns;

	private Level level;

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

	public boolean freeSubaltern( DependentCharacter dependentCharacter )
	{
		if( subalterns.contains( dependentCharacter ) )
		{
			dependentCharacter.setAbstractPrivateProperty( null );
			return true;

		}
		return false;
	}

	public Level getPlayerLevel()
	{
		return this.level;
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

	}

}

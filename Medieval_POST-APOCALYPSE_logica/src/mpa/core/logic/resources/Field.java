package mpa.core.logic.resources;

import mpa.core.logic.characters.Player;
import mpa.core.util.GameProperties;

enum FieldState
{
	PLOWING, SEEDING, GROWTH, HARVEST
}; // RACCOLTO, CRESCITA, ARATURA, SEMINA

public class Field extends AbstractResourceProducer
{
	private FieldState currentFieldState;
	private static final int PROVIDING = 50;
	private static final int EXTRA_PROVIDING = 10;

	public Field( float x, float y, Player player )
	{
		super( x, y, GameProperties.getInstance().getObjectWidth( "Field" ), GameProperties
				.getInstance().getObjectHeight( "Field" ), player );
		this.currentFieldState = FieldState.PLOWING;
	}

	public FieldState getCurrentFieldState()
	{
		return currentFieldState;
	}

	public boolean isFree()
	{
		try
		{
			readLock.lock();
			return owner == null;
		} finally
		{
			readLock.unlock();
		}
	}

	@Override
	public boolean providePlayer()
	{
		try
		{
			writeLock.lock();
			if( !super.providePlayer() )
				return false;
			if( owner != null )
			{
				if( this.currentFieldState.ordinal() < FieldState.values().length - 1 )
				{
					this.currentFieldState = FieldState.values()[currentFieldState.ordinal() + 1];
				}
				else
				{
					owner.putResources( "WHEAT", PROVIDING + getProvidingFactor() * PROVIDING
							+ EXTRA_PROVIDING * owner.getPlayerLevel().ordinal()
							+ getProvidingFactor() );
					this.currentFieldState = FieldState.values()[0];
				}

			}
			return true;
		} finally
		{
			writeLock.unlock();
		}
	}

	@Override
	public int getProviding()
	{
		return PROVIDING;
	}
}

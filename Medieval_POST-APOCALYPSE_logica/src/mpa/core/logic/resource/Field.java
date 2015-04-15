package mpa.core.logic.resource;

import mpa.core.logic.character.Player;

enum FieldState
{
	PLOWING, SEEDING, GROWTH, HARVEST
}; // RACCOLTO, CRESCITA, ARATURA, SEMINA

public class Field extends AbstractResourceProducer
{
	private FieldState currentFieldState;
	private static final int PROVIDING = 50;
	private static final int EXTRA_PROVIDING = 10;

	public Field(float x, float y, Player player)
	{
		super(x, y, 30, 30, player);
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
	public void providePlayer()
	{
		readLock.lock();
		if (owner == null)
			return;
		else
		{
			if (this.currentFieldState.ordinal() < FieldState.values().length)
			{
				this.currentFieldState = FieldState.values()[currentFieldState.ordinal() + 1];
			}
			else
			{
				owner.putResources(Resources.WHEAT, PROVIDING + EXTRA_PROVIDING * owner.getPlayerLevel().ordinal());
				this.currentFieldState = FieldState.values()[0];
			}

		}
		readLock.unlock();
	}

	@Override
	public int getProviding()
	{
		return PROVIDING;
	}
}

package mpa.core.logic.building;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import mpa.core.logic.character.DependentCharacter;
import mpa.core.logic.character.Player;

public abstract class AbstractPrivateProperty extends AbstractProperty
{

	protected Player owner;
	private DependentCharacter controller;

	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	protected Lock readLock = lock.readLock();
	protected Lock writeLock = lock.writeLock();

	public AbstractPrivateProperty( float x, float y, float width, float height, Player owner )
	{
		super( x, y, width, height );
		this.owner = owner;
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

	public void setOwner( Player player )
	{
		writeLock.lock();
		this.owner = player;
		writeLock.unlock();
	}

	public Player getOwner()
	{
		try
		{
			readLock.lock();
			return owner;
		} finally
		{
			readLock.unlock();
		}
	}

	public void setController( DependentCharacter controller )
			throws ControllerAlreadyPresentException, DifferentOwnerException
	{
		writeLock.lock();
		if( this.controller != null )
			throw new ControllerAlreadyPresentException();
		else if( owner != controller.getBoss() )
			throw new DifferentOwnerException();

		this.controller = controller;

		writeLock.unlock();
	}
}

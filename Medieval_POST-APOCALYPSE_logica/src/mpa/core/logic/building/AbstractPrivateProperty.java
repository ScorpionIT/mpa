package mpa.core.logic.building;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import mpa.core.logic.character.DependentCharacter;
import mpa.core.logic.character.Player;

public abstract class AbstractPrivateProperty extends AbstractProperty
{

	private Player owner;
	private DependentCharacter controller;
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	private Lock rLock = lock.readLock();
	private Lock wLock = lock.writeLock();

	public AbstractPrivateProperty( float x, float y, float width, float height, Player owner )
	{
		super( x, y, width, height );
		this.owner = owner;// TODO
	}

	public boolean setOwner( Player player )
	{
		try
		{
			wLock.lock();

			if( owner == null )
				return false;

			this.owner = player;
			return true;

		} finally
		{
			wLock.unlock();
		}
	}

	public Player getOwner()
	{
		try
		{
			rLock.lock();
			return owner;
		} finally
		{
			rLock.unlock();
		}
	}

	public void setController( DependentCharacter controller )
			throws ControllerAlreadyPresentException, DifferentOwnerException
	{
		wLock.lock();
		if( this.controller != null )
			throw new ControllerAlreadyPresentException();
		else if( owner != controller.getBoss() )
			throw new DifferentOwnerException();

		this.controller = controller;

		wLock.unlock();
	}
}
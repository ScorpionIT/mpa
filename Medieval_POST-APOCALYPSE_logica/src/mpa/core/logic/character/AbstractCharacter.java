package mpa.core.logic.character;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.vecmath.Vector2f;

import mpa.core.logic.AbstractObject;
import mpa.core.logic.Inventory;
import mpa.core.logic.building.Headquarter;
import mpa.core.maths.MyMath;

public abstract class AbstractCharacter extends AbstractObject
{
	private static final float PACE = 5;

	private ArrayList<Vector2f> path = new ArrayList<>();
	private Vector2f currentVector = new Vector2f( 0.0f, 0.0f );
	private int numberOfIterationsPerVector;
	private float rotationAngle;

	protected Headquarter headquarter;

	protected String name;
	protected int health; // 0 - 100
	protected Inventory bag;
	protected double paceX;
	protected double paceY;

	protected ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	protected Lock readLock = lock.readLock();
	protected Lock writeLock = lock.writeLock();

	public AbstractCharacter( String name, float x, float y, int health, int bagDimension,
			Headquarter headquarter )
	{
		super( x, y, 15, 15 ); // TODO
		this.name = name;
		this.health = health;
		this.bag = new Inventory( bagDimension );
		this.headquarter = headquarter;
		this.currentVector = new Vector2f( -x + headquarter.getX(), -y + headquarter.getY() );
		if( Math.abs( currentVector.x ) >= Math.abs( currentVector.y ) )
			currentVector = new Vector2f( 1f, currentVector.y / currentVector.x );
		else
			currentVector = new Vector2f( currentVector.x / currentVector.y, 1f );
		rotationAngle = MyMath.angleBetweenVectors( new Vector2f( 0, -1 ), currentVector );
		System.out.println();
		System.out.println( "ANGOLOMYMATH = " + rotationAngle + " con vectora = " + currentVector.x
				+ " " + currentVector.y );
		System.out.println( "HQ = " + headquarter.getX() + " " + headquarter.getY() );
		System.out.println( "Player = " + x + " " + y );
		System.out.println();
	}

	public void getWriteLock()
	{
		writeLock.lock();
	}

	public void leaveWriteLock()
	{
		writeLock.unlock();
	}

	public void getReadLock()
	{
		readLock.lock();
	}

	public void leaveReadLock()
	{
		readLock.unlock();
	}

	private void computeCurrentVector()
	{
		Vector2f oldVector = new Vector2f( currentVector );
		currentVector = new Vector2f( ( float ) ( -path.get( 0 ).x + path.get( 1 ).x ),
				( float ) ( -path.get( 0 ).y + path.get( 1 ).y ) );

		numberOfIterationsPerVector = ( int ) ( MyMath.distanceFloat( path.get( 0 ).x,
				path.get( 0 ).y, path.get( 1 ).x, path.get( 1 ).y ) / PACE );
		paceX = currentVector.x / numberOfIterationsPerVector;
		paceY = currentVector.y / numberOfIterationsPerVector;

		int greatestCommonDivisor = MyMath.greatestCommonDivisor(
				new Float( currentVector.x ).intValue(), new Float( currentVector.y ).intValue() );

		currentVector.set( currentVector.x / greatestCommonDivisor, currentVector.y
				/ greatestCommonDivisor );

		rotationAngle = MyMath.angleBetweenVectors( oldVector, currentVector );
		System.out.println();
		System.out.println( "ANGOLO = " + rotationAngle );
		System.out.println();
	}

	public void setPath( ArrayList<Vector2f> path )
	{
		writeLock.lock();

		this.path = path;
		if( path.size() > 1 )
		{
			computeCurrentVector();
		}
		else if( path.size() == 1 )
		{
			Vector2f point = new Vector2f( path.get( 0 ).x, path.get( 0 ).y );
			// currentVector = new Vector2f( point.x - x, point.y - y );
			currentVector = new Vector2f( -point.x + x, -point.y + y );

			numberOfIterationsPerVector = ( int ) ( MyMath.distanceFloat( x, point.x, y, point.y ) / PACE );
		}
		writeLock.unlock();
	}

	int counter = 1;

	public boolean movePlayer()
	{
		try
		{
			writeLock.lock();
			if( path == null || path.isEmpty() )
				return false;

			numberOfIterationsPerVector--;

			if( numberOfIterationsPerVector <= 0 )
			{
				path.remove( 0 );
				if( path.size() > 1 )
				{
					computeCurrentVector();
				}
				else
				{
					putThePlayerAtTheArrival();
					return false;
				}

				numberOfIterationsPerVector--;
				if( numberOfIterationsPerVector <= 0 )
				{
					putThePlayerAtTheArrival();
					return false;
				}
			}

			setX( ( ( float ) ( x + paceX ) ) );
			setY( ( float ) ( y + paceY ) );

			if( numberOfIterationsPerVector == 0 && path.size() == 2 )
			{
				putThePlayerAtTheArrival();
			}

			return true;

		} finally
		{
			writeLock.unlock();
		}

	}

	private void putThePlayerAtTheArrival()
	{
		if( path.size() >= 1 && path.size() <= 2 )
		{
			Vector2f finalPosition = path.get( path.size() - 1 );
			setX( finalPosition.x );
			setY( finalPosition.y );
			path = null;
		}
	}

	@Override
	public float getX()
	{
		try
		{
			readLock.lock();
			return super.getX();
		} finally
		{
			readLock.unlock();
		}
	}

	@Override
	public float getY()
	{
		try
		{
			readLock.lock();
			return super.getY();
		} finally
		{
			readLock.unlock();
		}
	}

	public ArrayList<Vector2f> getPath()
	{
		try
		{
			readLock.lock();
			return path;
		} finally
		{
			readLock.unlock();
		}
	}

	public Vector2f getPlayerDirection()
	{
		try
		{
			readLock.lock();
			return new Vector2f( currentVector.x, currentVector.y );
		} finally
		{
			readLock.unlock();
		}
	}

	public float getRotationAngle()
	{
		try
		{
			readLock.lock();
			return rotationAngle;
		} finally
		{
			rotationAngle = 0f;
			readLock.unlock();
		}
	}

	public void setName( String name )
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	@Override
	public String toString()
	{
		return name;
	}
}

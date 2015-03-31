package mpa.core.logic.character;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import mpa.core.logic.AbstractObject;
import mpa.core.logic.Inventory;
import mpa.core.logic.Pair;

public abstract class AbstractCharacter extends AbstractObject
{
	private static final float PACE = 10;

	private ArrayList<Pair<Integer, Integer>> path;
	private Pair<Float, Float> currentVector = new Pair<Float, Float>( new Float( 0.0 ), new Float(
			0.0 ) );
	private int numberOfIterationsPerVector;

	protected String name;
	protected int health; // 0 - 100
	protected Inventory bag;
	double paceX;
	double paceY;

	protected ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	protected Lock readLock = lock.readLock();
	protected Lock writeLock = lock.writeLock();

	public AbstractCharacter( String name, float x, float y, int health, int bagDimension )
	{
		super( x, y, 0, 0 ); // TODO
		this.name = name;
		this.health = health;
		this.bag = new Inventory( bagDimension );
	}

	private double distance( Integer x1, Integer y1, Integer x2, Integer y2 )
	{
		return Math.sqrt( Math.pow( ( x2 - x1 ), 2 ) + Math.pow( ( y2 - y1 ), 2 ) );
	}

	private void computeCurrentVector()
	{
		currentVector = new Pair<Float, Float>( ( float ) ( -path.get( 0 ).getFirst() + path
				.get( 1 ).getFirst() ), ( float ) ( -path.get( 0 ).getSecond() + path.get( 1 )
				.getSecond() ) );

		numberOfIterationsPerVector = ( int ) ( distance( path.get( 0 ).getFirst(), path.get( 0 )
				.getSecond(), path.get( 1 ).getFirst(), path.get( 1 ).getSecond() ) / PACE );
		paceX = currentVector.getFirst() / numberOfIterationsPerVector;
		paceY = currentVector.getSecond() / numberOfIterationsPerVector;

		System.out.println( numberOfIterationsPerVector );
		System.out.println( "paceX = " + paceX );
		System.out.println( "paceY = " + paceY );

	}

	public void setPath( ArrayList<Pair<Integer, Integer>> path )
	{
		writeLock.lock();

		System.out.println( "path assegnato per la " + counter++ + " volta" );
		System.out.println( "size del path " + path.size() );
		this.path = path;
		computeCurrentVector();
		// computeNumberOfSteps();

		writeLock.unlock();
	}

	static int counter = 1;

	public boolean movePlayer()
	{
		try
		{
			writeLock.lock();
			if( path == null || path.isEmpty() )
				return false;

			numberOfIterationsPerVector--;
			System.out.println( "numero iterazioni " + numberOfIterationsPerVector );
			System.out.println( "x=" + x + "| y=" + y );

			if( numberOfIterationsPerVector <= 0 )
			{
				path.remove( 0 );
				System.err.println( " sono nell'if" );
				if( path.size() > 1 )
				{
					computeCurrentVector();
				}
				else
				{
					return false;
				}

				numberOfIterationsPerVector--;
			}
			setX( ( ( float ) ( x + paceX ) ) );
			setY( ( float ) ( y + paceY ) );

			System.out.println();
			System.out.println();
			System.out.println( "sto muovendo" );
			System.out.println();
			System.out.println();

			return true;

		} finally
		{
			writeLock.unlock();
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
}

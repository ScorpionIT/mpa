package mpa.core.logic.character;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import mpa.core.logic.AbstractObject;
import mpa.core.logic.Inventory;
import mpa.core.logic.Pair;
import mpa.core.maths.MyMath;

public abstract class AbstractCharacter extends AbstractObject
{
	private static final float PACE = 5;

	private ArrayList<Pair<Integer, Integer>> path;
	private Pair<Float, Float> currentVector = new Pair<Float, Float>(new Float(0.0), new Float(0.0));
	private int numberOfIterationsPerVector;

	protected String name;
	protected int health; // 0 - 100
	protected Inventory bag;
	double paceX;
	double paceY;

	protected ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	protected Lock readLock = lock.readLock();
	protected Lock writeLock = lock.writeLock();

	public AbstractCharacter(String name, float x, float y, int health, int bagDimension)
	{
		super(x, y, 0, 0); // TODO
		this.name = name;
		this.health = health;
		this.bag = new Inventory(bagDimension);
	}

	private void computeCurrentVector()
	{
		currentVector = new Pair<Float, Float>((float) (-path.get(0).getFirst() + path.get(1).getFirst()), (float) (-path.get(0).getSecond() + path
				.get(1).getSecond()));

		numberOfIterationsPerVector = (int) (MyMath.distanceInteger(path.get(0).getFirst(), path.get(0).getSecond(), path.get(1).getFirst(), path
				.get(1).getSecond()) / PACE);
		paceX = currentVector.getFirst() / numberOfIterationsPerVector;
		paceY = currentVector.getSecond() / numberOfIterationsPerVector;

		System.out.println(numberOfIterationsPerVector);
		System.out.println("paceX = " + paceX);
		System.out.println("paceY = " + paceY);

	}

	public void setPath(ArrayList<Pair<Integer, Integer>> path)
	{
		writeLock.lock();

		System.out.println("path assegnato per la " + counter++ + " volta");
		System.out.println("size del path " + path.size());
		this.path = path;

		System.out.println();
		System.out.println();
		System.out.println("Path : ");
		for (Pair<Integer, Integer> pair : path)
		{
			System.out.println(pair);

		}
		System.out.println();
		System.out.println();
		if (path.size() > 1)
		{
			computeCurrentVector();
		}
		else
		{
			Pair<Integer, Integer> point = new Pair<Integer, Integer>(path.get(0).getFirst(), path.get(0).getSecond());
			currentVector = new Pair<Float, Float>(new Float(point.getFirst() - x), new Float(point.getSecond() - y));

			numberOfIterationsPerVector = (int) (MyMath.distanceInteger((int) x, point.getFirst(), (int) y, point.getSecond()) / PACE);
		}
		writeLock.unlock();
	}

	static int counter = 1;

	public boolean movePlayer()
	{
		try
		{
			writeLock.lock();
			if (path == null || path.isEmpty())
				return false;

			numberOfIterationsPerVector--;
			System.out.println("numero iterazioni " + numberOfIterationsPerVector);
			System.out.println("x=" + x + "| y=" + y);

			if (numberOfIterationsPerVector <= 0)
			{
				path.remove(0);
				if (path.size() > 1)
				{
					computeCurrentVector();
				}
				else
				{
					return false;
				}

				numberOfIterationsPerVector--;
			}
			setX(((float) (x + paceX)));
			setY((float) (y + paceY));

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

	public ArrayList<Pair<Integer, Integer>> getPath()
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

	public String getName()
	{
		return name;
	}
}

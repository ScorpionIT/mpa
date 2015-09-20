package mpa.core.logic;

import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import javax.vecmath.Vector2f;

import mpa.core.logic.character.AbstractCharacter;

public class PathCalculatorThread extends Thread
{
	private AbstractCharacter player;
	private float xGoal;
	private float yGoal;
	List<Vector2f> path;
	ReentrantLock lock = new ReentrantLock();
	Condition cond = lock.newCondition();

	public PathCalculatorThread( AbstractCharacter player, float xGoal, float yGoal )
	{
		this.player = player;
		this.xGoal = xGoal;
		this.yGoal = yGoal;

	}

	@Override
	public void run()
	{
		path = null;
		lock.lock();

		if( player != null )
		{
			path = new PathCalculator( new Vector2f( player.getX(), player.getY() ),
					new Vector2f( xGoal, yGoal ), player.getCollisionRay() ).computePath();
			this.player.setPath( path );
		}
		cond.signal();
		lock.unlock();
	}

}

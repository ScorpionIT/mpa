package mpa.core.logic;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import mpa.core.logic.character.Player;

public class PathCalculatorThread extends Thread
{
	private Player player;
	private float xGoal;
	private float yGoal;
	ArrayList<Pair<Integer, Integer>> path;
	ReentrantLock lock = new ReentrantLock();
	Condition cond = lock.newCondition();

	public PathCalculatorThread( Player player, float xGoal, float yGoal )
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
		path = new ProvaIntegerPathCalculator().computePath( GameManager.getInstance().getWorld(),
				xGoal, yGoal, player.getX(), player.getY() );
		this.player.setPath( path );
		cond.signal();
		lock.unlock();
	}

	public ArrayList<Pair<Integer, Integer>> getPath()
	{
		try
		{
			lock.lock();
			while( path == null )
				try
				{
					cond.await();
				} catch( InterruptedException e )
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			return path;
		} finally
		{
			lock.unlock();
		}
	}
}

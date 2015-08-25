package mpa.core.logic;

import mpa.core.logic.character.Player;

public class FlashBangThread extends Thread
{
	private Player player;
	private final long STANDARD_TIME_TO_WAKE = 15000;
	private long timeToWake;
	private long startingTime;
	private long residualTime;

	public FlashBangThread( Player player, long timeToSleep )
	{
		super();
		if( timeToSleep != -1 )
			timeToWake = timeToSleep;
		else
			timeToWake = STANDARD_TIME_TO_WAKE - 100 * player.getPlayerLevel().ordinal();
		this.player = player;
	}

	public long getResidualTime()
	{
		return residualTime;
	}

	@Override
	public void run()
	{
		startingTime = System.currentTimeMillis();

		residualTime = System.currentTimeMillis() - startingTime;
		System.out.println( "TIME TO WAKE " + timeToWake );
		while( residualTime < timeToWake )
		{
			System.out.println( residualTime + " " + isInterrupted() );
			if( isInterrupted() )
			{
				return;
			}
			residualTime = System.currentTimeMillis() - startingTime;
		}

		player.getWriteLock();
		player.setFlashed( false );
		System.out.println( "stiamo camminando? " + player.isFlashed() );
		player.leaveWriteLock();
	}
}

package mpa.core.logic;

import java.util.ArrayList;
import java.util.HashMap;

import mpa.core.logic.character.Player;

class ThreadManager
{
	private static ThreadManager threadManager;
	private ArrayList<MyThread> gameThreads = new ArrayList<>();
	private boolean pause = false;
	private HashMap<Player, FlashBangThread> flashBangThreads = new HashMap<>();
	private HashMap<Player, Long> flashBangThreadResidualTimeToSleep = new HashMap<>();

	private ThreadManager()
	{
	}

	static ThreadManager getInstance()
	{
		if( threadManager == null )
			threadManager = new ThreadManager();

		return threadManager;
	}

	void pause( boolean pause )
	{
		this.pause = pause;
		System.out.println( "sono nel metodo " + pause );

		for( MyThread t : gameThreads )
			if( this.pause )
				t.setWorking( false );
			else
			{
				System.out.println( "ci entro?!?!" );
				t.setWorking( true );
				synchronized( t )
				{
					t.notify();
				}
			}

		if( this.pause )
		{
			for( Player p : flashBangThreads.keySet() )
			{

				if( flashBangThreads.get( p ).isAlive() )
				{

					flashBangThreads.get( p ).interrupt();
					flashBangThreadResidualTimeToSleep.put( p, flashBangThreads.get( p )
							.getResidualTime() );
				}
			}
		}
		else
		{
			for( Player p : flashBangThreadResidualTimeToSleep.keySet() )
			{
				startFlashBangThread( p );
				if( p.isFlashed() )
					System.out.println( "effettivamente non mi ha svegliato" );
				else
					System.out.println( "abbiamo fagliato" );
			}
		}
	}

	void addGameThread( MyThread t )
	{
		gameThreads.add( t );
	}

	void startGameThreads()
	{
		for( Thread t : gameThreads )
			t.start();
	}

	void startFlashBangThread( Player p )
	{
		long residualTime;
		Long t = flashBangThreadResidualTimeToSleep.get( p );
		if( t != null )
		{
			residualTime = t;
			flashBangThreadResidualTimeToSleep.remove( p );
		}
		else
			residualTime = -1;

		FlashBangThread flashBangThread = new FlashBangThread( p, residualTime );
		flashBangThreads.put( p, flashBangThread );
		flashBangThread.start();
	}

	boolean getPauseState()
	{
		return pause;
	}

	public void destroyAllThreads( GameManager gameManager )
	{
		// TODO Auto-generated method stub

	}

}

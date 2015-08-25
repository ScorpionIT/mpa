package mpa.core.logic;

import mpa.core.logic.character.Player;

public class FlashBangThread extends MyThread
{
	private Player player;
	private final long TIME_TO_WAKE = 3000;

	public FlashBangThread( Player player )
	{
		super();
		this.player = player;
	}

	@Override
	public synchronized void run()
	{
		while( true )
		{
			try
			{
				super.run();
				sleep( TIME_TO_WAKE - 100 * player.getPlayerLevel().ordinal() );

				player.getWriteLock();
				player.setFlashed();
				System.out.println( "stiamo camminando? " + player.isFlashed() );
				player.leaveWriteLock();
				while( !player.isFlashed() )
				{
					System.out.println( "merda" );
					wait();
				}
			} catch( InterruptedException e )
			{
				e.printStackTrace();
			}
		}
	}
}

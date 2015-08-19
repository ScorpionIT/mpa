package mpa.core.logic;

public class PositionUpdater extends MyThread
{
	private static final long INTERVAL = 100;

	public PositionUpdater()
	{
	}

	@Override
	public void run()
	{
		while( true )
		{
			super.run();
			try
			{
				sleep( INTERVAL );
				GameManager.getInstance().updateCharacterPositions();
			} catch( InterruptedException e )
			{
				e.printStackTrace();
			}
		}
	}

}

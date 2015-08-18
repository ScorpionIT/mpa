package mpa.core.logic;

public class PositionUpdater extends Thread
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

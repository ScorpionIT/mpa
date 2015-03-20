package mpa.core.logic;

public class PositionUpdater extends Thread
{
	private static final long INTERVAL = 500;

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
				// System.err.println( "ho updato le posizioni" );
			} catch( InterruptedException e )
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}

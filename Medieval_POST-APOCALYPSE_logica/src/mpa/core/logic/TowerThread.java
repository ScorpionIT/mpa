package mpa.core.logic;

public class TowerThread extends MyThread
{
	private static long TIME_TO_SLEEP = 1000;

	@Override
	public synchronized void run()
	{
		while( true )
		{
			super.run();
			try
			{
				sleep( TIME_TO_SLEEP );
				GameManager.getInstance().checkForTowerDamages();
			} catch( InterruptedException e )
			{
				e.printStackTrace();
			}
		}
	}
}

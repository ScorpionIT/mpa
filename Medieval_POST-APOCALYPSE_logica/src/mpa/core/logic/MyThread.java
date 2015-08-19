package mpa.core.logic;

public class MyThread extends Thread
{
	private boolean working = true;

	public synchronized void setWorking( boolean b )
	{
		working = b;
	}

	@Override
	public synchronized void run()
	{
		while( !working )
		{
			try
			{
				wait();
			} catch( InterruptedException e )
			{
				e.printStackTrace();
			}
		}
	}

}

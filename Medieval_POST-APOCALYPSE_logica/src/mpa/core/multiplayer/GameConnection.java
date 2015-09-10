package mpa.core.multiplayer;

import java.net.Socket;

public class GameConnection extends Socket implements Runnable
{
	private String player;
	private boolean isPlayerAlive = true;

	public GameConnection( String player )
	{
		this.player = player;
	}

	@Override
	public void run()
	{
		while( isPlayerAlive )
		{

		}
	}
}

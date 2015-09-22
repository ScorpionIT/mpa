package mpa.gui.multiplayer.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class WelcomeServer extends Thread
{
	private int portNumber = 5000;
	private Map<InetAddress, String> humanPlayers = new HashMap<InetAddress, String>();
	private Map<InetAddress, ServerSideConnection> connections = new HashMap<>();
	private ServerSocket welcomeSocket;
	private int numberOfPlayerToAccept;
	private String mapPath;

	public WelcomeServer( int numberOfPlayerToAccept, String mapPath )
	{
		this.mapPath = mapPath;
		this.numberOfPlayerToAccept = numberOfPlayerToAccept;
		boolean created = false;

		do
		{
			try
			{
				welcomeSocket = new ServerSocket( portNumber );
				created = true;
			} catch( IOException e )
			{
				portNumber++;
			}
		} while( !created );
	}

	@Override
	public void run()
	{
		while( connections.keySet().size() < numberOfPlayerToAccept )
		{
			try
			{
				Socket connectionSocket = welcomeSocket.accept();
				ServerSideConnection connection = new ServerSideConnection( connectionSocket,
						mapPath );
				connection.start();
				connections.put( connectionSocket.getInetAddress(), connection );
			} catch( IOException e )
			{
				// TODO
			}
		}
	}
}

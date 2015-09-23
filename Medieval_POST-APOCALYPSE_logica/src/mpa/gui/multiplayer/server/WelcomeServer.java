package mpa.gui.multiplayer.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import mpa.core.ai.DifficultyLevel;

public class WelcomeServer extends Thread
{
	private int portNumber = 5000;
	private Map<InetAddress, String> humanPlayers = new HashMap<InetAddress, String>();
	private Map<InetAddress, ServerSideConnection> connections = new HashMap<>();
	private ServerSocket welcomeSocket;
	private int numberOfPlayerToAccept;
	private String mapPath;
	private DifficultyLevel difficultyLevelSelected;
	private boolean alive = true;
	private UDPSpammer udpSpammer;

	public WelcomeServer( int numberOfPlayerToAccept, String mapPath, DifficultyLevel difficultyLevelSelected, UDPSpammer udpSpammer )
	{
		this.mapPath = mapPath;
		this.numberOfPlayerToAccept = numberOfPlayerToAccept;
		this.difficultyLevelSelected = difficultyLevelSelected;
		this.udpSpammer = udpSpammer;
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
		while(alive  && connections.keySet().size() < numberOfPlayerToAccept )
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
		udpSpammer.stopSpammer();
	}
	
	public String getIP ()
	{
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
		
	}
	
	public int getPort ()
	{
		return portNumber;
	}
	
	public void stopServer()
	{
		alive = false;
		
		Set<InetAddress> keySet = connections.keySet();
		for (InetAddress inetAddress : keySet) {
			connections.get(inetAddress).stopThread();
		}
		
	}
}

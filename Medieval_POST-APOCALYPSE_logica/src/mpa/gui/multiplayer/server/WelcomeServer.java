package mpa.gui.multiplayer.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mpa.core.ai.DifficultyLevel;
import mpa.core.logic.GameManagerProxy;

public class WelcomeServer extends Thread
{
	private int portNumber = 5000;
	private Map<InetAddress, String> humanPlayers = new HashMap<InetAddress, String>();
	private Map<InetAddress, List<String>> deadPlayers = new HashMap<>();
	private Map<InetAddress, List<String>> deadMinions = new HashMap<>();
	private Map<InetAddress, List<String>> deadTowerCrushers = new HashMap<>();
	private Map<InetAddress, List<String>> playerAttacks = new HashMap<>();
	private Map<InetAddress, List<String>> minionAttacks = new HashMap<>();
	private Map<InetAddress, List<String>> towerCrusherAttacks = new HashMap<>();
	private Map<InetAddress, ServerSideConnection> connections = new HashMap<>();
	private ServerSocket welcomeSocket;
	private int numberOfPlayerToAccept;
	private String mapPath;
	private DifficultyLevel difficultyLevelSelected;
	private boolean alive = true;
	private UDPSpammer udpSpammer;

	public WelcomeServer( int numberOfPlayerToAccept, String mapPath,
			DifficultyLevel difficultyLevelSelected, UDPSpammer udpSpammer )
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
		while( alive && connections.keySet().size() < numberOfPlayerToAccept )
		{
			try
			{
				Socket connectionSocket = welcomeSocket.accept();
				ServerSideConnection connection = new ServerSideConnection( connectionSocket,
						mapPath, this );
				connection.start();
				connections.put( connectionSocket.getInetAddress(), connection );
				deadPlayers.put( connectionSocket.getInetAddress(), new ArrayList<String>() );
				deadMinions.put( connectionSocket.getInetAddress(), new ArrayList<String>() );
				deadTowerCrushers.put( connectionSocket.getInetAddress(), new ArrayList<String>() );
			} catch( IOException e )
			{
				// TODO
			}
		}
		udpSpammer.stopSpammer();
	}

	public String getIP()
	{
		try
		{
			return InetAddress.getLocalHost().getHostAddress();
		} catch( UnknownHostException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";

	}

	public synchronized List<String> getDeads( InetAddress applicant, String type )
	{
		type = type.toLowerCase();

		List<String> deads = new ArrayList<String>();
		Map<InetAddress, List<String>> mapToModify;

		switch( type )
		{
			case "players":
				deads = GameManagerProxy.getInstance().takeDeadPlayers();
				mapToModify = playerAttacks;
				break;
			case "minions":
				deads = GameManagerProxy.getInstance().takeDeadMinions();
				mapToModify = minionAttacks;
				break;
			case "towercrushers":
				deads = GameManagerProxy.getInstance().takeDeadTowerCrushers();
				mapToModify = towerCrusherAttacks;
				break;
			default:
				mapToModify = new HashMap<InetAddress, List<String>>();
				break;
		}

		for( InetAddress address : mapToModify.keySet() )
		{
			mapToModify.get( address ).addAll( deads );
		}

		return mapToModify.remove( applicant );

	}

	public synchronized List<String> getAttacks( InetAddress applicant, String type )
	{
		type = type.toLowerCase();

		List<String> attacks = new ArrayList<String>();
		Map<InetAddress, List<String>> mapToModify;

		switch( type )
		{
			case "players":
				attacks = GameManagerProxy.getInstance().takePlayerAttacks();
				mapToModify = deadPlayers;
				break;
			case "minions":
				attacks = GameManagerProxy.getInstance().takeMinionAttacks();
				mapToModify = deadMinions;
				break;
			case "towercrushers":
				attacks = GameManagerProxy.getInstance().takeTowerCrusherAttacks();
				mapToModify = deadTowerCrushers;
				break;
			default:
				mapToModify = new HashMap<InetAddress, List<String>>();
				break;
		}

		for( InetAddress address : mapToModify.keySet() )
		{
			mapToModify.get( address ).addAll( attacks );
		}

		return mapToModify.remove( applicant );

	}

	public int getPort()
	{
		return portNumber;
	}

	public void stopServer()
	{
		alive = false;

		Set<InetAddress> keySet = connections.keySet();
		for( InetAddress inetAddress : keySet )
		{
			connections.get( inetAddress ).stopThread();
		}

	}
}

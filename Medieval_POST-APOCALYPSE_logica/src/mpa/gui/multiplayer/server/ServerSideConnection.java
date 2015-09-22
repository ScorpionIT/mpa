package mpa.gui.multiplayer.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerSideConnection extends Thread
{
	public enum ConnectionState
	{
		GETTING_MAP, GAME_SETTING, PLAYING
	}

	private ConnectionState state = ConnectionState.GETTING_MAP;

	private String mapPath;
	private boolean keepConnectionOn = true;
	private DataOutputStream outToClient;
	private BufferedReader inFromClient;

	public ServerSideConnection( Socket socket, String mapPath )
	{
		this.mapPath = mapPath;
		boolean isOk = true;

		do
		{
			try
			{
				outToClient = new DataOutputStream( socket.getOutputStream() );
				inFromClient = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
				isOk = true;
			} catch( IOException e )
			{
				isOk = false;
			}
		} while( !isOk );
	}

	@Override
	public void run()
	{
		try
		{
			while( keepConnectionOn )
			{
				switch( state )
				{
					case GETTING_MAP:
						if( inFromClient.readLine().equals( "GetMap" ) )
							givingTheMap();
					case GAME_SETTING:
					case PLAYING:
				}
			}
		} catch( IOException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void givingTheMap()
	{

		try
		{
			BufferedReader br = new BufferedReader( new FileReader( mapPath ) );
			String line = br.readLine();

			outToClient.writeBytes( "BEGIN" );
			while( line != null )
			{
				outToClient.writeBytes( line );
				line = br.readLine();
			}
			outToClient.writeBytes( "END" );
			br.close();

		} catch( FileNotFoundException e )
		{

		} catch( IOException e )
		{
			// TODO: handle exception
		}

		state = ConnectionState.GAME_SETTING;

	}

}

package mpa.gui.multiplayer.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import mpa.core.multiplayer.processingChain.BuyPotionHandler;
import mpa.core.multiplayer.processingChain.ChangeItemHandler;
import mpa.core.multiplayer.processingChain.ComputePathHandler;
import mpa.core.multiplayer.processingChain.CreateMinionsHandler;
import mpa.core.multiplayer.processingChain.CreateTower;
import mpa.core.multiplayer.processingChain.GetPickedObjectHandler;
import mpa.core.multiplayer.processingChain.GetPickedObjectInfo;
import mpa.core.multiplayer.processingChain.GetPotionAmountHandler;
import mpa.core.multiplayer.processingChain.GetResourcesAmountHandler;
import mpa.core.multiplayer.processingChain.OccupyPropertyHandler;
import mpa.core.multiplayer.processingChain.PlayerActionHandler;
import mpa.core.multiplayer.processingChain.PlayerInfoHandler;
import mpa.core.multiplayer.processingChain.PlayersInfosHandler;
import mpa.core.multiplayer.processingChain.ProcessingChain;

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
	private ProcessingChain headOfTheChain = null;

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
						if( inFromClient.readLine().equals( "Enter" ) )
							outToClient.writeBytes("OK"+'\n');
						if( inFromClient.readLine().equals( "GetMap" ) )
							givingTheMap();
					case GAME_SETTING:
					case PLAYING:
						String request = inFromClient.readLine();
						if( headOfTheChain == null )
							initChain();
						String[] processRequest = headOfTheChain.processRequest( request );
						for( String message : processRequest )
							outToClient.writeBytes( message + '\n' );

				}
			}
			outToClient.writeBytes("CLOSING");
		} catch( IOException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void initChain()
	{

		BuyPotionHandler buyPotionHandler = null;
		ChangeItemHandler changeItemHandler = new ChangeItemHandler( buyPotionHandler );
		ComputePathHandler computePathHandler = new ComputePathHandler( changeItemHandler );
		CreateMinionsHandler createMinionsHandler = new CreateMinionsHandler( computePathHandler );
		CreateTower createTower = new CreateTower( createMinionsHandler );
		GetPickedObjectHandler getPickedObjectHandler = new GetPickedObjectHandler( createTower );
		GetPickedObjectInfo getPickedObjectInfo = new GetPickedObjectInfo( getPickedObjectHandler );
		GetPotionAmountHandler getPotionAmountHandler = new GetPotionAmountHandler(
				getPickedObjectInfo );
		GetResourcesAmountHandler getResourcesAmountHandler = new GetResourcesAmountHandler(
				getPotionAmountHandler );
		OccupyPropertyHandler occupyPropertyHandler = new OccupyPropertyHandler(
				getResourcesAmountHandler );
		PlayerActionHandler playerActionHandler = new PlayerActionHandler( occupyPropertyHandler );
		PlayersInfosHandler playersInfosHandler = new PlayersInfosHandler( playerActionHandler );
		headOfTheChain = new PlayerInfoHandler( playersInfosHandler );

	}

	private void givingTheMap()
	{

		try
		{
			BufferedReader br = new BufferedReader( new FileReader( mapPath ) );
			String line = br.readLine();

			outToClient.writeBytes( "BEGIN"+ '\n' );
			while( line != null )
			{
				outToClient.writeBytes( line + '\n');
				line = br.readLine();
			}
			outToClient.writeBytes( "END"+ '\n' );
			br.close();

		} catch( FileNotFoundException e )
		{

		} catch( IOException e )
		{
			// TODO: handle exception
		}

		state = ConnectionState.GAME_SETTING;

	}
	
	public void stopThread ()
	{
		keepConnectionOn= false;
	}

}

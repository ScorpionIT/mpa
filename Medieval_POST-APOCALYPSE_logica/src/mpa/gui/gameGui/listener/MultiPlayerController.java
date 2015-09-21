package mpa.gui.gameGui.listener;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import mpa.gui.gameGui.playingGUI.GuiObjectManager;

import com.jme3.math.Vector2f;

public class MultiPlayerController extends HandlerImplementation
{
	private Socket socket;

	public MultiPlayerController( Socket socket )
	{
		this.socket = socket;
	}

	@Override
	public void setPause()
	{
		return;
	}

	@Override
	public Map<String, Map<String, Integer>> getPlayersResourceAmount()
	{
		Map<String, Map<String, Integer>> reply = new HashMap<String, Map<String, Integer>>();

		boolean isOk = true;
		do
		{
			try
			{
				DataOutputStream outToServer = new DataOutputStream( socket.getOutputStream() );
				BufferedReader inFromServer = new BufferedReader( new InputStreamReader(
						socket.getInputStream() ) );

				outToServer.writeBytes( "getPlayerResourcesAmount" + '\n' );

				int numberOfPlayer = Integer.parseInt( inFromServer.readLine() );

				Map<String, Integer> resources = null;
				String player = null;
				for( int index = 0; index <= numberOfPlayer; )
				{
					String response = inFromServer.readLine();
					String[] fields = response.split( "=" );

					if( fields[0].equals( "Player" ) )
					{
						if( index != 0 )
							reply.put( player, resources );

						resources = new HashMap<String, Integer>();
						index++;
						player = fields[1];
					}
					else
					{
						resources.put( fields[0], Integer.parseInt( fields[1] ) );
					}
				}

			} catch( IOException e )
			{
				isOk = false;
			}
		} while( !isOk );
		return reply;
	}

	@Override
	public String getPickedObject( Vector2f click )
	{

		boolean isOk = true;
		String reply = null;
		do
		{
			try
			{
				DataOutputStream outToServer = new DataOutputStream( socket.getOutputStream() );
				BufferedReader inFromServer = new BufferedReader( new InputStreamReader(
						socket.getInputStream() ) );

				outToServer.writeBytes( "PickedObject" + String.valueOf( click.x ) + ","
						+ String.valueOf( click.y ) + '\n' );
				reply = inFromServer.readLine();

			} catch( IOException e )
			{

			}
		} while( !isOk );

		return reply;
	}

	@Override
	public void changeItem( String item )
	{
		boolean isOk = true;
		do
		{
			try
			{
				DataOutputStream outToServer = new DataOutputStream( socket.getOutputStream() );
				outToServer.writeBytes( "ChangeItem:"
						+ GuiObjectManager.getInstance().getPlayingPlayer() + ":" + item + '\n' );
			} catch( IOException e )
			{
				isOk = false;
			}

		} while( !isOk );
	}

	@Override
	public void playerAction( Vector2f direction )
	{
		boolean isOk = true;
		do
		{
			try
			{
				DataOutputStream outToServer = new DataOutputStream( socket.getOutputStream() );
				outToServer.writeBytes( "Do:" + GuiObjectManager.getInstance().getPlayingPlayer()
						+ String.valueOf( direction.x ) + "," + String.valueOf( direction.y )
						+ '\n' );
			} catch( IOException e )
			{
				isOk = false;
			}

		} while( !isOk );
	}

	@Override
	public boolean occupyProperty( String property )
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createTower( String property )
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createMinions( String boss, String target, int quantity )
	{
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void updateInformation()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void createStateInformation()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void computePath( Vector2f click )
	{
		// TODO Auto-generated method stub

	}

	@Override
	public String getPickedObjectOwner( String objectType, String objectID )
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPickedObjectProductivity( String objectType, String objectID )
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public HashMap<String, Integer> getPlayerResourcesAmount( String playerName )
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNumberOfPlayer()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void computePath( Vector2f click, String playerName )
	{
		// TODO Stub di metodo generato automaticamente

	}

	@Override
	public String getPlayerLevel( String player )
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPlayerHP( String player )
	{
		// TODO Stub di metodo generato automaticamente
		return 0;
	}

	@Override
	public int getPlayerMP( String player )
	{
		// TODO Stub di metodo generato automaticamente
		return 0;
	}

	@Override
	public Set<String> getPlayersName()
	{
		// TODO Stub di metodo generato automaticamente
		return null;
	}

	@Override
	public boolean createTowerCrusher( String boss, String target )
	{
		// TODO Stub di metodo generato automaticamente
		return true;
	}

	@Override
	public boolean buyHPPotion( String playerName )
	{
		// TODO Stub di metodo generato automaticamente
		return false;
	}

	@Override
	public boolean buyMPPotion( String playerName )
	{
		// TODO Stub di metodo generato automaticamente
		return false;
	}

	@Override
	public boolean buyGranade( String playerName )
	{
		// TODO Stub di metodo generato automaticamente
		return false;
	}

	@Override
	public int getPlayerHPPotion( String playerName )
	{
		// TODO Stub di metodo generato automaticamente
		return 0;
	}

	@Override
	public int getPlayerMPPotion( String playerName )
	{
		// TODO Stub di metodo generato automaticamente
		return 0;
	}

	@Override
	public int getPlayerGranade( String playerName )
	{
		// TODO Stub di metodo generato automaticamente
		return 0;
	}

	@Override
	public String getMinionBoss( String ID )
	{
		// TODO Stub di metodo generato automaticamente
		return null;
	}

	@Override
	public void endGame()
	{
		// TODO Auto-generated method stub

	}

}

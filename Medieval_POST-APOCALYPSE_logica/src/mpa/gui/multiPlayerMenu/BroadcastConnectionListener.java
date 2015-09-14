package mpa.gui.multiPlayerMenu;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class BroadcastConnectionListener extends Thread
{
	private ServerSelectionPanel serverSelectionPanel;
	private boolean listen = true;

	public BroadcastConnectionListener( ServerSelectionPanel serverSelectionPanel )
	{

	}

	@Override
	public void run()
	{
		MulticastSocket socket = null;
		InetAddress group;
		try
		{
			socket = new MulticastSocket( 4446 );
			group = InetAddress.getByName( "203.0.113.0" );
			socket.joinGroup( group );
			DatagramPacket packet;
			while( listen )
			{
				sleep( 2000 );
				byte[] buf = new byte[256];
				packet = new DatagramPacket( buf, buf.length );
				socket.receive( packet );

				String newServer = new String( packet.getData() );
				serverSelectionPanel.addServer( newServer );
			}
			socket.leaveGroup( group );
			socket.close();
		} catch( IOException e )
		{
			e.printStackTrace();
		} catch( InterruptedException e )
		{
			e.printStackTrace();
		}

	}

	public void setListenMode( boolean listen )
	{
		this.listen = listen;
	}
}

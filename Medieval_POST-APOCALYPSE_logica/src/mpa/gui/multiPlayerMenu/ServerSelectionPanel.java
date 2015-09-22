package mpa.gui.multiPlayerMenu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import mpa.core.logic.MapInfo;
import mpa.core.util.GameProperties;
import mpa.core.util.MapFromXMLCreator;

import org.jdom2.JDOMException;

public class ServerSelectionPanel extends JPanel
{
	private static final long serialVersionUID = -8958460289373316021L;
	private Image backgroundImage;
	private int numberOfServers = 20;
	private String[] servers = new String[numberOfServers];
	private List<JLabel> serverList = new ArrayList<>();
	private JPanel selectionPanel = new JPanel();
	private JLabel selectedServer;
	private int labelHeight;
	private JLabel back = new JLabel( "BACK" );
	private JLabel enter = new JLabel( "ENTER" );
	private JFrame mainFrame;

	public ServerSelectionPanel( int x, int y, int width, int height, final JFrame mainFrame )
	{
		this.mainFrame = mainFrame;
		setLayout( null );
		setBounds( x, y, width, height );

		selectionPanel.setBounds( width * 40 / 100, height * 15 / 100, width * 20 / 100,
				height * 70 / 100 );
		selectionPanel.setOpaque( false );
		selectionPanel.setVisible( true );
		selectionPanel.setLayout( null );
		selectionPanel.setBorder( BorderFactory.createLineBorder( Color.black, 10 ) );

		labelHeight = ( 70 * height / 100 ) / numberOfServers;

		for( int i = 0; i < numberOfServers; i++ )
		{
			JLabel server = new JLabel( "ciao compa" );
			server.addMouseListener( new MouseAdapter()
			{
				@Override
				public void mouseReleased( MouseEvent e )
				{
					List<JLabel> keys = ServerSelectionPanel.this.serverList;
					for( JLabel l : keys )
					{
						if( ( ( JLabel ) e.getSource() ).equals( l ) )
						{
							if( selectedServer != null )
								selectedServer.setBorder( null );
							selectedServer = ( ( JLabel ) e.getSource() );
							selectedServer.setBorder( BorderFactory.createLineBorder( Color.RED, 1 ) );
							break;
						}

					}
				}

			} );
			serverList.add( server );
		}
		int index = 0;
		for( JLabel server : serverList )
		{
			server.setBounds( 0, labelHeight * index++, selectionPanel.getWidth(), labelHeight );
			selectionPanel.add( server );
		}

		try
		{
			backgroundImage = ImageIO.read( new File( "Assets/BackgroundImages/mainMenu.jpg" ) );
		} catch( IOException e1 )
		{
			e1.printStackTrace();
		}

		enter.setBounds( 93 * width / 100, 92 * height / 100, 5 * width / 100, 5 * height / 100 );
		back.setBounds( 2 * width / 100, 92 * height / 100, 5 * width / 100, 5 * height / 100 );

		enter.addMouseListener( new MouseAdapter()
		{
			public void mouseReleased( MouseEvent e )
			{
				if( selectedServer == null )
					return;

				String[] addressAndPort = selectedServer.getText().split( ":" );
				getMap( addressAndPort );

			};
		} );

		add( selectionPanel );
		add( enter );
		add( back );
		setVisible( true );

	}

	public void getMap( String[] addressAndPort )
	{
		if( addressAndPort.length != 2 )
			return;

		try
		{
			Socket socket;
			socket = new Socket( addressAndPort[0], Integer.parseInt( addressAndPort[1] ) );
			DataOutputStream outToServer = new DataOutputStream( socket.getOutputStream() );
			BufferedReader inFromServer = new BufferedReader( new InputStreamReader(
					socket.getInputStream() ) );

			outToServer.writeBytes( "Enter" + '\n' );
			String reply = inFromServer.readLine();

			if( reply.equals( "OK" ) )
			{
				outToServer.writeBytes( "GetMap" + '\n' );
				reply = inFromServer.readLine();

				Writer writer = new BufferedWriter( new OutputStreamWriter( new FileOutputStream(
						"map.xml" ), "utf-8" ) );

				while( !reply.equals( "END" ) )
				{
					if( !reply.equals( "BEGIN" ) )
					{
						writer.write( reply + '\n' );
					}
					reply = inFromServer.readLine();
				}

				writer.close();
				MapFromXMLCreator creator = new MapFromXMLCreator();
				MapInfo mapInfo = creator.createMapInfo( GameProperties.getInstance().getPath(
						"MultiplayerMapPath" + "/map.xml" ) );
				mainFrame.setContentPane( new MultiplayerMenuPanel( getX(), getY(), getWidth(),
						getHeight(), socket, mapInfo, this ) );

			}
		} catch( IOException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch( JDOMException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void addServer( String newServer )
	{
		for( int i = 0; i < servers.length; i++ )
		{
			if( servers[i].equals( newServer ) )
				return;
		}
		for( int i = 1; i < servers.length; i++ )
		{
			servers[i - 1] = servers[i];
		}

		servers[servers.length - 1] = newServer;

		int index = 0;
		for( JLabel server : serverList )
			server.setText( servers[index++] );

	}

	@Override
	protected void paintComponent( Graphics g )
	{
		super.paintComponent( g );
		g.drawImage( backgroundImage, getX(), getY(), getWidth(), getHeight(), this );
	}

	public static void main( String[] args )
	{
		int screenWidth = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
		int screenHeight = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;

		JFrame frame = new JFrame();
		frame.setLocation( 0, 0 );

		frame.setSize( screenWidth, screenHeight );

		ServerSelectionPanel mp = new ServerSelectionPanel( 0, 0, screenWidth, screenHeight, frame );
		frame.add( mp );
		frame.setVisible( true );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}
}

package mpa.gui.multiplayer;

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
import mpa.gui.multiplayer.client.MultiplayerMenuPanel;

import org.jdom2.JDOMException;

public class ServerSelectionPanel extends JPanel
{
	private static final long serialVersionUID = -8958460289373316021L;
	private Image backgroundImage;
	private int numberOfServers = 10;
	private String[] servers = new String[numberOfServers];
	private List<JLabel> serverList = new ArrayList<>();
	private JPanel selectionPanel = new JPanel();
	private JLabel selectedServer;
	private int labelHeight;
	private JLabel back = new JLabel( "BACK" );
	private JLabel enter = new JLabel( "ENTER" );
	private JFrame mainFrame;
	private Image panelBackgroudImage;
	private int xPanel;
	private int yPanel;
	private int widthPanel;
	private int heightPanel;
	private JPanel mainMenuPanel;

	public ServerSelectionPanel( final JFrame mainFrame, JPanel mainMenuPanel )
	{
		this.mainFrame = mainFrame;
		this.mainMenuPanel = mainMenuPanel;
		setLayout( null );
		setBounds( mainFrame.getX(), mainFrame.getY(), mainFrame.getWidth(), mainFrame.getHeight() );

		xPanel = this.getWidth() * 20 / 100;
		yPanel = this.getHeight() * 20 / 100;
		widthPanel = this.getWidth() * 60 / 100;
		heightPanel = this.getHeight() * 60 / 100;

		addSelectionPanel();

		try
		{
			backgroundImage = ImageIO.read( new File( GameProperties.getInstance().getPath(
					"BackgroundImagesPath" )
					+ "/background1.jpg" ) );
			panelBackgroudImage = ImageIO.read( new File( GameProperties.getInstance().getPath(
					"BackgroundImagesPath" )
					+ "/backgroundHeadquarterPanel.png" ) );
		} catch( IOException e1 )
		{
			e1.printStackTrace();
		}

		addButtons();

		add( selectionPanel );
		add( enter );
		add( back );
		setVisible( true );

	}

	private void addButtons()
	{
		// enter.setBounds(93 * width / 100, 92 * height / 100, 5 * width / 100, 5 * height / 100);
		// back.setBounds(2 * width / 100, 92 * height / 100, 5 * width / 100, 5 * height / 100);

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
	}

	private void addSelectionPanel()
	{
		selectionPanel.setBounds( xPanel + xPanel * 70 / 100, yPanel + yPanel * 40 / 100,
				widthPanel * 50 / 100, heightPanel * 70 / 100 );
		selectionPanel.setOpaque( false );
		selectionPanel.setVisible( true );
		selectionPanel.setLayout( null );
		selectionPanel.setBorder( BorderFactory.createLineBorder( Color.black, 10 ) );

		labelHeight = selectionPanel.getHeight() / ( numberOfServers + 1 );
		int yLabel = labelHeight / 2;

		for( int i = 0; i < numberOfServers; i++ )
		{
			JLabel server = new JLabel(
					"<html> <font color='white'face='URW Chancery L' size='20'>Text color:red</font></html>",
					JLabel.CENTER );
			server.setBounds( 0, yLabel, selectionPanel.getWidth(), labelHeight );

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

			System.out.println( server.getText() );
			yLabel += labelHeight;
			selectionPanel.add( server );
		}

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
		System.out.println( "SONO QUA" );

		g.drawImage( backgroundImage, getX(), getY(), getWidth(), getHeight(), this );
		g.drawImage( panelBackgroudImage, xPanel, yPanel, widthPanel, heightPanel, this );
	}

	// public static void main( String[] args )
	// {
	// int screenWidth = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
	// int screenHeight = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
	//
	// JFrame frame = new JFrame();
	// frame.setLocation( 0, 0 );
	//
	// frame.setSize( screenWidth, screenHeight );
	//
	// ServerSelectionPanel mp = new ServerSelectionPanel( 0, 0, screenWidth, screenHeight, frame );
	// frame.add( mp );
	// frame.setVisible( true );
	// frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	// }
}

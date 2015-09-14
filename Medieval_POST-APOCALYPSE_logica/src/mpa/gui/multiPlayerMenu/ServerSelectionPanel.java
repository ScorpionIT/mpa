package mpa.gui.multiPlayerMenu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ServerSelectionPanel extends JPanel
{
	private Image backgroundImage;
	private int numberOfServers = 20;
	private String[] servers = new String[numberOfServers];
	private ArrayList<JLabel> serverList = new ArrayList<>();
	private JPanel selectionPanel = new JPanel();
	private String selectedServer;
	private int labelHeight;
	private JLabel back = new JLabel( "BACK" );
	private JLabel play = new JLabel( "PLAY" );

	public ServerSelectionPanel( int x, int y, int width, int height )
	{
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
			serverList.add( new JLabel( "ciao compa" ) );
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

		play.setBounds( 93 * width / 100, 92 * height / 100, 5 * width / 100, 5 * height / 100 );
		back.setBounds( 2 * width / 100, 92 * height / 100, 5 * width / 100, 5 * height / 100 );
		add( selectionPanel );
		add( play );
		add( back );
		setVisible( true );

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

		ServerSelectionPanel mp = new ServerSelectionPanel( 0, 0, screenWidth, screenHeight );
		frame.add( mp );
		frame.setVisible( true );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}
}

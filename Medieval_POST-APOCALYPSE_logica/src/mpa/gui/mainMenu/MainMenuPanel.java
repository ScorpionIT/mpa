package mpa.gui.mainMenu;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainMenuPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private JLabel title;

	private Image backgroundImage;
	private JLabel singlePlayer;
	private JLabel multiPlayer;
	private JLabel createServer;
	private JLabel mapEditor;
	private JLabel options;
	private JLabel quit;

	public MainMenuPanel( int x, int y, int width, int height )
	{
		setBounds( x, y, width, height );
		setLayout( null );
		try
		{
			backgroundImage = ImageIO.read( new File( "Assets/BackgroundImages/mainMenu.jpg" ) );
		} catch( IOException e1 )
		{
			e1.printStackTrace();
		}

		title = new JLabel( "CI SARÀ UN'IMMAGINE" );
		title.setBounds( 25 * width / 100, 0, 50 * width / 100, 10 * height / 100 );
		title.setVisible( true );
		add( title );

		singlePlayer = new JLabel( "Single Player" );
		singlePlayer.setBounds( 5, 15 * height / 100, 25 * width / 100, 10 * height / 100 );
		singlePlayer.setVisible( true );
		add( singlePlayer );

		multiPlayer = new JLabel( "Multi Player" );
		multiPlayer.setBounds( 5, 30 * height / 100, 25 * width / 100, 10 * height / 100 );
		multiPlayer.setVisible( true );
		add( multiPlayer );

		createServer = new JLabel( "Create Server" );
		createServer.setBounds( 5, 45 * height / 100, 25 * width / 100, 10 * height / 100 );
		createServer.setVisible( true );
		add( createServer );

		mapEditor = new JLabel( "Map Editor" );
		mapEditor.setBounds( 5, 60 * height / 100, 25 * width / 100, 10 * height / 100 );
		mapEditor.setVisible( true );
		add( mapEditor );

		options = new JLabel( "Options" );
		options.setBounds( 5, 75 * height / 100, 25 * width / 100, 10 * height / 100 );
		options.setVisible( true );
		add( options );

		quit = new JLabel( "Quit" );
		quit.setBounds( 5, 90 * height / 100, 25 * width / 100, 10 * height / 100 );
		quit.setVisible( true );
		add( quit );

		setVisible( true );
	}

	@Override
	protected void paintComponent( Graphics g )
	{
		super.paintComponent( g );
		g.drawImage( backgroundImage, 0, 0, getWidth(), getHeight(), this );
	}

	public static void main( String[] args )
	{
		int screenWidth = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
		int screenHeight = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;

		JFrame frame = new JFrame();
		frame.setLocation( 0, 0 );

		frame.setSize( screenWidth, screenHeight );

		MainMenuPanel mp = new MainMenuPanel( 0, 0, screenWidth, screenHeight );
		frame.add( mp );
		frame.setVisible( true );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}
}

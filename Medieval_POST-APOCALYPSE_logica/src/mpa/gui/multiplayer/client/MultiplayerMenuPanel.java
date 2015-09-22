package mpa.gui.multiplayer.client;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import mpa.core.ai.DifficultyLevel;
import mpa.core.logic.MapInfo;
import mpa.core.logic.Pair;
import mpa.core.logic.WorldLoader;
import mpa.core.util.GameProperties;
import mpa.core.util.MapFromXMLCreator;
import mpa.gui.menuMap.InputNamePanel;

import org.jdom2.JDOMException;

import com.jme3.system.AppSettings;

public class MultiplayerMenuPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	private MapInfo mapInfo;
	private MultiplayerMapPreview mapPreview;
	private InputNamePanel inputNamePanel;
	private WorldLoader worldLoader = new WorldLoader();
	private Pair<Float, Float> selectedHQ = null;
	private String playerName = null;
	private JLabel buttonReady;
	private Image backgroundImage;
	private int selectedHQIndex = 0;
	int screenWidth;
	int screenHeight;
	DifficultyLevel difficultyLevelSelected = null;
	private String textImagePath = GameProperties.getInstance().getPath( "TextImagePath" );
	private Socket socket;

	public MultiplayerMenuPanel( int x, int y, int width, int height, Socket socket,
			MapInfo mapInfo, JPanel ancestor )
	{

		this.socket = socket;
		this.mapInfo = mapInfo;
		this.setSize( width, height );
		this.setLocation( x, y );
		this.setLayout( null );
		screenWidth = this.getWidth();
		screenHeight = this.getHeight();

		addMapPreviewPanel();

		addInputNamePanel();

		Image imageReady = null;
		try
		{
			imageReady = ImageIO.read( new File( textImagePath + "/Play.png" ) );
			imageReady = imageReady.getScaledInstance( screenWidth * 20 / this.getWidth() * 10, 40,
					Image.SCALE_FAST );
			buttonReady = new JLabel( new ImageIcon( imageReady ) );
		} catch( IOException e2 )
		{
			// TODO Blocco catch generato automaticamente
			e2.printStackTrace();
		}
		buttonReady.setBounds( this.getWidth() - imageReady.getWidth( this ),
				52 + screenHeight * 80 / 100, imageReady.getWidth( this ),
				imageReady.getHeight( this ) );
		buttonReady.addMouseListener( new MouseAdapter()
		{
			@Override
			public void mouseReleased( MouseEvent e )
			{
				playerName = inputNamePanel.getPlayerName();
				if( playerName == null || playerName.equals( "" ) )
				{
					setButtonsStyle();
					JOptionPane.showMessageDialog( new Frame(), "Inserisci il nome che preferisci",
							"Message", JOptionPane.PLAIN_MESSAGE );
				}
				else if( selectedHQ == null )
					JOptionPane.showMessageDialog( new Frame(), "Seleziona il quartier generale",
							"Message", JOptionPane.PLAIN_MESSAGE );
				else
				{
					startGame();
				}

			}
		} );

		this.add( buttonReady );
		try
		{
			backgroundImage = ImageIO.read( new File( "Assets/BackgroundImages/mainMenu.jpg" ) );
		} catch( IOException e1 )
		{
			e1.printStackTrace();
		}
		this.setVisible( true );
	}

	private void setButtonsStyle()
	{

		Font font = null;
		try
		{
			font = Font
					.createFont( Font.PLAIN, new FileInputStream( "./Assets/Fonts/KELMSCOT.TTF" ) );
		} catch( FontFormatException | IOException e )
		{
			// TODO Blocco catch generato automaticamente
			e.printStackTrace();
		}
		font = font.deriveFont( Font.PLAIN, 15 );
		UIManager.put( "Label.font", font );
		UIManager.put( "Button.font", new Font( "URW Chancery L", Font.BOLD, 13 ) );
	}

	private void startGame()
	{

		try
		{
			BufferedReader inFromServer = new BufferedReader( new InputStreamReader(
					socket.getInputStream() ) );
			if( !inFromServer.readLine().equals( "LETSGO" ) )
				return;

		} catch( IOException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Thread t = new Thread( new Runnable()
		{
			@Override
			public void run()
			{
				mpa.gui.gameGui.playingGUI.GameGui app = new mpa.gui.gameGui.playingGUI.GameGui(
						playerName );
				AppSettings gameSettings = new AppSettings( false );
				gameSettings.setResolution(
						java.awt.Toolkit.getDefaultToolkit().getScreenSize().width,
						java.awt.Toolkit.getDefaultToolkit().getScreenSize().height );
				gameSettings.setFullscreen( true );
				gameSettings.setVSync( false );
				gameSettings.setTitle( "Stellar Conquest" );
				gameSettings.setUseInput( true );
				gameSettings.setFrameRate( 500 );
				gameSettings.setSamples( 0 );
				gameSettings.setRenderer( "LWJGL-OpenGL2" );
				app.setSettings( gameSettings );
				app.setShowSettings( false );

				// disable statistics
				app.setDisplayFps( false );
				app.setDisplayStatView( false );
				app.start();
			}
		} );
		t.start();
	}

	public void setMap( String mapName )
	{
		mapPreview.removeAll();
		try
		{
			worldLoader.loadMapInfo( new MapFromXMLCreator(), mapName );
		} catch( JDOMException | IOException e )
		{
			JOptionPane.showMessageDialog( this,
					"Sorry but this map seems to be lost or damaged :(", "MAP ERROR",
					JOptionPane.ERROR_MESSAGE );

			// TODO rimuovere la mappa danneggiata (o persa) dalla lista!!!!!
		}
		mapInfo = worldLoader.getMapInfo();
		mapPreview.setMapDimension( mapInfo.getWidth(), mapInfo.getHeight() );
		mapPreview.loadMap( mapInfo );
		selectedHQ = null;
		this.repaint();
	}

	public void setSelectedHeadQuarter( Pair<Float, Float> headQuarterPosition )
	{
		String reply = new String();
		try
		{
			DataOutputStream outToServer = new DataOutputStream( socket.getOutputStream() );
			BufferedReader inFromServer = new BufferedReader( new InputStreamReader(
					socket.getInputStream() ) );
			outToServer.writeBytes( new String( "GET"
					+ String.valueOf( headQuarterPosition.getFirst() ) + ","
					+ String.valueOf( headQuarterPosition.getSecond() + '\n' ) ) );
			reply = inFromServer.readLine();
		} catch( IOException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if( reply.equals( "OK" ) )
		{
			mapPreview.removeBorder( selectedHQ );
			selectedHQ = headQuarterPosition;
			mapPreview.addBorder( selectedHQ );
		}
		else
		{
			JOptionPane.showMessageDialog( this, "HQ already taken", ":( ",
					JOptionPane.INFORMATION_MESSAGE );
		}
	}

	public Pair<Float, Float> getSelectedHQ()
	{
		return selectedHQ;
	}

	public void setPlayerName( String playerName )
	{
		this.playerName = playerName;
	}

	public void setDifficultyLevel( DifficultyLevel difficultyLevel )
	{
		this.difficultyLevelSelected = difficultyLevel;
	}

	private void addMapPreviewPanel()
	{
		mapPreview = new MultiplayerMapPreview( this, mapInfo );
		mapPreview.setBounds( screenWidth * 55 / 100, 30, screenWidth * 45 / 100 - 30,
				screenHeight * 55 / 100 );
		mapPreview.repaint();
		this.add( mapPreview );
	}

	private void addInputNamePanel()
	{
		inputNamePanel = new InputNamePanel();
		inputNamePanel.setBounds( 40, 45, screenWidth * 50 / 100, screenHeight * 40 / 100 );
		inputNamePanel.setOpaque( false );
		this.add( inputNamePanel );
	}

	@Override
	protected void paintComponent( Graphics g )
	{
		super.paintComponent( g );
		g.drawImage( backgroundImage, 0, 0, screenWidth, screenHeight, this );
	}

	public static void main( String[] args )
	{

		int screenWidth = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
		int screenHeight = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;

		JFrame frame = new JFrame();
		frame.setLocation( 0, 0 );

		frame.setSize( screenWidth, screenHeight );
		MultiplayerMenuPanel mainMenuGamePanel = new MultiplayerMenuPanel( screenWidth * 15 / 100,
				screenHeight * 10 / 100, screenWidth * 70 / 100, screenHeight * 80 / 100, null,
				null, null );

		frame.setContentPane( new Panel() );
		frame.getContentPane().setBackground( Color.BLACK );
		frame.getContentPane().add( mainMenuGamePanel );
		frame.getContentPane().setLayout( null );
		frame.getContentPane().setVisible( true );;
		frame.setVisible( true );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}
}

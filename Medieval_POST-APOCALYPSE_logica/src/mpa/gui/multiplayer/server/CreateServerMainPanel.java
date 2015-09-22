package mpa.gui.multiplayer.server;

import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.io.IOException;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import mpa.core.logic.MapInfo;
import mpa.core.logic.WorldLoader;
import mpa.core.util.MapFromXMLCreator;
import mpa.gui.menuMap.MapListPanel;
import mpa.gui.menuMap.MapPreview;

import org.jdom2.JDOMException;

public class CreateServerMainPanel extends JPanel
{
	private JLabel backButton = new JLabel();
	private JLabel launchButton = new JLabel();
	private MapPreview mapPreview;
	private MapListPanel mapListPanel;
	private JComboBox<String> numberOfBotsBox;
	private int numberOfBots = 0;
	private Image backgroundImage;
	private JFrame mainFrame;
	private WorldLoader worldLoader = new WorldLoader();
	private MapInfo mapInfo = new MapInfo();
	private WelcomeServer welcomeServer;
	private String map;

	public CreateServerMainPanel( int x, int y, int width, int height, final JFrame mainFrame )
	{
		this.mainFrame = mainFrame;
		mapPreview = new MapPreview( this );
		mapPreview.setBounds( 25 * width / 100, 10 * height / 100, 50 * width / 100,
				50 * height / 100 );
		mapListPanel = new MapListPanel( this );
		mapListPanel.setBounds( 25 * width / 100, 65 * height / 100, 25 * width / 100,
				30 * height / 100 );

		numberOfBotsBox = new JComboBox<>();
		launchButton.addMouseListener( new MouseAdapter()
		{
			public void mouseReleased( java.awt.event.MouseEvent e )
			{
				welcomeServer = new WelcomeServer( mapInfo.getNumberOfPlayers() - numberOfBots, map );
				welcomeServer.start();
			};
		} );

	}

	public void setMap( String mapName )
	{
		map = mapName;
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
		( ( MapPreview ) mapPreview ).setMapDimension( mapInfo.getWidth(), mapInfo.getHeight() );
		( ( MapPreview ) mapPreview ).loadMap( mapInfo );
		this.repaint();

		numberOfBotsBox = new JComboBox<>();
		for( int i = 0; i <= mapInfo.getNumberOfPlayers() - 2; i++ )
		{
			numberOfBotsBox.add( new JLabel( String.valueOf( i ) ) );
		}

	}

}

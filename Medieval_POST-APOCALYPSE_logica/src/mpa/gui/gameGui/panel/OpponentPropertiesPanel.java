//package mpa.gui.gameGui.panel;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
//import mpa.core.logic.GameManager;
//import mpa.core.logic.character.Player;
//import mpa.gui.gameGui.playingGUI.GameGui;
//import de.lessvoid.nifty.Nifty;
//import de.lessvoid.nifty.builder.ImageBuilder;
//import de.lessvoid.nifty.builder.PanelBuilder;
//import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
//import de.lessvoid.nifty.elements.Element;
//import de.lessvoid.nifty.screen.Screen;
//
//public class OpponentPropertiesPanel
//{
//
//	PanelBuilder mainPanelBuilder;
//
//	ArrayList<PanelBuilder> resourcesPanels;
//
//	int x = 10;
//
//	int heightPanel = 30;
//
//	private GameGui gameGui;
//	private int currentPage = 1;
//	int numberOfPlayerForPage;
//	int numberOfPages;
//	ButtonBuilder buttonForwardBuilder = null;
//	ButtonBuilder buttonBackBuilder = null;
//
//	private LinkedHashMap<String, OpponentResourcesPanel> playersResources = new LinkedHashMap<>();
//
//	public OpponentPropertiesPanel( GameGui gameGui )
//	{
//
//		this.gameGui = gameGui;
//		List<Player> players = GameManager.getInstance().getPlayers();
//
//		numberOfPlayerForPage = 100 / ( heightPanel );
//		numberOfPages = ( players.size() - 1 ) / numberOfPlayerForPage;
//		numberOfPages++;
//		System.out.println( "NUMERO DI PAGINE " + numberOfPages + " E NUMERO DI PLAYER E' "
//				+ ( players.size() - 1 ) );
//
//		initButtons();
//
//		mainPanelBuilder = new PanelBuilder( "#opponentPropertiesPanelId" )
//		{
//			{
//				image( getImageBuilder( "selectedPanel.png" ) );
//
//				childLayoutAbsoluteInside();
//				control( buttonForwardBuilder );
//				control( buttonBackBuilder );
//
//			}
//		};
//
//		// TODO ripartire dalla prima prima pagina
//
//		// TODO salta il player attuale
//
//		// TODO alzare il bottone
//		// mainPanelBuilder.set("horizontal", "false");
//		// mainPanelBuilder.set("autoScroll", "bottom");
//		mainPanelBuilder.set( "width", "60%" );
//		mainPanelBuilder.set( "height", "60%" );
//		mainPanelBuilder.set( "x", "20%" );
//		mainPanelBuilder.set( "y", "20%" );
//
//		inizializePanels( players );
//
//		// TODO NON CREA TUTTI I PANNELLI
//		System.out.println( "NUMERO DI PANNELLI " + playersResources.size() );
//
//	}
//
//	private void initButtons()
//	{
//		buttonForwardBuilder = getButtonBuilder( "avanti", 55, 95, 10, 5 );
//
//		buttonForwardBuilder.interactOnClick( "onClickButtonForward()" );
//
//		buttonBackBuilder = getButtonBuilder( "indietro", 40, 95, 10, 5 );
//		buttonBackBuilder.interactOnClick( "onClickButtonBack()" );
//
//		buttonBackBuilder.visible( false );
//		if( numberOfPages == 1 )
//		{
//			buttonForwardBuilder.visible( false );
//		}
//	}
//
//	private void inizializePanels( List<Player> players )
//	{
//
//		int y = 3;
//
//		int index = 0;
//		for( int i = 0; i < players.size() /* && i != this.gameGui.getPlayerIndex() */; i++ )
//		{
//			if( index % numberOfPlayerForPage == 0 )
//			{
//				y = 3;
//			}
//			if( players.get( i ) != this.gameGui.getPlayingPlayer() )
//			{
//				HashMap<String, Integer> resources = players.get( i ).getResources();
//
//				OpponentResourcesPanel opponentResourcesPanel = new OpponentResourcesPanel(
//						resources, x, y, 80, heightPanel, players.get( i ).getName() );
//				playersResources.put( players.get( i ).getName(), opponentResourcesPanel );
//
//				// TODO SISTEMARE LA VISIBILITÀ
//				changeVisibility( i, opponentResourcesPanel );
//				mainPanelBuilder.panel( opponentResourcesPanel.getPanel() );
//
//				y += heightPanel;
//				index++;
//			}
//		}
//	}
//
//	private void changeVisibility( int i, OpponentResourcesPanel opponentResourcesPanel )
//	{
//		if( i >= currentPage * numberOfPlayerForPage
//				|| i < ( currentPage - 1 ) * numberOfPlayerForPage )
//			opponentResourcesPanel.getPanel().visible( false );
//		else
//			opponentResourcesPanel.getPanel().visible( true );
//	}
//
//	public void changePage( boolean back )
//	{
//		if( !back )
//		{
//			currentPage++;
//
//			int i = 0;
//			for( Map.Entry<String, OpponentResourcesPanel> entry : playersResources.entrySet() )
//			{
//				changeVisibility( i, entry.getValue() );
//				i++;
//
//			}
//			if( currentPage > 1 )
//			{
//				buttonBackBuilder.visible( true );
//			}
//			if( currentPage >= numberOfPages )
//			{
//				buttonForwardBuilder.visible( false );
//			}
//		}
//		else
//		{
//			currentPage--;
//
//			int i = 0;
//			for( Map.Entry<String, OpponentResourcesPanel> entry : playersResources.entrySet() )
//			{
//				changeVisibility( i, entry.getValue() );
//				i++;
//
//			}
//			if( currentPage <= 1 )
//			{
//				buttonBackBuilder.visible( false );
//			}
//			if( currentPage < numberOfPages )
//			{
//				buttonForwardBuilder.visible( true );
//			}
//		}
//	}
//
//	public Element build( Nifty nifty, Screen currentScreen, Element parent )
//	{
//		Element element = mainPanelBuilder.build( nifty, currentScreen, parent );
//		return element;
//	}
//
//	public void update()
//	{
//		List<Player> players = GameManager.getInstance().getPlayers();
//
//		for( int i = 0; i < players.size() && players.get( i ) != gameGui.getPlayingPlayer(); i++ )
//		{
//			HashMap<String, Integer> resources = players.get( i ).getResources();
//			playersResources.get( players.get( i ).getName() ).updateResources( resources );
//		}
//
//	}
//
//	private ImageBuilder getImageBuilder( final String fileName )
//	{
//		return new ImageBuilder()
//		{
//			{
//				filename( fileName );
//				width( "100%" );
//				height( "100%" );
//
//			}
//		};
//	}
//
//	private ButtonBuilder getButtonBuilder( final String text, final int x, final int y,
//			final int width, final int height )
//	{
//		return new ButtonBuilder( "#" + text + "Button", text )
//		{
//			{
//				width( Integer.toString( width ) + "%" );
//				height( Integer.toString( height ) + "%" );
//				x( Integer.toString( x ) + "%" );
//				y( Integer.toString( y ) + "%" );
//
//			}
//		};
//	}
//
//	public void turnToPageOne()
//	{
//		currentPage = 1;
//		int i = 0;
//		for( Map.Entry<String, OpponentResourcesPanel> entry : playersResources.entrySet() )
//		{
//			changeVisibility( i, entry.getValue() );
//			i++;
//		}
//		buttonBackBuilder.visible( false );
//		buttonForwardBuilder.visible( true );
//		if( numberOfPages == 1 )
//		{
//			buttonForwardBuilder.visible( false );
//		}
//
//	}
// }

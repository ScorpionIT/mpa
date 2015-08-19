package mpa.gui.gameGui.panel;

import mpa.core.logic.AbstractObject;
import mpa.core.logic.building.AbstractPrivateProperty;
import mpa.core.logic.resource.AbstractResourceProducer;
import mpa.core.logic.resource.Resources;
import mpa.gui.gameGui.GameGui;

import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.audio.AudioRenderer;
import com.jme3.input.InputManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.elements.Element;

public class NiftyHandler
{
	private Nifty nifty;
	private SelectionPanel selectionPanel;
	private ResourcesPanel resourcesPanel;
	private AbstractObject selectedObject;
	private OpponentPropertiesPanel opponentPropertiesPanel;
	private GameGui gameGui;

	public NiftyHandler( AssetManager assetManager, InputManager inputManager,
			AudioRenderer audioRenderer, ViewPort guiViewPort, AppStateManager stateManager,
			final GameGui gameGui )
	{
		this.gameGui = gameGui;
		NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay( assetManager, inputManager,
				audioRenderer, guiViewPort, 2048, 2048 );
		nifty = niftyDisplay.getNifty();
		NiftyController controller = new NiftyController( gameGui );

		initNifty( controller, assetManager );
		// ScreenController screenController = nifty.getCurrentScreen().getScreenController();

		// stateManager.attach( AppS screenController );

		// TimerTask timerTask = new TimerTask()
		// {
		// public void run()
		// {
		// gameGui.updateResourcePanel();
		//
		// }
		// };
		//
		// Timer timer = new Timer(true);
		// timer.scheduleAtFixedRate(timerTask, 0, 1000);

		// gameGui.updateResourcePanel();

		guiViewPort.addProcessor( niftyDisplay );
	}

	private void initNifty( final NiftyController niftyController, AssetManager assetManager )
	{

		nifty.loadStyleFile( "nifty-default-styles.xml" );
		nifty.loadControlFile( "nifty-default-controls.xml" );
		nifty.addScreen( "main", new ScreenBuilder( "main", niftyController )
		{
			{
				controller( niftyController );
				layer( new LayerBuilder( "selectedLayer" )
				{
					{
						childLayoutAbsolute();
					}
				} );

				layer( new LayerBuilder( "resourcesLayer" )
				{
					{
						childLayoutAbsolute();
					}
				} );

				layer( new LayerBuilder( "opponentPropertiesLayer" )
				{
					{
						childLayoutAbsolute();
					}
				} );

			}
		}.build( nifty ) );

		nifty.gotoScreen( "main" );

		assetManager.registerLocator( "./Assets/BackgroundImages", FileLocator.class );
		selectionPanel = new SelectionPanel();

		assetManager.registerLocator( "./Assets/iconResources", FileLocator.class );

		resourcesPanel = new ResourcesPanel( this.gameGui.getPlayingPlayer() );

		opponentPropertiesPanel = new OpponentPropertiesPanel( gameGui );

		Element findElementByName = nifty.getCurrentScreen().findElementByName( "resourcesLayer" );
		findElementByName.add( resourcesPanel.build( nifty, nifty.getCurrentScreen(),
				findElementByName ) );

		findElementByName = nifty.getCurrentScreen().findElementByName( "selectedLayer" );
		findElementByName.add( selectionPanel.build( nifty, nifty.getCurrentScreen(),
				findElementByName ) );

	}

	public void setSelectedPanel( final AbstractObject abstractObject )
	{

		removeSelectedPanel();
		String name = abstractObject.getClass().toString();
		System.out.println( name );

		String[] split = name.split( "\\." );

		selectionPanel.setObjectName( split[split.length - 1] );

		if( abstractObject instanceof AbstractResourceProducer )
		{
			selectionPanel.setProductivityLabel( ( ( AbstractResourceProducer ) abstractObject )
					.getProviding() );
		}
		if( abstractObject instanceof AbstractPrivateProperty )
			selectionPanel.setObjectOwner( ( ( AbstractPrivateProperty ) abstractObject )
					.getOwner() );
		this.selectedObject = abstractObject;

		Element findElementByName = nifty.getCurrentScreen().findElementByName( "selectedLayer" );
		findElementByName.add( selectionPanel.build( nifty, nifty.getCurrentScreen(),
				findElementByName ) );

	}

	public void removeSelectedPanel()
	{

		if( nifty.getCurrentScreen().findElementByName( "#selected" ) != null )
		{
			System.out.println( nifty.getCurrentScreen().findElementByName( "#selected" ) );
			nifty.getCurrentScreen().findElementByName( "#selected" ).markForRemoval();
			nifty.removeElement( nifty.getCurrentScreen(), nifty.getCurrentScreen()
					.findElementByName( "#selected" ) );

			selectedObject = null;
		}

	}

	public AbstractObject getSelectedObject()
	{
		return selectedObject;
	}

	public boolean isVisibleSelectionPanel()
	{
		return selectionPanel.getIsVisible();
	}

	public void setResourceValue( Resources key, int value )
	{

		LabelBuilder labelBuilder = resourcesPanel.getResourceLabel( key.toString() );
		labelBuilder.text( Integer.toString( value ) );
		if( nifty.getCurrentScreen().findElementByName( "Avaible" + key.toString() ) != null )
		{

			nifty.getCurrentScreen().findElementByName( "Avaible" + key ).markForRemoval();
			nifty.removeElement( nifty.getCurrentScreen(), nifty.getCurrentScreen()
					.findElementByName( "Avaible" + key ) );
			Element findElementByName = nifty.getCurrentScreen().findElementByName( key + "Panel" );
			findElementByName.add( labelBuilder.build( nifty, nifty.getCurrentScreen(),
					findElementByName ) );
		}
	}

	public Nifty getNifty()
	{
		return nifty;
	}

	public void emptySelectedPanel()
	{
		removeSelectedPanel();
		selectionPanel.emptyPanel();

		Element findElementByName = nifty.getCurrentScreen().findElementByName( "selectedLayer" );
		findElementByName.add( selectionPanel.build( nifty, nifty.getCurrentScreen(),
				findElementByName ) );

	}

	public void removeOpponentPropertiesPanel()
	{

		if( nifty.getCurrentScreen().findElementByName( "#opponentPropertiesPanelId" ) != null )
		{
			nifty.getCurrentScreen().findElementByName( "#opponentPropertiesPanelId" )
					.markForRemoval();
			// nifty.getCurrentScreen().findElementByName("#opponentPropertiesPanelId").setVisible(false);
			nifty.removeElement( nifty.getCurrentScreen(), nifty.getCurrentScreen()
					.findElementByName( "#opponentPropertiesPanelId" ) );

		}

	}

	public void setOpponentPropertiesPanel()
	{
		Element findElementByName = nifty.getCurrentScreen().findElementByName(
				"opponentPropertiesLayer" );

		// if (nifty.getCurrentScreen().findElementByName("#opponentPropertiesPanelId") != null)
		// {
		//
		// nifty.getCurrentScreen().findElementByName("#opponentPropertiesPanelId").setVisible(true);
		//
		// }
		// else
		{
			opponentPropertiesPanel.update();

			// System.out.println("sono elment find element by id " + findElementByName);
			findElementByName.add( opponentPropertiesPanel.build( nifty, nifty.getCurrentScreen(),
					findElementByName ) );
		}
		// Element findElementByName =
		// nifty.getCurrentScreen().findElementByName("opponentPropertiesLayer");
		// nifty.getCurrentScreen().findElementByName("opponentPropertiesLayer").setVisible(true);;
		// findElementByName.add(opponentPropertiesPanel.build(nifty, nifty.getCurrentScreen(),
		// findElementByName));

	}

	public void changePageOpponentResourcesPanel( boolean back )
	{
		opponentPropertiesPanel.changePage( back );
		removeOpponentPropertiesPanel();
		setOpponentPropertiesPanel();
	}
}

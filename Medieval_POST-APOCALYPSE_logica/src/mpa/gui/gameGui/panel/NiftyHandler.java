package mpa.gui.gameGui.panel;

import java.util.HashMap;
import java.util.Set;

import mpa.gui.gameGui.listener.ListenerImplementation;
import mpa.gui.gameGui.playingGUI.GameGui;
import mpa.gui.gameGui.playingGUI.GuiObjectManager;

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
	private String selectedObjectType;
	private String selectedObjectID;
	private OpponentPropertiesPanel opponentPropertiesPanel;
	private ChoosePanel choosePanel;
	private GameGui gameGui;
	private ListenerImplementation gameController;
	private boolean opponentPropertiesPanelIsVisible = false;
	private boolean chooseObjectPanelIsVisible = false;

	public NiftyHandler(AssetManager assetManager, InputManager inputManager, AudioRenderer audioRenderer, ViewPort guiViewPort,
			AppStateManager stateManager, ListenerImplementation playerController, GameGui gameGui)
	{
		this.gameController = playerController;
		this.gameGui = gameGui;
		NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort, 2048, 2048);
		nifty = niftyDisplay.getNifty();
		NiftyController controller = new NiftyController(this, playerController);

		initNifty(controller, assetManager);

		guiViewPort.addProcessor(niftyDisplay);
	}

	private void initNifty(final NiftyController niftyController, AssetManager assetManager)
	{

		nifty.loadStyleFile("nifty-default-styles.xml");
		nifty.loadControlFile("nifty-default-controls.xml");
		nifty.addScreen("main", new ScreenBuilder("main", niftyController)
		{
			{
				controller(niftyController);
				layer(new LayerBuilder("selectedLayer")
				{
					{
						childLayoutAbsolute();
					}
				});

				layer(new LayerBuilder("resourcesLayer")
				{
					{
						childLayoutAbsolute();
					}
				});

				layer(new LayerBuilder("opponentPropertiesLayer")
				{
					{
						childLayoutAbsolute();
					}
				});

				layer(new LayerBuilder("chooseLayer")
				{
					{
						childLayoutAbsolute();
					}
				});

			}
		}.build(nifty));

		nifty.gotoScreen("main");

		assetManager.registerLocator("./Assets/BackgroundImages", FileLocator.class);
		selectionPanel = new SelectionPanel(gameGui.windowWidth(), gameGui.windowHeight());

		assetManager.registerLocator("./Assets/iconResources", FileLocator.class);

		resourcesPanel = new ResourcesPanel();

		opponentPropertiesPanel = new OpponentPropertiesPanel(gameController);

		choosePanel = new ChoosePanel();

		Element findElementByName = nifty.getCurrentScreen().findElementByName("resourcesLayer");
		findElementByName.add(resourcesPanel.build(nifty, nifty.getCurrentScreen(), findElementByName));

	}

	public void setSelectedPanel(String objectType, String objectID, String objectProductivity, String pickedObjectOwner)
	{
		selectionPanel.setObjectName(objectType);
		selectionPanel.setProductivityLabel(objectProductivity);
		selectionPanel.setObjectOwner(pickedObjectOwner);
		selectedObjectID = objectID;
		selectedObjectType = objectType;

	}

	public void removeSelectedPanel()
	{

		if (nifty.getCurrentScreen().findElementByName("#selected") != null)
		{
			System.out.println(nifty.getCurrentScreen().findElementByName("#selected"));
			nifty.getCurrentScreen().findElementByName("#selected").markForRemoval();
			nifty.removeElement(nifty.getCurrentScreen(), nifty.getCurrentScreen().findElementByName("#selected"));
			selectionPanel.setVisible(false);
			selectedObjectID = null;
			selectedObjectType = null;

		}

	}

	public void relocateChoosePanel(int x, int y)
	{
		if ((x + choosePanel.getWidth()) > gameGui.windowWidth())
		{
			System.out.println(choosePanel);
			x -= choosePanel.getWidth();
		}

		if ((y + choosePanel.getHeight()) > gameGui.windowHeight() - (gameGui.windowHeight() * 10 / 100))
		{
			y -= choosePanel.getHeight();
		}
		choosePanel.changePosition(x, y);
		removeChoosePanel();
		choosePanel.setVisible(true);
		chooseObjectPanelIsVisible = true;
		Element findElementByName = nifty.getCurrentScreen().findElementByName("chooseLayer");
		findElementByName.add(choosePanel.build(nifty, nifty.getCurrentScreen(), findElementByName));

	}

	public void relocateSelectionPanel(int x, int y)
	{
		if ((x + selectionPanel.getWidth()) > gameGui.windowWidth())
		{
			System.out.println(choosePanel);
			x -= selectionPanel.getWidth();
		}

		if ((y + selectionPanel.getHeight()) > gameGui.windowHeight() - (gameGui.windowHeight() * 10 / 100))
		{
			y -= selectionPanel.getHeight();
		}
		selectionPanel.changePosition(x, y);

		selectionPanel.setVisible(true);
		Element findElementByName = nifty.getCurrentScreen().findElementByName("selectedLayer");
		findElementByName.add(selectionPanel.build(nifty, nifty.getCurrentScreen(), findElementByName));
	}

	public void changeChoosenElement(boolean back)
	{
		choosePanel.changeChoosenElement(back);
		removeChoosePanel();
		choosePanel.setVisible(true);
		Element findElementByName = nifty.getCurrentScreen().findElementByName("chooseLayer");
		findElementByName.add(choosePanel.build(nifty, nifty.getCurrentScreen(), findElementByName));

	}

	public void initChoosenElementPanel()
	{
		choosePanel.initSelectedElment();
	}

	public void removeChoosePanel()
	{

		if (nifty.getCurrentScreen().findElementByName("#choosePanel") != null)
		{
			nifty.getCurrentScreen().findElementByName("#choosePanel").markForRemoval();
			nifty.removeElement(nifty.getCurrentScreen(), nifty.getCurrentScreen().findElementByName("#choosePanel"));

		}
		choosePanel.setVisible(false);
		chooseObjectPanelIsVisible = false;

	}

	public String getSelectedObject()
	{
		return selectedObjectType + ":" + selectedObjectID;
	}

	public String getSelectedObjectType()
	{
		return selectedObjectType;
	}

	public String getSelectedObjectID()
	{
		return selectedObjectID;
	}

	public void updateResourcePanel()
	{
		HashMap<String, Integer> playingPlayerResourcesAmount = gameController.getPlayerResourcesAmount(GuiObjectManager.getInstance()
				.getPlayingPlayer());
		Set<String> keySet = playingPlayerResourcesAmount.keySet();
		for (String resourceName : keySet)
		{
			setResourceValue(resourceName, playingPlayerResourcesAmount.get(resourceName));
		}

	}

	private void setResourceValue(String resourceName, int value)
	{

		LabelBuilder labelBuilder = resourcesPanel.getResourceLabel(resourceName.toString());
		labelBuilder.text(Integer.toString(value));
		if (nifty.getCurrentScreen().findElementByName("Avaible" + resourceName.toString()) != null)
		{

			nifty.getCurrentScreen().findElementByName("Avaible" + resourceName).markForRemoval();
			nifty.removeElement(nifty.getCurrentScreen(), nifty.getCurrentScreen().findElementByName("Avaible" + resourceName));
			Element findElementByName = nifty.getCurrentScreen().findElementByName(resourceName + "Panel");
			findElementByName.add(labelBuilder.build(nifty, nifty.getCurrentScreen(), findElementByName));
		}
	}

	public Nifty getNifty()
	{
		return nifty;
	}

	public void removeOpponentPropertiesPanel()
	{

		if (nifty.getCurrentScreen().findElementByName("#opponentPropertiesPanelId") != null)
		{
			nifty.getCurrentScreen().findElementByName("#opponentPropertiesPanelId").markForRemoval();
			// nifty.getCurrentScreen().findElementByName("#opponentPropertiesPanelId").setVisible(false);
			nifty.removeElement(nifty.getCurrentScreen(), nifty.getCurrentScreen().findElementByName("#opponentPropertiesPanelId"));
			opponentPropertiesPanelIsVisible = false;
		}

	}

	public boolean isVisibleOpponentPropertiesPanel()
	{
		return opponentPropertiesPanelIsVisible;
	}

	public void turnToPageOne()
	{
		opponentPropertiesPanel.turnToPageOne();
	}

	public void setOpponentPropertiesPanel()
	{
		Element findElementByName = nifty.getCurrentScreen().findElementByName("opponentPropertiesLayer");

		opponentPropertiesPanelIsVisible = true;

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
			findElementByName.add(opponentPropertiesPanel.build(nifty, nifty.getCurrentScreen(), findElementByName));
		}
		// Element findElementByName =
		// nifty.getCurrentScreen().findElementByName("opponentPropertiesLayer");
		// nifty.getCurrentScreen().findElementByName("opponentPropertiesLayer").setVisible(true);;
		// findElementByName.add(opponentPropertiesPanel.build(nifty, nifty.getCurrentScreen(),
		// findElementByName));

	}

	public void changePageOpponentResourcesPanel(boolean back)
	{
		opponentPropertiesPanel.changePage(back);
		removeOpponentPropertiesPanel();
		setOpponentPropertiesPanel();
	}

	public String getSelectedItem()
	{
		return choosePanel.getSelectedElement();
	}

	public boolean isVisibleSelectionPanel()
	{
		return selectionPanel.isVisible();
	}

	public boolean isVisibleChoosePanel()
	{
		return chooseObjectPanelIsVisible;
	}

}

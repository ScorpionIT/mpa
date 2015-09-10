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
	private ListenerImplementation playerController;

	public NiftyHandler(AssetManager assetManager, InputManager inputManager, AudioRenderer audioRenderer, ViewPort guiViewPort,
			AppStateManager stateManager, ListenerImplementation playerController)
	{
		this.playerController = playerController;
		NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort, 2048, 2048);
		nifty = niftyDisplay.getNifty();
		NiftyController controller = new NiftyController(this);

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
		selectionPanel = new SelectionPanel();

		assetManager.registerLocator("./Assets/iconResources", FileLocator.class);

		resourcesPanel = new ResourcesPanel();

		opponentPropertiesPanel = new OpponentPropertiesPanel(playerController);

		choosePanel = new ChoosePanel();

		Element findElementByName = nifty.getCurrentScreen().findElementByName("resourcesLayer");
		findElementByName.add(resourcesPanel.build(nifty, nifty.getCurrentScreen(), findElementByName));

		findElementByName = nifty.getCurrentScreen().findElementByName("selectedLayer");
		findElementByName.add(selectionPanel.build(nifty, nifty.getCurrentScreen(), findElementByName));
	}

	public void setSelectedPanel(String objectType, String objectID, String objectProductivity, String pickedObjectOwner)
	{

		removeSelectedPanel();
		// String name = abstractObject.getClass().toString();
		// System.out.println(name);
		//
		// String[] split = name.split("\\.");
		//
		// selectionPanel.setObjectName(split[split.length - 1]);
		selectionPanel.setObjectName(objectType);
		selectionPanel.setProductivityLabel(objectProductivity);
		selectionPanel.setObjectOwner(pickedObjectOwner);

		// if (abstractObject instanceof AbstractResourceProducer)
		// {
		// selectionPanel.setProductivityLabel(((AbstractResourceProducer)
		// abstractObject).getProviding());
		// }
		// if (abstractObject instanceof AbstractPrivateProperty)
		// selectionPanel.setObjectOwner(((AbstractPrivateProperty) abstractObject).getOwner());
		// this.selectedObject = abstractObject;

		Element findElementByName = nifty.getCurrentScreen().findElementByName("selectedLayer");
		findElementByName.add(selectionPanel.build(nifty, nifty.getCurrentScreen(), findElementByName));

	}

	public void removeSelectedPanel()
	{

		if (nifty.getCurrentScreen().findElementByName("#selected") != null)
		{
			System.out.println(nifty.getCurrentScreen().findElementByName("#selected"));
			nifty.getCurrentScreen().findElementByName("#selected").markForRemoval();
			nifty.removeElement(nifty.getCurrentScreen(), nifty.getCurrentScreen().findElementByName("#selected"));

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
		Element findElementByName = nifty.getCurrentScreen().findElementByName("chooseLayer");
		findElementByName.add(choosePanel.build(nifty, nifty.getCurrentScreen(), findElementByName));

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

	public boolean isVisibleSelectionPanel()
	{
		return selectionPanel.getIsVisible();
	}

	public void updateResourcePanel()
	{
		HashMap<String, Integer> playingPlayerResourcesAmount = playerController.getPlayerResourcesAmount(GuiObjectManager.getInstance()
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

	public void emptySelectedPanel()
	{
		removeSelectedPanel();
		selectionPanel.emptyPanel();

		Element findElementByName = nifty.getCurrentScreen().findElementByName("selectedLayer");
		findElementByName.add(selectionPanel.build(nifty, nifty.getCurrentScreen(), findElementByName));

	}

	public void removeOpponentPropertiesPanel()
	{

		if (nifty.getCurrentScreen().findElementByName("#opponentPropertiesPanelId") != null)
		{
			nifty.getCurrentScreen().findElementByName("#opponentPropertiesPanelId").markForRemoval();
			// nifty.getCurrentScreen().findElementByName("#opponentPropertiesPanelId").setVisible(false);
			nifty.removeElement(nifty.getCurrentScreen(), nifty.getCurrentScreen().findElementByName("#opponentPropertiesPanelId"));

		}

	}

	public void turnToPageOne()
	{
		opponentPropertiesPanel.turnToPageOne();
	}

	public void setOpponentPropertiesPanel()
	{
		Element findElementByName = nifty.getCurrentScreen().findElementByName("opponentPropertiesLayer");

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
}

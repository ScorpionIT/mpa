package mpa.gui.gameGui.panel;

import java.util.Map;
import java.util.Set;

import mpa.gui.gameGui.listener.HandlerImplementation;
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
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.controls.MessageBox;
import de.lessvoid.nifty.controls.MessageBox.MessageType;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.Color;

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
	private HandlerImplementation gameController;
	private boolean opponentPropertiesPanelIsVisible = false;
	private boolean chooseObjectPanelIsVisible = false;
	private SelectionHeadquarterHandler selectionHeadquarterHandler;
	private boolean isVisibleHeadquarterPanel = false;
	private boolean isVisibleSelectionPanel = false;
	private boolean buttonPotionClicked = false;
	private boolean messageBoxVisible = false;
	private Screen mainScreen;

	public NiftyHandler(AssetManager assetManager, InputManager inputManager, AudioRenderer audioRenderer, ViewPort guiViewPort,
			AppStateManager stateManager, HandlerImplementation playerController, GameGui gameGui)
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

		nifty.loadControlFile("nifty-default-controls.xml");
		nifty.loadStyleFile("nifty-default-styles.xml");

		createMainScreen(niftyController);

		nifty.gotoScreen("main");

		initPanels(assetManager);
		createWinningScreen();
		createLosingScreen();

	}

	private void createMainScreen(final NiftyController niftyController)
	{
		mainScreen = new ScreenBuilder("main", niftyController)
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

				layer(new LayerBuilder("selectionHeadquarterLayer")
				{
					{
						childLayoutAbsolute();
					}
				});

			}
		}.build(nifty);
		nifty.addScreen("main", mainScreen);
	}

	private void createWinningScreen()
	{
		final Color color = new Color(Color.BLACK, 0.7f);
		ScreenBuilder winningScreen = new ScreenBuilder("#winningScreen")
		{
			{
				layer(new LayerBuilder("winningLayer")
				{
					{
						childLayoutAbsolute();
						panel(new PanelBuilder("#winningPanel")
						{
							{
								childLayoutCenter();
								width("100%");
								height("100%");
								backgroundColor(color);

								image(new ImageBuilder("#YouWin")
								{
									{
										filename("youWin.png");

									}

								});
							}

						});
					}
				});

			}
		};
		nifty.addScreen("#winningScreen", winningScreen.build(nifty));
	}

	private void createLosingScreen()
	{
		final Color color = new Color(Color.BLACK, 0.7f);
		ScreenBuilder winningScreen = new ScreenBuilder("#losingScreen")
		{
			{
				layer(new LayerBuilder("losingLayer")
				{
					{
						childLayoutAbsolute();
						panel(new PanelBuilder("#losingPanel")
						{
							{
								childLayoutCenter();
								width("100%");
								height("100%");
								backgroundColor(color);

								image(new ImageBuilder("#YouWLose")
								{
									{
										filename("youLose.png");

									}

								});
							}
						});
					}
				});

			}
		};
		nifty.addScreen("#losingScreen", winningScreen.build(nifty));
	}

	public void setWinningScreen()
	{
		nifty.gotoScreen("#winningScreen");
	}

	public void setLosingScreen()
	{
		nifty.gotoScreen("#losingScreen");

	}

	public void createMessageBox(final String message, String textButton)
	{

		MessageBox messageBox = new MessageBox(nifty, MessageType.CUSTOM, message, textButton);
		messageBoxVisible = true;

		messageBox.show();

	}

	private void initPanels(AssetManager assetManager)
	{
		assetManager.registerLocator("./Assets/BackgroundImages", FileLocator.class);
		assetManager.registerLocator("./Assets/textImage", FileLocator.class);
		selectionPanel = new SelectionPanel(gameGui.windowWidth(), gameGui.windowHeight());

		assetManager.registerLocator("./Assets/iconResources", FileLocator.class);

		resourcesPanel = new ResourcesPanel();

		opponentPropertiesPanel = new OpponentPropertiesPanel(gameController);

		choosePanel = new ChoosePanel();

		Element findElementByName = mainScreen.findElementByName("resourcesLayer");
		findElementByName.add(resourcesPanel.build(nifty, mainScreen, findElementByName));

		selectionHeadquarterHandler = new SelectionHeadquarterHandler(gameGui.windowWidth(), gameGui.windowHeight(), gameController);
	}

	public void setSelectedPanel(String objectType, String objectID, String objectProductivity, String pickedObjectOwner)
	{

		selectionPanel.setObjectName(objectType);
		selectionPanel.setProductivityLabel(objectProductivity);
		selectionPanel.setObjectOwner(pickedObjectOwner);
		selectedObjectID = objectID;
		selectedObjectType = objectType;

	}

	public void setHeadquarterPanel(String objectType, String objectID)
	{
		selectionHeadquarterHandler.addSelectionHeadquarter(nifty);
		selectedObjectID = objectID;
		selectedObjectType = objectType;
		isVisibleHeadquarterPanel = true;

	}

	public void removeSelectedPanel()
	{

		if (mainScreen.findElementByName("#selected") != null)
		{
			System.out.println(mainScreen.findElementByName("#selected"));
			mainScreen.findElementByName("#selected").markForRemoval();
			nifty.removeElement(mainScreen, mainScreen.findElementByName("#selected"));
			selectionPanel.setVisible(false);
			selectedObjectID = null;
			selectedObjectType = null;

		}
		isVisibleSelectionPanel = false;

	}

	public String getMinionsTarget()
	{
		return selectionHeadquarterHandler.getMinionsTarget();
	}

	public String getMinionsQuantity()
	{
		return selectionHeadquarterHandler.getMinionsQuantity();
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
		Element findElementByName = mainScreen.findElementByName("chooseLayer");
		findElementByName.add(choosePanel.build(nifty, mainScreen, findElementByName));

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
		isVisibleSelectionPanel = true;
		Element findElementByName = mainScreen.findElementByName("selectedLayer");
		findElementByName.add(selectionPanel.build(nifty, mainScreen, findElementByName));
	}

	public void changeChoosenElement(boolean back)
	{
		choosePanel.changeChoosenElement(back);
		removeChoosePanel();
		choosePanel.setVisible(true);
		Element findElementByName = mainScreen.findElementByName("chooseLayer");
		findElementByName.add(choosePanel.build(nifty, mainScreen, findElementByName));

	}

	public void initChoosenElementPanel()
	{
		choosePanel.initSelectedElment();
	}

	private void removeChoosePanel()
	{

		if (mainScreen.findElementByName("#choosePanel") != null)
		{
			mainScreen.findElementByName("#choosePanel").markForRemoval();
			nifty.removeElement(mainScreen, mainScreen.findElementByName("#choosePanel"));
		}

	}

	public void removeChooseItemPanel()
	{
		removeChoosePanel();
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

	public synchronized void updateResourcePanel()
	{
		removeResourcePanel();
		Map<String, Integer> playingPlayerResourcesAmount = gameController
				.getPlayerResourcesAmount(GuiObjectManager.getInstance().getPlayingPlayer());
		if (playingPlayerResourcesAmount == null)
			return;
		Set<String> keySet = playingPlayerResourcesAmount.keySet();
		for (String resourceName : keySet)
		{
			resourcesPanel.updateResourceLabel(nifty, resourceName, playingPlayerResourcesAmount.get(resourceName));
		}
		resourcesPanel.updateInformationPanel(nifty, gameController.getPlayerHP(GuiObjectManager.getInstance().getPlayingPlayer()),
				gameController.getPlayerMP(GuiObjectManager.getInstance().getPlayingPlayer()),
				gameController.getPlayerLevel(GuiObjectManager.getInstance().getPlayingPlayer()),
				gameController.getPlayerHPPotion(GuiObjectManager.getInstance().getPlayingPlayer()),
				gameController.getPlayerMPPotion(GuiObjectManager.getInstance().getPlayingPlayer()),
				gameController.getPlayerGranade(GuiObjectManager.getInstance().getPlayingPlayer()));
		setResourcePanel();

	}

	public Nifty getNifty()
	{
		return nifty;
	}

	public void removeOpponentPropertiesPanel()
	{

		if (mainScreen.findElementByName("#opponentPropertiesPanelId") != null)
		{
			mainScreen.findElementByName("#opponentPropertiesPanelId").markForRemoval();
			// mainScreen.findElementByName("#opponentPropertiesPanelId").setVisible(false);
			nifty.removeElement(mainScreen, mainScreen.findElementByName("#opponentPropertiesPanelId"));
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
		Element findElementByName = mainScreen.findElementByName("opponentPropertiesLayer");

		opponentPropertiesPanelIsVisible = true;

		// if (mainScreen.findElementByName("#opponentPropertiesPanelId") != null)
		// {
		//
		// mainScreen.findElementByName("#opponentPropertiesPanelId").setVisible(true);
		//
		// }
		// else
		{
			opponentPropertiesPanel.update();

			// System.out.println("sono elment find element by id " + findElementByName);
			findElementByName.add(opponentPropertiesPanel.build(nifty, mainScreen, findElementByName));
		}
		// Element findElementByName =
		// mainScreen.findElementByName("opponentPropertiesLayer");
		// mainScreen.findElementByName("opponentPropertiesLayer").setVisible(true);;
		// findElementByName.add(opponentPropertiesPanel.build(nifty, mainScreen,
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

	public void removePlayer(String playerName)
	{
		if (!playerName.equals(GuiObjectManager.getInstance().getPlayingPlayer()))
		{
			opponentPropertiesPanel.removePlayer(playerName);
			selectionHeadquarterHandler.removePlayer(playerName);
		}

	}

	private void removeResourcePanel()
	{
		if (mainScreen.findElementByName("#resourcesBackground") != null)
		{
			mainScreen.findElementByName("#resourcesBackground").markForRemoval();
			nifty.removeElement(mainScreen, mainScreen.findElementByName("#resourcesBackground"));
		}
	}

	private void setResourcePanel()
	{
		Element findElementByName = mainScreen.findElementByName("resourcesLayer");
		findElementByName.add(resourcesPanel.build(nifty, mainScreen, findElementByName));
	}

	public void removeHeadquarterPanel()
	{
		if (mainScreen.findElementByName("#selectedHeadquarter") != null)
		{
			mainScreen.findElementByName("#selectedHeadquarter").markForRemoval();
			nifty.removeElement(mainScreen, mainScreen.findElementByName("#selectedHeadquarter"));
			selectedObjectID = null;
			selectedObjectType = null;
		}
		isVisibleHeadquarterPanel = false;
	}

	public boolean isVisibleHeadquarterPanel()
	{
		return isVisibleHeadquarterPanel;
	}

	public boolean isButtonPotionClicked()
	{
		return buttonPotionClicked;
	}

	public boolean isVisibleSelectionPanel()
	{
		return isVisibleSelectionPanel;
	}

	public boolean isVisibleChoosePanel()
	{
		return chooseObjectPanelIsVisible;
	}

	public void setCreateButtonClicked(boolean clicked)
	{
		isVisibleHeadquarterPanel = clicked;
	}

	public void setSelectionPanelVisible(boolean clicked)
	{
		isVisibleSelectionPanel = clicked;

	}

	public void setButtonPotionClicked(boolean clicked)
	{
		System.out.println("HO CLICKATO SUL BOTTONE E HO SETTATO LA VARIABILE");
		buttonPotionClicked = clicked;
		System.out.println(buttonPotionClicked);
	}

	public void removeVisiblePanel()
	{
		if (messageBoxVisible)
		{
			messageBoxVisible = false;
		}
		else if (buttonPotionClicked)
		{
			buttonPotionClicked = false;
		}
		else if (isVisibleSelectionPanel)
		{
			removeSelectedPanel();
		}
		else if (isVisibleHeadquarterPanel)
		{
			removeHeadquarterPanel();
		}
		else if (chooseObjectPanelIsVisible)
		{
			removeChooseItemPanel();
		}

	}

	public boolean canClick()
	{
		return !(isVisibleSelectionPanel || opponentPropertiesPanelIsVisible || chooseObjectPanelIsVisible || isVisibleHeadquarterPanel
				|| buttonPotionClicked || messageBoxVisible);
	}

	public boolean canCreateChooseItemPanel()
	{
		return !(isVisibleSelectionPanel || isVisibleHeadquarterPanel || opponentPropertiesPanelIsVisible);
	}

	public boolean caCreateOpponentResourcePanel()
	{
		return !(isVisibleSelectionPanel || isVisibleHeadquarterPanel || chooseObjectPanelIsVisible);
	}

}

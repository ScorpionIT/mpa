package mpa.gui.gameGui.panel;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import mpa.gui.gameGui.listener.ListenerImplementation;
import mpa.gui.gameGui.playingGUI.GuiObjectManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

public class OpponentPropertiesPanel
{

	private PanelBuilder mainPanelBuilder;

	// HashMap<String, OpponentResourcesPanel> resourcesPanels = new HashMap<>();

	private int x = 10;

	private int heightPanel = 30;
	private int startY = 7;

	private ListenerImplementation gameController;
	private int currentPage = 1;
	int numberOfPlayerForPage;
	int numberOfPages;
	private ButtonBuilder buttonForwardBuilder = null;
	private ButtonBuilder buttonBackBuilder = null;
	private boolean inizialized = false;

	private LinkedHashMap<String, OpponentResourcesPanel> playersResources = new LinkedHashMap<>();

	public OpponentPropertiesPanel(ListenerImplementation playerController)
	{

		this.gameController = playerController;

		int numberOfPlayer = gameController.getNumberOfPlayer();

		numberOfPlayerForPage = 100 / (heightPanel);
		numberOfPages = (numberOfPlayer - 1) / numberOfPlayerForPage;
		numberOfPages++;
		// System.out.println( "NUMERO DI PAGINE " + numberOfPages + " E NUMERO DI PLAYER E' "
		// + ( numberOfPlayer - 1 ) );

		initButtons();

		mainPanelBuilder = new PanelBuilder("#opponentPropertiesPanelId")
		{
			{
				image(getImageBuilder("selectedPanel.png"));

				childLayoutAbsoluteInside();
				control(buttonForwardBuilder);
				control(buttonBackBuilder);

			}
		};

		// TODO ripartire dalla prima prima pagina

		// TODO salta il player attuale

		// TODO alzare il bottone
		// mainPanelBuilder.set("horizontal", "false");
		// mainPanelBuilder.set("autoScroll", "bottom");
		mainPanelBuilder.set("width", "60%");
		mainPanelBuilder.set("height", "60%");
		mainPanelBuilder.set("x", "20%");
		mainPanelBuilder.set("y", "20%");

		// TODO NON CREA TUTTI I PANNELLI
		// System.out.println( "NUMERO DI PANNELLI " + playersResources.size() );

	}

	private void initButtons()
	{
		buttonForwardBuilder = getButtonBuilder("avanti", 55, 95, 10, 5);

		buttonForwardBuilder.interactOnClick("onClickButtonForward()");

		buttonBackBuilder = getButtonBuilder("indietro", 40, 95, 10, 5);
		buttonBackBuilder.interactOnClick("onClickButtonBack()");

		buttonBackBuilder.visible(false);
		if (numberOfPages == 1)
		{
			buttonForwardBuilder.visible(false);
		}
	}

	private void inizializePanels()
	{
		if (inizialized)
			return;
		else
			inizialized = true;
		int y = startY;

		int index = 0;

		HashMap<String, HashMap<String, Integer>> playersResourceAmount = gameController.getPlayersResourceAmount();

		HashMap<String, Color> playersColors = GuiObjectManager.getInstance().getPlayersColors();

		Set<String> playerList = playersResourceAmount.keySet();
		for (String playerName : playerList)
		{
			int i = 0;
			if (index % numberOfPlayerForPage == 0)
			{
				y = startY;
			}
			if (!playerName.equals(GuiObjectManager.getInstance().getPlayingPlayer()) && playersColors.get(playerName) != null)
			{
				HashMap<String, Integer> resources = playersResourceAmount.get(playerName);

				de.lessvoid.nifty.tools.Color color = new de.lessvoid.nifty.tools.Color((float) playersColors.get(playerName).getRed() / 255,
						(float) playersColors.get(playerName).getGreen() / 255, (float) playersColors.get(playerName).getBlue() / 255, 1);

				OpponentResourcesPanel opponentResourcesPanel = new OpponentResourcesPanel(resources, x, y, 80, heightPanel, playerName, color);

				opponentResourcesPanel.setPlayerLevel(gameController.getPlayerLevel(playerName));
				opponentResourcesPanel.setPlayerHPAndMP(gameController.getPlayerHP(playerName), gameController.getPlayerMP(playerName));

				playersResources.put(playerName, opponentResourcesPanel);

				// TODO SISTEMARE LA VISIBILITÀ
				changeVisibility(i, opponentResourcesPanel);
				mainPanelBuilder.panel(opponentResourcesPanel.getPanel());

				y += heightPanel;
				index++;
			}

			i++;
		}
	}

	private void relocateAllPanel()
	{
		int numberOfPlayer = gameController.getNumberOfPlayer();
		numberOfPlayerForPage = 100 / (heightPanel);
		numberOfPages = (numberOfPlayer - 1) / numberOfPlayerForPage;
		numberOfPages++;

		int y = startY;

		int index = 0;

		Set<String> playerList = playersResources.keySet();

		for (String playerName : playerList)
		{
			int i = 0;
			if (index % numberOfPlayerForPage == 0)
			{
				y = startY;
			}
			if (!playerName.equals(GuiObjectManager.getInstance().getPlayingPlayer()))
			{
				playersResources.get(playerName).getPanel().x(Integer.toString(x) + "%");
				playersResources.get(playerName).getPanel().y(Integer.toString(y) + "%");

				// TODO SISTEMARE LA VISIBILITÀ
				changeVisibility(i, playersResources.get(playerName));

				y += heightPanel;
				index++;
			}

			i++;
		}

	}

	public void removePlayer(String playerName)
	{
		if (playersResources.get(playerName) != null)
		{
			playersResources.get(playerName).getPanel().visible(false);
			playersResources.remove(playerName);
			relocateAllPanel();
		}
	}

	private void changeVisibility(int i, OpponentResourcesPanel opponentResourcesPanel)
	{
		if (i >= currentPage * numberOfPlayerForPage || i < (currentPage - 1) * numberOfPlayerForPage)
			opponentResourcesPanel.getPanel().visible(false);
		else
			opponentResourcesPanel.getPanel().visible(true);
	}

	public void changePage(boolean back)
	{
		if (!back)
		{
			currentPage++;

			int i = 0;
			for (Map.Entry<String, OpponentResourcesPanel> entry : playersResources.entrySet())
			{
				changeVisibility(i, entry.getValue());
				i++;

			}
			if (currentPage > 1)
			{
				buttonBackBuilder.visible(true);
			}
			if (currentPage >= numberOfPages)
			{
				buttonForwardBuilder.visible(false);
			}
		}
		else
		{
			currentPage--;

			int i = 0;
			for (Map.Entry<String, OpponentResourcesPanel> entry : playersResources.entrySet())
			{
				changeVisibility(i, entry.getValue());
				i++;

			}
			if (currentPage <= 1)
			{
				buttonBackBuilder.visible(false);
			}
			if (currentPage < numberOfPages)
			{
				buttonForwardBuilder.visible(true);
			}
		}
	}

	public Element build(Nifty nifty, Screen currentScreen, Element parent)
	{
		Element element = mainPanelBuilder.build(nifty, currentScreen, parent);
		return element;
	}

	public void update()
	{
		inizializePanels();

		HashMap<String, HashMap<String, Integer>> playersResourceAmount = gameController.getPlayersResourceAmount();
		Set<String> keySet = playersResourceAmount.keySet();
		for (String playerName : keySet)
		{
			// System.out.println( playerName );
			if (!playerName.equals(GuiObjectManager.getInstance().getPlayingPlayer()) && playersResources.get(playerName) != null)
			{

				HashMap<String, Integer> resources = playersResourceAmount.get(playerName);

				playersResources.get(playerName).updateResources(resources);
				playersResources.get(playerName).setPlayerLevel(gameController.getPlayerLevel(playerName));

				playersResources.get(playerName).setPlayerHPAndMP(gameController.getPlayerHP(playerName), gameController.getPlayerMP(playerName));
			}
		}

	}

	private ImageBuilder getImageBuilder(final String fileName)
	{
		return new ImageBuilder()
		{
			{
				filename(fileName);
				width("100%");
				height("100%");

			}
		};
	}

	private ButtonBuilder getButtonBuilder(final String text, final int x, final int y, final int width, final int height)
	{
		return new ButtonBuilder("#" + text + "Button", text)
		{
			{
				width(Integer.toString(width) + "%");
				height(Integer.toString(height) + "%");
				x(Integer.toString(x) + "%");
				y(Integer.toString(y) + "%");

			}
		};
	}

	public void turnToPageOne()
	{
		currentPage = 1;
		int i = 0;
		for (Map.Entry<String, OpponentResourcesPanel> entry : playersResources.entrySet())
		{
			changeVisibility(i, entry.getValue());
			i++;
		}
		buttonBackBuilder.visible(false);
		buttonForwardBuilder.visible(true);
		if (numberOfPages == 1)
		{
			buttonForwardBuilder.visible(false);
		}

	}
}

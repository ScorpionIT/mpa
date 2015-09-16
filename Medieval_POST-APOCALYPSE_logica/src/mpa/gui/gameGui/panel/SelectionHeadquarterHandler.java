package mpa.gui.gameGui.panel;

import java.util.Set;

import mpa.gui.gameGui.listener.ListenerImplementation;
import mpa.gui.gameGui.playingGUI.GuiObjectManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

public class SelectionHeadquarterHandler
{
	SelectionHeadquarterPanel selectionHeadquarterPanel;
	private int windowWidth;
	private int windowHeight;
	private ListenerImplementation gameController;
	ListBox listBox = null;

	public SelectionHeadquarterHandler(int windowWidth, int windowHeight, ListenerImplementation gameController)

	{
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
		this.gameController = gameController;
		selectionHeadquarterPanel = new SelectionHeadquarterPanel(windowWidth, windowHeight, gameController);

	}

	public Element buildListBox(Nifty nifty, Screen currentScreen, Element parent)
	{
		return selectionHeadquarterPanel.build(nifty, currentScreen, parent);
	}

	public void removePlayer(String playerName)
	{

	}

	public void setListBox(Screen currentScreen)
	{
		listBox = currentScreen.findNiftyControl(selectionHeadquarterPanel.getId(), ListBox.class);
		updatePlayersName();

	}

	private void updatePlayersName()
	{
		listBox.clear();
		Set<String> playersName = gameController.getPlayersName();
		for (String playerName : playersName)
		{
			if (!playerName.equals(GuiObjectManager.getInstance().getPlayingPlayer()))
			{
				listBox.addItem(playerName);
			}
		}

	}

	public void setVisible(boolean visible)
	{
		selectionHeadquarterPanel.setVisible(visible);
	}

}

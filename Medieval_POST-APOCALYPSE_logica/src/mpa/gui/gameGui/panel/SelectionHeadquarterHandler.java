package mpa.gui.gameGui.panel;

import mpa.gui.gameGui.listener.ListenerImplementation;
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
		addPlayersName();

	}

	private void addPlayersName()
	{

	}

}

package mpa.gui.gameGui.panel;

import java.util.List;
import java.util.Set;

import mpa.gui.gameGui.listener.ListenerImplementation;
import mpa.gui.gameGui.playingGUI.GuiObjectManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

public class SelectionHeadquarterHandler
{
	SelectionHeadquarterPanel selectionHeadquarterPanel;
	private int windowWidth;
	private int windowHeight;
	private ListenerImplementation gameController;
	ListBox listBox = null;
	private TextField textFieldMinionsNumber = null;

	public SelectionHeadquarterHandler(int windowWidth, int windowHeight, ListenerImplementation gameController)

	{
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;

		int panelWidth = windowWidth * 50 / 100;
		int panelHeight = windowHeight * 50 / 100;
		int xPanel = windowWidth * 50 / 100 - panelWidth / 2;
		int yPanel = windowHeight * 50 / 100 - panelHeight / 2;
		this.gameController = gameController;
		selectionHeadquarterPanel = new SelectionHeadquarterPanel(panelWidth, panelHeight, xPanel, yPanel, gameController);

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
		if (listBox == null && textFieldMinionsNumber == null)
		{
			listBox = currentScreen.findNiftyControl(selectionHeadquarterPanel.getListBoxId(), ListBox.class);
			textFieldMinionsNumber = currentScreen.findNiftyControl(selectionHeadquarterPanel.getTextFieldId(), TextField.class);
		}
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

	public int getMinionsQuantity()
	{
		return Integer.parseInt(textFieldMinionsNumber.getText());
	}

	public String getMinionsTarget()
	{
		List selection = listBox.getSelection();
		return (String) selection.get(0);
	}

	public void setVisible(boolean visible)
	{
		selectionHeadquarterPanel.setVisible(visible);
	}

}

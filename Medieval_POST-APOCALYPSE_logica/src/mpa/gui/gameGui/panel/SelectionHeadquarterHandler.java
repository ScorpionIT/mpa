package mpa.gui.gameGui.panel;

import java.util.List;
import java.util.Set;

import mpa.gui.gameGui.listener.HandlerImplementation;
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
	private HandlerImplementation gameController;
	ListBox listBox = null;
	private TextField textFieldMinionsNumber = null;

	public SelectionHeadquarterHandler(int windowWidth, int windowHeight, HandlerImplementation gameController)

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

	public Element buildSelectionHeadquarterPanel(Nifty nifty, Screen currentScreen, Element parent)
	{
		return selectionHeadquarterPanel.build(nifty, currentScreen, parent);
	}

	public void removePlayer(String playerName)
	{

	}

	public void setListBox(Screen currentScreen)
	{

		listBox = currentScreen.findNiftyControl(selectionHeadquarterPanel.getListBoxId(), ListBox.class);
		textFieldMinionsNumber = currentScreen.findNiftyControl(selectionHeadquarterPanel.getTextFieldId(), TextField.class);
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

	public String getMinionsQuantity()
	{
		return textFieldMinionsNumber.getText();
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

	public void addSelectionHeadquarter(Nifty nifty)
	{
		Element findElementByName = nifty.getCurrentScreen().findElementByName("selectionHeadquarterLayer");
		findElementByName.add(selectionHeadquarterPanel.build(nifty, nifty.getCurrentScreen(), findElementByName));

		setListBox(nifty.getCurrentScreen());
	}

}

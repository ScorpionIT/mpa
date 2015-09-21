package mpa.gui.gameGui.panel;

import mpa.gui.gameGui.listener.HandlerImplementation;
import mpa.gui.gameGui.playingGUI.GuiObjectManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class NiftyController implements ScreenController
{

	private NiftyHandler niftyHandler;
	private HandlerImplementation gameController;

	public NiftyController(NiftyHandler niftyHandler, HandlerImplementation gameController)
	{
		this.niftyHandler = niftyHandler;
		this.gameController = gameController;

	}

	public void onClickCreateMinion()
	{
		String minionsQuantity = niftyHandler.getMinionsQuantity();

		if (!minionsQuantity.matches("\\d+"))
		{
			niftyHandler.createMessageBox("inserire un numero", "ok");

		}
		else if (niftyHandler.getMinionsTarget() == null)
		{
			niftyHandler.createMessageBox("selezionare un nemico", "ok");
		}
		else
		{

			if (!gameController.createMinions(GuiObjectManager.getInstance().getPlayingPlayer(), niftyHandler.getMinionsTarget(),
					Integer.parseInt(minionsQuantity)))
			{
				niftyHandler.createMessageBox("non hai le risorse necessarie", "ok");
			}
		}
		niftyHandler.removeHeadquarterPanel();
		niftyHandler.setCreateButtonClicked(true);

	}

	public void onClickButtonAttackTowerButton()
	{
		if (!gameController.createTowerCrusher(GuiObjectManager.getInstance().getPlayingPlayer(), niftyHandler.getSelectedObjectID()))
		{
			niftyHandler.createMessageBox("non hai le risorse necessarie", "ok");
		}
		niftyHandler.removeSelectedPanel();
		niftyHandler.setSelectionPanelVisible(true);
	}

	public void onClickBuyHPPotion()
	{
		if (!gameController.buyHPPotion(GuiObjectManager.getInstance().getPlayingPlayer()))
		{
			niftyHandler.createMessageBox("non hai le risorse necessarie", "ok");
			niftyHandler.setButtonPotionClicked(true);
		}

		// gameController.buyHPPotion(GuiObjectManager.getInstance().getPlayingPlayer());

	}

	public void onClickBuyMPPotion()
	{
		niftyHandler.setButtonPotionClicked(true);
		if (!gameController.buyMPPotion(GuiObjectManager.getInstance().getPlayingPlayer()))
			niftyHandler.createMessageBox("non hai le risorse necessarie", "ok");

	}

	public void onClickBuyGranade()
	{
		niftyHandler.setButtonPotionClicked(true);
		if (!gameController.buyGranade(GuiObjectManager.getInstance().getPlayingPlayer()))
			niftyHandler.createMessageBox("non hai le risorse necessarie", "ok");

	}

	public void onClickButtonOccupy()
	{

		gameController.occupyProperty(niftyHandler.getSelectedObject());
		niftyHandler.removeSelectedPanel();
		niftyHandler.setSelectionPanelVisible(true);
	}

	public void onClickButtonCreateTowerButton()
	{
		if (!gameController.createTower(niftyHandler.getSelectedObject()))
			niftyHandler.createMessageBox("non hai le risorse necessarie oppure non c'è spazio per costruire la torretta", "ok");

		niftyHandler.removeSelectedPanel();
		niftyHandler.setSelectionPanelVisible(true);

	}

	public void onClickButtonCreateTowerHeadquarterButton()
	{
		if (!gameController.createTower(niftyHandler.getSelectedObject()))
			niftyHandler.createMessageBox("non hai le risorse necessarie", "ok");
		niftyHandler.removeHeadquarterPanel();
		niftyHandler.setCreateButtonClicked(true);
	}

	public void onClickButtonForward()
	{

		niftyHandler.changePageOpponentResourcesPanel(false);

	}

	public void onClickButtonUpgradeLevelButton()
	{
		if (!gameController.upgradeLevel(GuiObjectManager.getInstance().getPlayingPlayer()))
			niftyHandler.createMessageBox("non hai le risorse necessarie per aumentare il tuo livello", "ok");
		niftyHandler.removeHeadquarterPanel();
		niftyHandler.setCreateButtonClicked(true);
	}

	public void onClickButtonBack()
	{
		niftyHandler.changePageOpponentResourcesPanel(true);
	}

	public void onStartScreen()
	{
	}

	public void onEndScreen()
	{
	}

	@Override
	public void bind(Nifty arg0, Screen arg1)
	{

	}
}

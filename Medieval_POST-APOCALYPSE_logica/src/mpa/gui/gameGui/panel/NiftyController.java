package mpa.gui.gameGui.panel;

import mpa.gui.gameGui.listener.ListenerImplementation;
import mpa.gui.gameGui.playingGUI.GuiObjectManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class NiftyController implements ScreenController
{

	private NiftyHandler niftyHandler;
	private ListenerImplementation gameController;

	public NiftyController(NiftyHandler niftyHandler, ListenerImplementation gameController)
	{
		this.niftyHandler = niftyHandler;
		this.gameController = gameController;

	}

	public void onClickCreateMinion()
	{
		String minionsQuantity = niftyHandler.getMinionsQuantity();
		if (minionsQuantity.matches("\\d+"))
		{

			gameController.createMinions(GuiObjectManager.getInstance().getPlayingPlayer(), niftyHandler.getMinionsTarget(),
					Integer.parseInt(minionsQuantity));
		}
		niftyHandler.removeHeadquarterPanel();
		niftyHandler.setCreateButtonClicked(true);

	}

	public void onClickButtonAttackTowerButton()
	{
		gameController.createTowerCrusher(GuiObjectManager.getInstance().getPlayingPlayer(), niftyHandler.getSelectedObjectID());
		niftyHandler.removeSelectedPanel();
	}

	public void onClickBuyHPPotion()
	{
		niftyHandler.setButtonPotionClicked(true);
		if (!gameController.buyHPPotion(GuiObjectManager.getInstance().getPlayingPlayer()))
			niftyHandler.createMessageBox("non hai le risorse necessarie", "ok");

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
		niftyHandler.setButtonOccupyClicked(true);
	}

	public void onClickButtonCreateTowerButton()
	{
		gameController.createTower(niftyHandler.getSelectedObject());
		niftyHandler.removeSelectedPanel();
		niftyHandler.setButtonOccupyClicked(true);
	}

	public void onClickButtonForward()
	{

		niftyHandler.changePageOpponentResourcesPanel(false);

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

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
		gameController.createMinions(GuiObjectManager.getInstance().getPlayingPlayer(), niftyHandler.getMinionsTarget(),
				niftyHandler.getMinionsQuantity());
		niftyHandler.removeHeadquarterPanel();

	}

	public void onClickButtonOccupy()
	{
		gameController.occupyProperty(niftyHandler.getSelectedObject());
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

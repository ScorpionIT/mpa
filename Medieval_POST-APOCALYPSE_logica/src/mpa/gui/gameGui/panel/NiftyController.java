package mpa.gui.gameGui.panel;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class NiftyController implements ScreenController
{

	private NiftyHandler niftyHandler;

	public NiftyController(NiftyHandler niftyHandler)
	{
		this.niftyHandler = niftyHandler;

	}

	public void onClickButtonOccupy()
	{

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

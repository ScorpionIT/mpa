package mpa.gui.gameGui.panel;

import mpa.gui.gameGui.playingGUI.GameGui;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class NiftyController implements ScreenController
{

	private GameGui gameGui;

	public NiftyController(GameGui gameGui)
	{
		this.gameGui = gameGui;

	}

	public void onClickButtonOccupy()
	{

		gameGui.occupy();

	}

	public void onClickButtonForward()
	{

		gameGui.forwardOpponentResourcesPanel();

	}

	public void onClickButtonBack()
	{
		gameGui.backOpponentResourcesPanel();

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

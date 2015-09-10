package mpa.gui.gameGui.listener;

import mpa.gui.gameGui.playingGUI.GameGui;

import com.jme3.input.controls.AnalogListener;
import com.jme3.math.Vector2f;
import com.jme3.system.AppSettings;

public class GameGuiMouseListener implements AnalogListener
{

	private GameGui gameGui;
	private AppSettings appSettings;

	public GameGuiMouseListener(GameGui gameGui)
	{
		this.gameGui = gameGui;
		appSettings = gameGui.getSettings();
	}

	@Override
	public void onAnalog(String name, float value, float tpf)
	{
		Vector2f mousePosition = gameGui.getInputManager().getCursorPosition();

		gameGui.getLock().lock();

		if (mousePosition.x <= 0)
		{
			if (gameGui.canClick())
			{
				gameGui.setCursorOnTheLeftEdge(true);
				gameGui.setCursorOnTheRightEdge(false);
			}
		}
		else if (mousePosition.x >= appSettings.getWidth() - 3)
		{
			if (gameGui.canClick())
			{

				gameGui.setCursorOnTheLeftEdge(false);
				gameGui.setCursorOnTheRightEdge(true);
			}
		}
		else
		{
			gameGui.setCursorOnTheLeftEdge(false);
			gameGui.setCursorOnTheRightEdge(false);
		}

		if (mousePosition.y <= 3)
		{
			if (gameGui.canClick())
			{
				gameGui.setCursorOnTheBottomEdge(true);
				gameGui.setCursorOnTheTopEdge(false);
			}
		}
		else if (mousePosition.y >= appSettings.getHeight() - 3)
		{
			if (gameGui.canClick())
			{
				gameGui.setCursorOnTheBottomEdge(false);
				gameGui.setCursorOnTheTopEdge(true);
			}
		}
		else
		{
			gameGui.setCursorOnTheBottomEdge(false);
			gameGui.setCursorOnTheTopEdge(false);
		}

		gameGui.getLock().unlock();
	}
}

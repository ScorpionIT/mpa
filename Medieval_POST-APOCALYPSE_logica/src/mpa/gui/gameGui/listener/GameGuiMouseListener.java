package mpa.gui.gameGui.listener;

import mpa.gui.gameGui.GameGui;

import com.jme3.input.controls.AnalogListener;
import com.jme3.math.Vector2f;
import com.jme3.system.AppSettings;

public class GameGuiMouseListener implements AnalogListener
{

	private GameGui gameGui;
	private AppSettings appSettings;

	public GameGuiMouseListener(GameGui gameGui, AppSettings appSettings)
	{
		this.gameGui = gameGui;
		this.appSettings = appSettings;
	}

	@Override
	public void onAnalog(String name, float value, float tpf)
	{
		Vector2f mousePosition = gameGui.getInputManager().getCursorPosition();

		gameGui.getLock().lock();

		gameGui.setText(" no edge");

		if (mousePosition.x <= 0)
		{
			gameGui.setCursorOnTheLeftEdge(true);
			gameGui.setCursorOnTheRightEdge(false);
			gameGui.setText("sono nell'x == 0");

		}
		else if (mousePosition.x >= appSettings.getWidth() - 3)
		{
			gameGui.setCursorOnTheLeftEdge(false);
			gameGui.setCursorOnTheRightEdge(true);
			gameGui.setText("sono nell'x == " + appSettings.getWidth());

		}
		else
		{
			gameGui.setCursorOnTheLeftEdge(false);
			gameGui.setCursorOnTheRightEdge(false);
		}

		if (mousePosition.y <= 3)
		{
			gameGui.setCursorOnTheBottomEdge(true);
			gameGui.setCursorOnTheTopEdge(false);
			gameGui.setText("sono nell'y == 0");

		}
		else if (mousePosition.y >= appSettings.getHeight() - 3)
		{
			gameGui.setCursorOnTheBottomEdge(false);
			gameGui.setCursorOnTheTopEdge(true);
			gameGui.setText("sono nell'y == " + appSettings.getHeight());

		}
		else
		{
			gameGui.setCursorOnTheBottomEdge(false);
			gameGui.setCursorOnTheTopEdge(false);
		}

		gameGui.getLock().unlock();
	}
}

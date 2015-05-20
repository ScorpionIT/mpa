package mpa.gui.gameGui.listener;

import mpa.gui.gameGui.GameGui;

import com.jme3.input.controls.ActionListener;

public class GameGuiKeyActionListener implements ActionListener
{

	private GameGui gameGui;

	public GameGuiKeyActionListener(GameGui gameGui)
	{
		this.gameGui = gameGui;
	}

	@Override
	public void onAction(String name, boolean keyPressed, float tpf)
	{

		if ("Tab".equals(name) && keyPressed)
		{

			gameGui.getNiftyHandler().setOpponentPropertiesPanel();

		}
		else
		{

			gameGui.getNiftyHandler().removeOpponentPropertiesPanel();

		}

	}
}

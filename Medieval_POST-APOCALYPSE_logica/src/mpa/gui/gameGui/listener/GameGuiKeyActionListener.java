package mpa.gui.gameGui.listener;

import mpa.gui.gameGui.panel.NiftyHandler;

import com.jme3.input.controls.ActionListener;

public class GameGuiKeyActionListener implements ActionListener
{

	private ListenerImplementation handler;
	private NiftyHandler niftyHandler;

	public GameGuiKeyActionListener(ListenerImplementation handler, NiftyHandler niftyHandler)
	{
		this.handler = handler;
		this.niftyHandler = niftyHandler;
	}

	@Override
	public void onAction(String name, boolean keyPressed, float tpf)
	{
		if ("pause".equals(name) && keyPressed)
		{
			System.out.println("pausa");
			handler.setPause();
		}

		if ("Tab".equals(name))
		{
			if (keyPressed)
			{
				niftyHandler.setOpponentPropertiesPanel();

			}
			else
			{
				niftyHandler.removeOpponentPropertiesPanel();
				niftyHandler.turnToPageOne();
			}

		}

	}
}

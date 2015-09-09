package mpa.gui.gameGui.listener;

import mpa.gui.gameGui.playingGUI.GameGui;

import com.jme3.input.controls.ActionListener;

public class GameGuiKeyActionListener implements ActionListener
{

	private GameGui gameGui;
	private ListenerImplementation handler;

	public GameGuiKeyActionListener( ListenerImplementation l, GameGui gameGui )
	{
		this.gameGui = gameGui;
		handler = l;
	}

	@Override
	public void onAction( String name, boolean keyPressed, float tpf )
	{
		if( "pause".equals( name ) && keyPressed )
		{
			System.out.println( "pausa" );
			handler.setPause();
		}

		if( "Tab".equals( name ) )
		{
			// if( keyPressed )
			// gameGui.getNiftyHandler().setOpponentPropertiesPanel();
			// else
			// {
			// gameGui.getNiftyHandler().removeOpponentPropertiesPanel();
			// gameGui.getNiftyHandler().turnToPageOne();
			// }

		}

	}
}

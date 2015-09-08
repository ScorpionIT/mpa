package mpa.gui.gameGui.listener;

import mpa.core.logic.GameManager;
import mpa.gui.gameGui.playingGUI.GameGui;

import com.jme3.input.controls.ActionListener;

public class GameGuiKeyActionListener implements ActionListener
{

	private GameGui gameGui;

	public GameGuiKeyActionListener( GameGui gameGui )
	{
		this.gameGui = gameGui;
	}

	@Override
	public void onAction( String name, boolean keyPressed, float tpf )
	{
		if( "pause".equals( name ) && keyPressed )
		{
			System.out.println( "pausa" );
			GameManager gm = GameManager.getInstance();
			gm.setPause();
		}

		if( GameManager.getInstance().getPauseState() )
			return;

		if( "Tab".equals( name ) )
		{
			if( keyPressed )
				gameGui.getNiftyHandler().setOpponentPropertiesPanel();
			else
			{
				gameGui.getNiftyHandler().removeOpponentPropertiesPanel();
				gameGui.getNiftyHandler().turnToPageOne();
			}

		}

	}
}

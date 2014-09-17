package mpa.gui.menu;

import java.awt.Button;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.JPanel;

import mpa.core.logic.MapManager;

@SuppressWarnings("serial")
public class MapPanel extends JPanel
{

	public MapPanel(MapManager map)
	{
		super();
		this.setBackground(Color.GREEN);
		GridLayout layout = new GridLayout(map.getHeight(),map.getWidth());
		
		this.setLayout(layout);
		
		for (int i = 0; i< map.getHeight(); i++)
		{
			for (int j = 0; j < map.getWidth(); j++)
				this.add(new Button(new String (Character.toString(map.getMap()[i][j]))));
			
		}
		
	}
	public void paintComponent (Graphics g)
	{
		super.paintComponent(g);
	
	
	}

}

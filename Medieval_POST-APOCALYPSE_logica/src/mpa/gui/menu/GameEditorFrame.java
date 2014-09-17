package mpa.gui.menu;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import mpa.core.logic.MapManager;


public class GameEditorFrame extends JFrame
{
	private static final String maps = new String ("/home/parcuri/Dropbox/Workspace_Java/MappeGioco/");
	private static MapManager map;
	private static JPanel panel;

	public GameEditorFrame()
	{
		super();
		this.panel = new GameEditorPanel();
		this.setContentPane(panel);
		this.setLocation(600, 300);
		this.setSize(500, 500);
		this.setTitle("Aru Q");
		panel.setLayout(new BorderLayout());
		
		JComboBox <String> comboBox = new GameEditorComboBox(maps);
		panel.add(comboBox, BorderLayout.NORTH);
		
		String mapPath = new String (maps);		
		
		
		 comboBox.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent event) {
	              
	                JComboBox comboBox = (JComboBox) event.getSource();

	                loadMap(comboBox.getSelectedItem().toString());

	            
	           
	            }
	        });
		 
		 
		this.setVisible(true);
		 
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}
	
	private void loadMap (String path) 
	{
		map = new MapManager(maps.concat(path));
		map.printMap();
		JPanel pane = new MapPanel (map);
		panel.add(pane, BorderLayout.CENTER);
		
		this.setVisible(true);
		
		
	}
}



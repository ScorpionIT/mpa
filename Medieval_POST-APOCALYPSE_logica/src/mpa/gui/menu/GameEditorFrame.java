package mpa.gui.menu;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import mpa.core.logic.MapManager;


@SuppressWarnings("serial")
public class GameEditorFrame extends JFrame
{
	private final String maps = new String (System.getProperty("user.home")+"/Medieval Post APOCALYPSE/MappeGioco/");
	private MapManager map;
	private JPanel panel;

	public GameEditorFrame()
	{
		super();
		this.panel = new GameEditorPanel();
		this.setContentPane(panel);
		this.setLocation(600, 300);
		this.setSize(500, 500);
		this.setTitle("Aru Q");
		File mapDir = new File(maps);
		if (!mapDir.exists())
			new File(maps).mkdirs();
			
		panel.setLayout(new BorderLayout());
		
		JComboBox <String> comboBox = new GameEditorComboBox(maps);
		panel.add(comboBox, BorderLayout.NORTH);

		 comboBox.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent event) {
	              
	                @SuppressWarnings("rawtypes")
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



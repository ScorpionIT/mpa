package mpa.gui.menuMap;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import mpa.core.logic.MapFromXMLCreator;
import mpa.core.logic.World;
import mpa.core.logic.WorldFromMapInfo;
import mpa.core.logic.WorldLoader;
import mpa.gui.menu.GameEditorComboBox;
import mpa.gui.menu.GameEditorPanel;

public class MapMenu___ extends JFrame
{
	private final String maps = new String(System.getProperty("user.home") + "/Medieval Post APOCALYPSE/MappeGioco/");
	private WorldLoader worldLoader = new WorldLoader();
	private JPanel panel;

	public MapMenu___()
	{

		super();
		this.panel = new GameEditorPanel();
		this.setContentPane(panel);
		this.setLocation(600, 300);
		this.setSize(500, 500);
		this.setTitle("Medieval Post Apocalypse");
		File mapDir = new File(maps);
		if (!mapDir.exists())
			new File(maps).mkdirs();

		panel.setLayout(new BorderLayout());

		JComboBox<String> comboBox = new GameEditorComboBox(maps);
		panel.add(comboBox, BorderLayout.NORTH);

		comboBox.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{

				@SuppressWarnings("rawtypes")
				JComboBox comboBox = (JComboBox) event.getSource();

				loadMap(comboBox.getSelectedItem().toString());

			}
		});

		this.setVisible(true);

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

	}

	private void loadMap(String mapName)
	{
		worldLoader.loadMapInfo(new MapFromXMLCreator(), maps + mapName);
		System.out.println("ho caricato la mappa info");
		System.out.println(worldLoader.getMapInfo().toString());

		World world = worldLoader.loadWorld(new WorldFromMapInfo());
		System.out.println("ho caricato il mondo");

	}

}

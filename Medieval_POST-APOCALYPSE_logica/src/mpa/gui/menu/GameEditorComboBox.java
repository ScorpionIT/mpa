package mpa.gui.menu;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JComboBox;


@SuppressWarnings("serial")
public class GameEditorComboBox extends JComboBox<String>
{

	public GameEditorComboBox(String mapsFolderPath)
	{
		super();
		File folder = new File(mapsFolderPath);
		File[] listOfFiles = folder.listFiles();
		ArrayList<String> maps = new ArrayList <String> ();
		for (int i = 0; i < listOfFiles.length; i++) 
		{
		      if (listOfFiles[i].isFile()) 
		      {
		    	  maps.add(listOfFiles[i].getName());
		      }
		}
		
		for (int i = 0; i < maps.size(); i++)
		{
			super.insertItemAt(maps.get(i),i); 
		}
	}

}

package mpa.core.logic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;

public class MapToXmlCreator
{

	public MapToXmlCreator()
	{
	}

	public void printOnFile(Element element, String path)
	{
		XMLOutputter output = new XMLOutputter();
		try
		{
			OutputStream outputStream = new FileOutputStream(new File(path + "/" + element.getChildren().get(0).getName() + ".xml"));
			output.output(element, outputStream);
		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}
}

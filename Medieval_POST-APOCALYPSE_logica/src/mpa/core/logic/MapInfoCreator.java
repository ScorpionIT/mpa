package mpa.core.logic;

import java.io.IOException;

import org.jdom2.JDOMException;

public interface MapInfoCreator
{
	public MapInfo createMapInfo( String path ) throws JDOMException, IOException;
}

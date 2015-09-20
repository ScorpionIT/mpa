package mpa.core.logic;

import java.util.ArrayList;
import java.util.List;

public class IDPool
{
	private List<Integer> avalaibleIDs = new ArrayList<>();
	private List<Integer> usedIDs = new ArrayList<>();
	private int lastGeneratedID;

	public IDPool( int numberOfIDs )
	{
		for( int i = 1; i <= numberOfIDs; i++ )
			avalaibleIDs.add( i );

		lastGeneratedID = numberOfIDs;
	}

	public String getID()
	{
		if( avalaibleIDs.isEmpty() )
			avalaibleIDs.add( lastGeneratedID++ );

		Integer id = avalaibleIDs.remove( 0 );
		usedIDs.add( id );

		return String.valueOf( id );
	}

	public void freeID( String id )
	{
		int position = 0;
		for( ; position < usedIDs.size(); position++ )
			if( usedIDs.get( position ).equals( Integer.parseInt( id ) ) )
				break;

		usedIDs.remove( position );
		avalaibleIDs.add( Integer.parseInt( id ) );
	}

}

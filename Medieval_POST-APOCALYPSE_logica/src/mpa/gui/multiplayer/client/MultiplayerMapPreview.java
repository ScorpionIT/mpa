package mpa.gui.multiplayer.client;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import mpa.core.logic.MapInfo;
import mpa.core.logic.Pair;
import mpa.core.util.GameProperties;
import mpa.gui.menuMap.MapPreview;

public class MultiplayerMapPreview extends MapPreview
{
	private static final long serialVersionUID = -6870854072749789153L;

	public MultiplayerMapPreview( JPanel menuSinglePlayerPanel, final MapInfo mapInfo )
	{
		super( menuSinglePlayerPanel );
		loadMap( mapInfo );
	}

	public void removeBorder( Pair<Float, Float> oldHQ )
	{
		for( JLabel l : headQuartersLabel.keySet() )
		{
			if( headQuartersLabel.get( l ).equals( oldHQ ) )
			{
				l.setBorder( BorderFactory.createEmptyBorder() );
				break;
			}
		}
	}

	public void addBorder( Pair<Float, Float> newHQ )
	{
		for( JLabel l : headQuartersLabel.keySet() )
		{
			if( headQuartersLabel.get( l ).equals( newHQ ) )
			{
				l.setBorder( BorderFactory.createLineBorder( Color.BLUE ) );
				break;
			}
		}
	}

	@Override
	public void loadMap( MapInfo mapInfo )
	{
		this.mapInfo = mapInfo;

		this.headQuartersLabel.clear();

		for( Pair<Float, Float> headQuarterPosition : mapInfo.getHeadQuarters() )
		{
			Image image = images.get( "headQuarter" );
			image = image.getScaledInstance(
					W( GameProperties.getInstance().getObjectWidth( "headQuarter" ) ),
					H( GameProperties.getInstance().getObjectHeight( "headQuarter" ) ), 0 );

			ImageIcon imageIcon = new ImageIcon( image );
			JLabel jLabel = new JLabel( imageIcon );
			jLabel.addMouseListener( new MouseAdapter()
			{
				@Override
				public void mouseReleased( MouseEvent e )
				{
					( ( MultiplayerMenuPanel ) MultiplayerMapPreview.this.menuMapPanel )
							.setSelectedHeadQuarter( headQuartersLabel.get( ( ( JLabel ) e
									.getSource() ) ) );
				}

			} );
			jLabel.setBounds( graphicX( headQuarterPosition.getFirst() ),
					graphicY( headQuarterPosition.getSecond() ), W( GameProperties.getInstance()
							.getObjectWidth( "headQuarter" ) ), H( GameProperties.getInstance()
							.getObjectHeight( "headQuarter" ) ) );
			this.add( jLabel );

			headQuartersLabel.put( jLabel, headQuarterPosition );
		}
	}
}

package mpa.gui.multiPlayerMenu;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import mpa.core.logic.MapInfo;
import mpa.core.logic.Pair;
import mpa.core.util.GameProperties;
import mpa.gui.menuMap.MapPreview;
import mpa.gui.menuMap.MenuSinglePlayerPanel;

public class MultiplayerMapPreview extends MapPreview
{
	private static final long serialVersionUID = -6870854072749789153L;
	private Socket socket;

	public MultiplayerMapPreview( JPanel menuSinglePlayerPanel, final MapInfo mapInfo, Socket socket )
	{
		super( menuSinglePlayerPanel );
		this.socket = socket;
		loadMap( mapInfo );
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
					try
					{
						DataOutputStream outToServer = new DataOutputStream( socket
								.getOutputStream() );
						BufferedReader inFromServer = new BufferedReader( new InputStreamReader(
								socket.getInputStream() ) );
						Set<JLabel> keys = MultiplayerMapPreview.this.headQuartersLabel.keySet();
						String pickedHeadquarter = new String();
						for( JLabel l : keys )
						{
							if( MultiplayerMapPreview.this.headQuartersLabel
									.get( l )
									.equals(
											( ( MultiplayerMenuPanel ) MultiplayerMapPreview.this.menuMapPanel )
													.getSelectedHQ() ) )
							{
								l.setBorder( BorderFactory.createEmptyBorder() );
							}

							if( l.equals( ( ( JLabel ) e.getSource() ) ) )
							{
								pickedHeadquarter = new String(
										String.valueOf( MultiplayerMapPreview.this.headQuartersLabel
												.get( l ).getFirst() )
												+ ","
												+ String.valueOf( MultiplayerMapPreview.this.headQuartersLabel
														.get( l ).getSecond() ) );
							}
						}
						outToServer.writeBytes( pickedHeadquarter + '\n' );
						String reply = inFromServer.readLine();
						if( reply.equals( "OK" ) )
							( ( MenuSinglePlayerPanel ) MultiplayerMapPreview.this.menuMapPanel )
									.setSelectedHeadQuarter( MultiplayerMapPreview.this.headQuartersLabel
											.get( e.getSource() ) );
						else
							JOptionPane.showMessageDialog( MultiplayerMapPreview.this,
									"HQ already taken", ":( ", JOptionPane.INFORMATION_MESSAGE );

					} catch( IOException e1 )
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					( ( JLabel ) e.getSource() ).setBorder( BorderFactory
							.createLineBorder( Color.red ) );

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

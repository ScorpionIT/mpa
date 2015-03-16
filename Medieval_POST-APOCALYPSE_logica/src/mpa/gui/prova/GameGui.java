package mpa.gui.prova;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

import mpa.core.logic.AbstractObject;
import mpa.core.logic.GameManager;
import mpa.core.logic.MapInfo;
import mpa.core.logic.Pair;
import mpa.core.logic.building.Headquarter;
import mpa.core.logic.building.Market;
import mpa.core.logic.character.Player;
import mpa.core.logic.resource.Cave;
import mpa.core.logic.resource.Field;
import mpa.core.logic.resource.Wood;
import mpa.gui.menuMap.MpaPanel;

public class GameGui extends MpaPanel
{

	private MapInfo mapInfo;
	private HashMap<String, Image> images = new HashMap<>();
	private HashMap<JLabel, Pair<Float, Float>> headQuartersLabel = new HashMap<>();
	private ArrayList<Pair<Integer, Integer>> path = null;
	private Pair<Float, Float> point = null;

	public GameGui()
	{
		this.setLayout( null );

		this.setBackground( Color.GREEN );
		this.setMapDimension( GameManager.getInstance().getWorld().getWidth(), GameManager
				.getInstance().getWorld().getHeight() );

		this.addMouseListener( new MouseAdapter()
		{
			@Override
			public void mouseReleased( MouseEvent e )
			{
				if( point == null )
				{
					point = new Pair<Float, Float>( new Float( worldX( e.getXOnScreen() ) ),
							new Float( worldY( e.getYOnScreen() ) ) );
					System.out.println( "Ho clickato nella posizione " + e.getXOnScreen() + " "
							+ e.getYOnScreen() );
					return;
				}
				else
				{

					System.out.println( "Ho clickato nella posizione " + worldX( e.getXOnScreen() )
							+ " " + worldY( e.getYOnScreen() ) );
					path = GameManager.getInstance().computePath( point.getFirst(),
							point.getSecond(), new Float( worldX( e.getXOnScreen() ) ),
							new Float( worldY( e.getYOnScreen() ) ) );
					System.out.println( "Ho calcolato il path" );
					point = null;
					GameGui.this.repaint();
				}
				// System.out.println("sono player " +
				// GameManager.getInstance().getPlayers().get(0).getX() + " "
				// + GameManager.getInstance().getPlayers().get(0).getY());
				//
				// path =
				// GameManager.getInstance().computePath(GameManager.getInstance().getPlayers().get(0),
				// e.getX(), e.getY());

			}
		} );
		this.setVisible( true );
	}

	private void loadImages()
	{

		try
		{
			File folder = new File( "./imagesPreview" );
			File[] listOfFiles = folder.listFiles();
			for( int i = 0; i < listOfFiles.length; i++ )
			{
				if( listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith( ".png" ) )
				{
					Image img = ImageIO.read( new File( "./imagesPreview/"
							+ listOfFiles[i].getName() ) );
					img = img.getScaledInstance( W( img.getWidth( null ) ),
							H( img.getHeight( null ) ), 0 );
					images.put(
							listOfFiles[i].getName().substring( 0,
									listOfFiles[i].getName().length() - 4 ), img );
				}
			}

		} catch( IOException e )
		{
			e.printStackTrace();
		}

	}

	@Override
	protected void paintComponent( Graphics g )
	{

		super.paintComponent( g );

		g.drawRoundRect( 0, 0, 50, 50, 5, 789 );
		// System.out.println(images.get("headQuarter").getWidth(null));
		// System.out.println(images.get("headQuarter").getHeight(null));

		HashMap<Pair<Float, Float>, ArrayList<AbstractObject>> objectX = GameManager.getInstance()
				.getWorld().getObjectX();
		Set<Pair<Float, Float>> keySet = objectX.keySet();
		for( Pair<Float, Float> pair : keySet )
		{
			for( AbstractObject obj : objectX.get( pair ) )
			{
				if( obj instanceof Wood )
					// g.drawImage(images.get("wood"), graphicX(obj.getX()), graphicY(obj.getY()),
					// images.get("wood").getWidth(null), images.get("wood")
					// .getHeight(null), this);

					g.drawRect( graphicX( obj.getX() - obj.getWidth() / 2 ), graphicY( obj.getY()
							- obj.getHeight() / 2 ), ( int ) obj.getWidth(),
							( int ) obj.getHeight() );

				else if( obj instanceof Headquarter )
				{
					// g.drawImage(images.get("headQuarter"), graphicX(obj.getX()),
					// graphicY(obj.getY()), images.get("wood").getWidth(null),
					// images.get("wood").getHeight(null), this);
					g.drawRect( graphicX( obj.getX() - obj.getWidth() / 2 ), graphicY( obj.getY()
							- obj.getHeight() / 2 ), ( int ) obj.getWidth(),
							( int ) obj.getHeight() );
					g.drawOval( graphicX( obj.getX() - obj.getWidth() / 2 ), graphicY( obj.getY()
							- obj.getHeight() / 2 ), ( int ) obj.getCollisionRay() * 2,
							( int ) obj.getCollisionRay() * 2 );

				}

				else if( obj instanceof Cave )
					// g.drawOval(graphicX(obj.getX()), graphicY(obj.getY()),
					// images.get("headQuarter").getWidth(null), images.get("headQuarter")
					// .getHeight(null));
					g.drawRect( graphicX( obj.getX() ), graphicY( obj.getY() ),
							( int ) obj.getWidth(), ( int ) obj.getHeight() );
				else if( obj instanceof Market )
					// g.drawImage(images.get("market"), graphicX(obj.getX()), graphicY(obj.getY()),
					// images.get("headQuarter").getWidth(null), images
					// .get("market").getHeight(null), this);
					g.drawRect( graphicX( obj.getX() ), graphicY( obj.getY() ),
							( int ) obj.getWidth(), ( int ) obj.getHeight() );
				else if( obj instanceof Field )
					// g.drawRect(graphicX(obj.getX()), graphicY(obj.getY()),
					// images.get("headQuarter").getWidth(null), images.get("headQuarter")
					// .getHeight(null));
					g.drawRect( graphicX( obj.getX() ), graphicY( obj.getY() ),
							( int ) obj.getWidth(), ( int ) obj.getHeight() );

			}
		}

		List<Player> players = GameManager.getInstance().getPlayers();
		for( Player player : players )
		{
			g.drawRoundRect( graphicX( player.getX() ), graphicY( player.getY() ), 10, 10, 10, 20 );
		}

		if( path != null )
		{
			System.out.println( "sono la size di path " + path.size() );
			// g.drawLine((int) GameManager.getInstance().getPlayers().get(0).getX(), (int)
			// GameManager.getInstance().getPlayers().get(0).getY(), path
			// .get(0).getFirst().intValue(), path.get(0).getSecond().intValue());
			// for (int i = 0; i < path.size() - 2; i += 2)
			for( int i = 0; i < path.size() - 1; i++ )
			{
				if( i % 2 == 0 )
					g.setColor( Color.MAGENTA );

				else
					g.setColor( Color.BLUE );
				g.drawLine( graphicX( path.get( i ).getFirst().intValue() ), graphicY( path.get( i )
						.getSecond() ), graphicX( path.get( i + 1 ).getFirst() ), graphicY( path
						.get( i + 1 ).getSecond() ) );
			}

			// System.out.println(GameManager.getInstance().getWorld().toString());
		}

	}

	@Override
	public void setBounds( int x, int y, int width, int height )
	{
		super.setBounds( x, y, width, height );
		loadImages();
	}

}

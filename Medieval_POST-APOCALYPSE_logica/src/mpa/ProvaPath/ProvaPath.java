package mpa.ProvaPath;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import mpa.core.logic.GameManager;
import mpa.core.logic.building.AbstractPrivateProperty;
import mpa.core.logic.building.Headquarter;
import mpa.core.logic.characters.Player;
import mpa.core.logic.resources.Cave;
import mpa.core.logic.resources.Field;
import mpa.core.logic.resources.Wood;
import mpa.core.util.GameProperties;
import mpa.gui.gameGui.listener.HandlerImplementation;
import mpa.gui.gameGui.listener.SinglePlayerController;
import mpa.gui.menuMap.MpaPanel;

import com.jme3.math.Vector2f;

public class ProvaPath extends MpaPanel
{

	private HashMap<String, Image> images = new HashMap<>();

	private Image backgroundImage;
	private String texturePath = GameProperties.getInstance().getPath( "TexturePath" );
	private String imagesPreviewPath = GameProperties.getInstance().getPath( "ImagesPreviewPath" );
	List<javax.vecmath.Vector2f> path = null;

	HandlerImplementation listenerImplementation = new SinglePlayerController();
	private String player = null;

	public ProvaPath( String player )
	{
		this.player = player;
		this.setLayout( null );
		try
		{
			backgroundImage = ImageIO.read( new File( texturePath + "/grass-pattern.png" ) );
		} catch( IOException e1 )
		{
			e1.printStackTrace();
		}
		this.setVisible( true );

		addMouseListener( new MouseAdapter()
		{

			@Override
			public void mouseReleased( MouseEvent e )
			{
				listenerImplementation.computePath(
						new Vector2f( worldX( e.getX() ), worldY( e.getY() ) ),
						ProvaPath.this.player );

			}

		} );
		Thread t = new Thread()
		{
			public void run()
			{
				while( true )
				{
					try
					{
						sleep( 1000 );
					} catch( InterruptedException e )
					{
						// TODO Blocco catch generato automaticamente
						e.printStackTrace();
					}
					ProvaPath.this.updateUI();
				}
			};
		};
		t.start();

	}

	private void loadImages()
	{

		try
		{
			File folder = new File( imagesPreviewPath );
			File[] listOfFiles = folder.listFiles();
			for( int i = 0; i < listOfFiles.length; i++ )
			{
				if( listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith( ".png" ) )
				{
					Image img = ImageIO.read( new File( imagesPreviewPath + "/"
							+ listOfFiles[i].getName() ) );
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
		setBackgroundPatternImage( g );

		System.out.println( "dimension panel " + this.getWidth() );
		System.out.println( "dimension panel " + this.getHeight() );

		List<AbstractPrivateProperty> allObjects = GameManager.getInstance().getWorld()
				.getAllObjects();
		// System.out.println(allObjects);
		for( AbstractPrivateProperty abstractPrivateProperty : allObjects )
		{
			float xUpperLeft = abstractPrivateProperty.getX() - abstractPrivateProperty.getWidth()
					/ 2;
			float yUpperLeft = abstractPrivateProperty.getY() - abstractPrivateProperty.getHeight()
					/ 2;

			float xUpperLeftCollisionRay = abstractPrivateProperty.getX()
					- abstractPrivateProperty.getCollisionRay();
			float yUpperLeftCollisionRay = abstractPrivateProperty.getY()
					- abstractPrivateProperty.getCollisionRay();

			if( abstractPrivateProperty instanceof Wood )
			{
				// System.out.println(woodPosition.getFirst() + " " + woodPosition.getSecond());
				g.drawImage( images.get( "wood" ), graphicX( xUpperLeft ), graphicY( yUpperLeft ),
						W( GameProperties.getInstance().getObjectWidth( "wood" ) ),
						H( GameProperties.getInstance().getObjectHeight( "wood" ) ), this );
				g.drawOval( graphicX( xUpperLeftCollisionRay ), graphicY( yUpperLeftCollisionRay ),
						W( abstractPrivateProperty.getCollisionRay() * 2 ),
						H( abstractPrivateProperty.getCollisionRay() * 2 ) );
				// g.drawImage(images.get("wood"), graphicX(woodPosition.getFirst()),
				// graphicY(woodPosition.getSecond()), images.get("wood").getWidth(null),
				// images.get("wood").getHeight(null), this);
			}
			if( abstractPrivateProperty instanceof Field )
			{

				g.drawImage( images.get( "field" ), graphicX( xUpperLeft ), graphicY( yUpperLeft ),
						W( GameProperties.getInstance().getObjectWidth( "field" ) ),
						H( GameProperties.getInstance().getObjectHeight( "field" ) ), this );
				g.drawOval( graphicX( xUpperLeftCollisionRay ), graphicY( yUpperLeftCollisionRay ),
						W( abstractPrivateProperty.getCollisionRay() * 2 ),
						H( abstractPrivateProperty.getCollisionRay() * 2 ) );
				// g.drawImage(images.get("field"), graphicX(fieldPosition.getFirst()),
				// graphicY(fieldPosition.getSecond()),
				// images.get("field").getWidth(null), images.get("field").getHeight(null), this);
			}
			if( abstractPrivateProperty instanceof Cave )
			{
				g.drawImage( images.get( "cave" ), graphicX( xUpperLeft ), graphicY( yUpperLeft ),
						W( GameProperties.getInstance().getObjectWidth( "cave" ) ),
						H( GameProperties.getInstance().getObjectHeight( "cave" ) ), this );
				g.drawOval( graphicX( xUpperLeftCollisionRay ), graphicY( yUpperLeftCollisionRay ),
						W( abstractPrivateProperty.getCollisionRay() * 2 ),
						H( abstractPrivateProperty.getCollisionRay() * 2 ) );
				// g.drawImage(images.get("cave"), graphicX(cavesPosition.getFirst()),
				// graphicY(cavesPosition.getSecond()), images.get("cave")
				// .getWidth(null), images.get("cave").getHeight(null), this);
			}
			if( abstractPrivateProperty instanceof Headquarter )
			{
				g.drawImage( images.get( "headQuarter" ), graphicX( xUpperLeft ),
						graphicY( yUpperLeft ),
						W( GameProperties.getInstance().getObjectWidth( "headQuarter" ) ),
						H( GameProperties.getInstance().getObjectHeight( "headQuarter" ) ), this );
				g.drawOval( graphicX( xUpperLeftCollisionRay ), graphicY( yUpperLeftCollisionRay ),
						W( abstractPrivateProperty.getCollisionRay() * 2 ),
						H( abstractPrivateProperty.getCollisionRay() * 2 ) );
			}
		}
		// g.drawImage(images.get("market"), graphicX(mapInfo.getMarket().getFirst()),
		// graphicY(mapInfo.getMarket().getSecond()),
		// images.get("headQuarter").getWidth(null), images.get("market").getHeight(null), this);

		List<Player> players = GameManager.getInstance().getPlayers();
		for( Player player : players )
		{
			if( player.getName().equals( this.player ) )
			{
				path = player.getPath();
			}
			float xUpperLeft = player.getX() - player.getWidth() / 2;
			float ypperLeft = player.getY() - player.getHeight() / 2;

			g.fillOval( graphicX( xUpperLeft ), graphicY( ypperLeft ),
					graphicX( player.getWidth() ), graphicX( player.getHeight() ) );
		}

		if( path != null )
		{
			System.out.println( "sono la size di path " + path.size() );

			for( int i = 0; i < path.size() - 1; i++ )
			{
				if( i % 2 == 0 )
					g.setColor( Color.MAGENTA );
				else
					g.setColor( Color.ORANGE );

				g.drawLine( graphicX( ( int ) path.get( i ).x ),
						graphicY( ( int ) path.get( i ).y ),
						graphicX( ( int ) path.get( i + 1 ).x ),
						graphicY( ( int ) path.get( i + 1 ).y ) );
			}
		}

	}

	@Override
	public void setBounds( int x, int y, int width, int height )
	{
		super.setBounds( x, y, width, height );
		setMapDimension( GameManager.getInstance().getWorld().getWidth(), GameManager.getInstance()
				.getWorld().getHeight() );

		this.updateUI();
	}

	private void setBackgroundPatternImage( Graphics g )
	{
		int backgroundWidth = backgroundImage.getWidth( this );
		int backgroundHeight = backgroundImage.getWidth( this );
		int maxI = this.getWidth() / backgroundWidth;
		int maxJ = this.getHeight() / backgroundHeight;

		for( int i = 0; i <= maxI; i++ )
		{
			for( int j = 0; j <= maxJ; j++ )
			{
				g.drawImage( backgroundImage, i * backgroundWidth, j * backgroundHeight,
						backgroundWidth, backgroundHeight, this );
			}
		}

	}

	@Override
	public void setMapDimension( float width, float height )
	{

		super.setMapDimension( width, height );
		loadImages();
	}

}

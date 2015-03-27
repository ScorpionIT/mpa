package mpa.gui.gameGui;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

import mpa.core.logic.AbstractObject;
import mpa.core.logic.GameManager;
import mpa.core.logic.building.House;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.font.BitmapText;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

public class GameGui extends SimpleApplication
{
	private ReentrantLock lock = new ReentrantLock();
	boolean cursorOnTheRightEdge = false;
	boolean cursorOnTheLeftEdge = false;
	boolean cursorOnTheTopEdge = false;
	boolean cursorOnTheBottomEdge = false;
	private ArrayList<AbstractObject> staticObjects = new ArrayList<>();
	BitmapText text;

	@Override
	public void simpleInitApp()
	{
		guiNode.detachAllChildren();

		guiFont = assetManager.loadFont( "Interface/Fonts/Default.fnt" );

		text = new BitmapText( guiFont, false );

		text.setSize( guiFont.getCharSet().getRenderedSize() );

		text.setText( "Mouse x = " + inputManager.getCursorPosition().x + " y =  "
				+ inputManager.getCursorPosition().y );

		text.setLocalTranslation( 300, text.getLineHeight() + 200, 0 );

		guiNode.attachChild( text );

		flyCam.setEnabled( false );

		inputManager.setCursorVisible( true );

		DirectionalLight dl = new DirectionalLight();
		dl.setDirection( new Vector3f( -0.8f, -0.6f, -0.08f ).normalizeLocal() );
		dl.setColor( new ColorRGBA( 0.9f, 0.9f, 0.9f, 1.0f ) );
		rootNode.addLight( dl );

		AmbientLight al = new AmbientLight();
		al.setColor( new ColorRGBA( 0.7f, 0.9f, 1.5f, 1.0f ) );
		rootNode.addLight( al );

		cam.setLocation( new Vector3f( 25, 100, 25 ) );
		cam.lookAt( new Vector3f( 25, 0, 25 ), new Vector3f( 0, 1, 0 ) );

		rootNode.attachChild( makeFloor() );

		loadStaticObjects();

		inputManager.addMapping( "Shift_Map_Negative_X", new MouseAxisTrigger( MouseInput.AXIS_X,
				true ) );

		inputManager.addMapping( "Shift_Map_Positive_X", new MouseAxisTrigger( MouseInput.AXIS_X,
				false ) );
		inputManager.addMapping( "Shift_Map_Negative_Y", new MouseAxisTrigger( MouseInput.AXIS_Y,
				true ) );
		inputManager.addMapping( "Shift_Map_Positive_Y", new MouseAxisTrigger( MouseInput.AXIS_Y,
				false ) );
		inputManager.addListener( mouseActionListener, "Shift_Map_Negative_X",
				"Shift_Map_Positive_X", "Shift_Map_Negative_Y", "Shift_Map_Positive_Y" );

		new GraphicUpdater( this ).start();
	}

	private void loadStaticObjects()
	{
		ArrayList<AbstractObject> allObjects = GameManager.getInstance().getWorld().getAllObjects();

		Material mat1 = new Material( assetManager, "Common/MatDefs/Misc/Unshaded.j3md" );
		mat1.setColor( "Color", ColorRGBA.Blue );

		Material mat2 = new Material( assetManager, "Common/MatDefs/Misc/Unshaded.j3md" );
		mat2.setColor( "Color", ColorRGBA.Red );

		assetManager.registerLocator( "assets/Models/house.zip", ZipLocator.class );
		for( AbstractObject object : allObjects )
		{
			if( object instanceof House )
			{
				// Box box1 = new Box( object.getWidth() / 2, 0, object.getHeight() / 2 );
				// System.err.println( "dimensioni casa : " + object.getWidth() / 2 + " , "
				// + object.getHeight() / 2 );
				// Geometry blue = new Geometry( "House " + i++, box1 );
				// blue.setLocalTranslation( new Vector3f( object.getX(), 0, object.getY() ) );
				// System.out.println( blue.getLocalTransform() );
				//
				// if( i % 2 == 0 )
				// blue.setMaterial( mat1 );
				// else
				// blue.setMaterial( mat2 );

				Spatial loadModel = assetManager.loadModel( "house.scene" );
				loadModel.setLocalTranslation( new Vector3f( object.getX(), 0, object.getY() ) );

				rootNode.attachChild( loadModel );
			}

		}
	}

	public void setText( String text )
	{
		this.text.setText( "Mouse x = " + inputManager.getCursorPosition().x + " y =  "
				+ inputManager.getCursorPosition().y + " " + text );

	}

	protected Geometry makeFloor()
	{
		Box box = new Box( GameManager.getInstance().getWorld().getWidth() / 2, 0, GameManager
				.getInstance().getWorld().getHeight() / 2 );
		System.err.println( GameManager.getInstance().getWorld().getWidth() / 2 );
		System.err.println( GameManager.getInstance().getWorld().getHeight() / 2 );
		Geometry floor = new Geometry( "the Floor", box );
		floor.setLocalTranslation( GameManager.getInstance().getWorld().getWidth() / 2, 0,
				GameManager.getInstance().getWorld().getHeight() / 2 );
		Material mat1 = new Material( assetManager, "Common/MatDefs/Misc/Unshaded.j3md" );
		mat1.setColor( "Color", ColorRGBA.Green );
		floor.setMaterial( mat1 );
		return floor;
	}

	private AnalogListener mouseActionListener = new AnalogListener()
	{

		@Override
		public void onAnalog( String name, float value, float tpf )
		{
			Vector2f mousePosition = inputManager.getCursorPosition();

			lock.lock();

			setText( " no edge" );

			if( mousePosition.x <= 0 )
			{
				cursorOnTheLeftEdge = true;
				cursorOnTheRightEdge = false;
				setText( "sono nell'x == 0" );

			}
			else if( mousePosition.x >= settings.getWidth() - 3 )
			{
				cursorOnTheRightEdge = true;
				cursorOnTheLeftEdge = false;
				setText( "sono nell'x == " + settings.getWidth() );

			}
			else
			{
				cursorOnTheLeftEdge = false;
				cursorOnTheRightEdge = false;
			}

			if( mousePosition.y <= 3 )
			{
				cursorOnTheBottomEdge = true;
				cursorOnTheTopEdge = false;
				setText( "sono nell'y == 0" );

			}
			else if( mousePosition.y >= settings.getHeight() - 3 )
			{
				cursorOnTheTopEdge = true;
				cursorOnTheBottomEdge = false;
				setText( "sono nell'y == " + settings.getHeight() );

			}
			else
			{
				cursorOnTheBottomEdge = false;
				cursorOnTheTopEdge = false;
			}

			lock.unlock();
		}
	};

	public void takeLock()
	{
		lock.lock();
	}

	public void leaveLock()
	{
		lock.unlock();
	}

	public void setCamera( Vector3f newPosition )
	{
		cam.setLocation( newPosition );
	}

	public Vector3f getCamPosition()
	{
		return cam.getLocation();
	}

	@Override
	public void simpleUpdate( float tpf )
	{
		super.simpleUpdate( tpf );
	}

}

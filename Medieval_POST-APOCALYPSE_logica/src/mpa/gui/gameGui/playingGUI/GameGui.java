package mpa.gui.gameGui.playingGUI;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import mpa.core.logic.GameManager;
import mpa.gui.gameGui.listener.GameGuiMouseListener;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.input.controls.AnalogListener;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;

public class GameGui extends SimpleApplication implements AnimEventListener
{
	private ReentrantLock lock = new ReentrantLock();
	boolean cursorOnTheRightEdge = false;
	boolean cursorOnTheLeftEdge = false;
	boolean cursorOnTheTopEdge = false;
	boolean cursorOnTheBottomEdge = false;
	private float cameraHeight = 220;// 120
	// private float scalingFactor = 2.2f;
	private float lz = ( float ) Math.sqrt( Math.pow( cameraHeight / Math.sin( 40 ), 2 )
			- Math.pow( cameraHeight, 2 ) );

	private Node groundNode;
	private Node mobileObjects = new Node( "Mobile Objects" );
	private Node staticObjects = new Node( "Static Objects" );

	// NiftyHandler niftyHandler;
	private AnalogListener mouseActionListener;

	public GameGui()
	{
		super();
	}

	public AppSettings getSettings()
	{
		return settings;
	}

	@Override
	public void simpleInitApp()
	{
		initialCameraConfiguration();
		setLights();

		// niftyHandler = new NiftyHandler( assetManager, inputManager, audioRenderer, guiViewPort,
		// stateManager, this );
		setCamera( new Vector3f( 250, cameraHeight, 250 ) );
		updateResourcePanel();

		groundNode = new Node( "Ground" );
		groundNode.attachChild( makeFloor() );
		rootNode.attachChild( groundNode );
		GuiObjectManager.init( this );

		mouseActionListener = new GameGuiMouseListener( this );
		inputManager.addListener( mouseActionListener, "Shift_Map_Negative_X",
				"Shift_Map_Positive_X", "Shift_Map_Negative_Y", "Shift_Map_Positive_Y" );

		new GraphicUpdater( this ).start();
	}

	void attachPlayer( Spatial model )
	{
		mobileObjects.attachChild( model );
	}

	void detachPlayer( Spatial model )
	{
		mobileObjects.detachChild( model );
	}

	void attachStaticObject( Spatial model )
	{
		staticObjects.attachChild( model );
	}

	void detachStaticObject( Spatial model )
	{
		staticObjects.detachChild( model );
	}

	protected Geometry makeFloor()
	{
		Box box = new Box( GameManager.getInstance().getWorld().getWidth() / 2, 0, GameManager
				.getInstance().getWorld().getHeight() / 2 );

		Geometry floor = new Geometry( "the Floor", box );
		floor.setLocalTranslation( GameManager.getInstance().getWorld().getWidth() / 2, 0,
				GameManager.getInstance().getWorld().getHeight() / 2 );

		assetManager.registerLocator( "Assets/Textures/", FileLocator.class );
		Material mat1 = new Material( assetManager, "Common/MatDefs/Misc/Unshaded.j3md" );
		Texture text1 = assetManager.loadTexture( "grass-pattern.png" );
		floor.getMesh().scaleTextureCoordinates( new Vector2f( 20, 20 ) );
		text1.setWrap( WrapMode.Repeat );

		mat1.setTexture( "ColorMap", text1 );
		floor.setMaterial( mat1 );
		return floor;
	}

	private void setLights()
	{
		DirectionalLight dl = new DirectionalLight();
		dl.setDirection( new Vector3f( -0.8f, -0.6f, -0.08f ).normalizeLocal() );
		// dl.setColor( new ColorRGBA( 0.9f, 0.9f, 0.9f, 1.0f ) );
		rootNode.addLight( dl );

		AmbientLight al = new AmbientLight();
		// al.setColor( new ColorRGBA( 0.7f, 0.9f, 1.5f, 1.0f ) );
		rootNode.addLight( al );
	}

	public void setCamera( Vector3f newPosition )
	{
		// if( newPosition.z + lz < 0 )
		// return;
		// if( newPosition.x < -1
		// || newPosition.x > GameManager.getInstance().getWorld().getWidth() + 10
		// || newPosition.z > GameManager.getInstance().getWorld().getHeight() )
		// return;

		// if (niftyHandler.isVisibleSelectionPanel())
		// niftyHandler.removeSelectedPanel();
		cam.setLocation( newPosition );
		cam.lookAt( new Vector3f( newPosition.x, 0, newPosition.z + lz ), new Vector3f( 0, 1, 0 ) );

	}

	public Vector3f getCamPosition()
	{
		return cam.getLocation();
	}

	private void initialCameraConfiguration()
	{
		cam.clearViewportChanged();
		flyCam.setEnabled( false );
		inputManager.setCursorVisible( true );
	}

	public void takeLock()
	{
		lock.lock();
	}

	public void leaveLock()
	{
		lock.unlock();
	}

	private int count = 0;

	@Override
	public void simpleUpdate( float tpf )
	{
		if( count == 100 )
		{
			updateResourcePanel();
			count = 0;
		}
		else
			count++;
		// super.simpleUpdate( tpf );
	}

	public Vector3f getCameraLookAt()
	{
		return cam.getDirection();
	}

	// public NiftyHandler getNiftyHandler()
	// {
	// return niftyHandler;
	// }

	public void updateResourcePanel()
	{
		HashMap<String, Integer> resources = GameManager.getInstance().getPlayers().get( 0 )
				.getResources();

		Set<String> keySet = resources.keySet();
		for( String key : keySet )
		{
			// System.out.println();
			// System.out.println();
			// System.out.println();
			// System.out.println(niftyHandler);
			// System.out.println();
			// System.out.println();
			// System.out.println();
			// System.out.println();
			// niftyHandler.setResourceValue( key, resources.get( key ) );
		}

	}

	public Node getGroundNode()
	{
		return groundNode;
	}

	public ReentrantLock getLock()
	{
		return lock;
	}

	public void setCursorOnTheBottomEdge( boolean cursorOnTheBottomEdge )
	{
		this.cursorOnTheBottomEdge = cursorOnTheBottomEdge;
	}

	public void setCursorOnTheLeftEdge( boolean cursorOnTheLeftEdge )
	{
		this.cursorOnTheLeftEdge = cursorOnTheLeftEdge;
	}

	public void setCursorOnTheRightEdge( boolean cursorOnTheRightEdge )
	{
		this.cursorOnTheRightEdge = cursorOnTheRightEdge;
	}

	public void setCursorOnTheTopEdge( boolean cursorOnTheTopEdge )
	{
		this.cursorOnTheTopEdge = cursorOnTheTopEdge;
	}

	@Override
	public void onAnimChange( AnimControl arg0, AnimChannel arg1, String arg2 )
	{
		// TODO Stub di metodo generato automaticamente

	}

	@Override
	public void onAnimCycleDone( AnimControl arg0, AnimChannel arg1, String arg2 )
	{
		// TODO Stub di metodo generato automaticamente

	}

	// public void forwardOpponentResourcesPanel()
	// {
	// niftyHandler.changePageOpponentResourcesPanel( false );
	//
	// }
	//
	// public void backOpponentResourcesPanel()
	// {
	// niftyHandler.changePageOpponentResourcesPanel( true );
	//
	// }

	public int windowWidth()
	{
		return settings.getWidth();
	}

	public int windowHeight()
	{
		return settings.getHeight();
	}
}

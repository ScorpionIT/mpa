package mpa.gui.gameGui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import mpa.core.logic.AbstractObject;
import mpa.core.logic.GameManager;
import mpa.core.logic.building.AbstractProperty;
import mpa.core.logic.building.House;
import mpa.core.logic.building.Market;
import mpa.core.logic.character.Player;
import mpa.core.logic.resource.Cave;
import mpa.core.logic.resource.Field;
import mpa.core.logic.resource.Resources;
import mpa.core.logic.resource.Wood;
import mpa.gui.gameGui.listener.GameGuiClickListener;
import mpa.gui.gameGui.listener.GameGuiKeyActionListener;
import mpa.gui.gameGui.listener.GameGuiMouseListener;
import mpa.gui.gameGui.panel.NiftyHandler;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.util.TangentBinormalGenerator;

public class GameGui extends SimpleApplication implements AnimEventListener
{
	private ReentrantLock lock = new ReentrantLock();
	boolean cursorOnTheRightEdge = false;
	boolean cursorOnTheLeftEdge = false;
	boolean cursorOnTheTopEdge = false;
	boolean cursorOnTheBottomEdge = false;
	private float cameraHeight = 500;// 120
	private float lz = ( float ) Math.sqrt( Math.pow( cameraHeight / Math.sin( 40 ), 2 )
			- Math.pow( cameraHeight, 2 ) );

	private Node groundNode;
	private Node mobileObjects = new Node( "Mobile Objects" );
	private Node pathNodes = new Node();
	private List<Player> players = GameManager.getInstance().getPlayers();
	private int playerIndex;

	private ArrayList<javax.vecmath.Vector2f> path = new ArrayList<>();
	BitmapText text;
	NiftyHandler niftyHandler;

	private AnalogListener clickActionListener;
	private AnalogListener mouseActionListener;
	private ActionListener keyActionListener;

	public GameGui( int playerIndex )
	{
		super();
		this.playerIndex = playerIndex;
	}

	@Override
	public void simpleInitApp()
	{
		setDebuggingText();
		initialCameraConfiguration();
		setLights();

		niftyHandler = new NiftyHandler( assetManager, inputManager, audioRenderer, guiViewPort,
				stateManager, this );
		setCamera( new Vector3f( 250, cameraHeight, 250 ) );
		updateResourcePanel();

		groundNode = new Node( "Ground" );
		groundNode.attachChild( makeFloor() );
		rootNode.attachChild( groundNode );

		loadCharacters();
		rootNode.attachChild( mobileObjects );
		rootNode.attachChild( pathNodes );

		loadStaticObjects();
		clickActionListener = new GameGuiClickListener( this );
		mouseActionListener = new GameGuiMouseListener( this, settings );
		keyActionListener = new GameGuiKeyActionListener( this );
		setEventTriggers();

		new GraphicUpdater( this ).start();
	}

	private void debuggingPath()
	{
		pathNodes.detachAllChildren();
		for( int i = 0; i < players.size(); i++ )
			if( i != playerIndex )
			{
				players.get( i ).getReadLock();
				ArrayList<javax.vecmath.Vector2f> path2 = players.get( i ).getPath();

				javax.vecmath.Vector2f vector2f;
				if( path2.size() > 1 )
				{
					vector2f = path2.get( path2.size() - 1 );

					Sphere sphereMesh;
					Geometry sphereGeo;

					sphereMesh = new Sphere( 32, 32, 2f );
					sphereGeo = new Geometry( "" + i, sphereMesh );

					sphereMesh.setTextureMode( Sphere.TextureMode.Projected ); // better quality on
					// spheres
					TangentBinormalGenerator.generate( sphereMesh ); // for lighting effect
					Material sphereMat = new Material( assetManager,
							"Common/MatDefs/Light/Lighting.j3md" );
					sphereMat.setTexture( "DiffuseMap",
							assetManager.loadTexture( "Textures/Terrain/Pond/Pond.jpg" ) );
					sphereMat.setTexture( "NormalMap",
							assetManager.loadTexture( "Textures/Terrain/Pond/Pond_normal.png" ) );
					sphereMat.setBoolean( "UseMaterialColors", true );
					sphereMat.setColor( "Diffuse", ColorRGBA.White );
					sphereMat.setColor( "Specular", ColorRGBA.White );
					sphereMat.setFloat( "Shininess", 64f ); // [0,128]
					sphereGeo.setMaterial( sphereMat );

					sphereGeo.rotate( 1.6f, 0, 0 ); // Rotate it a bit

					sphereGeo.setLocalTranslation( vector2f.x, 5, vector2f.y );
					pathNodes.attachChild( sphereGeo );
				}

				players.get( i ).leaveReadLock();

			}
	}

	private void setDebuggingText()
	{
		guiNode.detachAllChildren();
		guiFont = assetManager.loadFont( "Interface/Fonts/Default.fnt" );
		text = new BitmapText( guiFont, false );
		text.setSize( guiFont.getCharSet().getRenderedSize() );
		text.setText( "Mouse x = " + inputManager.getCursorPosition().x + " y =  "
				+ inputManager.getCursorPosition().y );
		text.setLocalTranslation( 300, text.getLineHeight() + 200, 0 );
		guiNode.attachChild( text );
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

	private void loadStaticObjects()
	{
		ArrayList<AbstractObject> allObjects = GameManager.getInstance().getWorld().getAllObjects();

		Material mat1 = new Material( assetManager, "Common/MatDefs/Misc/Unshaded.j3md" );
		mat1.setColor( "Color", ColorRGBA.Blue );

		Material mat2 = new Material( assetManager, "Common/MatDefs/Misc/Unshaded.j3md" );
		mat2.setColor( "Color", ColorRGBA.Red );

		int i = 0;
		for( AbstractObject object : allObjects )
		{
			if( object instanceof House )
			{
				assetManager.registerLocator( "Assets/Models/house.zip", ZipLocator.class );
				Spatial loadModel = assetManager.loadModel( "house.scene" );
				loadModel.setLocalTranslation( new Vector3f( object.getX(), 0, object.getY() ) );
				float cos = FastMath.cos( 130 + 180 );
				float sin = FastMath.sin( 130 + 180 );
				loadModel.setLocalRotation( new Matrix3f( cos, 0, sin, 0, 1, 0, -sin, 0, cos ) );
				loadModel.scale( 2f );
				rootNode.attachChild( loadModel );
			}
			else if( object instanceof Field )
			{

				assetManager.registerLocator( "Assets/Models/CornField.zip", ZipLocator.class );

				Spatial loadModel = assetManager.loadModel( "CornField.mesh.xml" );
				loadModel.setLocalTranslation( new Vector3f( object.getX(), 0, object.getY() ) );
				float cos = FastMath.cos( 90 );
				float sin = FastMath.sin( 90 );
				loadModel.setLocalRotation( new Matrix3f( 1, 0, 0, 0, cos, -sin, 0, sin, cos ) );
				loadModel.scale( 6f );
				rootNode.attachChild( loadModel );
			}

			else if( object instanceof Cave )
			{

				assetManager.registerLocator( "Assets/Models/stones.zip", ZipLocator.class );

				Spatial loadModel = assetManager.loadModel( "stones.mesh.xml" );
				loadModel.setLocalTranslation( new Vector3f( object.getX(), 0, object.getY() ) );
				float cos = FastMath.cos( 90 );
				float sin = FastMath.sin( 90 );
				loadModel.setLocalRotation( new Matrix3f( 1, 0, 0, 0, cos, -sin, 0, sin, cos ) );
				loadModel.scale( 3f );
				rootNode.attachChild( loadModel );
			}
			else if( object instanceof Market )
			{
				assetManager.registerLocator( "Assets/Models/wagen.zip", ZipLocator.class );

				Spatial loadModel = assetManager.loadModel( "wagen.mesh.xml" );
				loadModel.setLocalTranslation( new Vector3f( object.getX(), 0, object.getY() ) );

				loadModel.scale( 0.1f );
				rootNode.attachChild( loadModel );

			}

			else if( object instanceof Wood )
			{
				assetManager.registerLocator( "Assets/Models/Wood.zip", ZipLocator.class );

				Spatial loadModel = assetManager.loadModel( "Tree.mesh.xml" );
				loadModel.setLocalTranslation( new Vector3f( object.getX(), 0, object.getY() ) );
				float cos = FastMath.cos( 90 );
				float sin = FastMath.sin( 90 );
				loadModel.setLocalRotation( new Matrix3f( 1, 0, 0, 0, cos, -sin, 0, sin, cos ) );
				loadModel.scale( 6f );
				rootNode.attachChild( loadModel );

			}

			if( object instanceof AbstractProperty )
			{
				javax.vecmath.Vector2f gatheringPlace = ( ( AbstractProperty ) object )
						.getGatheringPlace();

				Sphere sphereMesh;
				Geometry sphereGeo;

				sphereMesh = new Sphere( 50, 50, 8f );
				sphereGeo = new Geometry( "" + i++, sphereMesh );

				sphereMesh.setTextureMode( Sphere.TextureMode.Projected ); // better quality on
				// spheres
				TangentBinormalGenerator.generate( sphereMesh ); // for lighting effect
				Material sphereMat = new Material( assetManager,
						"Common/MatDefs/Light/Lighting.j3md" );
				sphereMat.setTexture( "DiffuseMap",
						assetManager.loadTexture( "Textures/Terrain/Pond/Pond.jpg" ) );
				sphereMat.setTexture( "NormalMap",
						assetManager.loadTexture( "Textures/Terrain/Pond/Pond_normal.png" ) );
				sphereMat.setBoolean( "UseMaterialColors", true );
				sphereMat.setColor( "Diffuse", ColorRGBA.White );
				sphereMat.setColor( "Specular", ColorRGBA.White );
				sphereMat.setFloat( "Shininess", 64f ); // [0,128]
				sphereGeo.setMaterial( sphereMat );

				sphereGeo.rotate( 1.6f, 0, 0 ); // Rotate it a bit

				sphereGeo.setLocalTranslation( gatheringPlace.x, 5, gatheringPlace.y );
				pathNodes.attachChild( sphereGeo );

			}

		}
	}

	public void setText( String text )
	{
		this.text.setText( "Mouse x = " + inputManager.getCursorPosition().x + " y =  "
				+ inputManager.getCursorPosition().y + " " + text );

	}

	private void setEventTriggers()
	{
		inputManager.addMapping( "Shift_Map_Negative_X", new MouseAxisTrigger( MouseInput.AXIS_X,
				true ) );

		inputManager.addMapping( "Shift_Map_Positive_X", new MouseAxisTrigger( MouseInput.AXIS_X,
				false ) );
		inputManager.addMapping( "Shift_Map_Negative_Y", new MouseAxisTrigger( MouseInput.AXIS_Y,
				true ) );
		inputManager.addMapping( "Shift_Map_Positive_Y", new MouseAxisTrigger( MouseInput.AXIS_Y,
				false ) );

		inputManager.addMapping( "Tab", new KeyTrigger( KeyInput.KEY_TAB ) );
		inputManager.addListener( keyActionListener, "Tab" );

		inputManager.addListener( mouseActionListener, "Shift_Map_Negative_X",
				"Shift_Map_Positive_X", "Shift_Map_Negative_Y", "Shift_Map_Positive_Y" );

		inputManager.addMapping( "Click", new MouseButtonTrigger( 0 ) );
		inputManager.addListener( clickActionListener, "Click" );

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

	private void loadCharacters()
	{
		int i = 0;
		assetManager.registerLocator( "./Assets/Models/SeanDevlin.zip", ZipLocator.class );
		for( Player player : players )
		{
			Spatial model = assetManager.loadModel( "SeanDevlin.mesh.xml" );
			if( i == playerIndex )
			{
				model.scale( 15.2f, 15.2f, 15.2f );
				model.setLocalTranslation( new Vector3f( player.getX(), 95, player.getY() ) );
			}
			else
			{
				model.scale( 15.2f, 15.2f, 15.2f );
				// model.scale(10.2f, 10.2f, 10.2f);
				model.setLocalTranslation( new Vector3f( player.getX(), -20, player.getY() ) );

			}
			i++;
			mobileObjects.attachChild( model );

			Quaternion quad = new Quaternion();
			quad.fromAngleAxis( player.getRotationAngle(), new Vector3f( 0, 1, 0 ) );
			model.setLocalRotation( quad );
		}
	}

	private void updateMobileObjects()
	{
		players = GameManager.getInstance().getPlayers();

		int i = 0;
		AnimControl control;
		AnimChannel channel = null;
		for( Player p : players )
		{
			Spatial mobObject = mobileObjects.getChild( i );
			// if( i == 0 )
			// if( p.getPath() != null && !p.getPath().isEmpty() )
			// {
			//
			// control = mobObject.getControl( AnimControl.class );
			// control.clearListeners();
			// control.addListener( this );
			// channel = control.createChannel();
			// channel.setAnim( "WALK", 0.80f );
			// channel.setLoopMode( LoopMode.Loop );
			//
			// }
			// else
			// {
			// // if (channel != null)
			// // channel.setAnim(null);
			//
			// }

			mobObject.setLocalTranslation( p.getX(), -10, p.getY() );

			float rotationAngle = p.getRotationAngle();
			if( rotationAngle != 0f )
			{
				Quaternion quad = new Quaternion();
				quad.fromAngleAxis( rotationAngle, new Vector3f( 0, 1, 0 ) );
				mobObject.setLocalRotation( quad );
			}

			i++;
		}
		// debuggingPath();
	}

	public void drawPath()
	{
		pathNodes.detachAllChildren();
		int i = 0;

		if( path == null || path.isEmpty() )
			return;
		for( javax.vecmath.Vector2f pair : path )
		{
			Sphere sphereMesh;
			Geometry sphereGeo;

			sphereMesh = new Sphere( 32, 32, 2f );
			sphereGeo = new Geometry( "" + i, sphereMesh );

			sphereMesh.setTextureMode( Sphere.TextureMode.Projected ); // better quality on
			// spheres
			TangentBinormalGenerator.generate( sphereMesh ); // for lighting effect
			Material sphereMat = new Material( assetManager, "Common/MatDefs/Light/Lighting.j3md" );
			sphereMat.setTexture( "DiffuseMap",
					assetManager.loadTexture( "Textures/Terrain/Pond/Pond.jpg" ) );
			sphereMat.setTexture( "NormalMap",
					assetManager.loadTexture( "Textures/Terrain/Pond/Pond_normal.png" ) );
			sphereMat.setBoolean( "UseMaterialColors", true );
			sphereMat.setColor( "Diffuse", ColorRGBA.White );
			sphereMat.setColor( "Specular", ColorRGBA.White );
			sphereMat.setFloat( "Shininess", 64f ); // [0,128]
			sphereGeo.setMaterial( sphereMat );

			sphereGeo.rotate( 1.6f, 0, 0 ); // Rotate it a bit

			sphereGeo.setLocalTranslation( pair.x, 5, pair.y );
			pathNodes.attachChild( sphereGeo );
		}
	}

	private AbstractObject selectedObject;

	public void takeLock()
	{
		lock.lock();
	}

	public void leaveLock()
	{
		lock.unlock();
	}

	@Override
	public void simpleUpdate( float tpf )
	{
		updateMobileObjects();
		// super.simpleUpdate( tpf );
	}

	public Vector3f getCameraLookAt()
	{
		return cam.getDirection();
	}

	public NiftyHandler getNiftyHandler()
	{
		return niftyHandler;
	}

	public void updateResourcePanel()
	{
		HashMap<Resources, Integer> resources = GameManager.getInstance().getPlayers().get( 0 )
				.getResources();

		Set<Resources> keySet = resources.keySet();
		for( Resources key : keySet )
		{
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println( niftyHandler );
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println();
			niftyHandler.setResourceValue( key, resources.get( key ) );
		}

	}

	public void occupy()
	{
		selectedObject = niftyHandler.getSelectedObject();

	}

	public Node getGroundNode()
	{
		return groundNode;
	}

	public Node getPathNodes()
	{
		return pathNodes;
	}

	public List<Player> getPlayers()
	{
		return players;
	}

	public int getPlayerIndex()
	{
		return playerIndex;
	}

	public ArrayList<javax.vecmath.Vector2f> getPath()
	{
		return path;
	}

	public void setPath( ArrayList<javax.vecmath.Vector2f> path )
	{
		this.path = path;
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
}

package mpa.gui.gameGui.playingGUI;

import java.util.ArrayList;
import java.util.HashMap;

import javax.vecmath.Vector2f;

import mpa.core.maths.MyMath;
import mpa.core.util.GameProperties;

import com.jme3.asset.plugins.ZipLocator;
import com.jme3.bounding.BoundingBox;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import com.jme3.util.TangentBinormalGenerator;
//import mpa.gui.gameGui.listener.GameGuiClickListener;
//import mpa.gui.gameGui.listener.GameGuiKeyActionListener;

public class GuiObjectManager
{
	private static GuiObjectManager _guiGuiObjectManager = null;
	private HashMap<String, Spatial> players = new HashMap<>();
	private HashMap<String, Spatial> headQuarters = new HashMap<>();
	private HashMap<Integer, Spatial> woods = new HashMap<>();
	private HashMap<Integer, Spatial> fields = new HashMap<>();
	private HashMap<Integer, Spatial> caves = new HashMap<>();
	private HashMap<Integer, Spatial> mines = new HashMap<>();
	private HashMap<String, Spatial> minions = new HashMap<>();
	private HashMap<String, Spatial> towerCrushers = new HashMap<>();
	private HashMap<String, Spatial> towers = new HashMap<>();
	private HashMap<String, Spatial> hitPlayers = new HashMap<>();
	private float scalingFactor = 70.2f;
	private ArrayList<Vector2f> path = null;

	private float worldDimension;

	private GameGui gameGui;
	private Vector3f upVector = new Vector3f( 0, 1, 0 );
	private String playingPlayer;

	private GuiObjectManager( GameGui gameGui, String playingPlayer, boolean multiplayer )
	{
		this.gameGui = gameGui;
		if( !multiplayer )
		{

			// clickActionListener = new GameGuiClickListener( listenerImplementation, gameGui );
			// keyActionListener = new GameGuiKeyActionListener( listenerImplementation, gameGui );

		}
		this.playingPlayer = playingPlayer;
	}

	public void setPath( ArrayList<Vector2f> path )
	{
		if( this.path == null || this.path.isEmpty() )
		{
			this.path = path;

			gameGui.spheres.detachAllChildren();

			if( path != null )
				for( Vector2f position : path )
					addSphere( position.x, position.y );
		}
	}

	public ArrayList<Vector2f> getPath()
	{
		return path;
	}

	static void init( GameGui gm, String playingPlayer )
	{
		_guiGuiObjectManager = new GuiObjectManager( gm, playingPlayer, false );
	}

	public static GuiObjectManager getInstance()
	{
		return _guiGuiObjectManager;
	}

	public float getWorldDimension()
	{
		return worldDimension;
	}

	public void setWorldDimension( float worldDimension )
	{
		this.worldDimension = worldDimension;
		gameGui.createWorld( worldDimension );
	}

	public void addPlayer( String name, Vector2f hqPosition, Vector2f gatheringPlace,
			Vector2f direction )
	{
		gameGui.getAssetManager().registerLocator( "./Assets/Models/SeanDevlin.zip",
				ZipLocator.class );
		Spatial playerModel = gameGui.getAssetManager().loadModel( "SeanDevlin.mesh.xml" );
		// playerModel.scale( scalingFactor, scalingFactor, scalingFactor );
		playerModel.scale( MyMath.scaleFactor( getModelBounds( playerModel ), "player" ) );
		playerModel.setLocalTranslation( new Vector3f( gatheringPlace.x, 10, gatheringPlace.y ) );

		Vector3f vector = new Vector3f( -gatheringPlace.x, 0, gatheringPlace.y );
		vector = MyMath.rotateY( vector,
				GameProperties.getInstance().getRotationAngle( playerModel.getName() ) );

		Quaternion quad = new Quaternion();
		quad.lookAt( vector, upVector );
		playerModel.setLocalRotation( quad );

		players.put( name, playerModel );
		gameGui.attachPlayer( playerModel );

		gameGui.getAssetManager().registerLocator( "Assets/Models/baker_house.zip",
				ZipLocator.class );
		Spatial hqModel = gameGui.getAssetManager().loadModel( "baker_house.mesh.xml" );
		hqModel.setLocalTranslation( new Vector3f( hqPosition.x, 0, hqPosition.y ) );
		hqModel.rotate( 0, 90, 0 );

		System.out.println( "casa prima dello scale = " + getModelBounds( hqModel ) );
		hqModel.scale( MyMath.scaleFactor( getModelBounds( hqModel ), "headquarter" ) );
		headQuarters.put( name, hqModel );
		gameGui.attachStaticObject( hqModel );

		System.out.println( "casa dopo dello scale = " + getModelBounds( hqModel ) );

	}

	public Vector3f getModelBounds( Spatial model )
	{
		BoundingBox box = ( BoundingBox ) model.getWorldBound();
		Vector3f boxSize = new Vector3f();
		box.getExtent( boxSize );
		return boxSize;
	}

	public String getPlayingPlayer()
	{
		return playingPlayer;
	}

	public void setHitPlayers( ArrayList<String> hitPl )
	{
		hitPlayers.clear();
		for( String s : hitPl )
			if( players.containsKey( s ) )
				hitPlayers.put( s, players.get( s ) );
			else if( minions.containsKey( s ) )
				hitPlayers.put( s, minions.get( s ) );
			else if( towerCrushers.containsKey( s ) )
				hitPlayers.put( s, towerCrushers.get( s ) );
	}

	public ArrayList<Spatial> getHitCharacters()
	{
		try
		{
			ArrayList<Spatial> hit = new ArrayList<>();
			for( String s : hitPlayers.keySet() )
				hit.add( hitPlayers.get( s ) );
			return hit;
		} finally
		{
			hitPlayers.clear();
		}
	}

	public void addResource( String type, int ID, Vector2f position )
	{
		Spatial model = null;
		switch( type )
		{
			case "WOOD":
				gameGui.getAssetManager().registerLocator( "Assets/Models/Wood.zip",
						ZipLocator.class );
				model = gameGui.getAssetManager().loadModel( "Tree.mesh.xml" );
				model.scale( MyMath.scaleFactor( getModelBounds( model ), "wood" ) );
				woods.put( ID, model );
				break;
			case "FIELD":
				gameGui.getAssetManager().registerLocator( "Assets/Models/CornField.zip",
						ZipLocator.class );
				model = gameGui.getAssetManager().loadModel( "CornField.mesh.xml" );
				model.scale( MyMath.scaleFactor( getModelBounds( model ), "field" ) );
				fields.put( ID, model );
				break;
			case "CAVE":
				gameGui.getAssetManager().registerLocator( "Assets/Models/stones.zip",
						ZipLocator.class );
				model = gameGui.getAssetManager().loadModel( "stones.mesh.xml" );
				System.out.println( "prima dello scale + " + getModelBounds( model ) );
				model.setLocalScale( MyMath.scaleFactor( getModelBounds( model ), "cave" ) );
				// model.scale( MyMath.scaleFactor( getModelBounds( model ), "cave" ) );
				System.out.println( "dopo dello scale + " + getModelBounds( model ) );
				caves.put( ID, model );
				break;
			case "MINE":
				gameGui.getAssetManager().registerLocator( "Assets/Models/Wood.zip",
						ZipLocator.class );
				model = gameGui.getAssetManager().loadModel( "Tree.mesh.xml" );
				model.scale( MyMath.scaleFactor( getModelBounds( model ), "mine" ) );
				mines.put( ID, model );
				break;
		// TODO
		}

		model.setLocalTranslation( new Vector3f( position.x, 0, position.y ) );
		addSphere( position.x, position.y );

		model.rotate( 90, 0, 0 );
		gameGui.attachStaticObject( model );

	}

	private void addSphere( float x, float y )
	{
		Sphere sphereMesh = new Sphere( 10, 10, 9f );
		Geometry sphereGeo = new Geometry( "Shiny rock", sphereMesh );
		sphereMesh.setTextureMode( Sphere.TextureMode.Projected ); // better quality on spheres
		TangentBinormalGenerator.generate( sphereMesh ); // for lighting effect
		Material sphereMat = new Material( gameGui.getAssetManager(),
				"Common/MatDefs/Light/Lighting.j3md" );
		sphereMat.setTexture( "DiffuseMap",
				gameGui.getAssetManager().loadTexture( "Textures/Terrain/Pond/Pond.jpg" ) );
		sphereMat.setTexture( "NormalMap",
				gameGui.getAssetManager().loadTexture( "Textures/Terrain/Pond/Pond_normal.png" ) );
		sphereMat.setBoolean( "UseMaterialColors", true );
		sphereMat.setColor( "Diffuse", ColorRGBA.White );
		sphereMat.setColor( "Specular", ColorRGBA.White );
		sphereMat.setFloat( "Shininess", 64f ); // [0,128]
		sphereGeo.setMaterial( sphereMat );
		sphereGeo.setLocalTranslation( x, 0, y ); // Move it a bit
		sphereGeo.rotate( 1.6f, 0, 0 ); // Rotate it a bit
		gameGui.spheres.attachChild( sphereGeo );
	}

	public void addMinion( String ID )
	{
		// for( int i = 0; i < quantity; i++ )
		{
			// register locator
			// Spatial model = gameGui.getAssetManager().loadModel( arg0 );
			// model.setLocalTranslation( new Vector3f( position.x, 0, position.y ) );
			// minions.put( ID, model );
		}
	}

	public void addTowerCrusher( String ID )
	{

	}

	public void addTower( Vector2f position, String ID )
	{
		gameGui.getAssetManager().registerLocator( "Assets/Models/tower.zip", ZipLocator.class );
		Spatial tower = gameGui.getAssetManager().loadModel( "tower.mesh.xml" );
		tower.setLocalTranslation( position.x, 0, position.y );
		towers.put( ID, tower );
	}

	public void updatePlayerPosition( String name, Vector2f position, Vector2f direction )
	{
		Spatial player = players.get( name );
		Vector3f vector = new Vector3f( -direction.x, 0, -direction.y );

		vector = MyMath.rotateY( vector,
				GameProperties.getInstance().getRotationAngle( player.getName() ) );
		Quaternion quad = new Quaternion();
		quad.lookAt( vector, upVector );

		// player.setLocalRotation( quad );
		player.setLocalTranslation( position.x, 10, position.y );

		// player.setLocalTranslation( player.localToWorld(
		// new Vector3f( position.x, 10, position.y ), null ) );
		//
	}

	public void updateMinionPosition( String ID, Vector2f position, Vector2f direction )
	{
		Spatial minion = minions.get( ID );
		minion.setLocalTranslation( position.x, 0, position.y );
		Quaternion quad = new Quaternion();
		quad.lookAt( new Vector3f( -direction.x, 0, -direction.y ), upVector );
		minion.setLocalRotation( quad );
	}

	public void updateTowerCrusherPosition( String ID, Vector2f position, Vector2f direction )
	{
		Spatial crusher = towerCrushers.get( ID );
		crusher.setLocalTranslation( position.x, 0, position.y );
		Quaternion quad = new Quaternion();
		quad.lookAt( new Vector3f( -direction.x, 0, -direction.y ), upVector );
		crusher.setLocalRotation( quad );
	}

	public void removeTower( String ID )
	{
		gameGui.detachStaticObject( towers.remove( ID ) );
	}

	public void removePlayer( String name )
	{
		// lancia animazione
		gameGui.detachPlayer( players.remove( name ) );
		players.remove( name );
	}

	public void removeMinion( String ID )
	{
		// lancia animazione
		gameGui.detachPlayer( minions.get( ID ) );
		minions.remove( ID );
	}

	// TODO retrieve informations
}

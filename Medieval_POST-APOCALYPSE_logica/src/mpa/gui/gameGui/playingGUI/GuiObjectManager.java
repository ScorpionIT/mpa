package mpa.gui.gameGui.playingGUI;

import java.util.HashMap;

import javax.vecmath.Vector2f;

import com.jme3.asset.plugins.ZipLocator;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

public class GuiObjectManager
{
	private HashMap<String, Spatial> players = new HashMap<>();
	private HashMap<String, Spatial> headQuarters = new HashMap<>();
	private HashMap<Integer, Spatial> woods = new HashMap<>();
	private HashMap<Integer, Spatial> fields = new HashMap<>();
	private HashMap<Integer, Spatial> caves = new HashMap<>();
	private HashMap<Integer, Spatial> mines = new HashMap<>();
	private HashMap<String, Spatial> minions = new HashMap<>();
	private HashMap<String, Spatial> towers = new HashMap<>();
	private float scalingFactor = 2.2f;

	private GameGui gameGui;
	private Vector3f upVector = new Vector3f( 0, 1, 0 );

	GuiObjectManager( GameGui gameGui )
	{
		this.gameGui = gameGui;
	}

	public void addPlayer( String name, Vector2f hqPosition, Vector2f gatheringPlace,
			Vector2f direction )
	{
		gameGui.getAssetManager().registerLocator( "./Assets/Models/SeanDevlin.zip",
				ZipLocator.class );
		Spatial playerModel = gameGui.getAssetManager().loadModel( "SeanDevlin.mesh.xml" );
		playerModel.scale( scalingFactor, scalingFactor, scalingFactor );
		playerModel.setLocalTranslation( new Vector3f( gatheringPlace.x, 5, gatheringPlace.y ) );
		players.put( name, playerModel );
		gameGui.attachPlayer( playerModel );

		gameGui.getAssetManager().registerLocator( "Assets/Models/tower.zip", ZipLocator.class );
		Spatial hqModel = gameGui.getAssetManager().loadModel( "tower.mesh.xml" );
		hqModel.setLocalTranslation( new Vector3f( hqPosition.x, 0, hqPosition.y ) );
		headQuarters.put( name, hqModel );
		gameGui.attachStaticObject( hqModel );
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
				woods.put( ID, model );
			case "FIELD":
				gameGui.getAssetManager().registerLocator( "Assets/Models/CornField.zip",
						ZipLocator.class );
				model = gameGui.getAssetManager().loadModel( "CornField.mesh.xml" );
				fields.put( ID, model );
			case "CAVE":
				gameGui.getAssetManager().registerLocator( "Assets/Models/stones.zip",
						ZipLocator.class );
				model = gameGui.getAssetManager().loadModel( "stones.mesh.xml" );
				caves.put( ID, model );
			case "MINE":
				gameGui.getAssetManager().registerLocator( "Assets/Models/Wood.zip",
						ZipLocator.class );
				model = gameGui.getAssetManager().loadModel( "Tree.mesh.xml" );
				mines.put( ID, model );
				// TODO
		}

		model.setLocalTranslation( new Vector3f( position.x, 0, position.y ) );
		Quaternion quad = new Quaternion();
		quad.lookAt( new Vector3f( -position.x, 0, position.y ), upVector );
		model.setLocalRotation( quad );
		gameGui.attachStaticObject( model );

	}

	public void addMinions( String ID, Vector2f position, int quantity )
	{
		for( int i = 0; i < quantity; i++ )
		{
			// register locator
			// Spatial model = gameGui.getAssetManager().loadModel( arg0 );
			// model.setLocalTranslation( new Vector3f( position.x, 0, position.y ) );
			// minions.put( ID, model );
		}
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
		player.setLocalTranslation( position.x, 0, position.y );
		Quaternion quad = new Quaternion();
		quad.lookAt( new Vector3f( -direction.x, 0, direction.y ), upVector );
		player.setLocalRotation( quad );
	}

	public void updateMinionPosition( String ID, Vector2f position, Vector2f direction )
	{
		Spatial minion = minions.get( ID );
		minion.setLocalTranslation( position.x, 0, position.y );
		Quaternion quad = new Quaternion();
		quad.lookAt( new Vector3f( -direction.x, 0, -direction.y ), upVector );
		minion.setLocalRotation( quad );
	}

	public void removeTower( String ID )
	{
		gameGui.detachStaticObject( towers.remove( ID ) );
	}

	public void removePlayer( String name )
	{
		gameGui.detachPlayer( players.remove( name ) );
	}

	// TODO retrieve informations
}

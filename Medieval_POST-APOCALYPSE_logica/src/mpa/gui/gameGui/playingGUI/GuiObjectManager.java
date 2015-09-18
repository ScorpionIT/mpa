package mpa.gui.gameGui.playingGUI;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.vecmath.Vector2f;

import mpa.core.logic.AbstractObject;
import mpa.core.logic.GameManager;
import mpa.core.logic.building.AbstractProperty;
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
//import mpa.gui.gameGui.listener.GameGuiClickListener;
//import mpa.gui.gameGui.listener.GameGuiKeyActionListener;
import com.jme3.scene.shape.Sphere;
import com.jme3.util.TangentBinormalGenerator;

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
	private ArrayList<Vector2f> pointToVisit = new ArrayList<>();

	private HashMap<String, Circle2D> playersCirlces = new HashMap<>();

	private HashMap<String, Color> playersColors = new HashMap<>();

	private HashMap<Spatial, SpatialAnimationController> animationControllerSpatial = new HashMap<>();
	private ArrayList<Vector2f> path = null;

	private float worldDimension;

	private GameGui gameGui;
	private Vector3f upVector = new Vector3f(0, 1, 0);
	private String playingPlayer;

	private GuiObjectManager(GameGui gameGui, String playingPlayer, boolean multiplayer)
	{
		this.gameGui = gameGui;
		if (!multiplayer)
		{

			// clickActionListener = new GameGuiClickListener( listenerImplementation, gameGui );
			// keyActionListener = new GameGuiKeyActionListener( listenerImplementation, gameGui );

		}
		this.playingPlayer = playingPlayer;
	}

	public void setPath(ArrayList<Vector2f> path)
	{
		if (this.path == null || this.path.isEmpty())
		{
			this.path = path;

			// gameGui.spheres.detachAllChildren();

			// if (path != null)
			// for (Vector2f position : path)
			// addSphere(position.x, position.y);
		}
	}

	public ArrayList<Vector2f> getPath()
	{
		return path;
	}

	static void init(GameGui gm, String playingPlayer)
	{
		_guiGuiObjectManager = new GuiObjectManager(gm, playingPlayer, false);
	}

	public static GuiObjectManager getInstance()
	{
		return _guiGuiObjectManager;
	}

	public float getWorldDimension()
	{
		return worldDimension;
	}

	public void setWorldDimension(float worldDimension)
	{
		this.worldDimension = worldDimension;
		gameGui.createWorld(worldDimension);
	}

	public void setPointToVisit()
	{
		float ray = 100f;
		int fragmentsX = (int) (GameManager.getInstance().getWorld().getWidth() / ray);
		int fragmentsY = (int) (GameManager.getInstance().getWorld().getHeight() / ray);

		for (int i = 0; i < fragmentsX; i++)
			for (int j = 0; j < fragmentsY; j++)
			{
				Vector2f position = new Vector2f(ray + ray * i, ray + ray * j);

				ArrayList<AbstractObject> collisions = GameManager.getInstance().getWorld().checkForCollision(position.x, position.y, ray);
				if (collisions.isEmpty())
				{

				}
				else
				{
					position = ((AbstractProperty) collisions.get(0)).getGatheringPlace();

				}
				pointToVisit.add(position);
			}
		System.out.println();
		System.out.println();
		System.out.println("CE NE SONO " + pointToVisit.size());
		System.out.println();
		System.out.println();
		System.out.println();
		gameGui.spheres.detachAllChildren();

		// for (Vector2f p : pointToVisit)
		// {
		// System.out.println("provo a disegnare la sfera " + p.toString());
		// this.addSphere(p.x, p.y);
		// }
	}

	public void addPlayer(String name, Vector2f hqPosition, Vector2f gatheringPlace, Vector2f direction)
	{
		gameGui.getAssetManager().registerLocator("./Assets/Models/Skeleton.zip", ZipLocator.class);
		Spatial playerModel = gameGui.getAssetManager().loadModel("Skeleton.mesh.xml");
		playerModel.scale(MyMath.scaleFactor(getModelBounds(playerModel), "player"));

		Vector3f playerPosition = new Vector3f(gatheringPlace.x, 10, gatheringPlace.y);
		playerModel.setLocalTranslation(playerPosition);
		addPlayerCircle(playerPosition, name);

		animationControllerSpatial.put(playerModel, new SpatialAnimationController(playerModel, this, name, "player"));

		Vector3f vector = new Vector3f(-gatheringPlace.x, 0, gatheringPlace.y);
		vector = MyMath.rotateY(vector, GameProperties.getInstance().getRotationAngle(playerModel.getName()));

		Quaternion quad = new Quaternion();
		quad.lookAt(vector, upVector);
		playerModel.setLocalRotation(quad);

		players.put(name, playerModel);
		gameGui.attachMobileObject(playerModel);

		gameGui.getAssetManager().registerLocator("Assets/Models/baker_house.zip", ZipLocator.class);
		Spatial hqModel = gameGui.getAssetManager().loadModel("baker_house.mesh.xml");
		hqModel.setLocalTranslation(new Vector3f(hqPosition.x, 0, hqPosition.y));
		hqModel.rotate(0, 90, 0);

		hqModel.scale(MyMath.scaleFactor(getModelBounds(hqModel), "headquarter"));
		headQuarters.put(name, hqModel);
		gameGui.attachStaticObject(hqModel);

		System.out.println("casa dopo dello scale = " + getModelBounds(hqModel));

	}

	private void addPlayerCircle(Vector3f position, String playerName)
	{
		Color color = new Color(new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 0.8f);
		Circle2D circle2D = createCircle(color, GameProperties.getInstance().getObjectWidth("player") * 2);
		gameGui.attachCircle(circle2D);

		playersCirlces.put(playerName, circle2D);
		playersColors.put(playerName, color);

		if (playerName.equals(playingPlayer))
			gameGui.getNiftyHandler().updateResourcePanel();

	}

	public Vector3f getModelBounds(Spatial model)
	{
		BoundingBox box = (BoundingBox) model.getWorldBound();
		Vector3f boxSize = new Vector3f();
		box.getExtent(boxSize);
		return boxSize;
	}

	public String getPlayingPlayer()
	{
		return playingPlayer;
	}

	public void setHitPlayers(ArrayList<String> hitPl)
	{
		hitPlayers.clear();
		for (String s : hitPl)
			if (players.containsKey(s))
				hitPlayers.put(s, players.get(s));
			else if (minions.containsKey(s))
				hitPlayers.put(s, minions.get(s));
			else if (towerCrushers.containsKey(s))
				hitPlayers.put(s, towerCrushers.get(s));
	}

	public ArrayList<Spatial> getHitCharacters()
	{
		try
		{
			ArrayList<Spatial> hit = new ArrayList<>();
			for (String s : hitPlayers.keySet())
				hit.add(hitPlayers.get(s));
			return hit;
		} finally
		{
			hitPlayers.clear();
		}
	}

	public void addResource(String type, int ID, Vector2f position)
	{
		Spatial model = null;
		switch (type)
		{
			case "WOOD":
				gameGui.getAssetManager().registerLocator("Assets/Models/Wood.zip", ZipLocator.class);
				model = gameGui.getAssetManager().loadModel("Tree.mesh.xml");
				model.scale(MyMath.scaleFactor(getModelBounds(model), "wood"));
				woods.put(ID, model);
				model.rotate(90, 0, 0);
				break;
			case "FIELD":
				gameGui.getAssetManager().registerLocator("Assets/Models/CornField.zip", ZipLocator.class);
				model = gameGui.getAssetManager().loadModel("CornField.mesh.xml");
				model.scale(MyMath.scaleFactor(getModelBounds(model), "field"));
				fields.put(ID, model);
				model.rotate(90, 0, 0);
				break;
			case "CAVE":
				gameGui.getAssetManager().registerLocator("Assets/Models/stones.zip", ZipLocator.class);
				model = gameGui.getAssetManager().loadModel("stones.mesh.xml");
				System.out.println("prima dello scale + " + getModelBounds(model));
				model.scale(MyMath.scaleFactor(getModelBounds(model), "cave"));
				System.out.println("dopo dello scale + " + getModelBounds(model));
				caves.put(ID, model);
				break;
			case "MINE":
				gameGui.getAssetManager().registerLocator("Assets/Models/Wood.zip", ZipLocator.class);
				model = gameGui.getAssetManager().loadModel("Tree.mesh.xml");
				model.scale(MyMath.scaleFactor(getModelBounds(model), "mine"));
				mines.put(ID, model);
				model.rotate(90, 0, 0);
				break;
		// TODO
		}

		model.setLocalTranslation(new Vector3f(position.x, 0, position.y));
		addSphere(position.x, position.y);

		gameGui.attachStaticObject(model);

	}

	private Circle2D createCircle(Color color, float radius)
	{
		Circle2D circle2D = new Circle2D(gameGui.getAssetManager(), radius, 0, 360, color, 360);
		return circle2D;
	}

	private void addSphere(float x, float y)
	{

		Sphere sphereMesh = new Sphere(10, 10, 9f);
		Geometry sphereGeo = new Geometry("Shiny rock", sphereMesh);
		sphereMesh.setTextureMode(Sphere.TextureMode.Projected); // better quality on spheres
		TangentBinormalGenerator.generate(sphereMesh); // for lighting effect
		Material sphereMat = new Material(gameGui.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
		sphereMat.setTexture("DiffuseMap", gameGui.getAssetManager().loadTexture("Textures/Terrain/Pond/Pond.jpg"));
		sphereMat.setTexture("NormalMap", gameGui.getAssetManager().loadTexture("Textures/Terrain/Pond/Pond_normal.png"));
		sphereMat.setBoolean("UseMaterialColors", true);
		sphereMat.setColor("Diffuse", ColorRGBA.White);
		sphereMat.setColor("Specular", ColorRGBA.White);
		sphereMat.setFloat("Shininess", 64f); // [0,128]
		sphereGeo.setMaterial(sphereMat);
		sphereGeo.setLocalTranslation(x, 15, y); // Move it a bit
		sphereGeo.rotate(1.6f, 0, 0); // Rotate it a bit
		gameGui.spheres.attachChild(sphereGeo);
	}

	public void addMinion(String ID)
	{

		gameGui.getAssetManager().registerLocator("./Assets/Models/Monster1.zip", ZipLocator.class);
		Spatial minionModel = gameGui.getAssetManager().loadModel("Monster1.mesh.xml");
		minionModel.scale(MyMath.scaleFactor(getModelBounds(minionModel), "minion"));
		minions.put(ID, minionModel);
		gameGui.attachMobileObject(minionModel);
		animationControllerSpatial.put(minionModel, new SpatialAnimationController(minionModel, this, ID, "minion"));

	}

	public void addTowerCrusher(String ID)
	{
		gameGui.getAssetManager().registerLocator("./Assets/Models/Troll.zip", ZipLocator.class);
		Spatial towerCrusherModel = gameGui.getAssetManager().loadModel("Troll.mesh.xml");
		towerCrusherModel.scale(MyMath.scaleFactor(getModelBounds(towerCrusherModel), "towerCrusher"));
		towerCrushers.put(ID, towerCrusherModel);
		gameGui.attachMobileObject(towerCrusherModel);
		animationControllerSpatial.put(towerCrusherModel, new SpatialAnimationController(towerCrusherModel, this, ID, "towerCrusher"));

	}

	public void addTower(Vector2f position, String ID)
	{
		gameGui.getAssetManager().registerLocator("Assets/Models/tower.zip", ZipLocator.class);
		Spatial tower = gameGui.getAssetManager().loadModel("tower.mesh.xml");
		tower.setLocalTranslation(position.x, 0, position.y);
		tower.scale(MyMath.scaleFactor(getModelBounds(tower), "tower"));
		tower.rotate(90, 0, 0);
		gameGui.attachStaticObject(tower);
		towers.put(ID, tower);
	}

	public void updatePlayerPosition(String name, Vector2f position, Vector2f direction, boolean isMoving)
	{

		Spatial player = players.get(name);
		if (player != null)
		{
			if (isMoving)
			{
				animationControllerSpatial.get(player).startWalkAnimation(1f);
			}
			else
				animationControllerSpatial.get(player).stopWalkAnimation();

			Vector3f vector = new Vector3f(-direction.x, 0, -direction.y);

			vector = MyMath.rotateY(vector, GameProperties.getInstance().getRotationAngle(player.getName()));
			Quaternion quad = new Quaternion();
			quad.lookAt(vector, upVector);

			player.setLocalRotation(quad);
			player.setLocalTranslation(position.x, 15, position.y);

			Circle2D circle2d = playersCirlces.get(name);
			circle2d.setLocalTranslation(position.x - circle2d.getRadius() / 2, 1, position.y + circle2d.getRadius() / 2);
		}
	}

	public void updateMinionPosition(String ID, Vector2f position, Vector2f direction, boolean isMoving)
	{
		Spatial minion = minions.get(ID);
		if (minion != null)
		{
			if (isMoving)
			{
				animationControllerSpatial.get(minion).startWalkAnimation(1f);
			}
			else
				animationControllerSpatial.get(minion).stopWalkAnimation();

			Vector3f vector = new Vector3f(-direction.x, 0, -direction.y);
			vector = MyMath.rotateY(vector, GameProperties.getInstance().getRotationAngle(minion.getName()));
			Quaternion quad = new Quaternion();
			quad.lookAt(vector, upVector);
			minion.setLocalRotation(quad);
			minion.setLocalTranslation(position.x, 10, position.y);
		}
	}

	public void updateTowerCrusherPosition(String ID, Vector2f position, Vector2f direction)
	{

		Spatial towerCrusher = towerCrushers.get(ID);
		if (towerCrusher != null)
		{
			// if (isMoving)
			// {
			// animationControllerSpatial.get(minion).startWalkAnimation(1f);
			// }
			// else
			// animationControllerSpatial.get(minion).stopWalkAnimation();

			Vector3f vector = new Vector3f(-direction.x, 0, -direction.y);
			vector = MyMath.rotateY(vector, GameProperties.getInstance().getRotationAngle(towerCrusher.getName()));
			Quaternion quad = new Quaternion();
			quad.lookAt(vector, upVector);
			towerCrusher.setLocalRotation(quad);
			towerCrusher.setLocalTranslation(position.x, 10, position.y);

		}
		// Spatial crusher = towerCrushers.get(ID);
		// crusher.setLocalTranslation(position.x, 0, position.y);
		// Quaternion quad = new Quaternion();
		// quad.lookAt(new Vector3f(-direction.x, 0, -direction.y), upVector);
		// crusher.setLocalRotation(quad);
	}

	public void removeTower(String ID)
	{
		gameGui.detachStaticObject(towers.remove(ID));
	}

	public void killMinion(String ID)
	{
		if (minions.get(ID) != null)
		{
			SpatialAnimationController spatialAnimationController = animationControllerSpatial.get(minions.get(ID));
			if (spatialAnimationController != null)
			{
				if (!spatialAnimationController.startDeathAnimation())
				{
					removeMinion(ID);
				}
			}
		}
	}

	public void killPlayer(String name)
	{
		if (players.get(name) != null)
		{
			SpatialAnimationController spatialAnimationController = animationControllerSpatial.get(players.get(name));
			if (spatialAnimationController != null)
			{
				if (!spatialAnimationController.startDeathAnimation())
				{
					removePlayer(name);
				}
			}
		}

	}

	public void removePlayer(String name)
	{
		gameGui.detachMobileObject(players.get(name));
		animationControllerSpatial.remove(players.get(name));
		players.remove(name);
		gameGui.detachCircle(playersCirlces.remove(name));
		playersColors.remove(name);
		gameGui.getNiftyHandler().removePayer(name);
	}

	public HashMap<String, Color> getPlayersColors()
	{
		return playersColors;
	}

	public void removeMinion(String ID)
	{
		gameGui.detachMobileObject(minions.get(ID));
		animationControllerSpatial.remove(minions.get(ID));
		minions.remove(ID);
	}

	public void startPlayerAttackAnimation(String playingPlayerName)
	{
		animationControllerSpatial.get(players.get(playingPlayerName)).stopAnimation();
		animationControllerSpatial.get(players.get(playingPlayerName)).startAttackAnimation(1.8f);
	}

	public void startMInionAttackAnimation(String ID)
	{
		animationControllerSpatial.get(minions.get(ID)).stopAnimation();
		animationControllerSpatial.get(minions.get(ID)).startAttackAnimation(1f);
	}

	public Color getPlayngPlayerColor()
	{
		return playersColors.get(playingPlayer);
	}

	// TODO retrieve informations
}

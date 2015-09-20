package mpa.gui.gameGui.playingGUI;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.jme3.scene.shape.Sphere;
import com.jme3.util.TangentBinormalGenerator;

public class GuiObjectManager
{
	private static GuiObjectManager _guiGuiObjectManager = null;
	private Map<String, Spatial> players = new HashMap<>();
	private Map<String, Spatial> headQuarters = new HashMap<>();
	private Map<Integer, Spatial> woods = new HashMap<>();
	private Map<Integer, Spatial> fields = new HashMap<>();
	private Map<Integer, Spatial> caves = new HashMap<>();
	private Map<Integer, Spatial> mines = new HashMap<>();
	private Map<String, Spatial> minions = new HashMap<>();
	private Map<String, Spatial> towerCrushers = new HashMap<>();
	private Map<String, Spatial> towers = new HashMap<>();
	private Map<String, Spatial> hitPlayers = new HashMap<>();
	private List<Vector2f> pointToVisit = new ArrayList<>();

	private Map<Spatial, Circle2D> spatialCirlces = new HashMap<>();

	private Map<String, Color> playersColors = new HashMap<>();

	private Map<Spatial, LifeBar> spatialsLifeBars = new HashMap<>();

	private Map<Spatial, SpatialAnimationController> animationControllerSpatial = new HashMap<>();
	private List<Vector2f> path = null;

	private float worldDimension;

	private GameGui gameGui;
	private Vector3f upVector = new Vector3f(0, 1, 0);
	private String playingPlayer;
	private String modelsPath = GameProperties.getInstance().getPath("ModelsPath");

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

	public List<Vector2f> getPath()
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

	public void addPlayer(String name, Vector2f hqPosition, Vector2f gatheringPlace, Vector2f direction, int life)
	{
		gameGui.getAssetManager().registerLocator(modelsPath + GameProperties.getInstance().getModelName("player") + ".zip", ZipLocator.class);
		Spatial playerModel = gameGui.getAssetManager().loadModel(GameProperties.getInstance().getModelName("player") + ".mesh.xml");
		playerModel.scale(MyMath.scaleFactor(getModelBounds(playerModel), "player"));

		Vector3f playerPosition = new Vector3f(gatheringPlace.x, 10, gatheringPlace.y);
		playerModel.setLocalTranslation(playerPosition);
		addPlayerCircle(playerPosition, name, playerModel);

		animationControllerSpatial.put(playerModel, new SpatialAnimationController(playerModel, this, name, "player"));

		Vector3f vector = new Vector3f(-gatheringPlace.x, 0, gatheringPlace.y);
		vector = MyMath.rotateY(vector, GameProperties.getInstance().getRotationAngle(playerModel.getName()));

		Quaternion quad = new Quaternion();
		quad.lookAt(vector, upVector);
		playerModel.setLocalRotation(quad);

		players.put(name, playerModel);
		gameGui.attachMobileObject(playerModel);

		addModelLifeBar(playerModel, playerPosition, life);

		gameGui.getAssetManager().registerLocator(modelsPath + GameProperties.getInstance().getModelName("headquarter") + ".zip", ZipLocator.class);
		Spatial hqModel = gameGui.getAssetManager().loadModel(GameProperties.getInstance().getModelName("headquarter") + ".mesh.xml");
		hqModel.setLocalTranslation(new Vector3f(hqPosition.x, 0, hqPosition.y));
		hqModel.rotate(0, 90, 0);

		hqModel.scale(MyMath.scaleFactor(getModelBounds(hqModel), "headquarter"));
		headQuarters.put(name, hqModel);
		gameGui.attachStaticObject(hqModel);

		if (name.equals(playingPlayer))
			gameGui.setCamera(new Vector3f(playerPosition.x, gameGui.getCameraHeight(), playerPosition.z - 40));

		System.out.println("casa dopo dello scale = " + getModelBounds(hqModel));

	}

	private void addModelLifeBar(Spatial model, Vector3f position, int life)
	{
		LifeBar lifeBar = new LifeBar(gameGui.getAssetManager(), getModelBounds(model), position.y);
		lifeBar.setLife(life);

		lifeBar.setLocalTraslation(position);
		gameGui.attachLifeBar(lifeBar);
		spatialsLifeBars.put(model, lifeBar);

	}

	private void updateModelLifeBar(Spatial model, Vector2f position, int life)
	{
		LifeBar lifeBar = spatialsLifeBars.get(model);
		lifeBar.setLife(life);

		lifeBar.setLocalTraslation(position);

	}

	private void addPlayerCircle(Vector3f position, String playerName, Spatial playerModel)
	{
		Color color = null;
		if (playerName.equals(this.playingPlayer))
			color = new Color(0.9f, 0f, 0.2f, 0.8f);
		else
			color = new Color(new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 0.8f);
		Circle2D circle2D = createCircle(color, GameProperties.getInstance().getObjectWidth("player") * 2);
		gameGui.attachCircle(circle2D);

		spatialCirlces.put(playerModel, circle2D);
		playersColors.put(playerName, color);

		if (playerName.equals(playingPlayer))
			gameGui.getNiftyHandler().updateResourcePanel();

	}

	private void addMinionCircle(String boss, Spatial minionModel)
	{
		Circle2D circle2D = createCircle(playersColors.get(boss), GameProperties.getInstance().getObjectWidth("minion"));
		gameGui.attachCircle(circle2D);

		spatialCirlces.put(minionModel, circle2D);

	}

	private void addTowerCrusherCircle(String boss, Spatial towerCrusherModel)
	{
		Circle2D circle2D = createCircle(playersColors.get(boss), GameProperties.getInstance().getObjectWidth("towerCrusher"));
		gameGui.attachCircle(circle2D);

		spatialCirlces.put(towerCrusherModel, circle2D);

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

	public List<Spatial> getHitCharacters()
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
				gameGui.getAssetManager().registerLocator(modelsPath + GameProperties.getInstance().getModelName("wood") + ".zip", ZipLocator.class);
				model = gameGui.getAssetManager().loadModel(GameProperties.getInstance().getModelName("wood") + ".mesh.xml");
				model.scale(MyMath.scaleFactor(getModelBounds(model), "wood"));
				woods.put(ID, model);
				model.rotate(90, 0, 0);
				break;
			case "FIELD":
				gameGui.getAssetManager().registerLocator(modelsPath + GameProperties.getInstance().getModelName("field") + ".zip", ZipLocator.class);
				model = gameGui.getAssetManager().loadModel(GameProperties.getInstance().getModelName("field") + ".mesh.xml");
				model.scale(MyMath.scaleFactor(getModelBounds(model), "field"));
				fields.put(ID, model);
				model.rotate(90, 0, 0);
				break;
			case "CAVE":
				gameGui.getAssetManager().registerLocator(modelsPath + GameProperties.getInstance().getModelName("cave") + ".zip", ZipLocator.class);
				model = gameGui.getAssetManager().loadModel(GameProperties.getInstance().getModelName("cave") + ".mesh.xml");
				System.out.println("prima dello scale + " + getModelBounds(model));
				model.scale(MyMath.scaleFactor(getModelBounds(model), "cave"));
				System.out.println("dopo dello scale + " + getModelBounds(model));
				caves.put(ID, model);
				break;
			case "MINE":
				gameGui.getAssetManager().registerLocator(modelsPath + GameProperties.getInstance().getModelName("mine") + ".zip", ZipLocator.class);
				model = gameGui.getAssetManager().loadModel(GameProperties.getInstance().getModelName("mine") + ".mesh.xml");
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

	private void addMinion(String ID, String boss)
	{
		gameGui.getAssetManager().registerLocator(modelsPath + GameProperties.getInstance().getModelName("minion") + ".zip", ZipLocator.class);
		Spatial minionModel = gameGui.getAssetManager().loadModel(GameProperties.getInstance().getModelName("minion") + ".mesh.xml");
		minionModel.scale(MyMath.scaleFactor(getModelBounds(minionModel), "minion"));
		minions.put(ID, minionModel);
		gameGui.attachMobileObject(minionModel);
		animationControllerSpatial.put(minionModel, new SpatialAnimationController(minionModel, this, ID, "minion"));

		addMinionCircle(boss, minionModel);

	}

	private void addTowerCrusher(String ID, String boss, int life, Vector2f position)
	{
		gameGui.getAssetManager().registerLocator(modelsPath + GameProperties.getInstance().getModelName("towerCrusher") + ".zip", ZipLocator.class);
		Spatial towerCrusherModel = gameGui.getAssetManager().loadModel(GameProperties.getInstance().getModelName("towerCrusher") + ".mesh.xml");
		towerCrusherModel.scale(MyMath.scaleFactor(getModelBounds(towerCrusherModel), "towerCrusher"));
		towerCrushers.put(ID, towerCrusherModel);
		gameGui.attachMobileObject(towerCrusherModel);
		animationControllerSpatial.put(towerCrusherModel, new SpatialAnimationController(towerCrusherModel, this, ID, "towerCrusher"));

		addTowerCrusherCircle(boss, towerCrusherModel);

		Vector3f positionModel = new Vector3f(position.x, 20, position.y);
		addModelLifeBar(towerCrusherModel, positionModel, life);

	}

	public void updateTower(Vector2f position, String ID, int life)
	{
		if (!towers.containsKey(ID))
		{
			gameGui.getAssetManager().registerLocator(modelsPath + GameProperties.getInstance().getModelName("tower") + ".zip", ZipLocator.class);
			Spatial tower = gameGui.getAssetManager().loadModel(GameProperties.getInstance().getModelName("tower") + ".mesh.xml");
			Vector3f positionModel = new Vector3f(position.x, 0, position.y);
			tower.setLocalTranslation(positionModel);
			tower.rotate(90, 0, 0);
			tower.scale(MyMath.scaleFactor(getModelBounds(tower), "tower"));

			positionModel.z += 20;
			addModelLifeBar(tower, positionModel, life);

			gameGui.attachStaticObject(tower);
			towers.put(ID, tower);
		}
		else
		{
			Spatial tower = towers.get(ID);
			Vector2f positionModel = new Vector2f(position.x, position.y);
			positionModel.y += 20;
			updateModelLifeBar(tower, positionModel, life);
		}
	}

	public synchronized void updatePlayerPosition(String name, Vector2f position, Vector2f direction, boolean isMoving, int life)
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

			updateModelLifeBar(player, position, life);

			player.setLocalRotation(quad);
			player.setLocalTranslation(position.x, 15, position.y);

			Circle2D circle2d = spatialCirlces.get(player);
			updateCirclePosition(circle2d, position);

		}
	}

	private void updateCirclePosition(Circle2D circle2d, Vector2f position)
	{
		circle2d.setLocalTranslation(position.x - circle2d.getRadius() / 2, 1, position.y + circle2d.getRadius() / 2);
	}

	public synchronized void updateMinionPosition(String ID, Vector2f position, Vector2f direction, boolean isMoving, String boss)
	{

		if (!minions.containsKey(ID))
		{
			addMinion(ID, boss);
			return;
		}
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

			Circle2D circle2d = spatialCirlces.get(minion);
			updateCirclePosition(circle2d, position);
		}
	}

	public void updateTowerCrusherPosition(String ID, Vector2f position, Vector2f direction, boolean isMoving, String boss, int life)
	{
		if (!towerCrushers.containsKey(ID))
		{
			addTowerCrusher(ID, boss, life, position);
		}
		Spatial towerCrusher = towerCrushers.get(ID);
		if (towerCrusher != null)
		{
			if (isMoving)
			{
				animationControllerSpatial.get(towerCrusher).startWalkAnimation(1f);
			}
			else
				animationControllerSpatial.get(towerCrusher).stopWalkAnimation();

			Vector3f vector = new Vector3f(-direction.x, 0, -direction.y);
			vector = MyMath.rotateY(vector, GameProperties.getInstance().getRotationAngle(towerCrusher.getName()));
			Quaternion quad = new Quaternion();
			quad.lookAt(vector, upVector);
			towerCrusher.setLocalRotation(quad);
			towerCrusher.setLocalTranslation(position.x, 10, position.y);

			Circle2D circle2d = spatialCirlces.get(towerCrusher);
			updateCirclePosition(circle2d, position);

			updateModelLifeBar(towerCrusher, position, life);

		}
		// Spatial crusher = towerCrushers.get(ID);
		// crusher.setLocalTranslation(position.x, 0, position.y);
		// Quaternion quad = new Quaternion();
		// quad.lookAt(new Vector3f(-direction.x, 0, -direction.y), upVector);
		// crusher.setLocalRotation(quad);
	}

	public void removeTower(String ID)
	{
		gameGui.detachLifeBar(spatialsLifeBars.remove(towers.get(ID)));
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

	public void killTowerCrusher(String ID)
	{
		if (towerCrushers.get(ID) != null)
		{
			SpatialAnimationController spatialAnimationController = animationControllerSpatial.get(towerCrushers.get(ID));
			if (spatialAnimationController != null)
			{
				if (!spatialAnimationController.startDeathAnimation())
				{
					removeTowerCrusher(ID);
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

	synchronized void removeTowerCrusher(String ID)
	{
		gameGui.detachMobileObject(towerCrushers.get(ID));
		animationControllerSpatial.remove(towerCrushers.get(ID));
		gameGui.detachLifeBar(spatialsLifeBars.get(towerCrushers.get(ID)));
		gameGui.detachCircle(spatialCirlces.remove(towerCrushers.get(ID)));
		towerCrushers.remove(ID);

	}

	synchronized void removePlayer(String name)
	{
		if (name.equals(playingPlayer))
			System.exit(0);
		gameGui.detachMobileObject(players.get(name));
		animationControllerSpatial.remove(players.get(name));
		gameGui.detachLifeBar(spatialsLifeBars.get(players.get(name)));
		gameGui.detachCircle(spatialCirlces.remove(players.get(name)));
		players.remove(name);
		playersColors.remove(name);
		gameGui.getNiftyHandler().removePlayer(name);
	}

	synchronized void removeMinion(String ID)
	{
		gameGui.detachMobileObject(minions.get(ID));
		animationControllerSpatial.remove(minions.get(ID));
		gameGui.detachCircle(spatialCirlces.remove(minions.get(ID)));
		minions.remove(ID);
	}

	public void startPlayerAttackAnimation(String playingPlayerName)
	{
		animationControllerSpatial.get(players.get(playingPlayerName)).stopAnimation();
		animationControllerSpatial.get(players.get(playingPlayerName)).startAttackAnimation(1.8f);
	}

	public void startMinionAttackAnimation(String ID)
	{
		if (minions.get(ID) != null)
		{
			animationControllerSpatial.get(minions.get(ID)).stopAnimation();
			animationControllerSpatial.get(minions.get(ID)).startAttackAnimation(1f);
		}
	}

	public void startTowerCrusherAttackAnimation(String ID)
	{
		if (towerCrushers.get(ID) != null)
		{
			animationControllerSpatial.get(towerCrushers.get(ID)).stopAnimation();
			animationControllerSpatial.get(towerCrushers.get(ID)).startAttackAnimation(1f);
		}
	}

	public Color getPlayingPlayerColor()
	{
		return playersColors.get(playingPlayer);
	}

	public Map<String, Color> getPlayersColors()
	{
		return playersColors;
	}
	// TODO retrieve informations
}

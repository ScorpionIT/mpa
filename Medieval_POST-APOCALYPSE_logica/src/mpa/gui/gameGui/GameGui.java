package mpa.gui.gameGui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import mpa.core.logic.AbstractObject;
import mpa.core.logic.GameManager;
import mpa.core.logic.Pair;
import mpa.core.logic.building.House;
import mpa.core.logic.building.Market;
import mpa.core.logic.character.Player;
import mpa.core.logic.resource.Cave;
import mpa.core.logic.resource.Field;
import mpa.core.logic.resource.Resources;
import mpa.core.logic.resource.Wood;
import mpa.gui.gameGui.panel.NiftyHandler;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Ray;
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

public class GameGui extends SimpleApplication
{
	private ReentrantLock lock = new ReentrantLock();
	boolean cursorOnTheRightEdge = false;
	boolean cursorOnTheLeftEdge = false;
	boolean cursorOnTheTopEdge = false;
	boolean cursorOnTheBottomEdge = false;
	private float cameraHeight = 120;
	private float lx = (float) Math.sqrt(Math.pow(cameraHeight / Math.sin(10), 2) - Math.pow(cameraHeight, 2));
	private float lz = (float) Math.sqrt(Math.pow(cameraHeight / Math.sin(40), 2) - Math.pow(cameraHeight, 2));

	private Node groundNode;
	private Node mobileObjects = new Node("Mobile Objects");
	private Node pathNodes = new Node();
	private List<Player> players = GameManager.getInstance().getPlayers();

	private ArrayList<Pair<Integer, Integer>> path;
	BitmapText text;
	NiftyHandler niftyHandler;

	@Override
	public void simpleInitApp()
	{
		setDebuggingText();
		initialCameraConfiguration();
		setLights();

		niftyHandler = new NiftyHandler(assetManager, inputManager, audioRenderer, guiViewPort, stateManager, this);
		setCamera(new Vector3f(250, cameraHeight, 250));
		updateResourcePanel();

		groundNode = new Node("Ground");
		groundNode.attachChild(makeFloor());
		rootNode.attachChild(groundNode);

		loadCharacters();
		rootNode.attachChild(mobileObjects);
		rootNode.attachChild(pathNodes);

		loadStaticObjects();
		setEventTriggers();

		new GraphicUpdater(this).start();
	}

	private void setDebuggingText()
	{
		guiNode.detachAllChildren();
		guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
		text = new BitmapText(guiFont, false);
		text.setSize(guiFont.getCharSet().getRenderedSize());
		text.setText("Mouse x = " + inputManager.getCursorPosition().x + " y =  " + inputManager.getCursorPosition().y);
		text.setLocalTranslation(300, text.getLineHeight() + 200, 0);
		guiNode.attachChild(text);
	}

	protected Geometry makeFloor()
	{
		Box box = new Box(GameManager.getInstance().getWorld().getWidth() / 2, 0, GameManager.getInstance().getWorld().getHeight() / 2);

		Geometry floor = new Geometry("the Floor", box);
		floor.setLocalTranslation(GameManager.getInstance().getWorld().getWidth() / 2, 0, GameManager.getInstance().getWorld().getHeight() / 2);

		assetManager.registerLocator("Assets/Textures/", FileLocator.class);
		Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		Texture text1 = assetManager.loadTexture("grass-pattern.png");
		floor.getMesh().scaleTextureCoordinates(new Vector2f(20, 20));
		text1.setWrap(WrapMode.Repeat);

		mat1.setTexture("ColorMap", text1);
		floor.setMaterial(mat1);
		return floor;
	}

	private void loadStaticObjects()
	{
		ArrayList<AbstractObject> allObjects = GameManager.getInstance().getWorld().getAllObjects();

		Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat1.setColor("Color", ColorRGBA.Blue);

		Material mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat2.setColor("Color", ColorRGBA.Red);

		for (AbstractObject object : allObjects)
		{
			if (object instanceof House)
			{
				assetManager.registerLocator("Assets/Models/house.zip", ZipLocator.class);
				Spatial loadModel = assetManager.loadModel("house.scene");
				loadModel.setLocalTranslation(new Vector3f(object.getX(), 0, object.getY()));
				float cos = FastMath.cos(130 + 180);
				float sin = FastMath.sin(130 + 180);
				loadModel.setLocalRotation(new Matrix3f(cos, 0, sin, 0, 1, 0, -sin, 0, cos));
				loadModel.scale(2f);
				rootNode.attachChild(loadModel);
			}
			else if (object instanceof Field)
			{

				assetManager.registerLocator("Assets/Models/CornField.zip", ZipLocator.class);

				Spatial loadModel = assetManager.loadModel("CornField.mesh.xml");
				loadModel.setLocalTranslation(new Vector3f(object.getX(), 0, object.getY()));
				float cos = FastMath.cos(90);
				float sin = FastMath.sin(90);
				loadModel.setLocalRotation(new Matrix3f(1, 0, 0, 0, cos, -sin, 0, sin, cos));
				loadModel.scale(6f);
				rootNode.attachChild(loadModel);
			}

			else if (object instanceof Cave)
			{

				assetManager.registerLocator("Assets/Models/stones.zip", ZipLocator.class);

				Spatial loadModel = assetManager.loadModel("stones.mesh.xml");
				loadModel.setLocalTranslation(new Vector3f(object.getX(), 0, object.getY()));
				float cos = FastMath.cos(90);
				float sin = FastMath.sin(90);
				loadModel.setLocalRotation(new Matrix3f(1, 0, 0, 0, cos, -sin, 0, sin, cos));
				loadModel.scale(3f);
				rootNode.attachChild(loadModel);
			}
			else if (object instanceof Market)
			{
				assetManager.registerLocator("Assets/Models/wagen.zip", ZipLocator.class);

				Spatial loadModel = assetManager.loadModel("wagen.mesh.xml");
				loadModel.setLocalTranslation(new Vector3f(object.getX(), 0, object.getY()));

				loadModel.scale(0.1f);
				rootNode.attachChild(loadModel);

			}

			else if (object instanceof Wood)
			{
				// assetManager.registerLocator("Assets/Models/wood.zip", ZipLocator.class);
				//
				// Spatial loadModel = assetManager.loadModel("wood.mesh.xml");
				// loadModel.setLocalTranslation(new Vector3f(object.getX(), 0, object.getY()));
				// float cos = FastMath.cos(90);
				// float sin = FastMath.sin(90);
				// loadModel.setLocalRotation(new Matrix3f(1, 0, 0, 0, cos, -sin, 0, sin, cos));
				// loadModel.scale(2f);
				// rootNode.attachChild(loadModel);

			}

		}
	}

	public void setText(String text)
	{
		this.text.setText("Mouse x = " + inputManager.getCursorPosition().x + " y =  " + inputManager.getCursorPosition().y + " " + text);

	}

	private void setEventTriggers()
	{
		inputManager.addMapping("Shift_Map_Negative_X", new MouseAxisTrigger(MouseInput.AXIS_X, true));

		inputManager.addMapping("Shift_Map_Positive_X", new MouseAxisTrigger(MouseInput.AXIS_X, false));
		inputManager.addMapping("Shift_Map_Negative_Y", new MouseAxisTrigger(MouseInput.AXIS_Y, true));
		inputManager.addMapping("Shift_Map_Positive_Y", new MouseAxisTrigger(MouseInput.AXIS_Y, false));
		inputManager.addListener(mouseActionListener, "Shift_Map_Negative_X", "Shift_Map_Positive_X", "Shift_Map_Negative_Y", "Shift_Map_Positive_Y");

		inputManager.addMapping("Click", new MouseButtonTrigger(0));
		inputManager.addListener(clickActionListener, "Click");
	}

	private void setLights()
	{
		DirectionalLight dl = new DirectionalLight();
		dl.setDirection(new Vector3f(-0.8f, -0.6f, -0.08f).normalizeLocal());
		// dl.setColor( new ColorRGBA( 0.9f, 0.9f, 0.9f, 1.0f ) );
		rootNode.addLight(dl);

		AmbientLight al = new AmbientLight();
		// al.setColor( new ColorRGBA( 0.7f, 0.9f, 1.5f, 1.0f ) );
		rootNode.addLight(al);
	}

	public void setCamera(Vector3f newPosition)
	{
		// if( newPosition.z + lz < 0 )
		// return;
		// if( newPosition.x < -1
		// || newPosition.x > GameManager.getInstance().getWorld().getWidth() + 10
		// || newPosition.z > GameManager.getInstance().getWorld().getHeight() )
		// return;

		// if (niftyHandler.isVisibleSelectionPanel())
		// niftyHandler.removeSelectedPanel();
		cam.setLocation(newPosition);
		cam.lookAt(new Vector3f(newPosition.x, 0, newPosition.z + lz), new Vector3f(0, 1, 0));

	}

	public Vector3f getCamPosition()
	{
		return cam.getLocation();
	}

	private void initialCameraConfiguration()
	{
		cam.clearViewportChanged();
		flyCam.setEnabled(false);
		inputManager.setCursorVisible(true);
	}

	private void loadCharacters()
	{
		int i = 0;
		assetManager.registerLocator("./Assets/Models/male_civilian_1.zip", ZipLocator.class);
		for (Player player : players)
		{
			Spatial model = assetManager.loadModel("main.mesh.xml");
			if (i == 0)
			{
				model.scale(25.2f, 25.2f, 25.2f);
				model.setLocalTranslation(new Vector3f(player.getX(), 95, player.getY()));
			}
			else
			{
				model.scale(10.2f, 10.2f, 10.2f);
				model.setLocalTranslation(new Vector3f(player.getX(), 30, player.getY()));
			}
			i++;
			mobileObjects.attachChild(model);

		}
	}

	private AnalogListener clickActionListener = new AnalogListener()
	{

		@Override
		public void onAnalog(String arg0, float arg1, float arg2)
		{
			Vector2f click = inputManager.getCursorPosition();

			Vector3f cursor = cam.getWorldCoordinates(new Vector2f(click.x, click.y), 0.0f).clone();

			System.out.println("la pos di cursor è " + cursor);

			Vector3f dir = cam.getWorldCoordinates(new Vector2f(click.x, click.y), 1.0f).subtractLocal(cursor).normalizeLocal();
			System.out.println("la dir è " + dir);

			Ray ray = new Ray();

			ray.setOrigin(cursor);
			ray.setDirection(new Vector3f(dir.x, dir.y, dir.z));

			CollisionResults crs = new CollisionResults();
			groundNode.collideWith(ray, crs);

			if (crs.getClosestCollision() != null)
			{
				niftyHandler.emptySelectedPanel();
				Vector2f contactPoint = new Vector2f(crs.getClosestCollision().getContactPoint().x, crs.getClosestCollision().getContactPoint().z);

				if (GameManager.getInstance().getWorld().pickedObject(contactPoint.x, contactPoint.y) == null)
				{
					GameManager.getInstance().computePath(players.get(0), contactPoint.x, contactPoint.y);

					path = players.get(0).getPath();
					drawPath();
				}
				else
				{

					niftyHandler.setSelectedPanel(GameManager.getInstance().getWorld().pickedObject(contactPoint.x, contactPoint.y));
				}
			}
		}
	};

	private void drawPath()
	{
		pathNodes.detachAllChildren();
		int i = 0;
		for (Pair<Integer, Integer> pair : path)
		{
			Sphere sphereMesh;
			Geometry sphereGeo;

			sphereMesh = new Sphere(32, 32, 2f);
			sphereGeo = new Geometry("" + i, sphereMesh);

			sphereMesh.setTextureMode(Sphere.TextureMode.Projected); // better quality on
			// spheres
			TangentBinormalGenerator.generate(sphereMesh); // for lighting effect
			Material sphereMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
			sphereMat.setTexture("DiffuseMap", assetManager.loadTexture("Textures/Terrain/Pond/Pond.jpg"));
			sphereMat.setTexture("NormalMap", assetManager.loadTexture("Textures/Terrain/Pond/Pond_normal.png"));
			sphereMat.setBoolean("UseMaterialColors", true);
			sphereMat.setColor("Diffuse", ColorRGBA.White);
			sphereMat.setColor("Specular", ColorRGBA.White);
			sphereMat.setFloat("Shininess", 64f); // [0,128]
			sphereGeo.setMaterial(sphereMat);

			sphereGeo.rotate(1.6f, 0, 0); // Rotate it a bit

			sphereGeo.setLocalTranslation((float) pair.getFirst(), 5, (float) pair.getSecond());
			pathNodes.attachChild(sphereGeo);
		}
	}

	private AnalogListener mouseActionListener = new AnalogListener()
	{

		@Override
		public void onAnalog(String name, float value, float tpf)
		{
			Vector2f mousePosition = inputManager.getCursorPosition();

			lock.lock();

			setText(" no edge");

			if (mousePosition.x <= 0)
			{
				cursorOnTheLeftEdge = true;
				cursorOnTheRightEdge = false;
				setText("sono nell'x == 0");

			}
			else if (mousePosition.x >= settings.getWidth() - 3)
			{
				cursorOnTheRightEdge = true;
				cursorOnTheLeftEdge = false;
				setText("sono nell'x == " + settings.getWidth());

			}
			else
			{
				cursorOnTheLeftEdge = false;
				cursorOnTheRightEdge = false;
			}

			if (mousePosition.y <= 3)
			{
				cursorOnTheBottomEdge = true;
				cursorOnTheTopEdge = false;
				setText("sono nell'y == 0");

			}
			else if (mousePosition.y >= settings.getHeight() - 3)
			{
				cursorOnTheTopEdge = true;
				cursorOnTheBottomEdge = false;
				setText("sono nell'y == " + settings.getHeight());

			}
			else
			{
				cursorOnTheBottomEdge = false;
				cursorOnTheTopEdge = false;
			}

			lock.unlock();
		}
	};
	private AbstractObject selectedObject;

	public void takeLock()
	{
		lock.lock();
	}

	public void leaveLock()
	{
		lock.unlock();
	}

	private void updateMobileObjects()
	{
		players = GameManager.getInstance().getPlayers();

		int i = 0;
		for (Player p : players)
		{
			if (i == 0)
				mobileObjects.getChild(i).setLocalTranslation(p.getX(), 25, p.getY());

			i++;
		}
	}

	@Override
	public void simpleUpdate(float tpf)
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
		HashMap<Resources, Integer> resources = GameManager.getInstance().getPlayers().get(0).getResources();

		Set<Resources> keySet = resources.keySet();
		for (Resources key : keySet)
		{
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println(niftyHandler);
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println();
			niftyHandler.setResourceValue(key, resources.get(key));
		}

	}

	public void occupy()
	{
		selectedObject = niftyHandler.getSelectedObject();

	}

}

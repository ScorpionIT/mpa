package mpa.gui.gameGui.playingGUI;

import java.util.ArrayList;
import java.util.List;

import mpa.core.maths.MyMath;
import mpa.core.util.GameProperties;

import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.bounding.BoundingBox;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

public class GuiObjectProvider
{

	private List<Spatial> towerCrushers = new ArrayList<>();
	private List<Spatial> minions = new ArrayList<>();
	private List<Spatial> towers = new ArrayList<>();
	private String modelsPath = GameProperties.getInstance().getPath("ModelsPath");
	private AssetManager assetManager;

	public GuiObjectProvider(AssetManager assetManager, int numberOfMinions, int numberOfTowerCrusher, int numberOfTowers)
	{
		this.assetManager = assetManager;
		loadMinions(numberOfMinions);
		loadTowerCrushers(numberOfTowerCrusher);
		loadTowers(numberOfTowers);
	}

	public Spatial getTowerCrusherSpatial()
	{
		if (towerCrushers.size() == 0)
		{
			loadTowerCrushers(5);
		}
		return towerCrushers.remove(0);
	}

	public Spatial getMinionSpatial()
	{
		if (minions.size() == 0)
		{
			loadMinions(5);
		}
		return minions.remove(0);
	}

	public Spatial getTowerSpatial()
	{
		if (towers.size() == 0)
		{
			loadTowers(5);
		}
		return towers.remove(0);
	}

	public void addTowerCrusherSpatial(Spatial towerCrusher)
	{
		towerCrushers.add(towerCrusher);
	}

	public void addMinionSpatial(Spatial minion)
	{
		minions.add(minion);
	}

	public void addTowerSpatial(Spatial spatial)
	{
		towers.add(spatial);
	}

	private void loadMinions(int number)
	{
		for (int i = 0; i < number; i++)
		{
			assetManager.registerLocator(modelsPath + GameProperties.getInstance().getModelName("minion") + ".zip", ZipLocator.class);
			Spatial minionModel = assetManager.loadModel(GameProperties.getInstance().getModelName("minion") + ".mesh.xml");
			minionModel.scale(MyMath.scaleFactor(getModelBounds(minionModel), "minion"));
			minions.add(minionModel);
		}

	}

	private void loadTowerCrushers(int number)
	{
		for (int i = 0; i < number; i++)
		{
			assetManager.registerLocator(modelsPath + GameProperties.getInstance().getModelName("towerCrusher") + ".zip", ZipLocator.class);
			Spatial towerCrusherModel = assetManager.loadModel(GameProperties.getInstance().getModelName("towerCrusher") + ".mesh.xml");
			towerCrusherModel.scale(MyMath.scaleFactor(getModelBounds(towerCrusherModel), "towerCrusher"));
			towerCrushers.add(towerCrusherModel);
		}

	}

	private void loadTowers(int number)
	{
		assetManager.registerLocator(modelsPath + GameProperties.getInstance().getModelName("tower") + ".zip", ZipLocator.class);
		Spatial tower = assetManager.loadModel(GameProperties.getInstance().getModelName("tower") + ".mesh.xml");
		tower.rotate(FastMath.PI / 2, 0, 0);
		tower.scale(MyMath.scaleFactor(getModelBounds(tower), "tower"));
		towers.add(tower);
	}

	private Vector3f getModelBounds(Spatial model)
	{
		BoundingBox box = (BoundingBox) model.getWorldBound();
		Vector3f boxSize = new Vector3f();
		box.getExtent(boxSize);
		return boxSize;
	}

}

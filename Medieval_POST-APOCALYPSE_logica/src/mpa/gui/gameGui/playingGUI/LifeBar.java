package mpa.gui.gameGui.playingGUI;

import javax.vecmath.Vector2f;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

public class LifeBar
{
	final int maxLife = 100;
	final int maxBoxDimension = 10;
	final int limitRedLife = 30;
	float yTraslation;
	private AssetManager assetManager;
	Box greenBox;
	Box grayBox;
	Geometry geometryGreenBox;
	Geometry geometryGrayBox;
	Material materialGreenBox;
	Material materialGrayBox;
	Material materialRedBox;
	int life = -1;

	public LifeBar(AssetManager assetManager, Vector3f boundingBoxModel, float yTranslationModel)
	{
		this.assetManager = assetManager;
		yTraslation = boundingBoxModel.y * 2 + yTranslationModel;

		greenBox = new Box(10, 1, 1);
		geometryGreenBox = new Geometry("GreenBox", greenBox);
		materialGreenBox = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		materialGreenBox.setColor("Color", ColorRGBA.Green);
		geometryGreenBox.setMaterial(materialGreenBox);

		grayBox = new Box(10, 1, 1);
		geometryGrayBox = new Geometry("GreyBox", grayBox);
		materialGrayBox = materialGreenBox.clone();
		materialGrayBox.setColor("Color", ColorRGBA.Gray);
		geometryGrayBox.setMaterial(materialGrayBox);

		materialRedBox = materialGrayBox.clone();
		materialRedBox.setColor("Color", ColorRGBA.Red);
	}

	boolean setLife(int life)
	{
		if (this.life == life)
			return false;

		this.life = life;
		int xGreenBox = (life * maxBoxDimension) / maxLife;
		greenBox.xExtent = xGreenBox;

		if (life < limitRedLife)
			geometryGreenBox.setMaterial(materialRedBox);

		else
			geometryGreenBox.setMaterial(materialGreenBox);
		greenBox.updateBound();
		greenBox.updateGeometry();
		geometryGreenBox.updateModelBound();
		int xGreyBox = maxBoxDimension - xGreenBox;
		grayBox.xExtent = xGreyBox;
		grayBox.updateBound();
		grayBox.updateGeometry();
		geometryGreenBox.updateModelBound();
		return true;

	}

	void setLocalTraslation(Vector2f modelPosition)
	{
		geometryGreenBox.setLocalTranslation(modelPosition.x + grayBox.xExtent, yTraslation, modelPosition.y);
		geometryGrayBox.setLocalTranslation(modelPosition.x - greenBox.xExtent, yTraslation, modelPosition.y);
	}

	void setLocalTraslation(Vector3f modelPosition)
	{
		geometryGreenBox.setLocalTranslation(modelPosition.x + grayBox.xExtent, yTraslation, modelPosition.y);
		geometryGrayBox.setLocalTranslation(modelPosition.x - greenBox.xExtent, yTraslation, modelPosition.y);
	}

	public Geometry getGreenGeometry()
	{
		return geometryGreenBox;
	}

	public Geometry getGrayGeometry()
	{
		return geometryGrayBox;
	}

}

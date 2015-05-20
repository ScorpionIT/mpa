package mpa.gui.gameGui.listener;

import mpa.core.logic.GameManager;
import mpa.gui.gameGui.GameGui;

import com.jme3.collision.CollisionResults;
import com.jme3.input.controls.AnalogListener;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

public class GameGuiClickListener implements AnalogListener
{

	private GameGui gameGui;

	public GameGuiClickListener(GameGui gameGui)
	{

		this.gameGui = gameGui;
	}

	@Override
	public void onAnalog(String arg0, float arg1, float arg2)
	{

		Vector2f click = gameGui.getInputManager().getCursorPosition();

		Vector3f cursor = gameGui.getCamera().getWorldCoordinates(new Vector2f(click.x, click.y), 0.0f).clone();

		System.out.println("la pos di cursor è " + cursor);

		Vector3f dir = gameGui.getCamera().getWorldCoordinates(new Vector2f(click.x, click.y), 1.0f).subtractLocal(cursor).normalizeLocal();
		System.out.println("la dir è " + dir);

		Ray ray = new Ray();

		ray.setOrigin(cursor);
		ray.setDirection(new Vector3f(dir.x, dir.y, dir.z));

		CollisionResults crs = new CollisionResults();
		gameGui.getGroundNode().collideWith(ray, crs);

		if (crs.getClosestCollision() != null)
		{
			gameGui.getNiftyHandler().emptySelectedPanel();
			Vector2f contactPoint = new Vector2f(crs.getClosestCollision().getContactPoint().x, crs.getClosestCollision().getContactPoint().z);

			if (GameManager.getInstance().getWorld().pickedObject(contactPoint.x, contactPoint.y) == null)
			{
				GameManager.getInstance().computePath(gameGui.getPlayers().get(gameGui.getPlayerIndex()), contactPoint.x, contactPoint.y);

				gameGui.setPath(gameGui.getPlayers().get(gameGui.getPlayerIndex()).getPath());
				gameGui.drawPath();
			}
			else
			{

				gameGui.getNiftyHandler().setSelectedPanel(GameManager.getInstance().getWorld().pickedObject(contactPoint.x, contactPoint.y));
			}
		}
	}

}

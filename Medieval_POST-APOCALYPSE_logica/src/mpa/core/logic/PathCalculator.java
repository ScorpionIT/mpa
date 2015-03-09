package mpa.core.logic;

import java.util.ArrayList;


public interface PathCalculator
{

	public ArrayList<Pair<Float, Float>> computePath(World world, float xGoal, float yGoal, float xPlayer, float yPlayer);
}

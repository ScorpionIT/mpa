package mpa.core.logic;

import mpa.core.logic.character.Player;

public interface PathCalculator
{

	public void computePath(World world, Player player, float xGoal, float yGoal);
}

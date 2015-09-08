package mpa.gui.gameGui.listener;

import java.util.ArrayList;
import java.util.HashMap;

import mpa.core.logic.character.Player.Item;

import com.jme3.math.Vector2f;

public abstract class ListenerImplementation
{
	abstract void setPause();

	abstract HashMap<String, HashMap<String, Integer>> getPlayersResourceAmount();

	abstract String getPickedObject( Vector2f click );

	abstract void changeItem( Item item );

	abstract ArrayList<String> attack( Vector2f direction );

	abstract boolean occupyProperty( String property );

	abstract boolean createTower( Vector2f point );

	abstract boolean createMinions( String boss, String target, int quantity );

	abstract void updateInformation();

	abstract void createStateInformation();

}

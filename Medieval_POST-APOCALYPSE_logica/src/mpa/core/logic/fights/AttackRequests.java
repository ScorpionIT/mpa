package mpa.core.logic.fights;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

import javax.vecmath.Vector2f;

import mpa.core.ai.OpponentAI;
import mpa.core.logic.MyThread;
import mpa.core.logic.character.AbstractCharacter;
import mpa.core.logic.character.Minion;
import mpa.core.logic.character.Player;
import mpa.core.logic.character.Player.Item;
import mpa.core.logic.tool.Potions;

public class AttackRequests extends MyThread
{
	private ArrayList<AbstractCharacter> attackList = new ArrayList<>();
	private ArrayList<Vector2f> targets = new ArrayList<>();
	private HashMap<Player, OpponentAI> AI_players;
	private ReentrantLock lock = new ReentrantLock();

	public AttackRequests( HashMap<Player, OpponentAI> AI_players )
	{
		this.AI_players = AI_players;
	}

	public void addRequest( AbstractCharacter character, Vector2f target )
	{
		lock.lock();
		attackList.add( character );
		targets.add( target );
		lock.unlock();
	}

	@Override
	public void run()
	{
		while( true )
		{
			lock.lock();
			super.run();
			ArrayList<AbstractCharacter> hitPlayers = new ArrayList<>();
			if( !attackList.isEmpty() )
			{
				AbstractCharacter attacker = attackList.remove( 0 );
				Vector2f target = targets.get( 0 );
				if( attacker instanceof Minion )
					hitPlayers = CombatManager.getInstance().attackPhysically( ( Minion ) attacker );
				else if( attacker instanceof Player )
				{
					Player player = ( Player ) attacker;
					if( player.getSelectedItem().equals( Item.WEAPON ) )
						CombatManager.getInstance().attackPhysically( ( Player ) attacker );
					else if( player.getSelectedItem().equals( Item.GRANADE ) )
						hitPlayers = CombatManager.getInstance().distanceAttack( player,
								player.takePotion( Potions.GRANADE ), target );
					else if( player.getSelectedItem().equals( Item.FLASH_BANG ) )
						hitPlayers = CombatManager.getInstance().distanceAttack( player,
								player.takePotion( Potions.FLASH_BANG ), target );

				}
				for( AbstractCharacter hitPlayer : hitPlayers )
				{
					System.out.println();
					System.out.println();
					System.out.println();
					System.out.println( "mi ha rsistuito cosa ?!?!? " + hitPlayers.size() );
					System.out.println();
					System.out.println();
					System.out.println();
					if( hitPlayer != attacker && AI_players.keySet().contains( hitPlayer ) )
						AI_players.get( hitPlayer ).gotAttackedBy( attacker );

				}

			}
			lock.unlock();
		}
	}

}

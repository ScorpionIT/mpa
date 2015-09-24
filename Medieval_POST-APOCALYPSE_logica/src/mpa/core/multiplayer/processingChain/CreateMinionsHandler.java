package mpa.core.multiplayer.processingChain;

import java.util.List;

import mpa.core.logic.GameManagerProxy;

public class CreateMinionsHandler extends ProcessingChain
{

	public CreateMinionsHandler(ProcessingChain next)
	{
		super(next);
	}

	@Override
	public String[] processRequest(String request)
	{
		// TODO towerCrusher sono creati uno alla volta
		String[] strings = request.split(":");

		if (strings.length == 5 && strings[0].equals("Bless"))
		{
			List<String> minions = null;
			String tC = null;
			if (strings[1].equals("Minions"))
			{
				minions = GameManagerProxy.getInstance().createMinions(strings[2], Integer.parseInt(strings[3]), strings[4]);
			}
			else if (strings[1].equals("TowerCrusher"))
			{
				System.out.println("sono strings " + strings);
				tC = GameManagerProxy.getInstance().createTowerCrushers(strings[2], strings[4]);
			}
			String[] reply = new String[1];
			if ((minions == null || minions.isEmpty()) && tC == null)
				reply[0] = "NO";
			else
				reply[0] = "OK";
		}
		else if (hasNext())
			return next.processRequest(request);
		else
			return super.processRequest(request);
		return null;
	}
}

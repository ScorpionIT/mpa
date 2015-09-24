package mpa.core.multiplayer.processingChain;

import java.util.Map;

import mpa.core.logic.GameManagerProxy;

public class PlayerInfoHandler extends ProcessingChain
{

	public PlayerInfoHandler(ProcessingChain next)
	{
		super(next);
	}

	@Override
	public String[] processRequest(String request)
	{
		String[] fields = request.split(":");

		if (fields.length == 3 && fields[0].equals("PlayerInfo"))
		{
			if (fields[2].equals("HP"))
			{

				String[] reply = new String[1];
				reply[0] = String.valueOf(GameManagerProxy.getInstance().getPLayerHP(fields[1]));
				return reply;
			}
			else if (fields[2].equals("MP"))
			{
				String[] reply = new String[1];
				reply[0] = String.valueOf(GameManagerProxy.getInstance().getPlayerMP(fields[1]));
				return reply;
			}
			else if (fields[2].equals("RESOURCES"))
			{
				Map<String, Integer> playerResourcesAmout = GameManagerProxy.getInstance().getPlayerResourcesAmout(fields[1]);
				String[] reply = new String[6];

				int index = 0;
				for (String type : playerResourcesAmout.keySet())
					reply[index++] = type + "=" + playerResourcesAmout.get(type);

				reply[index] = ".";

				return reply;
			}
			else if (fields[2].equals("LEVEL"))
			{
				String[] reply = new String[1];
				reply[0] = GameManagerProxy.getInstance().getPlayerLevel(fields[1]);
				return reply;
			}
			else
			{
				String[] minionName = fields[2].split(",");
				if (minionName[0].equals("MINION"))
				{
					String[] reply = new String[1];
					reply[0] = GameManagerProxy.getInstance().getMinionBoss(minionName[1]);
					return reply;
				}
			}
		}
		if (hasNext())
			return next.processRequest(request);
		else
			return super.processRequest(request);
	}
}
